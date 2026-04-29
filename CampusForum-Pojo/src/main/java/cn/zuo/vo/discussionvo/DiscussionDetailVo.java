package cn.zuo.vo.discussionvo;

import cn.zuo.entity.Reply;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiscussionDetailVo {
    private String title; // 讨论标题

    private String content; // 讨论内容

    private String annexUrl; // 附件URL，逗号分隔

    private Long userId; // 发布者ID，外键关联users表

    private String category; // 讨论分类：learnAndCommunicate-学习交流, campusLife-校园生活, JobHuntingAndEmployment-求职就业, ClubActivities-社团活动

    private String tags; // 标签，逗号分隔

    private String status; // 讨论状态：active-活跃, closed-隐藏, deleted-已删除

    private Integer viewCount; // 浏览量

    private Integer replyCount; // 回复数量

    private Integer likeCount; // 点赞数量

    private Integer favoriteCount; // 收藏数量

    private List<Reply> replies; //回复列表

    private LocalDateTime createdTime; // 创建时间

    private LocalDateTime updatedTime; // 更新时间
}