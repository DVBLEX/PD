USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `type` INT(11) NOT NULL AFTER `code`;

UPDATE pad.trips SET type = 100 WHERE vehicle_id != -1 AND driver_id != -1 AND id > 0;
UPDATE pad.trips SET type = 200 WHERE vehicle_id = -1 AND driver_id = -1 AND id > 0;
