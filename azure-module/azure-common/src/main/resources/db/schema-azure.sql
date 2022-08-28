/*
 此表存放缓存基本信息

 - id：
 该字段为 Microsoft Graph 接口获取到的文件 ID

 - mine_type：
 该字段为当前文件类型，若为目录则取 `directory`，否则取
 Microsoft Graph 接口获取到的类型，例如 `txt` 文件为 `text/plain`。
 */
CREATE TABLE IF NOT EXISTS `cache_central` (
    `id` VARCHAR(100) NOT NULL,
    `canonical_path` TEXT NOT NULL,
    `canonical_path_hash` INT NOT NULL,
    `parent_id` VARCHAR(100),
    `name` TEXT NOT NULL,
    `mine_type` VARCHAR(100) NOT NULL,
    `is_dir` BOOLEAN NOT NULL,
    `create_time` LONG NOT NULL,
    `modified_time` LONG NOT NULL,
    `size` LONG NOT NULL,
    `web_url` TEXT NOT NULL,
    `expires_time` LONG NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX IF NOT EXISTS `path_hash_index` ON `cache_central` (`canonical_path_hash`);
CREATE INDEX IF NOT EXISTS `mine_type_index` ON `cache_central` (`mine_type`);
CREATE INDEX IF NOT EXISTS `parent_id_index` ON `cache_central` (`parent_id`);

/* 此表存放目录缓存信息 */
CREATE TABLE IF NOT EXISTS `dir_cache` (
   `id` VARCHAR(100),
   `child_count` INT NOT NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`id`) REFERENCES `cache_central`(`id`) ON DELETE CASCADE
);

/*
 此表存放文件缓存信息

 - owner_id：
 该字段为当前文件所属文件夹的 id
 */
CREATE TABLE IF NOT EXISTS `file_cache` (
    `id` VARCHAR(100) NOT NULL,
    `quick_xor_hash` VARCHAR(255) NOT NULL,
    `sha1_hash` VARCHAR(255) NOT NULL DEFAULT '',
    `sha256_hash` VARCHAR(255) NOT NULL DEFAULT '',
    `download_url` TEXT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id`) REFERENCES `cache_central`(`id`) ON DELETE CASCADE
);

/* 此表存放 HEAD.md、README.md 及 .password 等特殊文件内容缓存 */
CREATE TABLE IF NOT EXISTS `content_cache` (
    `id` VARCHAR(100),
    `parent_id` VARCHAR(100),
    `type` VARCHAR(100),
    `content` TEXT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`parent_id`) REFERENCES `dir_cache`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`id`) REFERENCES `file_cache`(`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `parent_id_index` ON `content_cache` (`parent_id`);