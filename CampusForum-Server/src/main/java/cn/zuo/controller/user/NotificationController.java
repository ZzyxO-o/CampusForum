package cn.zuo.controller.user;

import cn.zuo.dto.notificationdto.NotificationPageQueryDto;
import cn.zuo.entity.Notification;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "通知管理", description = "通知相关操作")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "获取所有通知列表", description = "获取当前用户的所有通知列表")
    public Result<PageResult<Notification>> getNotifications(NotificationPageQueryDto notificationPageQueryDto) {
        log.info("获取通知列表: {}", notificationPageQueryDto);
        return Result.success(notificationService.getUserNotifications(notificationPageQueryDto));
    }


    @GetMapping("/unread/count")
    @Operation(summary = "获取未读通知数量", description = "获取当前用户的未读通知数量")
    public Result<Integer> getUnreadCount() {
        log.info("获取未读通知数量");
        int count = notificationService.getUnreadCount();
        return Result.success(count);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记通知为已读", description = "将指定通知标记为已读")
    public Result<String> markAsRead(@Parameter(description = "通知ID") @PathVariable Long id) {
        log.info("标记通知为已读: {}", id);
        notificationService.markAsRead(id);
        return Result.success("标记已读成功");
    }

    @PutMapping("/read/all")
    @Operation(summary = "标记所有通知为已读", description = "将当前用户的所有通知标记为已读")
    public Result<String> markAllAsRead() {
        log.info("标记所有通知为已读");
        notificationService.markAllAsRead();
        return Result.success("全部标记已读成功");
    }

    @DeleteMapping("/clear/all/{userId}")
    @Operation(summary = "清空通知", description = "清空当前用户的所有通知")
    public Result<String> clearAllNotifications(@Parameter(description = "用户ID") @PathVariable Long userId) {
        log.info("清空所有通知: {}", userId);
        notificationService.deleteAllNotifications(userId);
        return Result.success("通知清空成功");
    }

}