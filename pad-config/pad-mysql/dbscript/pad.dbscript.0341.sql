USE pad;

ALTER TABLE `pad`.`api_remote_addr` 
CHANGE COLUMN `remote_addr` `remote_addr` VARCHAR(128) NOT NULL ;
