USE pad;

ALTER TABLE `pad`.`port_access` 
ADD COLUMN `entry_lane_id` INT(11) NOT NULL AFTER `date_deny`,
ADD COLUMN `exit_lane_id` INT(11) NOT NULL AFTER `entry_lane_id`;

UPDATE pad.port_access SET entry_lane_id = -1, exit_lane_id = -1 where id > 0;
