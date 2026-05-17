package cn.zuo.dto.feedbackdto;

import lombok.Data;

/**
 * 提交反馈的请求体（POST /api/feedbacks）
 *
 * 请求示例:
 * {
 *   "type": "Bug报告",
 *   "title": "登录页面按钮点击无响应",
 *   "content": "在Chrome浏览器中，点击登录按钮后没有任何反应..."
 * }
 */
@Data
public class FeedbackDto {
    /** 反馈类型（必填），可选值: "Bug报告" | "体验反馈" | "功能建议" */
    private String type;
    /** 反馈标题（必填），最长200字符 */
    private String title;
    /** 反馈详细内容（必填），无长度限制 */
    private String content;
}
