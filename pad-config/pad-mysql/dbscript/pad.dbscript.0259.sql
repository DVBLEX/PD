USE pad;

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `iis_server_state` VARCHAR(32) NOT NULL AFTER `is_iis_server_enabled`;