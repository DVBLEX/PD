USE pad;

ALTER TABLE `pad`.`port_access_whitelist` 
ADD COLUMN `parking_permission_id` INT(11) NOT NULL AFTER `vehicle_registration`;

UPDATE pad.port_access_whitelist SET parking_permission_id = -1 WHERE id > 0;

ALTER TABLE `pad`.`anpr_log` 
ADD COLUMN `port_access_whitelist_id` INT(11) NOT NULL AFTER `trip_id`;

ALTER TABLE `pad`.`anpr_scheduler` 
ADD COLUMN `port_access_whitelist_id` INT(11) NOT NULL AFTER `trip_id`;

ALTER TABLE `pad`.`port_access_whitelist` 
ADD COLUMN `date_edited` DATETIME NULL AFTER `date_created`;

ALTER TABLE `pad`.`port_access_whitelist` 
ADD COLUMN `status` INT(11) NOT NULL AFTER `code`;
