package org.demo.permission.controller;

import lombok.AllArgsConstructor;
import org.demo.common.domain.vo.Result;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/internal/permissions")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InternalPermissionController {

    private PermissionService permissionService;

    @PostMapping("/default-bing/{userId}")
    public void bingDefaultRole(@PathVariable Long userId) {
        permissionService.bindDefaultRole(userId);
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
