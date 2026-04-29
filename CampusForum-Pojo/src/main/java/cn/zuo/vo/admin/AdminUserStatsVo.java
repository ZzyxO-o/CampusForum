package cn.zuo.vo.admin;

import lombok.Data;

@Data
public class AdminUserStatsVo {
    private Long totalUsers;
    private Long totalActiveUsers;
    private Long totalInactiveUsers;
    private Long totalBannedUsers;
}
