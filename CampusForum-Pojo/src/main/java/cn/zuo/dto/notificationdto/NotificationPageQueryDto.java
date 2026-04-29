package cn.zuo.dto.notificationdto;

import lombok.Data;

@Data
public class NotificationPageQueryDto {
    private Long userId;
    private Integer page = 1;   // 页码 默认为第一页
    private Integer size = 10;  // 每页数量 默认为10条
    private String type; //all,like,favorite,reply,system
}