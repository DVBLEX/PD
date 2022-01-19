USE pad;

CREATE TABLE `pad`.`vehicle_counter` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `lane_id` INT(11) NOT NULL,
  `session_id` INT(11) NOT NULL,
  `type` VARCHAR(1) NOT NULL COMMENT 'A - Automatic\nM - Manual',
  `date_log` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`vehicle_counter` 
ADD COLUMN `date_created` DATETIME NOT NULL AFTER `date_count`,
CHANGE COLUMN `lane_id` `device_id` VARCHAR(128) NOT NULL ,
CHANGE COLUMN `date_log` `date_count` DATETIME NOT NULL ;

CREATE TABLE `pad`.`device_lane` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `device_id` VARCHAR(64) NOT NULL COMMENT 'MAC Number',
  `lane_id` INT(11) NOT NULL,
  `lane_number` INT(11) NOT NULL,
  `zone_id` INT(11) NOT NULL,
  `allowed_hosts` VARCHAR(128) NOT NULL,
  `date_last_request` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`device_lane` 
ADD COLUMN `device_name` VARCHAR(128) NOT NULL AFTER `device_id`;

ALTER TABLE `pad`.`vehicle_counter` 
ADD COLUMN `device_name` VARCHAR(128) NOT NULL AFTER `device_id`,
ADD COLUMN `lane_id` INT(11) NOT NULL AFTER `device_name`,
ADD COLUMN `lane_number` INT(11) NOT NULL AFTER `lane_id`,
CHANGE COLUMN `id` `id` INT(11) NOT NULL ;

ALTER TABLE `pad`.`vehicle_counter` 
ADD COLUMN `zone_id` INT(11) NOT NULL AFTER `lane_number`;

ALTER TABLE `pad`.`vehicle_counter` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1383', '1', 'KEY_SCREEN_VEHICLE_COUNTER_LABEL', 'Vehicle Counter');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1384', '2', 'KEY_SCREEN_VEHICLE_COUNTER_LABEL', 'Compteur de véhicules');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1385', '1', 'KEY_SCREEN_DATE_COUNT', 'Date Count');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1386', '2', 'KEY_SCREEN_DATE_COUNT', 'compter la date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1387', '1', 'KEY_SCREEN_DEVICE', 'Device');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1388', '2', 'KEY_SCREEN_DEVICE', 'Dispositif');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1389', '1', 'KEY_SCREEN_DATE_COUNT_TO', 'Date Count To');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1390', '2', 'KEY_SCREEN_DATE_COUNT_TO', 'Compter la Date à');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1391', '1', 'KEY_SCREEN_DATE_COUNT_FROM', 'Date Count From');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1392', '2', 'KEY_SCREEN_DATE_COUNT_FROM', 'Compter la Date de');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1393', '1', 'KEY_SCREEN_SESSION_LABEL', 'Session');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1394', '2', 'KEY_SCREEN_SESSION_LABEL', 'Session');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DATE_COUNT_FROM_LABEL' WHERE (`id` = '1392');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DATE_COUNT_FROM_LABEL' WHERE (`id` = '1391');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DATE_COUNT_TO_LABEL' WHERE (`id` = '1390');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DATE_COUNT_TO_LABEL' WHERE (`id` = '1389');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DEVICE_LABEL' WHERE (`id` = '1388');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DEVICE_LABEL' WHERE (`id` = '1387');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DATE_COUNT_LABEL' WHERE (`id` = '1386');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DATE_COUNT_LABEL' WHERE (`id` = '1385');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1395', '1', 'KEY_SCREEN_AUTOMATICT_LABEL', 'Automatic');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1396', '2', 'KEY_SCREEN_AUTOMATICT_LABEL', 'Automatique');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1397', '1', 'KEY_SCREEN_MANUAL_LABEL', 'Manual');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1398', '2', 'KEY_SCREEN_MANUAL_LABEL', 'Manuel');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1403', '1', 'KEY_SCREEN_DEVICE_ID_LABEL', 'Device ID');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1404', '2', 'KEY_SCREEN_DEVICE_ID_LABEL', 'Identifiant de l\'appareil');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1405', '1', 'KEY_SCREEN_DEVICE_NAME_LABEL', 'Device Name');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1406', '2', 'KEY_SCREEN_DEVICE_NAME_LABEL', 'Nom de l\'appareil');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1407', '1', 'KEY_SCREEN_LANE_NUMBER_FILTER_LABEL', 'Lane Number');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1408', '2', 'KEY_SCREEN_LANE_NUMBER_FILTER_LABEL', 'Numéro de voie');
