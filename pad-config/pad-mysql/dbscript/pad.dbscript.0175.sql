USE pad;

ALTER TABLE `pad`.`anpr_log` 
ADD COLUMN `date_valid_from` DATETIME NOT NULL AFTER `vehicle_registration`,
ADD COLUMN `date_valid_to` DATETIME NOT NULL AFTER `date_valid_from`;


ALTER TABLE `pad`.`anpr_scheduler` 
ADD COLUMN `date_valid_from` DATETIME NOT NULL AFTER `vehicle_registration`,
ADD COLUMN `date_valid_to` DATETIME NOT NULL AFTER `date_valid_from`;
