package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 讨论帖实体类
 * 对应数据库 discussions 表
 */
@Data
@TableName("discussions")
public class Discussion {
    @TableId(type = IdType.AUTO)
    private Long id; // 讨论ID，主键，自增

    @TableField("title")
    private String title; // 讨论标题

    @TableField("content")
    private String content; // 讨论内容

    @TableField("annex_url")
    private String annexUrl; // 附件URL，逗号分隔

    @TableField("user_id")
    private Long userId; // 发布者ID，外键关联users表

    @TableField("category")
    private String category; // 讨论分类：learnAndCommunicate-学习交流, campusLife-校园生活, JobHuntingAndEmployment-求职就业, ClubActivities-社团活动

    @TableField("tags")
    private String tags; // 标签，逗号分隔

    @TableField("status")
    private String status; // 讨论状态：active-活跃, closed-隐藏, deleted-已删除

    @TableField("view_count")
    private Integer viewCount; // 浏览量

    @TableField("reply_count")
    private Integer replyCount; // 回复数量

    @TableField("like_count")
    private Integer likeCount; // 点赞数量

    @TableField("favorite_count")
    private Integer favoriteCount; // 收藏数量

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime; // 创建时间

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime; // 更新时间
}