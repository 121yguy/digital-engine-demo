package org.demo.permission.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "将普通用户升级为管理员",
            description = "请求时在请求参数中附上被升级权限用户的user_id，系统确保只有超级管理员能访问此接口"
    )
    @PutMapping("/super-admin/upgrade-to-admin")
    public Result<Boolean> upgradeToAdmin(@RequestParam("user-id") Long userId) {
        permissionService.upgradeToAdmin(userId);
        return Result.success(true);
    }

    @Operation(
            summary = "将普管理员降级为普通用户",
            description = "请求时在请求参数中附上被降级权限用户的user_id，系统确保只有超级管理员能访问此接口"
    )
    @PutMapping("/super-admin/downgrade-to-user")
    public Result<Boolean> downgradeToUser(@RequestParam("user-id") Long userId) {
        permissionService.downgradeToUser(userId);
        return Result.success(true);
    }

}
