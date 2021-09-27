SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ec_address
-- ----------------------------
DROP TABLE IF EXISTS `ec_address`;
CREATE TABLE `ec_address`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wx_ma_user_id` int(11) NULL DEFAULT NULL,
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_del` int(3) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_cart
-- ----------------------------
DROP TABLE IF EXISTS `ec_cart`;
CREATE TABLE `ec_cart`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wx_ma_user_id` int(11) NULL DEFAULT NULL,
  `sku_id` int(11) NULL DEFAULT NULL,
  `count` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_category
-- ----------------------------
DROP TABLE IF EXISTS `ec_category`;
CREATE TABLE `ec_category`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wx_app_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_express_type
-- ----------------------------
DROP TABLE IF EXISTS `ec_express_type`;
CREATE TABLE `ec_express_type`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1271 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_order
-- ----------------------------
DROP TABLE IF EXISTS `ec_order`;
CREATE TABLE `ec_order`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wx_app_id` int(11) NULL DEFAULT NULL,
  `wx_ma_user_id` int(11) NULL DEFAULT NULL,
  `address_id` int(11) NULL DEFAULT NULL,
  `out_trade_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `transaction_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_fee` int(11) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 0,
  `trade_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `express_type_id` int(11) NULL DEFAULT NULL,
  `express_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_del` int(3) NULL DEFAULT 0,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `success_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniqOutTradeNo`(`out_trade_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_order_affiliate
-- ----------------------------
DROP TABLE IF EXISTS `ec_order_affiliate`;
CREATE TABLE `ec_order_affiliate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NULL DEFAULT NULL,
  `sku_id` int(11) NULL DEFAULT NULL,
  `count` int(11) NULL DEFAULT NULL,
  `price` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_price
-- ----------------------------
DROP TABLE IF EXISTS `ec_price`;
CREATE TABLE `ec_price`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NULL DEFAULT NULL,
  `policy` int(255) NULL DEFAULT NULL,
  `price` int(10) NULL DEFAULT NULL COMMENT '分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_product
-- ----------------------------
DROP TABLE IF EXISTS `ec_product`;
CREATE TABLE `ec_product`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `category_id` int(11) NULL DEFAULT NULL,
  `is_del` tinyint(4) NULL DEFAULT 0,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ec_sku
-- ----------------------------
DROP TABLE IF EXISTS `ec_sku`;
CREATE TABLE `ec_sku`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `specs` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_del` tinyint(4) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for gg_intro
-- ----------------------------
DROP TABLE IF EXISTS `gg_intro`;
CREATE TABLE `gg_intro`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tab_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `sort` int(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for wx_app
-- ----------------------------
DROP TABLE IF EXISTS `wx_app`;
CREATE TABLE `wx_app`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `auth` int(3) NOT NULL DEFAULT 1,
  `wxa_code_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for wx_ma_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_ma_user`;
CREATE TABLE `wx_ma_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wx_app_id` int(11) NULL DEFAULT NULL,
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unionid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `language` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `country` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `city` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `session_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `session_wx_app_id` int(11) NULL DEFAULT NULL COMMENT '当前session_key所属的app_id',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_openid`(`openid`) USING BTREE,
  INDEX `session_key_index`(`session_key`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 414 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

SET FOREIGN_KEY_CHECKS = 1;
