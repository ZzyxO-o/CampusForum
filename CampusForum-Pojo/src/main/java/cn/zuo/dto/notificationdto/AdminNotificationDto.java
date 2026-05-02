package cn.zuo.dto.notificationdto;

import lombok.Data;

@Data
public class AdminNotificationDto {
    private Long adminId;
    private Long userId;
    private String title;
    private String content;
}
