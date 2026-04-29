package cn.zuo.vo.uservo;

import lombok.Data;

@Data
public class UserStatsVo {
    private Long postCount;
    private Long replyCount;
    private Long likeCount;
    private Long favoriteCount;
}
