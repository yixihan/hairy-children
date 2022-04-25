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

 Date: 25/04/2022 21:59:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `user_id` bigint(20) NOT NULL COMMENT '用户 id',
  `user_real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户真实姓名',
  `user_identity_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户身份证',
  `user_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户地址',
  `address_show` int(11) NOT NULL DEFAULT 0 COMMENT '是否展示地址 0 : 不展示, 1 : 展示',
  `user_gender` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户性别',
  `gender_show` int(11) NOT NULL DEFAULT 0 COMMENT '是否展示性别 0 : 不展示, 1 : 展示',
  `user_birth` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '用户生日',
  `birth_show` int(11) NOT NULL DEFAULT 0 COMMENT '是否展示生日 0 : 不展示, 1 : 展示',
  `user_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '//avatar//default.png' COMMENT '用户头像',
  `user_autograph` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户个性签名',
  `user_pet_cond` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '目前养宠情况',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
