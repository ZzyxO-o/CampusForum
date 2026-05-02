package cn.zuo.vo.uservo;

import lombok.Data;

@Data
public class UserOverviewDataVo {
    private Long totalUsers; //总用户
    private Long totalDiscussions; //总帖子
    private Long newUsers; //新增用户
    private Long newDiscussions; //新增帖子
}
