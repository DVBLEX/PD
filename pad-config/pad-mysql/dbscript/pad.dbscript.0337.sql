USE pad;

UPDATE `pad`.`language_keys` SET `translate_value` = 'Approval Date' WHERE (`id` = '1517');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Request Date' WHERE (`id` = '1257');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Date de la Demande' WHERE (`id` = '1258');

ALTER TABLE `pad`.`missions` 
CHANGE COLUMN `count_trips` `count_trips_completed` INT(11) NOT NULL ;

UPDATE pad.missions SET count_trips_completed = 0 WHERE count_trips_completed = -1 AND id > 0;
