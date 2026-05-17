package cn.zuo.constant.businessConstant;

/**
 * 反馈模块常量
 * 前端可根据这些常量来做下拉选项和状态展示的文案映射
 */
public class FeedbackConstants {

    /* ===== 反馈类型（创建时由前端传入，中文值直接存入数据库）===== */
    public static final String FEEDBACK_TYPE_BUG = "Bug报告";
    public static final String FEEDBACK_TYPE_EXPERIENCE = "体验反馈";
    public static final String FEEDBACK_TYPE_SUGGESTION = "功能建议";

    /* ===== 反馈处理状态（状态流转: 待处理 → 处理中 → 已解决 → 已关闭）===== */
    public static final String FEEDBACK_STATUS_PENDING = "待处理";       // 新提交的默认状态
    public static final String FEEDBACK_STATUS_PROCESSING = "处理中";
    public static final String FEEDBACK_STATUS_RESOLVED = "已解决";
    public static final String FEEDBACK_STATUS_CLOSED = "已关闭";

    /* ===== 接口返回的消息文案 ===== */
    public static final String FEEDBACK_DELETE_SUCCESS = "反馈删除成功";
    public static final String FEEDBACK_SUBMIT_SUCCESS = "反馈提交成功，感谢您的反馈！";
    public static final String FEEDBACK_UPDATE_SUCCESS = "反馈状态更新成功";
    public static final String FEEDBACK_NOT_FOUND = "反馈不存在";
    public static final String FEEDBACK_PERMISSION_DENIED = "您没有权限操作此反馈";
}
