CREATE TABLE loves (
                       id bigint AUTO_INCREMENT PRIMARY KEY,
                       date date,
                       patient_id bigint NOT NULL,
                       doctor_id int NOT NULL,
                       rating tinyint NOT NULL DEFAULT 0 CHECK (rating BETWEEN -2 AND 2),
                       comment varchar(2047),
                       UNIQUE (patient_id, doctor_id),
                       FOREIGN KEY (patient_id) REFERENCES patients(id),
                       FOREIGN KEY (doctor_id) REFERENCES doctors(id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;