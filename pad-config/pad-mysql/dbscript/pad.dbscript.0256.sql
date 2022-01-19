USE pad;

ALTER TABLE `pad`.`anpr_entry_log` 
CHANGE COLUMN `notes` `notes` VARCHAR(256) NOT NULL ;

ALTER TABLE `pad`.`anpr_entry_scheduler` 
CHANGE COLUMN `notes` `notes` VARCHAR(256) NOT NULL ;
