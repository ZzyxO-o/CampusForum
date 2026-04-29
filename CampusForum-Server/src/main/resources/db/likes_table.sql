-- 点赞表创建SQL
-- 表名: likes
-- 描述: 存储用户点赞信息（支持讨论帖和回复的点赞）
USE `campus_forum`;

CREATE TABLE `likes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '点赞ID，主键，自增',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，外键关联users表',
  `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型：discussion-讨论帖, reply-回复',
  `target_id` BIGINT NOT NULL COMMENT '目标ID（讨论帖ID或回复ID）',
  `status` INT NOT NULL DEFAULT 1 COMMENT '点赞状态：1-已点赞, 0-已取消',
  `created_time` DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_user_target` (`user_id`, `target_type`, `target_id`) COMMENT '用户和目标组合唯一索引',
  INDEX `idx_user_id` (`user_id` ASC) COMMENT '用户ID索引',
  INDEX `idx_target` (`target_type`, `target_id`) COMMENT '目标类型和ID组合索引',
  INDEX `idx_status` (`status` ASC) COMMENT '状态索引',
  CONSTRAINT `fk_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) COMMENT = '点赞表';

-- 添加表注释
ALTER TABLE `likes` COMMENT = '点赞表，存储用户对讨论帖和回复的点赞关系，支持取消点赞功能';