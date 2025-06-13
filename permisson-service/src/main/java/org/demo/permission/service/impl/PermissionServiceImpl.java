package org.demo.permission.service.impl;

import lombok.AllArgsConstructor;
import org.demo.common.constant.RoleCode;
import org.demo.permission.dao.PermissionDao;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao;

    @Override
    public void bindDefaultRole(Long userId) {
        permissionDao.insertUserRole(userId, RoleCode.USER);
    }

    @Override
    public String getUserRoleCode(Long userId) {
        return permissionDao.getRoleCodeByUserId(userId);
    }

    @Override
    public void upgradeToAdmin(Long userId) {
        permissionDao.updateUserRole(userId, RoleCode.ADMIN);
    }

    @Override
    public void downgradeToUser(Long userId) {
        permissionDao.updateUserRole(userId, RoleCode.USER);
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
}
