-- 用户表创建SQL
-- 表名: users
-- 描述: 存储系统用户信息
USE `ai_learning_partner`;

CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名，长度3-50个字符，不能为空',
  `email` VARCHAR(100) NOT NULL COMMENT '用户邮箱，用于登录和通信，不能为空',
  `password` VARCHAR(255) NOT NULL COMMENT '用户密码，加密存储，不能为空',
  `avatar_url` VARCHAR(500) NULL COMMENT '用户头像URL，可选字段',
  `bio` VARCHAR(500) NULL COMMENT '用户个人简介，可选字段',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '用户状态：ACTIVE-活跃，INACTIVE-未激活，SUSPENDED-已暂停',
  `created_time` DATETIME NULL COMMENT '用户创建时间',
  `updated_time` DATETIME NULL COMMENT '用户最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `users_email` (`email` ASC) COMMENT '邮箱唯一索引',
  UNIQUE INDEX `users_username` (`username` ASC) COMMENT '用户名唯一索引'
) COMMENT = '用户信息表';

-- 添加表注释
ALTER TABLE `users` COMMENT = '用户信息表，存储系统所有用户的基本信息和相关状态';

-- 创建索引以优化查询性能
CREATE INDEX `idx_status` ON `users` (`status` ASC) COMMENT '用户状态索引';
CREATE INDEX `idx_created_time` ON `users` (`created_time` ASC) COMMENT '创建时间索引';