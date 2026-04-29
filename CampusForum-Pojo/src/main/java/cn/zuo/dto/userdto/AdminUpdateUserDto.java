package cn.zuo.dto.userdto;

import lombok.Data;

@Data
public class AdminUpdateUserDto {
    private Long userId;
    private String nickname;
    private String college;
    private String avatarUrl;
    private String bio;
    private String role;
    private String status;
}