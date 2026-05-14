package cn.zuo.controller.admin;

import cn.zuo.dto.userdto.UserPageQueryDto;
import cn.zuo.dto.userdto.UserUpdateDTO;
import cn.zuo.entity.User;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.UserService;
import cn.zuo.vo.admin.AdminUserStatsVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@Slf4j
@Tag(name = "管理员用户管理", description = "管理员对用户的管理操作")
public class AdminUserController {

    @Resource
    private UserService userService;

    @GetMapping
    @Operation(summary = "获取用户列表", description = "分页获取所有用户信息")
    public Result<PageResult> getUserList(UserPageQueryDto userPageQueryDto) {
        PageResult pageResult = userService.getUserList(userPageQueryDto);
        return Result.success(pageResult);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取详细信息")
    public Result<User> getUserDetail(@Parameter(description = "用户ID") @PathVariable Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping
    @Operation(summary = "管理员更新用户信息", description = "管理员更新用户的基本信息")
    public Result<String> updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("更新用户信息:{}", userUpdateDTO);
        userService.updateUserById(userUpdateDTO);
        return Result.success("用户信息更新成功");
    }

    @PostMapping("/{userId}/deactivate")
    @Operation(summary = "停用用户", description = "管理员停用用户账号")
    public Result<String> deactivateUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        userService.updateUserStatus(userId, "INACTIVE");
        return Result.success("用户已停用");
    }

    @PostMapping("/{userId}/activate")
    @Operation(summary = "激活用户", description = "管理员激活用户账号")
    public Result<String> activateUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        userService.updateUserStatus(userId, "ACTIVE");
        return Result.success("用户已激活");
    }

    @PostMapping("/{userId}/ban")
    @Operation(summary = "封禁用户", description = "管理员封禁用户账号")
    public Result<String> banUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        userService.updateUserStatus(userId, "BANNED");
        return Result.success("用户已封禁");
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "管理员删除用户账号（软删除）")
    public Result<String> deleteUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        userService.deleteUserById(userId);
        return Result.success("用户已删除");
    }


    @GetMapping("/stats")
    @Operation(summary = "用户统计", description = "获取用户统计数据")
    public Result<AdminUserStatsVo> getUserStats() {
        AdminUserStatsVo userStatsVo = userService.getUserStats();
        return Result.success(userStatsVo);
    }

    @PostMapping("/{userId}/promote")
    @Operation(summary = "提升用户权限", description = "将普通用户提升为管理员")
    public Result<String> promoteUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        log.info("提升用户权限: {}", userId);
        userService.updateUserRole(userId, "admin");
        return Result.success("用户权限提升成功");
    }

}