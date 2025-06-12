package org.demo.permission.service.impl;

import lombok.AllArgsConstructor;
import org.demo.common.constant.RoleConstants;
import org.demo.permission.dao.PermissionDao;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao;

    @Override
    public void bindDefaultRole(Long userId) {
        permissionDao.insertUserRole(userId, RoleConstants.USER);
    }

    @Override
    public String getUserRoleCode(Long userId) {
        Long roleIdByUserId = permissionDao.getRoleIdByUserId(userId);
        return String.valueOf(roleIdByUserId);
    }

    @Override
    public void upgradeToAdmin(Long userId) {
        permissionDao.updateUserRole(userId, RoleConstants.ADMIN);
    }

    @Override
    public void downgradeToUser(Long userId) {
        permissionDao.updateUserRole(userId, RoleConstants.USER);
    }
}
