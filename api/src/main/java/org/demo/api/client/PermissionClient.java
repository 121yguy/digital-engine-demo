package org.demo.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "permission-service", path = "/internal/permissions")
public interface PermissionClient {

    @PostMapping("/default-bing/{userId}")
    void bindDefaultRole(@PathVariable Long userId);

}