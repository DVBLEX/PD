USE pad;

ALTER TABLE `pad`.`invoice` 
ADD COLUMN `reference` VARCHAR(32) NOT NULL AFTER `type`;
