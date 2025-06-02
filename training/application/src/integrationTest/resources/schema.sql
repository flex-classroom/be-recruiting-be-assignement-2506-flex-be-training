/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

CREATE TABLE `example_entity`
(
    `id`          BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(50) NOT NULL,
    `type`        VARCHAR(20) NOT NULL,
    `created_at`  DATETIME    NOT NULL,
    `updated_at`  DATETIME    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

