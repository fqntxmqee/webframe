-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.4.1-beta-community-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema websitecms
--

CREATE DATABASE IF NOT EXISTS websitecms;USE websitecms;


--
-- Definition of table `file_need_del`
--

DROP TABLE IF EXISTS `file_need_del`;
CREATE TABLE `file_need_del` (
  `id_` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `file_path_` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `file_name_` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
