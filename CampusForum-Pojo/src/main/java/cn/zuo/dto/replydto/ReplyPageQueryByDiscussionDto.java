package cn.zuo.dto.replydto;

import lombok.Data;

@Data
public class ReplyPageQueryByDiscussionDto {
    private Long discussionId;  // 讨论ID
    private Long userId;        // 用户ID（用于查询用户回复）
    private Integer replyLayer; // 回复层级（可选，用于筛选特定层级的回复）
    private Integer page = 1;   // 页码 默认为第一页
    private Integer size = 10;  // 每页数量 默认为10条
}