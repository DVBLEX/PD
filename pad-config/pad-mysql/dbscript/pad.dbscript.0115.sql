USE pad;

ALTER TABLE `pad`.`port_access` 
ADD COLUMN `parking_id` INT(11) NOT NULL AFTER `status`;