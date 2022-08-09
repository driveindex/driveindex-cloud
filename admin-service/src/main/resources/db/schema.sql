CREATE TABLE IF NOT EXISTS `user` (
    `id` INT NOT NULL,
    `message` TEXT NOT NULL,
    `password` VARCHAR(30) NOT NULL DEFAULT 'driveindex',
    PRIMARY KEY (`id`)
);
