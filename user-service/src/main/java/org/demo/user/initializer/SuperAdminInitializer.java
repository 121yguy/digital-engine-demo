package org.demo.user.initializer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.user.service.AdminInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SuperAdminInitializer implements ApplicationRunner {

    private AdminInitService adminInitService;

    @Override
    public void run(ApplicationArguments args) {

        try {
            String username = "admin";

            if (adminInitService.checkAdmin(username)) {
                return;
            }

            String randomPsw = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "1aA";
            adminInitService.initAdmin(username, randomPsw);

            log.warn("系统未检测到超级管理员，已自动创建默认超级管理员账户！");
            log.warn("用户名: {}", username);
            log.warn("初始密码: {}", randomPsw);
            log.warn("请尽快登录系统修改默认密码！");
        } catch (Exception e) {
            log.error("超级管理员检测或创建失败，请检查权限系统状态后重启服务!");
        }

    }
}

