USE pad;


ALTER TABLE `pad`.`trips` 
ADD COLUMN `company_name` VARCHAR(64) NOT NULL AFTER `driver_language_id`;
