package cn.zuo.dto.replydto;

import lombok.Data;

@Data
public class ReplyUpdateDto {
    private Long id;           // 回复ID
    private String content;     // 回复内容
}