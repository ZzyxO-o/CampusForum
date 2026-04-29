package cn.zuo.dto.userdto;

import lombok.Data;

@Data
public class UserChangePasswordDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
