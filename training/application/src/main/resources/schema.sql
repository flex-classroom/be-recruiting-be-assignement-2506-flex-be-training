/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

CREATE TABLE "department"
(
    `id`              BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `company_id`      BIGINT      NOT NULL,
    `parent_id`       BIGINT      NULL,
    `name`            VARCHAR(50) NOT NULL,
    `created_at`      DATETIME    NOT NULL,
    `updated_at`      DATETIME    NOT NULL
);

CREATE TABLE "job_role"
(
    `id`              BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `company_id`      BIGINT      NOT NULL,
    `name`            VARCHAR(50) NOT NULL,
    `created_at`      DATETIME    NOT NULL,
    `updated_at`      DATETIME    NOT NULL
);

CREATE TABLE "company"
(
    `id`              BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`            VARCHAR(50) NOT NULL,
    `created_at`      DATETIME    NOT NULL,
    `updated_at`      DATETIME    NOT NULL
);

CREATE TABLE "employee"
(
    `id`              BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `company_id`      BIGINT      NOT NULL,
    `employee_number` VARCHAR(50) NOT NULL,
    `name`            VARCHAR(50) NOT NULL,
    `created_at`      DATETIME    NOT NULL,
    `updated_at`      DATETIME    NOT NULL
);
