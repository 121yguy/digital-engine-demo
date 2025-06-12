package org.demo.permission.controller;

import lombok.AllArgsConstructor;
import org.demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/permissions")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InternalPermissionController {

    private PermissionService permissionService;

    @PostMapping("/default-bing/{userId}")
    public void bingDefaultRole(@PathVariable Long userId) {
        permissionService.bindDefaultRole(userId);
    }

}
