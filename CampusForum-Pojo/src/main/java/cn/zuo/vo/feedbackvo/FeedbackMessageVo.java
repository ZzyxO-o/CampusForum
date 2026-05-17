package cn.zuo.vo.feedbackvo;

import lombok.Data;

/**
 * 提交反馈成功后的响应
 *
 * 响应示例:
 * {
 *   "code": 200,
 *   "message": "成功",
 *   "data": {
 *     "feedbackId": 42,
 *     "message": "反馈提交成功，感谢您的反馈！"
 *   }
 * }
 */
@Data
public class FeedbackMessageVo {
    /** 新创建的反馈ID */
    private Long feedbackId;
    /** 操作结果提示信息 */
    private String message;
}
