USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `lane_session_id` INT(11) NOT NULL AFTER `operator_id`;

UPDATE pad.trips SET lane_session_id = -1 WHERE id > 0;
