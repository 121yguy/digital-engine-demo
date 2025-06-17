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


    @PutMapping("/super-admin/upgrade-to-admin")
    public Result<Boolean> upgradeToAdmin(@RequestParam("user-id") Long userId) {
        permissionService.upgradeToAdmin(userId);
        return Result.success(true);
    }

    @PutMapping("/super-admin/downgrade-to-user")
    public Result<Boolean> downgradeToUser(@RequestParam("user-id") Long userId) {
        permissionService.downgradeToUser(userId);
        return Result.success(true);
    }

}
