CREATE DATABASE  IF NOT EXISTS `gym_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gym_system`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: gym_system
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `teacher` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` int NOT NULL,
  `max_capacity` int NOT NULL,
  `current_capacity` int DEFAULT '0',
  `course_day` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `course_time` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `img_path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'晨間哈達瑜珈','Sophia',500,20,11,'Monday','09:00','yoga_hatha.jpg'),(2,'爆燃伸展TABATA','Mark',600,25,16,'Monday','19:00','tabata_hiit.jpg'),(3,'重量訓練入門','Kevin',800,15,9,'Tuesday','18:30','weight_basic.jpg'),(4,'核心力量普拉提','Ivy',550,18,7,'Wednesday','19:00','pilates_core.jpg'),(5,'Zumba有氧派對','Elena',450,30,18,'Thursday','20:00','zumba_dance.jpg'),(6,'基礎拳擊泰拳','Bruce',900,12,5,'Friday','18:00','boxing_muay.jpg'),(7,'TRX懸吊訓練','Chris',750,15,4,'Saturday','10:00','trx_train.jpg'),(8,'冥想與深層放鬆','Sophia',400,30,2,'Sunday','20:00','meditation.jpg');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_logs`
--

DROP TABLE IF EXISTS `login_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `member_id` int DEFAULT NULL,
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_logs`
--

LOCK TABLES `login_logs` WRITE;
/*!40000 ALTER TABLE `login_logs` DISABLE KEYS */;
INSERT INTO `login_logs` VALUES (1,19,'2026-02-24 09:21:40','10.10.1.74'),(2,19,'2026-02-24 09:27:44','10.10.1.74'),(3,19,'2026-02-24 09:31:08','10.10.1.74'),(4,19,'2026-02-24 09:37:54','10.10.1.74'),(5,19,'2026-02-24 09:46:21','10.10.1.74'),(6,19,'2026-02-24 09:47:55','10.10.1.74'),(7,19,'2026-02-24 09:52:10','10.10.1.74'),(8,19,'2026-02-24 10:06:31','10.10.1.74'),(9,19,'2026-02-24 10:47:41','10.10.1.74');
/*!40000 ALTER TABLE `login_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `balance` int DEFAULT '0',
  `role` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'admin','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','admin@gym.com','0900-000-000',999999,'ADMIN'),(2,'teacher','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','teacher@gym.com','0900-111-111',88000,'USER'),(3,'student','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','student@gym.com','0900-222-222',5000,'USER'),(4,'Alice_W','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','alice@mail.com','0912-345-678',12000,'USER'),(5,'Bob_Lin','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','bob@mail.com','0922-333-444',3500,'USER'),(6,'Cindy_Chen','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','cindy@mail.com','0933-444-555',8000,'USER'),(7,'David_Kuo','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','david@mail.com','0944-555-666',200,'USER'),(8,'Eva_Zhang','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','eva@mail.com','0955-666-777',15000,'USER'),(9,'Frank_Liu','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','frank@mail.com','0966-777-888',6000,'USER'),(10,'Grace_Hsu','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','grace@mail.com','0977-888-999',4500,'USER'),(11,'Henry_Wu','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','henry@mail.com','0988-999-000',9000,'USER'),(12,'Ivy_Tsai','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','ivy@mail.com','0911-123-123',3000,'USER'),(13,'Jack_Wang','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','jack@mail.com','0922-234-234',500,'USER'),(14,'Kevin_Lee','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','kevin@mail.com','0933-345-345',20000,'USER'),(15,'Lily_Yang','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','lily@mail.com','0944-456-456',7500,'USER'),(16,'Mike_Chou','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','mike@mail.com','0955-567-567',1000,'USER'),(17,'Nina_Ho','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','nina@mail.com','0966-678-678',5500,'USER'),(18,'Oscar_Lu','$2a$10$EixZ9.5uUp.xYy6k9k.vGe.Nf/8oWpYy6k9k.vGe.','oscar@mail.com','0977-789-789',8800,'USER'),(19,'gjun','$2a$10$rkfyF69HiCMattQHt7ucQehEQsIk9LizuEKuCHQSLNlw1UxVh1bPi','gjun@gym.com','0955-555-555',7099,'ADMIN');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registration`
--

DROP TABLE IF EXISTS `registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registration` (
  `id` int NOT NULL AUTO_INCREMENT,
  `member_id` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `registration_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `registration_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registration`
