package org.demo.permission.controller;

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

    @GetMapping("/get-role-code/{userId}")
    public String getRoleCode(@PathVariable("userId") Long userId) {
        return permissionService.getUserRoleCode(userId);
    }

    @PostMapping("/default-bind/{userId}")
    public void bindDefaultRole(@PathVariable("userId") Long userId) {
        permissionService.bindDefaultRole(userId);
    }

    @PostMapping("/super-admin-bind/{userId}")
    void bindSuperAdmin(@PathVariable("userId") Long userId) {
        permissionService.bingSuperAdmin(userId);
    }

    @GetMapping("/get-user-ids")
    List<Long> getUserIds(@RequestParam("role-id") Long roleId,
                          @RequestParam(name = "start-user-id") Long startUserId) {
        return permissionService.getUserIds(startUserId, roleId);
    }

    @GetMapping("/get-role")
    Long getRoleByUserId(@RequestParam("user-id") Long userId) {
        return permissionService.getRoleId(userId);
    }

}
