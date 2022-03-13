/*
 Navicat Premium Data Transfer

 Source Server         : 175.24.229.41_mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 175.24.229.41:1617
 Source Schema         : hairy_children

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 13/03/2022 16:05:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adopt
-- ----------------------------
DROP TABLE IF EXISTS `adopt`;
CREATE TABLE `adopt`  (
  `adopt_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '领养 id',
  `user_id` bigint(20) NOT NULL COMMENT '申请人 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `adopt_reason` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '申请理由',
  `adopt_user_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人当前所在城市',
  `adopt_user_phone` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人联系方式',
  `adopt_concept` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '养宠理念',
  `adopt_way` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接动物方式',
  `adopt_user_age` int(11) NULL DEFAULT NULL COMMENT '申请人年龄',
  `adopt_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片存储路径',
  `imgs_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片url',
  `is_return_visit` int(11) NOT NULL DEFAULT 1 COMMENT '是否接受定期回访, 0 : 否, 1 : 是',
  `is_feedback` int(11) NOT NULL DEFAULT 1 COMMENT '是否接受定期反馈领养信息, 0 : 否, 1 : 是',
  `is_success` int(11) NOT NULL DEFAULT 0 COMMENT '是否成功, 0 : 未成功, 1 : 成功',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`adopt_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for adopt_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `adopt_mailbox`;
CREATE TABLE `adopt_mailbox`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `adopt_id` bigint(20) NOT NULL COMMENT '领养 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送者 id',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接受者 id',
  `is_read` int(11) NOT NULL DEFAULT 0 COMMENT '是否已阅读, 0 : 否, 1 :是',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for clue
-- ----------------------------
DROP TABLE IF EXISTS `clue`;
CREATE TABLE `clue`  (
  `clue_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '线索 id',
  `user_id` bigint(20) NOT NULL COMMENT '提供者 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `clue_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '线索内容',
  `clue_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片存储路径',
  `imgs_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片url',
  `is_success` int(11) NOT NULL DEFAULT 0 COMMENT '是否成功, 0 : 未成功, 1 : 成功',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`clue_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for clue_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `clue_mailbox`;
CREATE TABLE `clue_mailbox`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `clue_id` bigint(20) NOT NULL COMMENT '线索 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送者 id',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接受者 id',
  `is_read` int(11) NOT NULL DEFAULT 0 COMMENT '是否已阅读, 0 : 否, 1 :是',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for collection_title
-- ----------------------------
DROP TABLE IF EXISTS `collection_title`;
CREATE TABLE `collection_title`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `collection_id` bigint(20) NOT NULL COMMENT '收藏夹 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comment_like_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `comment_like_mailbox`;
CREATE TABLE `comment_like_mailbox`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `root_id` bigint(20) NOT NULL COMMENT '父评论 id',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送者 id',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接受者 id',
  `is_read` int(11) NOT NULL DEFAULT 0 COMMENT '是否已阅读, 0 : 否, 1 :是',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comment_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `comment_mailbox`;
CREATE TABLE `comment_mailbox`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `root_id` bigint(20) NOT NULL COMMENT '父评论 id',
  `comment_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送者 id',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接受者 id',
  `is_read` int(11) NOT NULL DEFAULT 0 COMMENT '是否已阅读, 0 : 否, 1 :是',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `comment_reply`;
CREATE TABLE `comment_reply`  (
  `reply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '子评论 id',
  `root_id` bigint(20) NOT NULL COMMENT '父评论 id',
  `reply_comment_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的子评论 id',
  `reply_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的子评论 用户 id',
  `user_id` bigint(20) NOT NULL COMMENT '评论者 id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`reply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comment_root
-- ----------------------------
DROP TABLE IF EXISTS `comment_root`;
CREATE TABLE `comment_root`  (
  `root_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '父评论 id',
  `answer_id` bigint(20) NOT NULL COMMENT '文章 id',
  `user_id` bigint(20) NOT NULL COMMENT '评论者 id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `reply_count` int(11) NOT NULL DEFAULT 0 COMMENT '回复数',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`root_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for reply_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `reply_mailbox`;
CREATE TABLE `reply_mailbox`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `root_id` bigint(20) NOT NULL COMMENT '父评论 id',
  `reply_id` bigint(20) NOT NULL COMMENT '子评论 id',
  `reply_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送者 id',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接受者 id',
  `is_read` int(11) NOT NULL DEFAULT 0 COMMENT '是否已阅读, 0 : 否, 1 :是',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for title
-- ----------------------------
DROP TABLE IF EXISTS `title`;
CREATE TABLE `title`  (
  `title_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章 id',
  `user_id` bigint(20) NOT NULL COMMENT '作者 id',
  `user_address` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者 城市',
  `title_type` int(11) NOT NULL COMMENT '文章类型, 1 : 领养, 2 : 寻宠',
  `title_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章标题',
  `title_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章正文',
  `title_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章图片路径',
  `collection_count` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数',
  `comment_count` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `is_finish` int(11) NOT NULL DEFAULT 0 COMMENT '是否完成, 0 : 未完成, 1 : 完成',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`title_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for title_like_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `title_like_mailbox`;
CREATE TABLE `title_like_mailbox`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `title_id` bigint(20) NOT NULL COMMENT '文章 id',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送者 id',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接受者 id',
  `is_read` int(11) NOT NULL DEFAULT 0 COMMENT '是否已阅读, 0 : 否, 1 :是',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户 id',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_salt` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户密码盐',
  `user_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `user_email` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_collection`;
CREATE TABLE `user_collection`  (
  `collection_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏夹 id',
  `user_id` bigint(20) NOT NULL COMMENT '用户 id',
  `collection_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收藏夹名',
  `collection_count` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数量',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`collection_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `user_id` bigint(20) NOT NULL COMMENT '用户 id',
  `user_real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户真实姓名',
  `user_identity_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户身份证',
  `user_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户地址',
  `address_show` int(11) NOT NULL DEFAULT 0 COMMENT '是否展示地址 0 : 不展示, 1 : 展示',
  `user_gender` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户性别',
  `gender_show` int(11) NOT NULL DEFAULT 0 COMMENT '是否展示性别 0 : 不展示, 1 : 展示',
  `user_birth` datetime NULL DEFAULT NULL COMMENT '用户生日',
  `birth_show` int(11) NOT NULL DEFAULT 0 COMMENT '是否展示生日 0 : 不展示, 1 : 展示',
  `user_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_autograph` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户个性签名',
  `user_pet_cond` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目前养宠情况',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
