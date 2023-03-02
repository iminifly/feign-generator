/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 80016
Source Host           : 
Source Database       : 

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2020-11-20 14:46:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for feign_client
-- ----------------------------
DROP TABLE IF EXISTS `feign_client`;
CREATE TABLE `feign_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `feign_class_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fallback_factory_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `feign_class_content` mediumtext COLLATE utf8mb4_general_ci,
  `fallback_factory_content` mediumtext COLLATE utf8mb4_general_ci,
  `size` bigint(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for feign_model
-- ----------------------------
DROP TABLE IF EXISTS `feign_model`;
CREATE TABLE `feign_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `package_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
  `model_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `model_class_content` mediumtext COLLATE utf8mb4_general_ci,
  `size` bigint(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for feign_project
-- ----------------------------
DROP TABLE IF EXISTS `feign_project`;
CREATE TABLE `feign_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `project_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `service_version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `feign_zip_file` mediumblob,
  `size` bigint(20) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` int(255) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_project_name_unique` (`group_id`,`project_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
