-- 收藏表创建SQL
-- 表名: favorites
-- 描述: 存储用户收藏讨论帖的信息
USE `campus_forum`;

CREATE TABLE `favorites` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键，自增',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，外键关联users表',
  `discussion_id` BIGINT NOT NULL COMMENT '讨论ID，外键关联discussions表',
  `status` INT NOT NULL DEFAULT 1 COMMENT '收藏状态：1-已收藏, 0-已取消',
  `created_time` DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_user_discussion` (`user_id`, `discussion_id`) COMMENT '用户和讨论组合唯一索引',
  INDEX `idx_user_id` (`user_id` ASC) COMMENT '用户ID索引',
  INDEX `idx_discussion_id` (`discussion_id` ASC) COMMENT '讨论ID索引',
  INDEX `idx_status` (`status` ASC) COMMENT '状态索引',
  CONSTRAINT `fk_favorites_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorites_discussion_id` FOREIGN KEY (`discussion_id`) REFERENCES `discussions` (`id`) ON DELETE CASCADE
) COMMENT = '收藏表';

-- 添加表注释
ALTER TABLE `favorites` COMMENT = '收藏表，存储用户对讨论帖的收藏关系，支持取消收藏功能';