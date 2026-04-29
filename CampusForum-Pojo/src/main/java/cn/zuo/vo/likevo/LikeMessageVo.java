package cn.zuo.vo.likevo;

import lombok.Data;

@Data
public class LikeMessageVo {
    private boolean isLiked;   // 是否已点赞
    private int likeCount;     // 点赞数
    private String message;    // 操作结果消息
}