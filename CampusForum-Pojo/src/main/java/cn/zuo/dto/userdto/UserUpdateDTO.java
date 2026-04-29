package cn.zuo.dto.userdto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Long userId;
    private String nickname;
    private String college;
    private String avatarUrl;
    private String phone;
    private String bio;
}
