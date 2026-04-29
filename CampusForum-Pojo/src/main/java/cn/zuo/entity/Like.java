package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 点赞实体类
 * 对应数据库 likes 表
 */
@Data
@TableName("likes")
public class Like {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("target_type")
    private String targetType; // discussion 或 reply

    @TableField("target_id")
    private Long targetId;

    @TableField("status")
    private Integer status; // 1-已点赞, 0-已取消

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
