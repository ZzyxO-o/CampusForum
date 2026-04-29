package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 回复实体类
 * 对应数据库 replies 表
 */
@Data
@TableName("replies")
public class Reply {
    @TableId(type = IdType.AUTO)
    private Long id; // 回复ID，主键，自增

    @TableField("content")
    private String content; // 回复内容

    @TableField("user_id")
    private Long userId; // 回复者ID，外键关联users表

    @TableField("discussion_id")
    private Long discussionId; // 所属讨论ID，外键关联discussions表

    @TableField("reply_layer")
    private Integer replyLayer; // 回复层级，0表示顶级回复，1表示子回复，2类推

    @TableField("parent_reply_id")
    private Long parentReplyId; // 父级回复ID，用于嵌套回复，为null表示顶级回复

    @TableField("status")
    private String status; // 回复状态：active-活跃, hidden-隐藏, deleted-已删除

    @TableField("like_count")
    private Integer likeCount; // 点赞数量

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime; // 创建时间

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime; // 更新时间
}