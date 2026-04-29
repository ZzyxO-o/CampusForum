package cn.zuo.record;

/**
 * AI聊天回复信息记录
 */
public record ChatInfoRecordTest(
    /**
     * AI回复的主要内容
     */
    String content,

    /**
     * 回复状态：success-成功，error-失败
     */
    String status,

    /**
     * 错误信息（如果有）
     */
    String errorMsg,

    /**
     * 回复时间戳
     */
    Long timestamp,

    /**
     * 回复ID（用于追踪）
     */
    String replyId
) {

}
