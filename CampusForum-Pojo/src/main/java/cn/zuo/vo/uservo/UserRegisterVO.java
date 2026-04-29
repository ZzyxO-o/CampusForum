package cn.zuo.vo.uservo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserRegisterVO {
    private Long id;
    private String username;
    private String nickname;
    private String college;
    private String avatarUrl;
    private String bio;
    private String role;
    private String status;
    private LocalDateTime createdTime;
}