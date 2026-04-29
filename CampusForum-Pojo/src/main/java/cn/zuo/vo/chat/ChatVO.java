package cn.zuo.vo.chat;

import lombok.Data;

@Data
public class ChatVO {
    private String content; //内容
    private String status; //状态
    private String errorMsg; //错误信息
    private Long timestamp; //时间戳
    private Long token;
    private Long responseTime; //响应时间
}
