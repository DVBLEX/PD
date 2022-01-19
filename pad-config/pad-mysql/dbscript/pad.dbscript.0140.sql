USE pad;

-- ALTER TABLE `pad`.`trips_port_operator` 
-- DROP INDEX `port_operator_id_UNIQUE` ,
-- ADD UNIQUE INDEX `port_operator_id_reference_number_uk` (`port_operator_id` ASC, `reference_number` ASC),
-- DROP INDEX `reference_number_UNIQUE` ;

-- ALTER TABLE `pad`.`trips_port_operator` 
-- ADD COLUMN `date_edited` DATETIME NOT NULL AFTER `date_created`;

ALTER TABLE `pad`.`trips_port_operator` 
ADD COLUMN `file_upload_id` INT(11) NOT NULL AFTER `destination`;