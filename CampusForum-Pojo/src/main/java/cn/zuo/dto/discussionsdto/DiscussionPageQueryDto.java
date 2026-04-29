package cn.zuo.dto.discussionsdto;

import lombok.Data;

@Data
public class DiscussionPageQueryDto {
    private Integer page = 1; //页码 默认为第一页
    private Integer size = 10; //每页数量 默认为10条
    private String category = "all"; //分类 默认为全部
    private String tags; //标签
    private String keyword; //搜索关键词
}
