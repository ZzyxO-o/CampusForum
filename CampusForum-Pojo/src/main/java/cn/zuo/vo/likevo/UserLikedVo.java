package cn.zuo.vo.likevo;

import lombok.Data;

@Data
public class UserLikedVo {
    private Long userId;
    private String targetType;
    private Long targetId;
}
