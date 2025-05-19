/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.11-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mdas
-- ------------------------------------------------------
-- Server version	10.11.11-MariaDB-0ubuntu0.24.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `entradas`
--

DROP TABLE IF EXISTS `entradas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `entradas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` enum('GENERAL','VIP','NUMERADA') NOT NULL,
  `tipoVenta` enum('VENTAPRIMARIA','VENTASECUNDARIA') NOT NULL,
  `disponibles` int(11) NOT NULL,
  `precio` float NOT NULL,
  `evento` varchar(255) NOT NULL,
  `correoVendedor` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `evento` (`evento`),
  CONSTRAINT `entradas_ibfk_1` FOREIGN KEY (`evento`) REFERENCES `eventos` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entradas`
--

LOCK TABLES `entradas` WRITE;
/*!40000 ALTER TABLE `entradas` DISABLE KEYS */;
INSERT INTO `entradas` VALUES
(19,'GENERAL','VENTAPRIMARIA',9,10,'hola',NULL),
(20,'VIP','VENTAPRIMARIA',10,10,'hola',NULL),
(21,'NUMERADA','VENTAPRIMARIA',10,10,'hola',NULL),
(22,'GENERAL','VENTAPRIMARIA',100,30,'Concierto Rock 2025',NULL),
(23,'VIP','VENTAPRIMARIA',20,60,'Concierto Rock 2025',NULL),
(24,'NUMERADA','VENTAPRIMARIA',150,15,'Concierto Rock 2025',NULL),
(25,'VIP','VENTASECUNDARIA',1,60,'Concierto Rock 2025',NULL),
(26,'NUMERADA','VENTASECUNDARIA',1,21,'Concierto Rock 2025','i22pecum@uco.es'),
(27,'GENERAL','VENTASECUNDARIA',1,42,'Concierto Rock 2025','i22pecum@uco.es'),
(28,'NUMERADA','VENTASECUNDARIA',1,20,'Concierto Rock 2025','i22pecum@uco.es'),
(29,'NUMERADA','VENTASECUNDARIA',1,20.3,'Concierto Rock 2025','i22pecum@uco.es'),
(30,'GENERAL','VENTAPRIMARIA',9,10,'pene','organizador@gmail.com'),
(31,'VIP','VENTAPRIMARIA',10,10,'pene','organizador@gmail.com'),
(32,'NUMERADA','VENTAPRIMARIA',10,10,'pene','organizador@gmail.com'),
(33,'GENERAL','VENTASECUNDARIA',1,2,'pene','i22pecum@uco.es'),
(34,'GENERAL','VENTASECUNDARIA',1,12,'pene','i22pecum@uco.es'),
(35,'GENERAL','VENTASECUNDARIA',1,12,'pene','i22pecum@uco.es'),
(36,'GENERAL','VENTASECUNDARIA',1,8,'pene','i22pecum@uco.es'),
(37,'GENERAL','VENTASECUNDARIA',1,1,'pene','i22pecum@uco.es'),
(38,'GENERAL','VENTASECUNDARIA',1,3,'pene','i22pecum@uco.es');
/*!40000 ALTER TABLE `entradas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entradasVendidas`
--

DROP TABLE IF EXISTS `entradasVendidas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `entradasVendidas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idEntrada` int(11) NOT NULL,
  `correoUsuario` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idEntrada` (`idEntrada`),
  KEY `correoUsuario` (`correoUsuario`),
  CONSTRAINT `entradasVendidas_ibfk_1` FOREIGN KEY (`idEntrada`) REFERENCES `entradas` (`id`),
  CONSTRAINT `entradasVendidas_ibfk_2` FOREIGN KEY (`correoUsuario`) REFERENCES `usuarios` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entradasVendidas`
--

LOCK TABLES `entradasVendidas` WRITE;
/*!40000 ALTER TABLE `entradasVendidas` DISABLE KEYS */;
INSERT INTO `entradasVendidas` VALUES
(4,22,'i22pecum@uco.es'),
(5,23,'i22pecum@uco.es'),
(6,24,'i22pecum@uco.es'),
(7,19,'i22pecum@uco.es'),
(8,38,'i22pecum@uco.es');
/*!40000 ALTER TABLE `entradasVendidas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eventos`
--

DROP TABLE IF EXISTS `eventos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventos` (
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(1000) NOT NULL,
  `ubicacion` varchar(255) NOT NULL,
  `fecha` date NOT NULL,
  `limiteReventa` float NOT NULL,
  `correoOrganizador` varchar(150) NOT NULL,
  PRIMARY KEY (`nombre`),
  KEY `correoOrganizador` (`correoOrganizador`),
  CONSTRAINT `eventos_ibfk_1` FOREIGN KEY (`correoOrganizador`) REFERENCES `organizadores` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventos`
--

LOCK TABLES `eventos` WRITE;
/*!40000 ALTER TABLE `eventos` DISABLE KEYS */;
INSERT INTO `eventos` VALUES
('Concierto Rock 2025','Gran concierto de bandas nacionales.','Madrid Arena','2025-06-15',1.5,'organizador@gmail.com'),
('hola','adios','adios','3000-12-12',0.25,'organizador@gmail.com'),
('Maratón Ciudad 2025','Carrera popular anual.','Parque del Retiro, Madrid','2025-09-10',1.2,'organizador@gmail.com'),
('pene','pene','pene','2025-12-26',0.4,'organizador@gmail.com');
/*!40000 ALTER TABLE `eventos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organizadores`
--

DROP TABLE IF EXISTS `organizadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `organizadores` (
  `correo` varchar(150) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `descripcion` varchar(1000) NOT NULL,
  `monedero` float DEFAULT 0,
  PRIMARY KEY (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organizadores`
--

LOCK TABLES `organizadores` WRITE;
/*!40000 ALTER TABLE `organizadores` DISABLE KEYS */;
INSERT INTO `organizadores` VALUES
('organizador@gmail.com','$2a$10$Tdzno1MvLb5wCtI/wem1VuuoNvZxLloe38dFOVuIZdyKKsA.AiBh6','Ejemplo',10);
/*!40000 ALTER TABLE `organizadores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transacciones`
--

DROP TABLE IF EXISTS `transacciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `transacciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` enum('VENTAPRIMARIA','VENTASECUNDARIA') NOT NULL,
  `importe` float NOT NULL,
  `fecha` date NOT NULL,
  `comprador` varchar(150) NOT NULL,
  `vendedor` varchar(150) NOT NULL,
  `evento` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `evento` (`evento`),
  CONSTRAINT `transacciones_ibfk_1` FOREIGN KEY (`evento`) REFERENCES `eventos` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transacciones`
--

LOCK TABLES `transacciones` WRITE;
/*!40000 ALTER TABLE `transacciones` DISABLE KEYS */;
INSERT INTO `transacciones` VALUES
(1,'VENTAPRIMARIA',10,'2025-05-19','i22pecum@uco.es','organizador@gmail.com','pene');
/*!40000 ALTER TABLE `transacciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `correo` varchar(150) NOT NULL,
  `nombreCompleto` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `telefono` int(11) NOT NULL,
  `dni` varchar(255) NOT NULL,
  `monedero` float DEFAULT 0,
  PRIMARY KEY (`correo`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES
('i22pecum@uco.es','Manuel Peinado','$2a$10$Tdzno1MvLb5wCtI/wem1VuuoNvZxLloe38dFOVuIZdyKKsA.AiBh6',626394400,'31880419G',116);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-19 21:13:09
