CREATE DATABASE IF NOT EXISTS personal_finance
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_bin;

USE personal_finance;

CREATE TABLE IF NOT EXISTS `user` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    register_date DATE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    token VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS transaction_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    code VARCHAR(50) NULL,
    is_default TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transaction_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    remark VARCHAR(255),
    record_date DATE NOT NULL
);

CREATE INDEX idx_user_token
    ON `user` (token);

CREATE INDEX idx_category_user_id
    ON transaction_category (user_id);

CREATE UNIQUE INDEX uk_category_user_type_name
    ON transaction_category (user_id, type, name);

CREATE INDEX idx_record_user_id
    ON transaction_record (user_id);

CREATE INDEX idx_record_user_date
    ON transaction_record (user_id, record_date);

CREATE INDEX idx_record_user_type
    ON transaction_record (user_id, type);

CREATE INDEX idx_record_user_category
    ON transaction_record (user_id, category_id);