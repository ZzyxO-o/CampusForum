package cn.zuo.dto.likedto;

import lombok.Data;

@Data
public class LikePageQueryDto {
    private Long userId;
    private String targetType;  // 目标类型: discussion/reply
    private Integer page = 1;   // 页码 默认为第一页
    private Integer size = 10;  // 每页数量 默认为10条
}
