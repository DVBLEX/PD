USE pad;

ALTER TABLE `pad`.`statements` 
ADD COLUMN `payment_id` INT(11) NOT NULL AFTER `type`;

ALTER TABLE `pad`.`statements` 
ADD INDEX `payment_id_ik` (`payment_id` ASC);

UPDATE pad.statements SET payment_id = -1 WHERE id > 0;
