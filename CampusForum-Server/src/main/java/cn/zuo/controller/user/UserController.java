package cn.zuo.controller.user;

import cn.zuo.dto.userdto.UserChangePasswordDTO;
import cn.zuo.dto.userdto.UserLoginDTO;
import cn.zuo.dto.userdto.UserRegisterDTO;
import cn.zuo.dto.userdto.UserUpdateDTO;
import cn.zuo.entity.User;
import cn.zuo.result.Result;
import cn.zuo.service.UserService;
import cn.zuo.vo.uservo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<UserRegisterVO> register (@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        UserRegisterVO userRegisterVO = userService.userRegister(userRegisterDTO);
        return Result.success(userRegisterVO);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户通过用户名和密码进行登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.userLogin(userLoginDTO);
        return Result.success(userLoginVO);
    }

    @GetMapping("/getUserById/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    public Result<User> getUserInfo(@PathVariable Long userId) {
        log.info("根据id获取用户信息：{}", userId);
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/getUserStats/{userId}")
    @Operation(summary = "获取用户统计信息", description = "根据用户ID获取用户统计信息")
    public Result<UserStatsVo> getUserStats(@PathVariable Long userId) {
        log.info("获取用户统计信息：{}", userId);
        UserStatsVo userStatsVo = userService.getUserStatsByUserId(userId);
        return Result.success(userStatsVo);
    }


    @GetMapping("/getUserByUsername/{username}")
    @Operation(summary = "根据用户名获取用户信息", description = "根据用户名获取用户详细信息")
    public Result<User> getUserByUsername(@Parameter(description = "用户名") @PathVariable String username) {
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping
    @Operation(summary = "更新用户信息", description = "更新用户的基本信息")
    public Result<String> updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("更新用户信息:{}", userUpdateDTO);
        userService.updateUserById(userUpdateDTO);
        return Result.success("用户信息更新成功");
    }

    @PutMapping("/change-password")
    @Operation(summary = "修改密码", description = "修改用户密码")
    public Result<String> changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        log.info("用户修改密码：{}", userChangePasswordDTO);
        userService.changePassword(userChangePasswordDTO);
        return Result.success("密码修改成功");
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "注销用户", description = "根据用户ID注销用户")
    public Result<String> deleteUserById(@PathVariable Long userId) {
        log.info("注销用户：{}", userId);
        userService.deleteUserById(userId);
        return Result.success("用户注销成功");
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出系统")
    public Result<String> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;
        userService.logout(token);
        return Result.success("用户登出成功");
    }

    @GetMapping("/stats")
    @Operation(summary = "获取登录统计", description = "获取在线用户数和今日登录次数")
    public Result<UserLoginStatsVo> getLoginStats() {
        //TODO
        UserLoginStatsVo stats = userService.getLoginStats();
        return Result.success(stats);
    }

    @GetMapping("/overview")
    @Operation(summary = "系统总览", description = "获取系统总览数据")
    public Result<UserOverviewDataVo> getSystemOverview() {
        return Result.success(userService.getSystemOverview());
    }

}