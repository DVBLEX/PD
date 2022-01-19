USE pad;

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `bt_downtime_seconds_limit` INT(11) NOT NULL AFTER `parking_permission_hours_after_exit_date`,
ADD COLUMN `bt_uptime_seconds_limit` INT(11) NOT NULL AFTER `bt_downtime_seconds_limit`;

UPDATE `pad`.`anpr_parameters` SET `bt_downtime_seconds_limit` = '300', `bt_uptime_seconds_limit` = '300' WHERE (`id` = '1');

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `is_iis_server_enabled` TINYINT(1) NOT NULL AFTER `bt_uptime_seconds_limit`;

UPDATE `pad`.`anpr_parameters` SET `is_iis_server_enabled` = '1' WHERE (`id` = '1');
