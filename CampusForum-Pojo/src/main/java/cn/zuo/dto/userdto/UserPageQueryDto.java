package cn.zuo.dto.userdto;

import lombok.Data;

@Data
public class UserPageQueryDto {
    private Integer page = 1; //页码 默认为第一页
    private Integer size = 10; //每页数量 默认为10条
    private String username; //用户名搜索
    private String status; //用户状态
}
