package org.demo.permission.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.common.constant.RoleCode;
import org.demo.permission.dao.PermissionDao;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao;

    @Override
    public void bindDefaultRole(Long userId) {
        permissionDao.insertUserRole(userId, RoleCode.USER);
        log.info("用户{}被绑定为普通用户", userId);
    }

    @Override
    public String getUserRoleCode(Long userId) {
        return permissionDao.getRoleCodeByUserId(userId);
    }

    @Override
    public void upgradeToAdmin(Long userId) {
        permissionDao.updateUserRole(userId, RoleCode.ADMIN);
        log.info("用户{}被授予管理员权限", userId);
    }

    @Override
    public void downgradeToUser(Long userId) {
        permissionDao.updateUserRole(userId, RoleCode.USER);
        log.info("用户{}管理员权限被撤销", userId);
    }

    @Override
    public List<Long> getUserIds(Long startUserId, Long roleId) {
        // 管理员获取所有普通用户
        if (roleId.equals(RoleCode.ADMIN)) {
            return permissionDao.getUserIdsByStartUserId(startUserId, 10, RoleCode.USER);
        }

        // 超级管理员获取所有用户
        if (roleId.equals(RoleCode.SUPER_ADMIN)) {
            return permissionDao.getUserIdsByStartUserId(startUserId, 10, null);
        }

        return Collections.emptyList();
    }

    @Override
    public Long getRoleId(Long userId) {
        return permissionDao.getRoleIdByUserId(userId);
    }

    @Override
    public void bingSuperAdmin(Long userId) {
        permissionDao.insertUserRole(userId, RoleCode.SUPER_ADMIN);
    }
}
