package org.demo.user.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.api.clients.PermissionClient;
import org.demo.common.constant.RoleCode;
import org.demo.common.domain.dto.ResetPasswordDTO;
import org.demo.common.domain.po.User;
import org.demo.common.utils.BcryptUtil;
import org.demo.user.dao.UserDao;
import org.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService { // todo 自定义异常

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

        userDao.findPassword(user.getUserId());

        return "";
    }

    @Override
    public List<User> getUsers(Long startUserId, Long roleId, Long userId) {
        // 普通用户，返回自己
        if (Objects.equals(roleId, RoleCode.USER)) {
            User user = userDao.getUser(userId);
            return Collections.singletonList(user);
        }

        // 管理员，返回所有普通用户
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            List<Long> ids = permissionClient.getUserIds(RoleCode.ADMIN, startUserId);
            return userDao.getUsers(ids);
        }

        // 超级管理员，返回所有用户
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            List<Long> ids = permissionClient.getUserIds(RoleCode.SUPER_ADMIN, startUserId);
            return userDao.getUsers(ids);
        }

        log.error("");
        throw new RuntimeException();
    }

    @Override
    public User getUser(Long userId, Long uid, Long roleId) {
        // 普通用户，只能返回自己
        if (roleId.equals(RoleCode.USER)) {
            if (!Objects.equals(userId, uid)) {
                throw new RuntimeException();
            }
            return userDao.getUser(uid);
        }

        // 管理员，只能返回普通用户
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            if (!Objects.equals(permissionClient.getRoleByUserId(userId), RoleCode.USER)) {
                throw new RuntimeException();
            }
            return userDao.getUser(userId);
        }

        // 超级管理员，可返回任何用户
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            return userDao.getUser(userId);
        }

        throw new RuntimeException();

    }

    @Override
    public boolean updateUser(Long userId, Long uid, Long roleId, User user) {
        if (Objects.isNull(user)) {
            throw new RuntimeException();
        }
        // 所有用户都可以修改自己的个人信息


        // 普通用户，只能修改自己的个人信息
        if (Objects.equals(roleId, RoleCode.USER)) {
            if (!Objects.equals(userId, uid)) {
                throw new RuntimeException();
            }

        }

        // 管理员，只能修改普通用户和自己的信息
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            if (!Objects.equals(userId, uid) &&
                    !Objects.equals(permissionClient.getRoleByUserId(userId), RoleCode.USER) ) {
                throw new RuntimeException();
            }

            return userDao.updateUser(user, userId);

        }

        // 超级管理员，能修改所有人的信息
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {

        }

        throw new RuntimeException();
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO, Long userId, Long roleId) { //todo null值判定
        if (Objects.isNull(resetPasswordDTO)) {
            throw new RuntimeException();
        }

        // 所有角色都可以修改自己的密码
        if (Objects.isNull(resetPasswordDTO.getUserId())) {

            //todo 新密码合法性判定

            String psw = userDao.findPassword(userId);
            if (!BcryptUtil.matches(resetPasswordDTO.getRowPsw(), psw)) {
                throw new RuntimeException();
            }
            return true;
        }

        // 普通用户，只能修改自己的密码
        if (Objects.equals(roleId, RoleCode.USER)) {
            throw new RuntimeException();
        }

        // 管理员，只能修改普通用户的密码
        if (Objects.equals(roleId, RoleCode.ADMIN)) {
            Long resetUid = resetPasswordDTO.getUserId();
            if (!Objects.equals(permissionClient.getRoleByUserId(resetUid), RoleCode.USER)) {
                throw new RuntimeException();
            }

            if (!isPasswordValid(resetPasswordDTO.getNewPsw())) {
                throw new RuntimeException();
            }

            return true;
        }

        // 超级管理员，可以修改所有用户的密码
        if (Objects.equals(roleId, RoleCode.SUPER_ADMIN)) {
            if (!isPasswordValid(resetPasswordDTO.getNewPsw())) {
                throw new RuntimeException();
            }

            return true;
        }

        throw new RuntimeException();
    }


    private boolean isPasswordValid(String psw) {
        //todo 密码合法性判定
        return psw.length() >= 8;
    }


}
