package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知实体类
 * 对应数据库 notifications 表
 */
@Data
@TableName("notifications")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id; // 通知ID，主键，自增

    @TableField("user_id")
    private Long userId; // 接收通知的用户ID

    @TableField("type")
    private String type; // 通知类型：like-点赞, reply-回复, favorite-收藏, system-系统通知

    @TableField("related_id")
    private Long relatedId; // 相关内容ID（帖子ID、回复ID等）

    @TableField("sender_id")
    private Long senderId; // 发送者ID（如果是用户发送的通知）

    @TableField("title")
    private String title; // 通知标题

    @TableField("content")
    private String content; // 通知内容

    @TableField("is_read")
    private Boolean isRead; // 是否已读

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime; // 创建时间
}