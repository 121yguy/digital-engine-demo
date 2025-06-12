package org.demo.user.controller;

import lombok.AllArgsConstructor;
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
    public Result<Boolean> resetPassword(long id) {
        return Result.success(userService.resetPassword(id));
    }

    @GetMapping("/users")
    public Result<List<User>> getUsers() {
        return Result.success(userService.getUsers());
    }

    @GetMapping("/user/{userId}")
    public Result<User> getUser(@PathVariable long userId) {
        return Result.success(userService.getUser(userId));
    }

    @PutMapping("/user/{userId}")
    public Result<Boolean> updateUser(@PathVariable long userId, User user) {
        return Result.success(userService.updateUser(userId, user));
    }

}

