package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户反馈实体类（API响应中直接返回此实体）
 * 对应数据库 feedbacks 表
 *
 * JSON序列化时 LocalDateTime 会转为 "yyyy-MM-ddTHH:mm:ss" 格式字符串
 * 例如: "2026-05-16T14:30:00"
 */
@Data
@TableName("feedbacks")
public class Feedback {
    /** 反馈ID，创建时无需传，由后端自动生成 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 提交用户的ID，后端从JWT中获取，前端无需传 */
    @TableField("user_id")
    private Long userId;

    /**
     * 反馈类型（必填）
     * 可选值: "Bug报告" | "体验反馈" | "功能建议"
     */
    @TableField("type")
    private String type;

    /** 反馈标题，最长200字符 */
    @TableField("title")
    private String title;

    /** 反馈详细内容，TEXT类型，无长度限制 */
    @TableField("content")
    private String content;

    /**
     * 处理状态，提交时后端自动设为 "待处理"
     * 可选值: "待处理" | "处理中" | "已解决" | "已关闭"
     */
    @TableField("status")
    private String status;

    /** 创建时间，后端自动填充 */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /** 最后更新时间，后端自动填充 */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
