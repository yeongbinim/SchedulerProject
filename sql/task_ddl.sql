CREATE TABLE task
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    author     VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
