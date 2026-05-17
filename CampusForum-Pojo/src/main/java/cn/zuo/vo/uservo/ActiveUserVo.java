package cn.zuo.vo.uservo;

import lombok.Data;

/**
 * 活跃用户 VO
 */
@Data
public class ActiveUserVo {
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String bio;
    private String college;

    /**
     * 发帖数
     */
    private Long postCount;

    /**
     * 回复数
     */
    private Long replyCount;

    /**
     * 获得的点赞数
     */
    private Long receivedLikeCount;

    /**
     * 活跃度分数（综合指标）
     */
    private Long activityScore;
}
