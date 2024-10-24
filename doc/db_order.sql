/*
 Navicat Premium Data Transfer

 Source Server         : 传一
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : db_order

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 26/09/2024 12:16:44
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for main_db_order
-- ----------------------------
DROP TABLE IF EXISTS `main_db_order`;
CREATE TABLE `main_db_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户ID',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成的订单号（唯一',
  `customer_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的支付账号',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '该次订单的价格',
  `currency` int NULL DEFAULT 0 COMMENT '0：人民币 1：美金',
  `channel` int NULL DEFAULT NULL COMMENT '0：支付宝 1：微信',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for withdraw_order
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_order`;
CREATE TABLE `withdraw_order`  (
  `id` int NOT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `tax` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手续费',
  `withdraw_account` int NULL DEFAULT NULL COMMENT '提现账户',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提现时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
