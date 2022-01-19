USE pad;

DROP TABLE pad.api_remote_addr;
DROP TABLE pad.api_users;

CREATE TABLE `pad`.`api_remote_addr` (
  `operator_id` int NOT NULL,
  `remote_addr` varchar(128) NOT NULL,
  `is_allowed` tinyint NOT NULL,
  KEY `api_remote_addr_fk_01` (`operator_id`),
  KEY `api_remote_addr_ik_01` (`remote_addr`),
  CONSTRAINT `api_remote_addr_fk_01` FOREIGN KEY (`operator_id`) REFERENCES `operators` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
