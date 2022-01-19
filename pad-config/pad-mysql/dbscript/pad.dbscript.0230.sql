USE pad;
ALTER TABLE `pad`.`system_checks` 
ADD COLUMN `config_params` VARCHAR(128) NOT NULL AFTER `query_params`;
