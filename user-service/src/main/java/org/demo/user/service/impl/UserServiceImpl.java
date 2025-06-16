package org.demo.user.service.impl;

import lombok.AllArgsConstructor;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.demo.api.clients.PermissionClient;
import org.demo.common.constant.OperationLogActions;
import org.demo.common.constant.RabbitConstants;
import org.demo.common.constant.RoleCode;
import org.demo.common.domain.dto.ResetPasswordDTO;
import org.demo.common.domain.po.OperationLog;
import org.demo.common.domain.po.User;
import org.demo.common.enums.OperationLogDetailEnum;
import org.demo.common.utils.BcryptUtil;
import org.demo.common.utils.EncryptUtil;
import org.demo.common.utils.JwtUtils;
import org.demo.common.utils.OperationLogUtil;
import org.demo.user.config.JwtProperties;
import org.demo.user.config.SecretKeysProperties;
import org.demo.user.dao.UserDao;
import org.demo.user.exceptions.*;
import org.demo.user.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private PermissionClient permissionClient;
    private RedisTemplate<String, Object> redisTemplate;
    private RabbitTemplate rabbitTemplate;
    private JwtProperties jwtProperties;
    private SecretKeysProperties secretKeysProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingSphereTransactionType(TransactionType.BASE)
    public boolean register(User user, String ip) {
        if (Objects.isNull(user)) {
            throw new IllegalDataException();
        }

        String username = user.getUsername();
        String password = user.getPassword();

        if (isInvalidUsername(username) || isInvalidPassword(password)) {
            throw new UsernameOrPasswordFormatException();
        }

        if (!Objects.isNull(userDao.getUserByUsername(username))) {
            throw new UsernameAlreadyExistException(username);
        }

        user.setPassword(BcryptUtil.encode(user.getPassword()));
        EncryptUtil.encryptUserInfo(user, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());
        user.setGmtCreate(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        // 分库分表写入用户表
        userDao.insertUser(user);

        // RPC调用绑定默认角色
        permissionClient.bindDefaultRole(user.getUserId());

        // 事务回滚测试
//        if (1 == 1) throw new RuntimeException();

        // 发送日志消息至MQ
        OperationLog log = OperationLogUtil.builder()
                .userId(user.getUserId())
                .ip(ip)
                .action(OperationLogActions.ADD)
                .detail(OperationLogDetailEnum.REGISTER.getDetail())
                .build();
        rabbitTemplate.convertAndSend(RabbitConstants.REGISTER_EXCHANGE, "", log);

        return true;
    }

    @Override
    public String login(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalDataException();
        }

        String username = user.getUsername();
        String password = user.getPassword();

        if (isInvalidUsername(username) || isInvalidPassword(password)) {
            throw new UsernameOrPasswordFormatException();
        }

        User loginUser = userDao.getUserByUsername(username);
        Long userId = loginUser.getUserId();

        if (!BcryptUtil.matches(password, loginUser.getPassword())) {
            throw new PasswordIncorrectException();
        }

        Long roleCode = permissionClient.getRoleByUserId(userId);

        redisTemplate.opsForValue().set(String.valueOf(userId), String.valueOf(roleCode));

        return JwtUtils.createJwt(userId, jwtProperties.getSecret(), jwtProperties.getExp());
    }

    @Override
    public List<User> getUsers(Long startUserId, Long roleId, Long userId) {
        // 普通用户，返回自己
        if (Objects.equals(roleId, RoleCode.USER)) {
            User user = userDao.getUser(userId);
            EncryptUtil.decryptUserInfo(user, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());
            return Collections.singletonList(user);
        }

        // 管理员，返回所有普通用户；超级管理员，返回所有用户
        if (Objects.equals(roleId, RoleCode.ADMIN) ||
                Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            List<Long> ids = permissionClient.getUserIds(roleId, startUserId);
            if (ids.isEmpty()) {
                return Collections.emptyList();
            }
            List<User> users = userDao.getUsers(ids);
            EncryptUtil.decryptUserInfo(users, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());
            return users;
        }

        throw new IllegalRoleException();
    }

    @Override
    public User getUser(Long userId, Long uid, Long roleId) {
        // 普通用户，只能返回自己
        if (roleId.equals(RoleCode.USER)) {
            if (!Objects.equals(userId, uid)) {
                throw new IllegalOperationException();
            }

            User user = userDao.getUser(uid);
            EncryptUtil.decryptUserInfo(user, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());
            return user;
        }

        // 管理员，只能返回普通用户
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            if (!Objects.equals(permissionClient.getRoleByUserId(userId), RoleCode.USER)) {
                throw new IllegalOperationException();
            }

            User user = userDao.getUser(userId);
            if (Objects.isNull(user)) {
                throw new UserNotFoundException();
            }
            EncryptUtil.decryptUserInfo(user, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());
            return user;
        }

        // 超级管理员，可返回任何用户
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            User user = userDao.getUser(userId);
            if (Objects.isNull(user)) {
                throw new UserNotFoundException();
            }
            EncryptUtil.decryptUserInfo(user, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());
            return user;
        }

        throw new IllegalRoleException();

    }

    @Override
    public boolean updateUser(Long userId, Long uid, Long roleId, User user) {
        if (Objects.isNull(user)) {
            throw new IllegalDataException();
        }

        EncryptUtil.encryptUserInfo(user, secretKeysProperties.getEmailSecretKey(), secretKeysProperties.getPhoneSecretKey());

        // 普通用户，只能修改自己的个人信息
        if (Objects.equals(roleId, RoleCode.USER)) {
            if (!Objects.equals(userId, uid)) {
                throw new IllegalOperationException();
            }
            return userDao.updateUser(user, userId);
        }

        // 管理员，只能修改普通用户和自己的信息
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            if (!Objects.equals(userId, uid) &&
                    !Objects.equals(permissionClient.getRoleByUserId(userId), RoleCode.USER) ) {
                throw new IllegalOperationException();
            }

            return userDao.updateUser(user, userId);

        }

        // 超级管理员，能修改所有人的信息
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            return userDao.updateUser(user, userId);
        }

        throw new IllegalRoleException();
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO, Long userId, Long roleId) {
        if (Objects.isNull(resetPasswordDTO)) {
            throw new IllegalDataException();
        }

        if (isInvalidPassword(resetPasswordDTO.getNewPsw())) {
            throw new PasswordFormatException();
        }

        // 所有角色都可以修改自己的密码
        if (Objects.isNull(resetPasswordDTO.getUserId())) {

            String psw = userDao.findPassword(userId);
            if (!BcryptUtil.matches(resetPasswordDTO.getRowPsw(), psw)) {
                throw new PasswordIncorrectException();
            }

            String newPsw = BcryptUtil.encode(resetPasswordDTO.getNewPsw());
            return userDao.updatePassword(newPsw, userId);
        }

        // 普通用户，只能修改自己的密码
        if (Objects.equals(roleId, RoleCode.USER)) {
            throw new IllegalOperationException();
        }

        // 管理员，只能修改普通用户的密码
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            Long resetUid = resetPasswordDTO.getUserId();

            if (!Objects.equals(permissionClient.getRoleByUserId(resetUid), RoleCode.USER)) {
                throw new IllegalOperationException();
            }

            String newPsw = BcryptUtil.encode(resetPasswordDTO.getNewPsw());
            return userDao.updatePassword(newPsw, resetUid);
        }

        // 超级管理员，可以修改所有用户的密码
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            String newPsw = BcryptUtil.encode(resetPasswordDTO.getNewPsw());
            return userDao.updatePassword(newPsw, resetPasswordDTO.getUserId());
        }

        throw new IllegalRoleException();
    }

    // 正则表达式由chatgpt生成
    private boolean isInvalidPassword(String psw) {
        return psw == null
                || psw.length() < 8
                || psw.length() > 20
                || !psw.matches(".*[A-Z].*")
                || !psw.matches(".*[a-z].*")
                || !psw.matches(".*\\d.*");
    }

    // 正则表达式由chatgpt生成
    private boolean isInvalidUsername(String username) {
        return username == null
                || username.length() > 15
                || username.length() < 4
                || !username.matches("^[A-Za-z0-9][A-Za-z0-9_]*$");
    }

}
