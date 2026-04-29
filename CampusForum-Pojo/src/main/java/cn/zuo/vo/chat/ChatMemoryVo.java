package cn.zuo.vo.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMemoryVo {
    private Long id; // 主键ID
    private String conversationId; // 会话ID = userId_sessionId
    private String content; // 消息内容
    private String type; // 消息类型，可选值：user(用户)、assistant(助手)
    private LocalDateTime timestamp; // 消息时间
}
