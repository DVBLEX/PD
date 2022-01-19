USE pad;

CREATE TABLE `pad`.`api_users` (
  `id` int NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `client_name` varchar(32) NOT NULL,
  `is_active` tinyint NOT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_users_uk_01` (`username`)
) ENGINE=InnoDB;

CREATE TABLE `pad`.`api_remote_addr` (
  `api_user_id` int NOT NULL,
  `remote_addr` varchar(16) NOT NULL,
  `is_allowed` tinyint NOT NULL,
  KEY `api_remote_addr_fk_01` (`api_user_id`),
  KEY `api_remote_addr_ik_01` (`remote_addr`),
  CONSTRAINT `api_remote_addr_fk_01` FOREIGN KEY (`api_user_id`) REFERENCES `api_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;


INSERT INTO `pad`.`api_users` (`id`, `username`, `password`, `client_name`, `is_active`, `date_created`) VALUES ('1', 'dpworld', 'dpw!test@pad', 'DPWorld', '1', '2020-07-14 10:10:10');
INSERT INTO `pad`.`api_remote_addr`(`api_user_id`,`remote_addr`,`is_allowed`) VALUES (1, '127.0.0.1', 1);


CREATE TABLE `pad`.`dpw_trips` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `reference_number` varchar(32) NOT NULL DEFAULT '',
  `transaction_type` varchar(16) NOT NULL DEFAULT '',
  `transporter_short_name` varchar(32) NOT NULL DEFAULT '',
  `vehicle_registration` varchar(32) NOT NULL,
  `container_id` varchar(16) NOT NULL,
  `date_slot_from` datetime DEFAULT NULL,
  `date_slot_to` datetime DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_uk` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 ;

ALTER TABLE `pad`.`dpw_trips` 
ADD COLUMN `container_type` VARCHAR(16) NOT NULL AFTER `container_id`;

ALTER TABLE `pad`.`dpw_trips` 
ADD COLUMN `status` INT NOT NULL AFTER `container_type`;
