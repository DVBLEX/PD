USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `operator_id_created` INT(11) NOT NULL AFTER `driver_language_id`;

UPDATE pad.trips set operator_id_created = -1;