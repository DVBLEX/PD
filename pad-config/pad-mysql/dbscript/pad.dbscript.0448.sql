USE pad;

ALTER TABLE `pad`.`email_log`
ADD COLUMN `trip_id` INT NOT NULL AFTER `mission_id`;
ALTER TABLE `pad`.`email_scheduler`
ADD COLUMN `trip_id` INT NOT NULL AFTER `mission_id`;

UPDATE pad.email_log SET trip_id = - 1;
UPDATE pad.email_scheduler SET trip_id = -1;
