/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云-Qq20001114!
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 101.33.218.191:3306
 Source Schema         : db_order

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 10/10/2024 16:26:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db123456
-- ----------------------------
DROP TABLE IF EXISTS `db123456`;
CREATE TABLE `db123456`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成的订单号（唯一',
  `customer_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的支付账号',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '该次订单的价格',
  `currency` int NULL DEFAULT 0 COMMENT '0：人民币 1：美金',
  `channel` int NULL DEFAULT NULL COMMENT '0：支付宝 1：微信',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
