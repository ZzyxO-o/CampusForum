package cn.zuo.vo.uservo;

import lombok.Data;

@Data
public class ActiveUserVo {
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String bio;
    private String college;
    private Long postCount;
    private Long replyCount;
    private Long receivedLikeCount;
    private Long activityScore;
}
