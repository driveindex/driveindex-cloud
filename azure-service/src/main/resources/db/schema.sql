CREATE TABLE IF NOT EXISTS `azure_client` (
    `id` VARCHAR(30) NOT NULL,
    `called_name` TEXT NOT NULL,
    `client_id` VARCHAR(255) NOT NULL,
    `client_secret` TEXT NOT NULL,
    `default_target_flag` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `account_token` (
    `id`  VARCHAR(30) NOT NULL,
    `parent_client` VARCHAR(255) NOT NULL,
    `called_name` VARCHAR(255) NOT NULL,
    `token_type` TEXT NOT NULL,
    `expires_in` TIMESTAMP NOT NULL,
    `scope` TEXT NOT NULL,
    `access_token` TEXT NOT NULL,
    `refresh_token` TEXT NOT NULL,
    `id_token` TEXT NOT NULL,
    `default_target_flag` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`, `parent_client`)
);

CREATE TABLE IF NOT EXISTS `drive_config` (
    `id` VARCHAR(30) NOT NULL,
    `parent_client` VARCHAR(255) NOT NULL,
    `parent_account` VARCHAR(30) NOT NULL,
    `called_name` TEXT NOT NULL,
    `dir_home` TEXT NOT NULL,
    `default_target_flag` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`, `parent_account`)
);
