USE pad;

CREATE TABLE `pad`.`sms_config` (
  `id` int(11) NOT NULL,
  `url` varchar(128) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pad`.`sms_templates` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `account_id` int(11) NOT NULL,
  `config_id` int(11) NOT NULL,
  `source_addr` varchar(32) NOT NULL,
  `message` varchar(640) NOT NULL,
  `variables` varchar(128) NOT NULL,
  `priority` int(11) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pad`.`sms_scheduler` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_processed` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `config_id` varchar(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `trip_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `msisdn` varchar(32) NOT NULL,
  `source_addr` varchar(32) NOT NULL,
  `message` varchar(320) NOT NULL,
  `channel` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_scheduled` datetime NOT NULL,
  `retry_count` int(11) NOT NULL,
  `date_processed` datetime DEFAULT NULL,
  `transaction_id` int(11) NOT NULL,
  `response_code` int(11) NOT NULL,
  `response_text` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date_scheduled_ik` (`date_scheduled`),
  KEY `msisdn_ik` (`msisdn`),
  KEY `template_id_ik` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pad`.`sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_processed` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `config_id` varchar(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `trip_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `msisdn` varchar(32) NOT NULL,
  `source_addr` varchar(32) NOT NULL,
  `message` varchar(320) NOT NULL,
  `channel` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_scheduled` datetime NOT NULL,
  `retry_count` int(11) NOT NULL,
  `date_processed` datetime DEFAULT NULL,
  `transaction_id` int(11) NOT NULL,
  `response_code` int(11) NOT NULL,
  `response_text` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date_scheduled_ik` (`date_scheduled`),
  KEY `msisdn_ik` (`msisdn`),
  KEY `template_id_ik` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `pad`.`parking` 
ADD COLUMN `date_sms_exit` DATETIME NULL AFTER `date_exit`;

INSERT INTO `pad`.`sms_config` (`id`, `url`, `username`, `password`, `operator_id`, `date_created`, `date_edited`) VALUES ('-1', 'https://www.allpointsmessaging.com/bulksms/sendsms/sendbulksms.htm', '', '', '-1', '2019-03-04 12:28:19', '2019-03-04 12:28:19');
INSERT INTO `pad`.`sms_config` (`id`, `url`, `username`, `password`, `operator_id`, `date_created`, `date_edited`) VALUES ('1001', 'https://www.allpointsmessaging.com/bulksms/sendsms/sendbulksms.htm', 'sremium', 'a47era1ne', '-1', '2019-03-04 12:28:19', '2019-03-04 12:28:19');

ALTER TABLE `pad`.`sms_templates` 
ADD COLUMN `language_id` INT(11) NOT NULL AFTER `name`;

INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('1', '1', 'Exit Parking - EN', '1', '-1', '1', 'PAD', 'Please leave parking now', '', '10', '-1', '2019-03-04 14:10:10', '2019-03-04 14:10:10');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('2', '1', 'Exit Parking - FR', '2', '-1', '1', 'PAD', 'S\'il vous plaît quitter le parking maintenant', '', '10', '-1', '2019-03-04 14:10:10', '2019-03-04 14:10:10');

UPDATE `pad`.`sms_config` SET `username`='portaccessdakar', `password`='tuHfqhlNQTxGg9prwr64' WHERE `id`='1001';

UPDATE `pad`.`sms_config` SET `id`='1' WHERE `id`='1001';

