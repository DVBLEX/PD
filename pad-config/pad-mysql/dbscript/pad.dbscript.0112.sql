USE pad;

DELETE FROM `pad`.`system_timer_tasks` WHERE `id`='103';

ALTER TABLE `pad`.`system_parameters` 
DROP COLUMN `trip_slot_range_days`;