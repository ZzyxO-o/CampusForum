package cn.zuo.dto.discussionsdto;

import lombok.Data;

@Data
public class DiscussionDto {
    private String title;
    private String content;
    private String annexUrl;
    private String category;
    private String tags;
}