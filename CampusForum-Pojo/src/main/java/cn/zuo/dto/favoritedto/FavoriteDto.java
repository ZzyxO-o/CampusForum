package cn.zuo.dto.favoritedto;

import lombok.Data;

@Data
public class FavoriteDto {
    private Long userId;
    private Long discussionId;  // 讨论ID
}