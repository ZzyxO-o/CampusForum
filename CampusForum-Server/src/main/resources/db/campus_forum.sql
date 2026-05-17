/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : campus_forum

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 02/05/2026 23:16:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE IF NOT EXISTS `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id,自增',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名 非空唯一',
  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码 非空',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `avatar_url` varchar(640) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `bio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户个人简介',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户角色',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户状态',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `college` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ai_chat_memory
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ai_chat_memory`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conversation_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `timestamp` timestamp NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `chk_message_type` CHECK (`type` in (_utf8mb4'USER',_utf8mb4'ASSISTANT',_utf8mb4'SYSTEM',_utf8mb4'TOOL'))
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for discussions
-- ----------------------------
CREATE TABLE IF NOT EXISTS `discussions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '讨论ID，主键，自增',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '讨论标题，不能为空',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '讨论内容，不能为空',
  `user_id` bigint NOT NULL COMMENT '发布者ID，外键关联users表',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '讨论分类：学习交流、校园生活、求职就业、社团活动',
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签，逗号分隔',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'active' COMMENT '讨论状态：active-活跃, closed-隐藏, deleted-已删除',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `reply_count` int NOT NULL DEFAULT 0 COMMENT '回复数量',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数量',
  `favorite_count` int NOT NULL DEFAULT 0 COMMENT '收藏数量',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `annex_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件URL，逗号分隔',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_category`(`category` ASC) USING BTREE COMMENT '分类索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '状态索引',
  INDEX `idx_created_time`(`created_time` ASC) USING BTREE COMMENT '创建时间索引',
  CONSTRAINT `fk_discussions_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '讨论帖表，存储用户发布的所有讨论帖信息和相关统计数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for replies
-- ----------------------------
CREATE TABLE IF NOT EXISTS `replies`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复ID，主键，自增',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容，不能为空',
  `user_id` bigint NOT NULL COMMENT '回复者ID，外键关联users表',
  `discussion_id` bigint NOT NULL COMMENT '所属讨论ID，外键关联discussions表',
  `parent_reply_id` bigint NULL DEFAULT NULL COMMENT '父级回复ID，用于嵌套回复，为null表示顶级回复',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'active' COMMENT '回复状态：active-活跃, hidden-隐藏, deleted-已删除',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数量',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `reply_layer` int NOT NULL DEFAULT 0 COMMENT '回复层级，0表示顶级回复，1表示子回复，2类推',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_discussion_id`(`discussion_id` ASC) USING BTREE COMMENT '讨论ID索引',
  INDEX `idx_parent_reply_id`(`parent_reply_id` ASC) USING BTREE COMMENT '父级回复ID索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '状态索引',
  INDEX `idx_created_time`(`created_time` ASC) USING BTREE COMMENT '创建时间索引',
  CONSTRAINT `fk_replies_discussion_id` FOREIGN KEY (`discussion_id`) REFERENCES `discussions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_replies_parent_reply_id` FOREIGN KEY (`parent_reply_id`) REFERENCES `replies` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_replies_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '回复表，存储讨论帖的所有回复信息，支持嵌套回复功能' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
CREATE TABLE IF NOT EXISTS `favorites`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键，自增',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联users表',
  `discussion_id` bigint NOT NULL COMMENT '讨论ID，外键关联discussions表',
  `status` int NOT NULL DEFAULT 1 COMMENT '收藏状态：1-已收藏, 0-已取消',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_discussion`(`user_id` ASC, `discussion_id` ASC) USING BTREE COMMENT '用户和讨论组合唯一索引',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_discussion_id`(`discussion_id` ASC) USING BTREE COMMENT '讨论ID索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '状态索引',
  CONSTRAINT `fk_favorites_discussion_id` FOREIGN KEY (`discussion_id`) REFERENCES `discussions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_favorites_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表，存储用户对讨论帖的收藏关系，支持取消收藏功能' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for likes
-- ----------------------------
CREATE TABLE IF NOT EXISTS `likes`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID，主键，自增',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联users表',
  `target_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型：discussion-讨论帖, reply-回复',
  `target_id` bigint NOT NULL COMMENT '目标ID（讨论帖ID或回复ID）',
  `status` int NOT NULL DEFAULT 1 COMMENT '点赞状态：1-已点赞, 0-已取消',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_target`(`user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE COMMENT '用户和目标组合唯一索引',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_target`(`target_type` ASC, `target_id` ASC) USING BTREE COMMENT '目标类型和ID组合索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '状态索引',
  CONSTRAINT `fk_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞表，存储用户对讨论帖和回复的点赞关系，支持取消点赞功能' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
CREATE TABLE IF NOT EXISTS `notifications`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID，主键，自增',
  `user_id` bigint NOT NULL COMMENT '接收通知的用户ID，外键关联users表',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知类型：like-点赞, reply-回复, favorite-收藏, system-系统通知',
  `related_id` bigint NULL DEFAULT NULL COMMENT '相关内容ID（帖子ID、回复ID等）',
  `sender_id` bigint NULL DEFAULT NULL COMMENT '发送者ID，外键关联users表（如果是用户发送的通知）',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读：true-已读, false-未读',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_type`(`type` ASC) USING BTREE COMMENT '通知类型索引',
  INDEX `idx_sender_id`(`sender_id` ASC) USING BTREE COMMENT '发送者ID索引',
  INDEX `idx_is_read`(`is_read` ASC) USING BTREE COMMENT '已读状态索引',
  INDEX `idx_created_time`(`created_time` ASC) USING BTREE COMMENT '创建时间索引',
  CONSTRAINT `fk_notifications_sender_id` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知表，存储系统所有通知信息，包括点赞、回复、收藏等用户互动通知和系统通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for feedbacks
-- ----------------------------
CREATE TABLE IF NOT EXISTS `feedbacks`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `user_id` bigint NOT NULL COMMENT '提交用户ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈类型：Bug报告、体验反馈、功能建议',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈内容',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '待处理' COMMENT '处理状态：待处理、处理中、已解决、已关闭',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_time`(`created_time` ASC) USING BTREE,
  CONSTRAINT `fk_feedbacks_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_prompt
-- ----------------------------
CREATE TABLE IF NOT EXISTS `system_prompt`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id,自增',
  `prompt_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词类型',
  `prompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提示词内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '提示词表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
