USE pad;

ALTER TABLE `pad`.`device_lane` 
CHANGE COLUMN `device_id` `device_id` VARCHAR(64) NOT NULL COMMENT 'MAC Number' AFTER `zone_id`,
CHANGE COLUMN `device_name` `device_name` VARCHAR(128) NOT NULL AFTER `device_id`, RENAME TO  `pad`.`lanes` ;