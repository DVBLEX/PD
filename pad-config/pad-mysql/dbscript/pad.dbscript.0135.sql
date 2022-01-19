USE pad;

INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('103', 'filePickUpTimerTask', '2019-06-05 10:08:06', 'continuous', '900000', 'pad');

CREATE TABLE `pad`.`file_config` (
  `id` INT(11) NOT NULL,
  `port_operator_id` INT(11) NOT NULL,
  `file_pickup_uri` VARCHAR(256) NOT NULL,
  `file_name_regex` VARCHAR(128) NOT NULL,
  `file_name_date_format` VARCHAR(128) NOT NULL,
  `date_file_pickup_from` DATETIME NOT NULL,
  `date_file_pickup_to` DATETIME NOT NULL,
  `connection_retry_count` INT(11) NOT NULL,
  `permission_retry_count` INT(11) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_edited` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`file_config` 
ADD COLUMN `is_active` TINYINT(1) NOT NULL AFTER `permission_retry_count`;


CREATE TABLE `pad`.`file_upload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `port_operator_id` int(11) NOT NULL,
  `file_config_id` int(11) NOT NULL,
  `file_name` varchar(128) NOT NULL,
  `file_system_name` varchar(128) NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `error_code` int(11) NOT NULL,
  `error_text` varchar(128) NOT NULL,
  `row_count` int(11) NOT NULL,
  `processed_rows` int(11) NOT NULL DEFAULT '0',
  `date_uploaded` datetime NOT NULL,
  `upload_type` varchar(25) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=701 DEFAULT CHARSET=utf8;



CREATE TABLE `pad`.`trips_port_operator` (
  `id` INT(11) NOT NULL,
  `port_operator_id` INT(11) NOT NULL,
  `state` VARCHAR(64) NOT NULL,
  `reference_number` VARCHAR(64) NOT NULL,
  `date` DATE NOT NULL,
  `date_slot_start` DATETIME NOT NULL,
  `date_slot_end` DATETIME NOT NULL,
  `transaction_type` INT(11) NOT NULL COMMENT '1 - Drop off export\n2 - Drop off empty\n3 - Pick up import\n4 - Pick up empty',
  `vehicle_registration` VARCHAR(32) NOT NULL,
  `company_short_name` VARCHAR(32) NOT NULL,
  `reference_nbr` VARCHAR(32) NOT NULL,
  `order_number` VARCHAR(32) NOT NULL,
  `destination` VARCHAR(32) NOT NULL,
  `date_created` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`trips_port_operator` 
ADD COLUMN `ctr_id` VARCHAR(32) NOT NULL AFTER `order_number`;

ALTER TABLE `pad`.`trips_port_operator` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;