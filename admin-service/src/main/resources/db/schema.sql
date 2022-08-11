CREATE TABLE IF NOT EXISTS `user` (
    `username` VARCHAR(30) NOT NULL DEFAULT 'driveindex',
    `password` VARCHAR(60) NOT NULL DEFAULT 'driveindex',
    PRIMARY KEY (`username`)
);

/*
 - id：
 该字段需当前上下文唯一，用于确定一个唯一的目标，体现在接口对目标的访问上

 - called_name：
 该字段用于给目标设置一个昵称，体现在网页标题上

 - default_target_flag：
 该字段用于确认默认目标，值最大者为默认目标，创建时初始值为当前时间戳取反，
 设定新的默认目标时只需要将 default_target_flag 设置为当前时间戳即可
*/

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
    PRIMARY KEY (`parent_client`, `id`)
);

CREATE TABLE IF NOT EXISTS `drive_config` (
  `id` VARCHAR(30) NOT NULL,
  `parent_client` VARCHAR(255) NOT NULL,
  `parent_account` VARCHAR(30) NOT NULL,
  `called_name` TEXT NOT NULL,
  `dir_home` TEXT NOT NULL,
  `default_target_flag` TIMESTAMP NOT NULL,
  PRIMARY KEY (`parent_client`, `parent_account`, `id`)
);
