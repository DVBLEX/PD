USE pad;

ALTER TABLE `pad`.`accounts` 
ADD COLUMN `type_cargo` VARCHAR(64) NOT NULL AFTER `nationality`,
ADD COLUMN `countries_active` VARCHAR(64) NULL AFTER `type_cargo`,
ADD COLUMN `special_tax_status` TINYINT(1) NULL AFTER `countries_active`;

UPDATE `pad`.`accounts` SET `special_tax_status`='0' WHERE `id` > 0;
