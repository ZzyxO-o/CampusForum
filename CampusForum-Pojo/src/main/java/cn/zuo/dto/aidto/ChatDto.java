package cn.zuo.dto.aidto;

import lombok.Data;

@Data
public class ChatDto {
    private Long userId;
    private Long sessionId;
    private String userInput;
}
