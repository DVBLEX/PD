USE pad;

ALTER TABLE `pad`.`parking` 
ADD COLUMN `entry_lane_number` INT NOT NULL AFTER `entry_lane_id`,
ADD COLUMN `exit_lane_number` INT NOT NULL AFTER `exit_lane_id`;

ALTER TABLE `pad`.`port_access` 
ADD COLUMN `entry_lane_number` INT NOT NULL AFTER `entry_lane_id`,
ADD COLUMN `exit_lane_number` INT NOT NULL AFTER `exit_lane_id`;

UPDATE pad.port_access set entry_lane_number = -1, exit_lane_number = -1 WHERE id > 0;
UPDATE pad.parking set entry_lane_number = -1, exit_lane_number = -1 WHERE id > 0;
