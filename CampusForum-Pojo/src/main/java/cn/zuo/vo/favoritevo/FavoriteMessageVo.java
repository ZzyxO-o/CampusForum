package cn.zuo.vo.favoritevo;

import lombok.Data;

@Data
public class FavoriteMessageVo {
    private Integer status;  // 0:取消收藏 1:收藏
    private Integer favoriteCount;     // 帖子收藏数
    private String message;         // 操作结果消息
}