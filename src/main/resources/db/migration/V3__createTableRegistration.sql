CREATE TABLE IF NOT EXISTS registration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    student_email VARCHAR(100) NOT NULL,
    registration_date DATETIME NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT unique_registration UNIQUE (course_id, student_email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
