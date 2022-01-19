USE pad;

ALTER TABLE `pad`.`online_payments`
ADD COLUMN `payment_option` INT NULL AFTER `payment_id`;

UPDATE `pad`.`online_payments` SET `payment_option` = '2' WHERE (`mno_id` = '101');
UPDATE `pad`.`online_payments` SET `payment_option` = '3' WHERE (`mno_id` = '102');
UPDATE `pad`.`online_payments` SET `payment_option` = '4' WHERE (`mno_id` = '103');
UPDATE `pad`.`online_payments` SET `payment_option` = '5' WHERE (`mno_id` = '104');
UPDATE `pad`.`online_payments` SET `payment_option` = '6' WHERE (`mno_id` = '105');

ALTER TABLE `pad`.`online_payments`
CHANGE COLUMN `payment_option` `payment_option` INT NOT NULL ;
