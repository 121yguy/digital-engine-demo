package org.demo.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "permission-service", path = "/internal/permissions")
public interface PermissionClient {

    @PostMapping("/default-bing/{userId}")
    void bindDefaultRole(@PathVariable("userId") Long userId);

    @GetMapping("/get-user-ids")
    List<Long> getUserIds(@RequestParam("role-id") Long roleId,
                          @RequestParam(name = "start-user-id") Long startUserId);


    @GetMapping("/get-role")
    Long getRoleByUserId(@RequestParam("user-id") Long userId);

}