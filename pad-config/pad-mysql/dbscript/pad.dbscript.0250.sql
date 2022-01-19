USE pad;

ALTER TABLE `pad`.`parking` 
ADD COLUMN `port_access_whitelist_id` INT(11) NOT NULL AFTER `mission_id`;

UPDATE pad.parking SET port_access_whitelist_id = -1 where id > 0;
