package cn.zuo.dto.feedbackdto;

import lombok.Data;

/**
 * 反馈列表分页查询参数（GET 请求，Query String 传参）
 *
 * 请求示例:
 * /api/feedbacks?page=1&size=10&status=待处理
 * /api/admin/feedbacks?page=1&size=20&status=待处理
 */
@Data
public class FeedbackPageQueryDto {
    /** 页码，从1开始，默认1 */
    private Integer page = 1;
    /** 每页条数，默认10 */
    private Integer size = 10;
    /**
     * 按状态筛选（可选），传空或不传时查全部
     * 可选值: "待处理" | "处理中" | "已解决" | "已关闭"
     */
    private String status;
}
