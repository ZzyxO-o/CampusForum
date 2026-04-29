package cn.zuo.dto.replydto;

import lombok.Data;

@Data
public class ReplyDto {
    private Long discussionId;      // 讨论ID
    private Long parentReplyId;     // 父回复ID（可选）
    private String content;         // 回复内容
    private Integer replyLayer;     // 回复层级（可选，如果不提供则自动计算）
}