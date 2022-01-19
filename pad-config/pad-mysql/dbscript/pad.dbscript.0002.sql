USE pad;

ALTER TABLE `pad`.`accounts`
CHANGE COLUMN `company_address_1` `address_1` VARCHAR(64) NOT NULL ,
CHANGE COLUMN `company_address_2` `address_2` VARCHAR(64) NOT NULL ,
CHANGE COLUMN `company_address_3` `address_3` VARCHAR(64) NOT NULL ,
CHANGE COLUMN `company_address_4` `address_4` VARCHAR(64) NOT NULL ,
ADD COLUMN `post_code` VARCHAR(16) NOT NULL AFTER `address_4`;

ALTER TABLE `pad`.`drivers`
DROP COLUMN `title`,
DROP COLUMN `type`;
