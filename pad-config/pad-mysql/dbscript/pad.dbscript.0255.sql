USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `parking_permission_id_parking_entry_first` INT(11) NOT NULL AFTER `parking_permission_id`;
UPDATE pad.trips SET parking_permission_id_parking_entry_first = -1 where id > 0;
