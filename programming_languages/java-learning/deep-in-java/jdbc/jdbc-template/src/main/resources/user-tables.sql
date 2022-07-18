create database `demo` default character set utf8mb4 collate utf8mb4_general_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users`
(
    id          INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(16) NOT NULL,
    password    VARCHAR(64) NOT NULL,
    email       VARCHAR(64) NOT NULL,
    phoneNumber VARCHAR(32) NOT NULL
)