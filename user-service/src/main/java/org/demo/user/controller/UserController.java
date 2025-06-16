package org.demo.user.controller;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.AllArgsConstructor;
import org.demo.common.domain.dto.LoginDTO;
import org.demo.common.domain.dto.RegisterDTO;
import org.demo.common.domain.dto.ResetPasswordDTO;
import org.demo.common.domain.dto.UserInfoDTO;
import org.demo.common.domain.vo.Result;
import org.demo.common.domain.vo.UserVO;
import org.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @PostMapping("/user/register")
    public Result<Boolean> register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        return Result.success(userService.register(registerDTO, ServletUtil.getClientIP(request)));
    }

    @PostMapping("/user/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @PostMapping("/user/reset-password")
    public Result<Boolean> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,
                                         @RequestHeader("X-User-ID") Long userId,
                                         @RequestHeader("X-Role-ID") Long roleId) {
        return Result.success(userService.resetPassword(resetPasswordDTO, userId, roleId));
    }

    @GetMapping("/users")
    public Result<List<UserVO>> getUsers(@RequestParam("start-user-id") Long startUserId,
                                         @RequestHeader("X-Role-ID") Long roleId,
                                         @RequestHeader("X-User-ID") Long userId) {
        return Result.success(userService.getUsers(startUserId, roleId, userId));
    }

    @GetMapping("/user/{userId}")
    public Result<UserVO> getUser(@PathVariable("userId") Long userId,
                                @RequestHeader("X-User-ID") Long uid,
                                @RequestHeader("X-Role-ID") Long roleId) {
        return Result.success(userService.getUserInfo(userId, uid, roleId));
    }

    @PutMapping("/user/{userId}")
    public Result<Boolean> updateUserInfo(@PathVariable Long userId,
                                      @RequestHeader("X-User-ID") Long uid,
                                      @RequestHeader("X-Role-ID") Long roleId,
                                      @RequestBody UserInfoDTO userInfoDTO) {
        return Result.success(userService.updateUser(userId, uid, roleId, userInfoDTO));
    }

}

