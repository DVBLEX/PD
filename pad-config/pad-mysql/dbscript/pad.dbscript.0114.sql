USE pad;

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `trip_slot_start_range_days` INT(11) NOT NULL AFTER `hours_exited_from_parking`;
