USE pad;

ALTER TABLE `pad`.`vehicles` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL ,
ADD COLUMN `anpr_id` INT(11) NOT NULL AFTER `id`;

UPDATE pad.vehicles SET anpr_id = -1;

CREATE TABLE `pad`.`anpr_scheduler` (
  `id` int(11) NOT NULL,
  `is_processed` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `trip_id` int(11) NOT NULL,
  `vehicle_registration` varchar(32) NOT NULL,
  `priority` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_scheduled` datetime NOT NULL,
  `retry_count` int(11) NOT NULL,
  `date_processed` datetime DEFAULT NULL,
  `response_code` varchar(128) NOT NULL,
  `response_text` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date_scheduled_ik` (`date_scheduled`),
  KEY `is_processed_ik` (`is_processed`),
  KEY `priority_ik` (`priority`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `pad`.`anpr_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_processed` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `trip_id` int(11) NOT NULL,
  `vehicle_registration` varchar(32) NOT NULL,
  `priority` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_scheduled` datetime NOT NULL,
  `retry_count` int(11) NOT NULL,
  `date_processed` datetime DEFAULT NULL,
  `response_code` varchar(128) NOT NULL,
  `response_text` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date_scheduled_ik` (`date_scheduled`),
  KEY `mission_id_ik` (`mission_id`),
  KEY `trip_id_ik` (`trip_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('105', 'anprTimerTask', '2019-08-13 10:25:16', 'continuous', '1000', 'pad');

