USE pad;

ALTER TABLE `pad`.`system_parameters` 
CHANGE COLUMN `hours_exited_from_parking` `in_transit_validity_minutes` INT(11) NOT NULL;

UPDATE `pad`.`system_parameters` SET `in_transit_validity_minutes` = '360' WHERE (`id` = '1');