--

LOCK TABLES `registration` WRITE;
/*!40000 ALTER TABLE `registration` DISABLE KEYS */;
INSERT INTO `registration` VALUES (1,2,5,'2026-02-23 19:34:28'),(2,3,5,'2026-02-23 19:34:28'),(3,4,5,'2026-02-23 19:34:28'),(4,5,5,'2026-02-23 19:34:28'),(5,6,5,'2026-02-23 19:34:28'),(6,7,5,'2026-02-23 19:34:28'),(7,8,5,'2026-02-23 19:34:28'),(8,9,5,'2026-02-23 19:34:28'),(9,10,5,'2026-02-23 19:34:28'),(10,11,5,'2026-02-23 19:34:28'),(11,12,5,'2026-02-23 19:34:28'),(12,13,5,'2026-02-23 19:34:28'),(13,14,5,'2026-02-23 19:34:28'),(14,15,5,'2026-02-23 19:34:28'),(15,16,5,'2026-02-23 19:34:28'),(16,17,5,'2026-02-23 19:34:28'),(17,18,5,'2026-02-23 19:34:28'),(32,3,2,'2026-02-23 19:34:28'),(33,4,2,'2026-02-23 19:34:28'),(34,5,2,'2026-02-23 19:34:28'),(35,6,2,'2026-02-23 19:34:28'),(36,7,2,'2026-02-23 19:34:28'),(37,8,2,'2026-02-23 19:34:28'),(38,9,2,'2026-02-23 19:34:28'),(39,10,2,'2026-02-23 19:34:28'),(40,11,2,'2026-02-23 19:34:28'),(41,12,2,'2026-02-23 19:34:28'),(42,13,2,'2026-02-23 19:34:28'),(43,14,2,'2026-02-23 19:34:28'),(44,15,2,'2026-02-23 19:34:28'),(45,16,2,'2026-02-23 19:34:28'),(46,17,2,'2026-02-23 19:34:28'),(47,5,1,'2026-02-23 19:34:28'),(48,6,1,'2026-02-23 19:34:28'),(49,7,1,'2026-02-23 19:34:28'),(50,8,1,'2026-02-23 19:34:28'),(51,9,1,'2026-02-23 19:34:28'),(52,10,1,'2026-02-23 19:34:28'),(53,11,1,'2026-02-23 19:34:28'),(54,12,1,'2026-02-23 19:34:28'),(55,13,1,'2026-02-23 19:34:28'),(56,14,1,'2026-02-23 19:34:28'),(62,8,3,'2026-02-23 19:34:28'),(63,9,3,'2026-02-23 19:34:28'),(64,10,3,'2026-02-23 19:34:28'),(65,11,3,'2026-02-23 19:34:28'),(66,12,3,'2026-02-23 19:34:28'),(67,13,3,'2026-02-23 19:34:28'),(68,14,3,'2026-02-23 19:34:28'),(69,15,3,'2026-02-23 19:34:28'),(77,2,4,'2026-02-23 19:34:28'),(78,3,4,'2026-02-23 19:34:28'),(79,4,4,'2026-02-23 19:34:28'),(80,5,4,'2026-02-23 19:34:28'),(81,6,4,'2026-02-23 19:34:28'),(82,7,4,'2026-02-23 19:34:28'),(84,10,6,'2026-02-23 19:34:28'),(85,11,6,'2026-02-23 19:34:28'),(86,12,6,'2026-02-23 19:34:28'),(87,13,6,'2026-02-23 19:34:28'),(88,14,6,'2026-02-23 19:34:28'),(91,12,7,'2026-02-23 19:34:28'),(92,13,7,'2026-02-23 19:34:28'),(93,14,7,'2026-02-23 19:34:28'),(94,15,7,'2026-02-23 19:34:28'),(98,2,8,'2026-02-23 19:34:28'),(99,3,8,'2026-02-23 19:34:28'),(100,19,1,'2026-02-24 09:22:22'),(101,19,2,'2026-02-24 09:22:24'),(102,19,3,'2026-02-24 09:22:27'),(103,19,4,'2026-02-24 09:22:29'),(104,19,5,'2026-02-24 09:22:32');
/*!40000 ALTER TABLE `registration` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-24 10:59:15
