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

 Date: 23/05/2025 16:47:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_post
-- ----------------------------
DROP TABLE IF EXISTS `blog_post`;
CREATE TABLE `blog_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_md` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `cover_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author_id` bigint NOT NULL,
  `status` enum('draft','published') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'draft',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `author_id`(`author_id` ASC) USING BTREE,
  CONSTRAINT `blog_post_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 100000010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sidebar_menu` VALUES (3, 4, '云盘', '/cloud', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (4, NULL, '管理', NULL, NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (5, 4, '用户管理', '/admin/users', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (6, 4, 'Blog管理', '/admin/blogs', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (7, 4, '媒体管理', '/admin/clouds', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (8, 4, '发表Blog', '/admin/deliver', NULL, 'ROLE_ULTIMATE', NULL, NULL);
INSERT INTO `sidebar_menu` VALUES (9, NULL, '关于&联系', '/about', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for temporary_links
-- ----------------------------
DROP TABLE IF EXISTS `temporary_links`;
CREATE TABLE `temporary_links`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'uuid()',
  `file_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
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
  `user_permission` enum('NORMAl','JUNIOR','INTERMEDIATE','SENIOR','ULTIMATE') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NORMAl',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000000014 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1000000010, 'admin', 'admin', '', 'http://localhost:6688/static/2e3148a20aeb48b6a7b30acad9edda1a.png', 'ULTIMATE');
INSERT INTO `user_info` VALUES (1000000011, 'tester', 'tester', '', 'http://localhost:6688/static/33e1fdc1cb99445e8d76921bb73bd936.png', 'NORMAl');

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

SET FOREIGN_KEY_CHECKS = 1;
