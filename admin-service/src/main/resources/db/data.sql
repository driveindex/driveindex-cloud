INSERT INTO `user` (`username`, `password`)
SELECT 'driveindex', 'driveindex' FROM dual
WHERE NOT EXISTS(SELECT `username` FROM `user` WHERE `username`='driveindex');