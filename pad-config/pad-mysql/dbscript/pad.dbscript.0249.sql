USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `parking_permission_id_parking_entry` INT(11) NOT NULL AFTER `parking_permission_id`,
ADD COLUMN `parking_permission_id_parking_exit` INT(11) NOT NULL AFTER `parking_permission_id_parking_entry`,
ADD COLUMN `parking_permission_id_port_entry` INT(11) NOT NULL AFTER `parking_permission_id_parking_exit`,
ADD COLUMN `parking_permission_id_port_exit` INT(11) NOT NULL AFTER `parking_permission_id_port_entry`;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `parking_entry_count` INT(11) NOT NULL AFTER `date_slot_requested`,
ADD COLUMN `parking_exit_count` INT(11) NOT NULL AFTER `parking_entry_count`,
ADD COLUMN `port_entry_count` INT(11) NOT NULL AFTER `parking_exit_count`,
ADD COLUMN `port_exit_count` INT(11) NOT NULL AFTER `port_entry_count`;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `date_entry_parking` DATETIME NULL AFTER `date_deny`,
ADD COLUMN `date_exit_parking` DATETIME NULL AFTER `date_entry_parking`,
ADD COLUMN `date_entry_port` DATETIME NULL AFTER `date_exit_parking`,
ADD COLUMN `date_exit_port` DATETIME NULL AFTER `date_entry_port`;
