USE pad;
INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('107', 'anprEntryLogProcessTimerTask', '2019-09-06 14:58:19', 'continuous', '30000', 'pad');
ALTER TABLE `pad`.`anpr_log` 
ADD COLUMN `zone_id` INT(11) NOT NULL AFTER `request_type`;

UPDATE pad.anpr_log SET zone_id = -1 where id > 0;

ALTER TABLE `pad`.`anpr_scheduler` 
ADD COLUMN `zone_id` INT(11) NOT NULL AFTER `request_type`;

UPDATE pad.anpr_scheduler SET zone_id = -1 where id > 0;

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `anpr_zone_id_agsparking` INT(11) NOT NULL AFTER `default_conn_request_timeout`,
ADD COLUMN `anpr_zone_id_mole2` INT(11) NOT NULL AFTER `anpr_zone_id_agsparking`,
ADD COLUMN `anpr_zone_id_mole4` INT(11) NOT NULL AFTER `anpr_zone_id_mole2`,
ADD COLUMN `anpr_zone_id_mole8` INT(11) NOT NULL AFTER `anpr_zone_id_mole4`;

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `anpr_zone_id_outside` INT(11) NOT NULL AFTER `default_conn_request_timeout`;
UPDATE `pad`.`anpr_parameters` SET `anpr_zone_id_outside` = '-1', `anpr_zone_id_agsparking` = '10004', `anpr_zone_id_mole2` = '10005', `anpr_zone_id_mole4` = '10006', `anpr_zone_id_mole8` = '10007' WHERE (`id` = '1');
