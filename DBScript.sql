CREATE DATABASE `account-management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

CREATE TABLE `account` (
  `account_number` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `pin` int(11) NOT NULL,
  `holder_id_number` int(11) NOT NULL,
  `current_balance` float NOT NULL,
  `deleted` tinyint(4) NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `type` varchar(45) NOT NULL,
  `amount` float NOT NULL,
  `description` varchar(45) NOT NULL,
  `account_number` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_trans_accountNumber_idx` (`account_number`),
  CONSTRAINT `fk_trans_accountNumber` FOREIGN KEY (`account_number`) REFERENCES `account` (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;