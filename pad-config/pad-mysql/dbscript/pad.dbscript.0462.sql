USE pad;

ALTER TABLE `pad`.`sms_log`
    ADD COLUMN `mission_id` INT NOT NULL AFTER `account_id`;
ALTER TABLE `pad`.`sms_scheduler`
    ADD COLUMN `mission_id` INT NOT NULL AFTER `account_id`;

UPDATE pad.sms_log SET mission_id = -1;
UPDATE pad.sms_scheduler SET mission_id = -1;

ALTER TABLE `pad`.`sms_templates`
    DROP COLUMN `account_id`,
    DROP INDEX `account_id_ik` ;
;

