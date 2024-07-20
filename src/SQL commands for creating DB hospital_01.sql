CREATE DATABASE IF NOT EXISTS `hospital_01`;
USE `hospital_01`;

DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`
(
    `id`   int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `doctors`;
CREATE TABLE `doctors`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `name`          varchar(255) DEFAULT NULL,
    `birth`         date         DEFAULT NULL,
    `sex`           varchar(255) DEFAULT NULL,
    `address`       varchar(255) DEFAULT NULL,
    `phone`         varchar(255) DEFAULT NULL,
    `interests`     varchar(255) DEFAULT NULL,
    `position`      varchar(255) DEFAULT NULL,
    `department_id` int          DEFAULT NULL,
    `email`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `doc_department_id_idx` (`department_id`),
    CONSTRAINT `doc_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `patients`;
CREATE TABLE `patients`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `name`      varchar(255)  DEFAULT NULL,
    `birth`     date          DEFAULT NULL,
    `sex`       varchar(255)  DEFAULT NULL,
    `phone`     varchar(255)  DEFAULT NULL,
    `interests` varchar(255)  DEFAULT NULL,
    `address`   varchar(255)  DEFAULT NULL,
    `email`     varchar(255)  DEFAULT NULL,
    `status`    varchar(255)  DEFAULT NULL,
    `notation`  varchar(2047) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `wards`;
CREATE TABLE `wards`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `level`         int          DEFAULT NULL,
    `name`          varchar(255) DEFAULT NULL,
    `department_id` int          DEFAULT NULL,
    `capacity`      int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `department_id_idx` (`department_id`),
    CONSTRAINT `ward_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `treatments`;
CREATE TABLE `treatments`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `patient_id` bigint        DEFAULT NULL,
    `doctor_id`  int           DEFAULT NULL,
    `ward_id`    int           DEFAULT NULL,
    `date_in`    date          DEFAULT NULL,
    `date_out`   date          DEFAULT NULL,
    `diagnosis`  varchar(1023) DEFAULT NULL,
    `notation`   varchar(2047) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `treat_doctor_id_idx` (`doctor_id`),
    KEY `treat_ward_id_idx` (`ward_id`),
    KEY `treat_patient_id_idx` (`patient_id`),
    CONSTRAINT `treat_doctor_id` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE SET NULL,
    CONSTRAINT `treat_patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE SET NULL,
    CONSTRAINT `treat_ward_id` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `id`   smallint AUTO_INCREMENT,
    `name` varchar(127) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
INSERT INTO `roles` (`name`)
VALUES ('ADMIN');
INSERT INTO `roles` (`name`)
VALUES ('MANAGER');
INSERT INTO `roles` (`name`)
VALUES ('USER');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`       bigint AUTO_INCREMENT,
    `username` varchar(255) NOT NULL UNIQUE,
    `email`    varchar(255) NOT NULL UNIQUE,
    `role_id`  smallint,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO users (username, email, role_id)
VALUES ('admin', 'pavlenko.istobud@gmail.com', 1);