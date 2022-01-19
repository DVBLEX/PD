USE pad;

ALTER TABLE `pad`.`missions` 
DROP COLUMN `container_id`,
DROP INDEX `container_id_ik` ;
;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `container_id` VARCHAR(32) NOT NULL AFTER `reference_number`;
