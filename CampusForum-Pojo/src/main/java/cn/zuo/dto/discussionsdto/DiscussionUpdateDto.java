package cn.zuo.dto.discussionsdto;

import lombok.Data;

@Data
public class DiscussionUpdateDto {
    private Long id;
    private String title;
    private String content;
    private String annexUrl;
    private String category;
    private String tags;
}