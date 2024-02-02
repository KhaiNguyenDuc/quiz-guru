CREATE DATABASE  IF NOT EXISTS `quiz_guru` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `quiz_guru`;
-- MySQL dump 10.16  Distrib 10.1.19-MariaDB, for Win32 (AMD64)
--
-- Host: 127.0.0.1    Database: 127.0.0.1
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `choices`
--

DROP TABLE IF EXISTS `choices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `choices` (
  `id` varchar(255) NOT NULL,
  `is_correct` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `question_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfxdoiodb9d771owartb3bl7f1` (`question_id`),
  CONSTRAINT `FKfxdoiodb9d771owartb3bl7f1` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `choices`
--

LOCK TABLES `choices` WRITE;
/*!40000 ALTER TABLE `choices` DISABLE KEYS */;
/*!40000 ALTER TABLE `choices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `images` (
  `id` varchar(255) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library`
--

LOCK TABLES `library` WRITE;
/*!40000 ALTER TABLE `library` DISABLE KEYS */;
/*!40000 ALTER TABLE `library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` varchar(255) NOT NULL,
  `explanation` longtext DEFAULT NULL,
  `query` varchar(255) DEFAULT NULL,
  `type` enum('SINGLE_CHOICE','MULTIPLE_CHOICE') DEFAULT NULL,
  `quiz_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoxlff1la7kex4b1dkbcfrv1gs` (`quiz_id`),
  CONSTRAINT `FKoxlff1la7kex4b1dkbcfrv1gs` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quizzes`
--

DROP TABLE IF EXISTS `quizzes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quizzes` (
  `id` varchar(255) NOT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) NOT NULL,
  `duration` int(11) DEFAULT NULL,
  `given_text` longtext DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `level` enum('EASY','MEDIUM','HARD') DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `type` enum('SINGLE_CHOICE_QUESTION','MULTIPLE_CHOICE_QUESTION','MIX_QUESTION') DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa2rodablx8mhce3bdaj19eqhe` (`user_id`),
  CONSTRAINT `FKa2rodablx8mhce3bdaj19eqhe` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quizzes`
--

LOCK TABLES `quizzes` WRITE;
/*!40000 ALTER TABLE `quizzes` DISABLE KEYS */;
/*!40000 ALTER TABLE `quizzes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record_item`
--

DROP TABLE IF EXISTS `record_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record_item` (
  `id` varchar(255) NOT NULL,
  `question_id` varchar(255) DEFAULT NULL,
  `record_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbhmc4aun0m9revus2sd6wq027` (`question_id`),
  KEY `FK79174ub43p6brb1pyi70y5k7b` (`record_id`),
  CONSTRAINT `FK79174ub43p6brb1pyi70y5k7b` FOREIGN KEY (`record_id`) REFERENCES `records` (`id`),
  CONSTRAINT `FKbhmc4aun0m9revus2sd6wq027` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record_item`
--

LOCK TABLES `record_item` WRITE;
/*!40000 ALTER TABLE `record_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `record_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record_item_choice`
--

DROP TABLE IF EXISTS `record_item_choice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record_item_choice` (
  `record_item_id` varchar(255) NOT NULL,
  `choice_id` varchar(255) NOT NULL,
  KEY `FK3nemn15et47bkebwier0dxkw` (`choice_id`),
  KEY `FKorogge28kog8iy9whhqmcnam7` (`record_item_id`),
  CONSTRAINT `FK3nemn15et47bkebwier0dxkw` FOREIGN KEY (`choice_id`) REFERENCES `choices` (`id`),
  CONSTRAINT `FKorogge28kog8iy9whhqmcnam7` FOREIGN KEY (`record_item_id`) REFERENCES `record_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record_item_choice`
--

LOCK TABLES `record_item_choice` WRITE;
/*!40000 ALTER TABLE `record_item_choice` DISABLE KEYS */;
/*!40000 ALTER TABLE `record_item_choice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `records`
--

DROP TABLE IF EXISTS `records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `records` (
  `id` varchar(255) NOT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) NOT NULL,
  `duration` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `time_left` int(11) DEFAULT NULL,
  `quiz_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqnko2enxowo32chvdvi032jvu` (`quiz_id`),
  KEY `FK6p95uajgka0j0dc9vlbjw1sf1` (`user_id`),
  CONSTRAINT `FK6p95uajgka0j0dc9vlbjw1sf1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqnko2enxowo32chvdvi032jvu` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `records`
--

LOCK TABLES `records` WRITE;
/*!40000 ALTER TABLE `records` DISABLE KEYS */;
/*!40000 ALTER TABLE `records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refresh_token` (
  `id` varchar(255) NOT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r4k4edos30bx9neoq81mdvwph` (`token`),
  UNIQUE KEY `UK_f95ixxe7pa48ryn1awmh2evt7` (`user_id`),
  CONSTRAINT `FKjtx87i0jvq2svedphegvdwcuy` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` varchar(255) NOT NULL,
  `name` enum('USER','ADMIN') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL,
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`),
  KEY `FKj345gk1bovqvfame88rcx7yyx` (`user_id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `background_image_id` varchar(255) DEFAULT NULL,
  `image_id` varchar(255) DEFAULT NULL,
  `library_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n94jgwpo9yd2kskxued0sloec` (`background_image_id`),
  UNIQUE KEY `UK_94dj9ry3k3tmcsyg8eatp7vvn` (`image_id`),
  UNIQUE KEY `UK_k8ivh9aujh6rjqh1lny3k9j14` (`library_id`),
  CONSTRAINT `FK17herqt2to4hyl5q5r5ogbxk9` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`),
  CONSTRAINT `FKbhnd802u0jlyfjp3gjaelnq75` FOREIGN KEY (`background_image_id`) REFERENCES `images` (`id`),
  CONSTRAINT `FKt2lp5ool8ptmhpgj6j9nyo91a` FOREIGN KEY (`library_id`) REFERENCES `library` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_records`
--

DROP TABLE IF EXISTS `users_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_records` (
  `user_id` varchar(255) NOT NULL,
  `records_id` varchar(255) NOT NULL,
  UNIQUE KEY `UK_88d4mh5qo9423kbrsgqx44ssb` (`records_id`),
  KEY `FKs5657m2q2pqdu4x92qqttj4ky` (`user_id`),
  CONSTRAINT `FKewjuwq11lwexcddnjjnigepe` FOREIGN KEY (`records_id`) REFERENCES `records` (`id`),
  CONSTRAINT `FKs5657m2q2pqdu4x92qqttj4ky` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_records`
--

LOCK TABLES `users_records` WRITE;
/*!40000 ALTER TABLE `users_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `users_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `word_sets`
--

DROP TABLE IF EXISTS `word_sets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `word_sets` (
  `id` varchar(255) NOT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) NOT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `review_number` int(11) DEFAULT NULL,
  `word_number` int(11) DEFAULT NULL,
  `library_id` varchar(255) DEFAULT NULL,
  `quiz_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mi39ptchxujq3o2j0i48cnmg2` (`quiz_id`),
  KEY `FK1mjqbrtbrdejt89hmnolfj8lw` (`library_id`),
  CONSTRAINT `FK1mjqbrtbrdejt89hmnolfj8lw` FOREIGN KEY (`library_id`) REFERENCES `library` (`id`),
  CONSTRAINT `FKmlmbmapfluu0ok4wlq1yxqa16` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `word_sets`
--

LOCK TABLES `word_sets` WRITE;
/*!40000 ALTER TABLE `word_sets` DISABLE KEYS */;
/*!40000 ALTER TABLE `word_sets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `words`
--

DROP TABLE IF EXISTS `words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `words` (
  `id` varchar(255) NOT NULL,
  `content` longtext DEFAULT NULL,
  `definition` longtext DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `word_set_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5dy5gmlymrknjcx1pua93rlt0` (`word_set_id`),
  CONSTRAINT `FK5dy5gmlymrknjcx1pua93rlt0` FOREIGN KEY (`word_set_id`) REFERENCES `word_sets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `words`
--

LOCK TABLES `words` WRITE;
/*!40000 ALTER TABLE `words` DISABLE KEYS */;
/*!40000 ALTER TABLE `words` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

SET @ORIG_SQL_REQUIRE_PRIMARY_KEY = @@SQL_REQUIRE_PRIMARY_KEY;
SET SQL_REQUIRE_PRIMARY_KEY = 0;
-- Dump completed on 2024-02-02 17:30:30
