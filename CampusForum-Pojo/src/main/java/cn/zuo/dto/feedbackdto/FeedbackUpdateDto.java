package cn.zuo.dto.feedbackdto;

import lombok.Data;

/**
 * 管理员更新反馈状态的请求体（PUT /api/admin/feedbacks/{id}）
 *
 * 注意: id 从 URL 路径获取，status 从请求体获取
 * 请求示例: PUT /api/admin/feedbacks/5  body: {"status": "处理中"}
 */
@Data
public class FeedbackUpdateDto {
    /** 反馈ID，由Controller层从路径参数注入，前端在请求体中可不传或传null */
    private Long id;
    /**
     * 新状态（必填），有效状态流转:
     * 待处理 → 处理中 → 已解决 → 已关闭
     * 可选值: "待处理" | "处理中" | "已解决" | "已关闭"
     */
    private String status;
}
