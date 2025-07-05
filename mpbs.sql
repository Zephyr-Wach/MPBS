/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : mpbs

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 26/05/2025 15:15:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `blog_comment` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `blog_post` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------

-- ----------------------------
-- Table structure for blog_post
-- ----------------------------
DROP TABLE IF EXISTS `blog_post`;
CREATE TABLE `blog_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_md` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cover_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author_id` bigint NOT NULL,
  `status` enum('draft','published') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'draft',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `author_id`(`author_id` ASC) USING BTREE,
  CONSTRAINT `blog_post_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_post
-- ----------------------------

-- ----------------------------
-- Table structure for file_permissions
-- ----------------------------
DROP TABLE IF EXISTS `file_permissions`;
CREATE TABLE `file_permissions`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `file_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_id` int NOT NULL,
  `can_read` tinyint(1) NULL DEFAULT 0,
  `can_download` tinyint(1) NULL DEFAULT 0,
  `can_update` tinyint(1) NULL DEFAULT 0,
  `can_delete` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `file_id`(`file_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_permissions
-- ----------------------------

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`  (
  `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `storage_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uploader_id` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `size` bigint NULL DEFAULT NULL,
  `mime_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_public` tinyint(1) NULL DEFAULT 0,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of files
-- ----------------------------

-- ----------------------------
-- Table structure for medias
-- ----------------------------
DROP TABLE IF EXISTS `medias`;
CREATE TABLE `medias`  (
  `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `storage_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uploader_id` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `size` bigint NULL DEFAULT NULL,
  `mime_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_public` tinyint(1) NULL DEFAULT 0,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medias
-- ----------------------------

-- ----------------------------
-- Table structure for sidebar_menu
-- ----------------------------
DROP TABLE IF EXISTS `sidebar_menu`;
CREATE TABLE `sidebar_menu`  (
    `id` bigint NOT NULL,
    `parent_id` bigint NULL DEFAULT NULL,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `permission_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `order_num` int NULL DEFAULT NULL,
    `is_visible` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sidebar_menu
-- ----------------------------
INSERT INTO `sidebar_menu` VALUES (1, NULL, '首页', '/', NULL, NULL, NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (2, NULL, 'Blog', '/blog', NULL, NULL, NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (3, NULL, '笔记&合集', '/gather', NULL, NULL, NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (10, NULL, '管理', NULL, NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (11, 10, '云盘', '/cloud', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (12, 10, '媒体管理', '/admin/clouds', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (13, 10, '发表Blog', '/admin/deliver', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (14, 10, '发表笔记&合集', '/admin/postNote', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (15, 10, 'Blog管理', '/admin/blogs', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (16, 10, '笔记&合集管理', '/admin/gather', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (28, 10, '用户管理', '/admin/users', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (29, 10, '日志列表', '/admin/log', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (30, NULL, '管理', NULL, NULL, 'ROLE_SENIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (31, 30, '云盘', '/cloud', NULL, 'ROLE_SENIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (32, 30, '媒体管理', '/admin/clouds', NULL, 'ROLE_SENIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (35, 30, '用户管理', '/admin/users', NULL, 'ROLE_SENIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (36, 30, '日志列表', '/admin/log', NULL, 'ROLE_SENIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (40, NULL, '管理', NULL, NULL, 'ROLE_INTERMEDIATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (41, 40, '用户管理', '/admin/users', NULL, 'ROLE_INTERMEDIATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (42, 40, '日志列表', '/admin/log', NULL, 'ROLE_INTERMEDIATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (50, NULL, '管理', NULL, NULL, 'ROLE_JUNIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (51, 50, '日志列表', '/admin/log', NULL, 'ROLE_JUNIOR', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (60, NULL, '关于&联系', '/about', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for temporary_links
-- ----------------------------
DROP TABLE IF EXISTS `temporary_links`;
CREATE TABLE `temporary_links`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'uuid()',
  `file_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `temporary_links_ibfk_1`(`file_id` ASC) USING BTREE,
  CONSTRAINT `temporary_links_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of temporary_links
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_permission` enum('NORMAL','JUNIOR','INTERMEDIATE','SENIOR','ULTIMATE') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NORMAL',
  `email_status` enum('unconfirmed','confirmed','undefined') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'undefined',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uniq_user_name`(`user_name` ASC) USING BTREE,
  UNIQUE INDEX `uniq_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1000000000, 'admin', '$2a$10$A7UvX3POX7I9F.Es8V/aa.CSzjerknd/V1dhGFidDohbsSAmdeMYu', '', '', 'ULTIMATE', 'undefined');

-- ----------------------------
-- Triggers structure for table blog_post
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_blog_post_before_insert`;
delimiter ;;
CREATE TRIGGER `trg_blog_post_before_insert` BEFORE INSERT ON `blog_post` FOR EACH ROW BEGIN
  IF NEW.created_at IS NULL THEN
    SET NEW.created_at = NOW();
  END IF;
  SET NEW.updated_at = NOW();
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table blog_post
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_blog_post_before_update`;
delimiter ;;
CREATE TRIGGER `trg_blog_post_before_update` BEFORE UPDATE ON `blog_post` FOR EACH ROW BEGIN
  SET NEW.updated_at = NOW();
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table files
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_after_insert_files`;
delimiter ;;
CREATE TRIGGER `trg_after_insert_files` AFTER INSERT ON `files` FOR EACH ROW BEGIN
  INSERT INTO file_permissions (file_id, role_id, can_read, can_download, can_update, can_delete)
  VALUES (NEW.id, NEW.uploader_id, 1, 1, 1, 1);
END
;;
delimiter ;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
    `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `operation_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
    `source_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP',
    `target_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器IP',
    `source_port` int NULL DEFAULT NULL COMMENT '客户端端口',
    `client_hardware` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '客户端硬件信息(User-Agent)',
    `operation_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作详情',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_account`(`user_account` ASC) USING BTREE,
    INDEX `idx_operation_time`(`operation_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '笔记标题',
    `content_md` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'Markdown 内容',
    `is_public` tinyint(1) NULL DEFAULT 0 COMMENT '是否公开（默认私有）',
    `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for note_collection
-- ----------------------------
DROP TABLE IF EXISTS `note_collection`;
CREATE TABLE `note_collection`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '合集ID',
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '合集标题',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '合集描述',
    `author_id` bigint NOT NULL COMMENT '作者ID',
    `is_public` tinyint(1) NULL DEFAULT 0 COMMENT '是否公开（默认私有）',
    `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for note_collection_relation
-- ----------------------------
DROP TABLE IF EXISTS `note_collection_relation`;
CREATE TABLE `note_collection_relation`  (
    `collection_id` bigint NOT NULL COMMENT '合集ID',
    `note_id` bigint NOT NULL COMMENT '笔记ID',
    `order_index` int NULL DEFAULT 0 COMMENT '合集中的顺序',
    PRIMARY KEY (`collection_id`, `note_id`) USING BTREE,
    INDEX `note_id`(`note_id` ASC) USING BTREE,
    CONSTRAINT `note_collection_relation_ibfk_1` FOREIGN KEY (`collection_id`) REFERENCES `note_collection` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `note_collection_relation_ibfk_2` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;