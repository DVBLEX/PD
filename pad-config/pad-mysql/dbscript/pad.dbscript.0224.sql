USE pad;

ALTER TABLE `pad`.`anpr_entry_scheduler` 
ADD COLUMN `response_code` VARCHAR(128) NULL AFTER `retry_count`,
ADD COLUMN `response_text` VARCHAR(256) NULL AFTER `response_code`;

ALTER TABLE `pad`.`anpr_entry_log` 
ADD COLUMN `response_code` VARCHAR(128) NULL AFTER `retry_count`,
ADD COLUMN `response_text` VARCHAR(256) NULL DEFAULT NULL AFTER `response_code`;

ALTER TABLE `pad`.`anpr_entry_log` 
CHANGE COLUMN `response_code` `response_code` INT(11) NULL ;

ALTER TABLE `pad`.`anpr_entry_scheduler` 
CHANGE COLUMN `response_code` `response_code` INT(11) NULL ;
