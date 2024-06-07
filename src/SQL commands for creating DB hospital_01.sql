CREATE DATABASE  IF NOT EXISTS `hospital_01`;
USE `hospital_01`;

DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `doctors`;
CREATE TABLE `doctors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `doc_department_id_idx` (`department_id`),
  CONSTRAINT `doc_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `patients`;
CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `notation` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `wards`;
CREATE TABLE `wards` (
  `id` int NOT NULL AUTO_INCREMENT,
  `level` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `capacity` int NOT NULL DEFAULT '0',
  `taken` int DEFAULT '0',
  `free` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id_idx` (`department_id`),
  CONSTRAINT `ward_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `treatments`;
CREATE TABLE `treatments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `ward_id` int DEFAULT NULL,
  `dateIn` date DEFAULT NULL,
  `dateOut` date DEFAULT NULL,
  `notation` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `treat_doctor_id_idx` (`doctor_id`),
  KEY `treat_ward_id_idx` (`ward_id`),
  KEY `treat_patient_id_idx` (`patient_id`),
  CONSTRAINT `treat_doctor_id` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE SET NULL,
  CONSTRAINT `treat_patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE SET NULL,
  CONSTRAINT `treat_ward_id` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `heads`;
CREATE TABLE `heads` (
  `department_id` INT NOT NULL,
  `doctor_id` INT NULL,
  PRIMARY KEY (`department_id`),
  INDEX `doctor_id_idx` (`doctor_id` ASC) VISIBLE,
  CONSTRAINT `department_id`
    FOREIGN KEY (`department_id`)
    REFERENCES `hospital_01`.`departments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `doctor_id`
    FOREIGN KEY (`doctor_id`)
    REFERENCES `hospital_01`.`doctors` (`id`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;