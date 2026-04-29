package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库 users 表
 */
@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id; // 用户ID，主键，自增

    @TableField("username")
    private String username; // 用户名，唯一

    @TableField("password")
    private String password; // 密码（加密存储）

    @TableField("email")
    private String email; // 邮箱，唯一

    @TableField("nickname")
    private String nickname; // 昵称

    @TableField("college")
    private String college; // 学院

    @TableField("avatar_url")
    private String avatarUrl; // 头像URL

    @TableField("bio")
    private String bio; // 个人简介

    @TableField("phone")
    private String phone; // 手机号，唯一

    @TableField("role")
    private String role; // 用户角色：user-普通用户, admin-管理员

    @TableField("status")
    private String status; // 用户状态：ACTIVE-正常, INACTIVE-禁用

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime; // 创建时间

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime; // 更新时间
}