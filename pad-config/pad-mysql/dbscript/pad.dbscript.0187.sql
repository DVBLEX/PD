USE pad;
ALTER TABLE `pad`.`anpr_scheduler` 
ADD COLUMN `parking_permission_id` INT(11) NOT NULL AFTER `vehicle_registration`;

ALTER TABLE `pad`.`parking` 
ADD COLUMN `entry_lane_id` INT(11) NOT NULL AFTER `operator_id`,
ADD COLUMN `exit_lane_id` INT(11) NOT NULL AFTER `entry_lane_id`;

UPDATE pad.parking SET entry_lane_id = -1, exit_lane_id = -1 where id > 0;

ALTER TABLE `pad`.`anpr_entry_scheduler` 
ADD COLUMN `trip_id` INT(11) NOT NULL AFTER `date_processed`,
ADD COLUMN `mission_id` INT(11) NOT NULL AFTER `trip_id`;

ALTER TABLE `pad`.`anpr_entry_log` 
ADD COLUMN `trip_id` INT(11) NOT NULL AFTER `date_processed`,
ADD COLUMN `mission_id` INT(11) NOT NULL AFTER `trip_id`;

UPDATE pad.anpr_entry_log SET trip_id = -1, mission_id = -1 where id > 0;
UPDATE pad.anpr_entry_scheduler SET trip_id = -1, mission_id = -1 where id > 0;

ALTER TABLE `pad`.`anpr_entry_log` 
ADD COLUMN `retry_count` INT(11) NOT NULL AFTER `date_processed`;

ALTER TABLE `pad`.`anpr_entry_scheduler` 
ADD COLUMN `retry_count` INT(11) NOT NULL AFTER `date_processed`;
