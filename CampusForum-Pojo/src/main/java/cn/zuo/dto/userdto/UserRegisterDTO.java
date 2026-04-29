package cn.zuo.dto.userdto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username; //用户名

    private String password; //密码

    private String nickname; //昵称

    private String college; //学院

    private String email; //邮箱

    private String bio; //个人简介
}