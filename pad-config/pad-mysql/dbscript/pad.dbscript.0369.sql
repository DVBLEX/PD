USE pad;

ALTER TABLE `pad`.`payments` 
ADD COLUMN `type` INT(11) NOT NULL AFTER `operator_id`;

ALTER TABLE `pad`.`payments` 
ADD COLUMN `mission_id` INT(11) NOT NULL AFTER `account_id`,
ADD COLUMN `trip_id` INT(11) NOT NULL AFTER `mission_id`,
ADD COLUMN `response_code` INT(11) NOT NULL AFTER `amount_change_due`,
ADD COLUMN `date_response` DATETIME NULL AFTER `response_code`;
