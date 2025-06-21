package org.demo.user.controller;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "注册",
            description = "用于用户注册账号。用户需提供username、password、email和phone。系统将校验提交信息的合法性"
    )
    @PostMapping("/user/register")
    public Result<Boolean> register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        userService.register(registerDTO, ServletUtil.getClientIP(request));
        return Result.success(true);
    }

    @Operation(
            summary = "登录",
            description = "用于用户登录账号。用户需提供username和password，系统将校验提交信息的合法性"
    )
    @PostMapping("/user/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @Operation(
            summary = "重新设置密码",
            description = "修改自己的密码只需传入原密码和新密码，若是管理员修改他人密码则要传入被修改人的user_id和新密码"
    )
    @PostMapping("/user/reset-password")
    public Result<Boolean> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,
                                         @RequestHeader("X-User-ID") Long userId,
                                         @RequestHeader("X-Role-ID") Long roleId) {
        userService.resetPassword(resetPasswordDTO, userId, roleId);
        return Result.success(true);
    }

    @Operation(
            summary = "分页获取用户列表",
            description = "请求时在请求参数中附上start-user-id为上次查询的最后一个用户的user_id，若第一次查询使其为0即可，系统将根据用户权限给出不同结果"
    )
    @GetMapping("/users")
    public Result<List<UserVO>> getUsers(@RequestParam("start-user-id") Long startUserId,
                                         @RequestHeader("X-Role-ID") Long roleId,
                                         @RequestHeader("X-User-ID") Long userId) {
        return Result.success(userService.getUsers(startUserId, roleId, userId));
    }

    @Operation(
            summary = "获取用户信息",
            description = "请求时在请求路径中附上被查询者的user_id，系统将根据用户权限确认是否返回该用户信息"
    )
    @GetMapping("/user/{userId}")
    public Result<UserVO> getUser(@PathVariable("userId") Long userId,
                                @RequestHeader("X-User-ID") Long uid,
                                @RequestHeader("X-Role-ID") Long roleId) {
        return Result.success(userService.getUserInfo(userId, uid, roleId));
    }

    @Operation(
            summary = "修改个人信息",
            description = "支持修改邮箱和电话号码，请求路径附上被修改人的user_id，系统会自行判断是否有权限修改此用户的个人信息"
    )
    @PutMapping("/user/{userId}")
    public Result<Boolean> updateUserInfo(@PathVariable Long userId,
                                      @RequestHeader("X-User-ID") Long uid,
                                      @RequestHeader("X-Role-ID") Long roleId,
                                      @RequestBody UserInfoDTO userInfoDTO) {
        userService.updateUser(userId, uid, roleId, userInfoDTO);
        return Result.success(true);
    }

}

