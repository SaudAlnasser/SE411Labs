CREATE TABLE course (
    id BIGINT PRIMARY KEY CHECK (id >= 0),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    credits INTEGER NOT NULL CHECK (credits BETWEEN 1 AND 10)
);
