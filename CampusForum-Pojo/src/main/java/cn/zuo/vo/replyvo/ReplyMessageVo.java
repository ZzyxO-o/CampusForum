package cn.zuo.vo.replyvo;

import lombok.Data;

@Data
public class ReplyMessageVo {
    private Long replyId;   // 回复ID
    private String message; // 操作结果消息
}