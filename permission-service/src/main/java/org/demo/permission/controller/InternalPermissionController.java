package org.demo.permission.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/permissions")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InternalPermissionController {

    private PermissionService permissionService;

    @Operation(
            summary = "获取用户的角色",
            description = "内部接口，请求时在请求路径上附上被查询用户的user_id"
    )
    @GetMapping("/get-role-code/{userId}")
    public String getRoleCode(@PathVariable("userId") Long userId) {
        return permissionService.getUserRoleCode(userId);
    }

    @Operation(
            summary = "绑定用户为普通用户",
            description = "内部接口，请求时在请求路径上附上被绑定用户的user_id"
    )
    @PostMapping("/default-bind/{userId}")
    public void bindDefaultRole(@PathVariable("userId") Long userId) {
        permissionService.bindDefaultRole(userId);
    }

    @Operation(
            summary = "绑定用户为超级管理员",
            description = "内部接口，请求时在请求路径上附上被绑定用户的user_id"
    )
    @PostMapping("/super-admin-bind/{userId}")
    void bindSuperAdmin(@PathVariable("userId") Long userId) {
        permissionService.bingSuperAdmin(userId);
    }


    @Operation(
            summary = "分页获取用户的user_id",
            description = "内部接口，请求时在请求参数中附上start-user-id为上次查询的最后一个用户的user_id，若第一次查询使其为0即可，系统将根据用户权限给出不同结果"
    )
    @GetMapping("/get-user-ids")
    List<Long> getUserIds(@RequestParam("role-id") Long roleId,
                          @RequestParam(name = "start-user-id") Long startUserId) {
        return permissionService.getUserIds(startUserId, roleId);
    }

    @Operation(
            summary = "获取用户角色的role_id",
            description = "内部接口，请求时在请求参数附上user-id为被查询用户的user_id"
    )
    @GetMapping("/get-role")
    Long getRoleByUserId(@RequestParam("user-id") Long userId) {
        return permissionService.getRoleId(userId);
    }

}
