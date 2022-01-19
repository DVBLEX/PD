USE pad;


ALTER TABLE `pad`.`vehicles` 
ADD COLUMN `is_active` TINYINT(1) NOT NULL AFTER `operator_id`;

UPDATE pad.vehicles set is_active = 1;
