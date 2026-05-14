package cn.zuo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatVo {
    private LocalDate date;
    private Long newUsers;
    private Long newDiscussions;
    private Long newReplies;
}
