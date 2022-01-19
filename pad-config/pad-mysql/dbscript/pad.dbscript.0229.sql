USE pad;
ALTER TABLE `pad`.`system_checks` 
DROP COLUMN `is_query`;
UPDATE pad.system_checks SET type = 2 WHERE id = 99;
