package org.demo.permission.controller;

import lombok.AllArgsConstructor;
import org.demo.common.domain.vo.Result;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionController {

    private PermissionService permissionService;

    @GetMapping("/get-role-code/{userId}")
    public Result<String> getRoleCode(@PathVariable Long userId) {
        return Result.success(permissionService.getUserRoleCode(userId));
    }

    @PutMapping("/admin/upgrade-to-admin")
    public Result<Boolean> upgradeToAdmin(@RequestParam("userId") Long userId) {
        permissionService.upgradeToAdmin(userId);
        return Result.success(true);
    }

    @PutMapping("/admin/downgrade-to-user")
    public Result<Boolean> downgradeToUser(@RequestParam("userId") Long userId) {
        permissionService.downgradeToUser(userId);
        return Result.success(true);
    }

}
