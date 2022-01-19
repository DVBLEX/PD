USE pad;

CREATE TABLE `pad`.`activity_log_description` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `pad`.`activity_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) NOT NULL,
  `activity_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `date_log` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `account_id` INT(11) NOT NULL AFTER `activity_id`;

ALTER TABLE `pad`.`activity_log` 
CHANGE COLUMN `activity_id` `activity_id` INT(11) NOT NULL AFTER `id`;


INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('1', 'Log In', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('2', 'Log Out', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('3', 'Registration', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('4', 'Activate Account', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('5', 'Add Driver', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('6', 'Add Vehicle', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('7', 'Add Trip', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('8', 'Download Invoice', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('9', 'Add Mission', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('10', 'Delete Mission', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('11', 'Aprove Mission (Port Operator)', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('12', 'Aprove Mission (Parking Office)', '2019-10-01 12:15:35');
UPDATE `pad`.`activity_log_description` SET `name`='Delete Trip' WHERE `id`='10';
UPDATE `pad`.`activity_log_description` SET `name`='Parking Entry' WHERE `id`='11';
UPDATE `pad`.`activity_log_description` SET `name`='Parking Exit' WHERE `id`='12';
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('13', 'Parking Exit SMS Send', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('14', 'Parking Exit Only', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('15', 'TOP UP', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('16', 'TOP UP Office Operator', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('17', 'Port Entry', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('18', 'Port Whitelist Entry', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('19', 'Port Deny', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('20', 'Port Exit', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('21', 'Port Whitelist Add', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('22', 'Port Whitelist Delete', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('23', 'Session Add', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('24', 'Session End', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('25', 'Session Validate', '2019-10-01 12:15:35');
UPDATE `pad`.`activity_log_description` SET `name`='Driver Add' WHERE `id`='5';
UPDATE `pad`.`activity_log_description` SET `name`='Vehicle Add' WHERE `id`='6';
UPDATE `pad`.`activity_log_description` SET `name`='Trip Add' WHERE `id`='7';
UPDATE `pad`.`activity_log_description` SET `name`='Invoice Download' WHERE `id`='8';
UPDATE `pad`.`activity_log_description` SET `name`='Mission Add' WHERE `id`='9';
UPDATE `pad`.`activity_log_description` SET `name`='Trip Delete' WHERE `id`='10';
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('26', 'Trip ADHOC Add', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('27', 'Trip Update', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('28', 'Trip Driver Mobile Update', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('29', 'Operator Create', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('30', 'Operator Update', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('31', 'Operator Password Change', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('32', 'Operator Reset Password Send', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('33', 'Trip Search', '2019-10-01 12:15:35');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('34', 'Trip Validate Reference Number', '2019-10-01 12:15:35');


ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `statement_id` INT(11) NOT NULL AFTER `mission_id`,
ADD COLUMN `vehicle_registration` VARCHAR(32) NOT NULL AFTER `statement_id`,
ADD COLUMN `port_operator_id` INT(11) NOT NULL AFTER `vehicle_registration`,
ADD COLUMN `transaction_type` INT(11) NOT NULL AFTER `port_operator_id`,
ADD COLUMN `reference_number` VARCHAR(32) NOT NULL AFTER `transaction_type`;

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `trip_id` INT(11) NOT NULL AFTER `mission_id`;

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `parking_id` INT(11) NOT NULL AFTER `trip_id`,
ADD COLUMN `port_access_id` INT(11) NOT NULL AFTER `parking_id`;

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `port_access_whitelist_id` INT(11) NOT NULL AFTER `port_access_id`;

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `session_id` INT(11) NOT NULL AFTER `statement_id`;

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `new_updated_operator_id` INT(11) NOT NULL AFTER `session_id`;