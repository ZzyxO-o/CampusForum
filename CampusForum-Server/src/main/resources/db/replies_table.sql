-- 回复表创建SQL
-- 表名: replies
-- 描述: 存储讨论帖的回复信息
USE `campus_forum`;

CREATE TABLE `replies` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '回复ID，主键，自增',
  `content` TEXT NOT NULL COMMENT '回复内容，不能为空',
  `user_id` BIGINT NOT NULL COMMENT '回复者ID，外键关联users表',
  `discussion_id` BIGINT NOT NULL COMMENT '所属讨论ID，外键关联discussions表',
  `reply_layer` INT NOT NULL DEFAULT 0 COMMENT '回复层级，0表示顶级回复，1表示子回复，2类推',
  `parent_reply_id` BIGINT NULL COMMENT '父级回复ID，用于嵌套回复',
  `status` VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '回复状态：active-活跃, hidden-隐藏, deleted-已删除',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数量',
  `created_time` DATETIME NULL COMMENT '创建时间',
  `updated_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) COMMENT '用户ID索引',
  INDEX `idx_discussion_id` (`discussion_id` ASC) COMMENT '讨论ID索引',
  INDEX `idx_parent_reply_id` (`parent_reply_id` ASC) COMMENT '父级回复ID索引',
  INDEX `idx_status` (`status` ASC) COMMENT '状态索引',
  INDEX `idx_created_time` (`created_time` ASC) COMMENT '创建时间索引',
  CONSTRAINT `fk_replies_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_replies_discussion_id` FOREIGN KEY (`discussion_id`) REFERENCES `discussions` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_replies_parent_reply_id` FOREIGN KEY (`parent_reply_id`) REFERENCES `replies` (`id`) ON DELETE CASCADE
) COMMENT = '回复表';

-- 添加表注释
ALTER TABLE `replies` COMMENT = '回复表，存储讨论帖的所有回复信息，支持嵌套回复功能';

