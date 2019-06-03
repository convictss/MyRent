/*
Navicat MySQL Data Transfer

Source Server         : localhost3306
Source Server Version : 50149
Source Host           : localhost:3306
Source Database       : db_gzzf

Target Server Type    : MYSQL
Target Server Version : 50149
File Encoding         : 65001

Date: 2019-06-03 09:26:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for 58tongcheng
-- ----------------------------
DROP TABLE IF EXISTS `58tongcheng`;
CREATE TABLE `58tongcheng` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` longtext,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for 58tongcheng_image
-- ----------------------------
DROP TABLE IF EXISTS `58tongcheng_image`;
CREATE TABLE `58tongcheng_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_58tongcheng_image` (`rent_id`) USING BTREE,
  CONSTRAINT `fk_58tongcheng_image` FOREIGN KEY (`rent_id`) REFERENCES `58tongcheng` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `user_photo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for anjuke
-- ----------------------------
DROP TABLE IF EXISTS `anjuke`;
CREATE TABLE `anjuke` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3871 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for anjuke_image
-- ----------------------------
DROP TABLE IF EXISTS `anjuke_image`;
CREATE TABLE `anjuke_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anjuke_image` (`rent_id`),
  CONSTRAINT `fk_anjuke_image` FOREIGN KEY (`rent_id`) REFERENCES `anjuke` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baixingwang
-- ----------------------------
DROP TABLE IF EXISTS `baixingwang`;
CREATE TABLE `baixingwang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baixingwang_image
-- ----------------------------
DROP TABLE IF EXISTS `baixingwang_image`;
CREATE TABLE `baixingwang_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_baixingwang_image` (`rent_id`) USING BTREE,
  CONSTRAINT `fk_baixingwang` FOREIGN KEY (`rent_id`) REFERENCES `baixingwang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for fangtianxia
-- ----------------------------
DROP TABLE IF EXISTS `fangtianxia`;
CREATE TABLE `fangtianxia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for fangtianxia_image
-- ----------------------------
DROP TABLE IF EXISTS `fangtianxia_image`;
CREATE TABLE `fangtianxia_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fangtianxia_image` (`rent_id`),
  CONSTRAINT `fk_fangtianxia_image` FOREIGN KEY (`rent_id`) REFERENCES `fangtianxia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ganjiwang
-- ----------------------------
DROP TABLE IF EXISTS `ganjiwang`;
CREATE TABLE `ganjiwang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ganjiwang_image
-- ----------------------------
DROP TABLE IF EXISTS `ganjiwang_image`;
CREATE TABLE `ganjiwang_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anjuke_image` (`rent_id`),
  CONSTRAINT `fk_ganjiwang_image` FOREIGN KEY (`rent_id`) REFERENCES `ganjiwang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for leyoujia
-- ----------------------------
DROP TABLE IF EXISTS `leyoujia`;
CREATE TABLE `leyoujia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for leyoujia_image
-- ----------------------------
DROP TABLE IF EXISTS `leyoujia_image`;
CREATE TABLE `leyoujia_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anjuke_image` (`rent_id`),
  CONSTRAINT `fk_leyoujia_image` FOREIGN KEY (`rent_id`) REFERENCES `leyoujia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for lianjia
-- ----------------------------
DROP TABLE IF EXISTS `lianjia`;
CREATE TABLE `lianjia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for lianjia_image
-- ----------------------------
DROP TABLE IF EXISTS `lianjia_image`;
CREATE TABLE `lianjia_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anjuke_image` (`rent_id`),
  CONSTRAINT `fk_lianjia_image` FOREIGN KEY (`rent_id`) REFERENCES `lianjia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for qfangwang
-- ----------------------------
DROP TABLE IF EXISTS `qfangwang`;
CREATE TABLE `qfangwang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `community` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `house_type` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `price` int(255) DEFAULT NULL,
  `deposit_type` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `traffic_route` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for qfangwang_image
-- ----------------------------
DROP TABLE IF EXISTS `qfangwang_image`;
CREATE TABLE `qfangwang_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `rent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anjuke_image` (`rent_id`),
  CONSTRAINT `fk_qfangwang_image` FOREIGN KEY (`rent_id`) REFERENCES `qfangwang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for spider_log
-- ----------------------------
DROP TABLE IF EXISTS `spider_log`;
CREATE TABLE `spider_log` (
  `uuid` varchar(255) NOT NULL,
  `spider_name` varchar(255) DEFAULT NULL,
  `data_source` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `crawl_second` int(11) DEFAULT NULL,
  `page_count` int(11) DEFAULT NULL,
  `spider_status` int(11) DEFAULT NULL,
  `spider_status_message` longtext,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
