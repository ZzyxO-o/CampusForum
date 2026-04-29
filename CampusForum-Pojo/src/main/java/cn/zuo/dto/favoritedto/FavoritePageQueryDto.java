package cn.zuo.dto.favoritedto;

import lombok.Data;

@Data
public class FavoritePageQueryDto {
    private Long userId;
    private Integer page = 1;   // 页码 默认为第一页
    private Integer size = 10;  // 每页数量 默认为10条
}
