package cn.zuo.dto.likedto;

import lombok.Data;

@Data
public class LikeDto {
    private Long userId;
    private String targetType;  // 目标类型: discussion/reply
    private Long targetId;      // 目标ID
}