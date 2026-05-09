package cn.zuo.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionsVo {
    private String sessionId;
    private String sessionName;
    private LocalDateTime sessionTime;
}
