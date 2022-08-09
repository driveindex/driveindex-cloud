CREATE TABLE IF NOT EXISTS `logs` (
      `username` VARCHAR(20) NOT NULL,
      `password` VARCHAR(30) NOT NULL DEFAULT 'driveindex',
      PRIMARY KEY (`username`)
);
