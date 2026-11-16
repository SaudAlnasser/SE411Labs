-- Lab exercise: resets the course table on each application startup.
DROP TABLE IF EXISTS course;
CREATE TABLE course (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100),
    credits INT
);
