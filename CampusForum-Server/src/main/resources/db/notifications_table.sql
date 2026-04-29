-- 通知表创建SQL
-- 表名: notifications
-- 描述: 存储系统通知信息
USE `campus_forum`;

CREATE TABLE `notifications` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID，主键，自增',
  `user_id` BIGINT NOT NULL COMMENT '接收通知的用户ID，外键关联users表',
  `type` VARCHAR(20) NOT NULL COMMENT '通知类型：like-点赞, reply-回复, favorite-收藏, system-系统通知',
  `related_id` BIGINT NULL COMMENT '相关内容ID（帖子ID、回复ID等）',
  `sender_id` BIGINT NULL COMMENT '发送者ID，外键关联users表（如果是用户发送的通知）',
  `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
  `content` TEXT NULL COMMENT '通知内容',
  `is_read` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否已读：true-已读, false-未读',
  `created_time` DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) COMMENT '用户ID索引',
  INDEX `idx_type` (`type` ASC) COMMENT '通知类型索引',
  INDEX `idx_sender_id` (`sender_id` ASC) COMMENT '发送者ID索引',
  INDEX `idx_is_read` (`is_read` ASC) COMMENT '已读状态索引',
  INDEX `idx_created_time` (`created_time` ASC) COMMENT '创建时间索引',
  CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_notifications_sender_id` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) COMMENT = '通知表';

-- 添加表注释
ALTER TABLE `notifications` COMMENT = '通知表，存储系统所有通知信息，包括点赞、回复、收藏等用户互动通知和系统通知';