package org.demo.user.service.impl;

import lombok.AllArgsConstructor;
import org.demo.api.clients.PermissionClient;
import org.demo.common.constant.RoleCode;
import org.demo.common.domain.dto.ResetPasswordDTO;
import org.demo.common.domain.po.User;
import org.demo.common.utils.BcryptUtil;
import org.demo.common.utils.JwtUtils;
import org.demo.user.dao.UserDao;
import org.demo.user.exceptions.*;
import org.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private PermissionClient permissionClient;

    @Override
    public boolean register(User user) {
        //todo 合法性检验

        String encodedPsw = BcryptUtil.encode(user.getPassword());
        user.setPassword(encodedPsw);



        return false;
    }

    @Override
    public String login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        User loginUser = userDao.getUser(username);

        if (!BcryptUtil.matches(password, loginUser.getPassword())) {
            throw new RuntimeException();
        }

        Long roleCode = permissionClient.getRoleByUserId(loginUser.getUserId());

        //todo 存入redis

        String jwt = JwtUtils.createJwt(loginUser.getUserId(), "secret", 7 * 24 * 3600);

        return jwt;
    }

    @Override
    public List<User> getUsers(Long startUserId, Long roleId, Long userId) {
        // 普通用户，返回自己
        if (Objects.equals(roleId, RoleCode.USER)) {
            User user = userDao.getUser(userId);
            return Collections.singletonList(user);
        }

        // 管理员，返回所有普通用户；超级管理员，返回所有用户
        if (Objects.equals(roleId, RoleCode.ADMIN) ||
                Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            List<Long> ids = permissionClient.getUserIds(roleId, startUserId);
            if (ids.isEmpty()) {
                return Collections.emptyList();
            }
            return userDao.getUsers(ids);
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

            return userDao.getUser(uid);
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
            return user;
        }

        // 超级管理员，可返回任何用户
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            User user = userDao.getUser(userId);
            if (Objects.isNull(user)) {
                throw new UserNotFoundException();
            }
            return user;
        }

        throw new IllegalRoleException();

    }

    @Override
    public boolean updateUser(Long userId, Long uid, Long roleId, User user) {
        if (Objects.isNull(user)) {
            throw new IllegalDataException();
        }

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

        if (!isPasswordValid(resetPasswordDTO.getNewPsw())) {
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
    private boolean isPasswordValid(String psw) {
        return psw != null &&
                psw.length() >= 8 &&
                psw.matches(".*[A-Z].*") &&
                psw.matches(".*[a-z].*") &&
                psw.matches(".*\\d.*");
    }

}
