package cn.zuo.controller.admin;

import cn.zuo.dto.notificationdto.AdminNotificationDto;
import cn.zuo.result.Result;
import cn.zuo.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/notification")
@Slf4j
public class AdminNotificationController {

    @Resource
    private NotificationService notificationService;

    @PostMapping("/system")
    @Operation(summary = "发送系统通知", description = "管理员向指定用户发送系统通知")
    public Result<String> sendSystemNotification(@RequestBody AdminNotificationDto adminNotificationDto) {
        log.info("发送系统通知: userId={}, title={}", adminNotificationDto.getUserId(), adminNotificationDto.getTitle());
        notificationService.sendSystemNotification(adminNotificationDto);
        return Result.success("系统通知发送成功");
    }

    @PostMapping("/broadcast")
    @Operation(summary = "发送广播通知", description = "管理员向所有用户发送广播通知")
    public Result<String> sendBroadcastNotification(@RequestBody AdminNotificationDto adminNotificationDto) {
        log.info("发送广播通知: title={}", adminNotificationDto.getTitle());
        notificationService.sendBroadcastNotification(adminNotificationDto);
        return Result.success("广播通知发送成功");
    }
}
