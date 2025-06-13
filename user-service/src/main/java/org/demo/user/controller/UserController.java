package org.demo.user.controller;

import lombok.AllArgsConstructor;
import org.demo.common.domain.dto.ResetPasswordDTO;
import org.demo.common.domain.po.User;
import org.demo.common.domain.vo.Result;
import org.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @PostMapping("/user/register")
    public Result<Boolean> register(User user) {
        return Result.success(userService.register(user));
    }

    @PostMapping("/user/login")
    public Result<String> login(User user) {
        return Result.success(userService.login(user));
    }

    @PostMapping("/user/reset-password")
    public Result<Boolean> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,
                                         @RequestHeader("X-User-ID") Long userId,
                                         @RequestHeader("X-Role-ID") Long roleId) {
        return Result.success(userService.resetPassword(resetPasswordDTO, userId, roleId));
    }

    @GetMapping("/users")
    public Result<List<User>> getUsers(@RequestParam("start-user-id") Long startUserId,
                                       @RequestHeader("X-Role-ID") Long roleId,
                                       @RequestHeader("X-User-ID") Long userId) {
        return Result.success(userService.getUsers(startUserId, roleId, userId));
    }

    @GetMapping("/user/{userId}")
    public Result<User> getUser(@PathVariable("userId") Long userId,
                                @RequestHeader("X-User-ID") Long uid,
                                @RequestHeader("X-Role-ID") Long roleId) {
        return Result.success(userService.getUser(userId, uid, roleId));
    }

    @PutMapping("/user/{userId}")
    public Result<Boolean> updateUser(@PathVariable Long userId,
                                      @RequestHeader("X-User-ID") Long uid,
                                      @RequestHeader("X-Role-ID") Long roleId,
                                      @RequestBody User user) {
        return Result.success(userService.updateUser(userId, uid, roleId, user));
    }

}

