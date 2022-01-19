USE pad;

ALTER TABLE `pad`.`anpr_log`
ADD COLUMN `request_type` INT(11) NOT NULL AFTER `is_processed`;

ALTER TABLE `pad`.`anpr_scheduler`
ADD COLUMN `request_type` INT(11) NOT NULL AFTER `is_processed`;

UPDATE pad.anpr_scheduler SET request_type = 1 where id > 0;
UPDATE pad.anpr_log SET request_type = 1 where id > 0;
