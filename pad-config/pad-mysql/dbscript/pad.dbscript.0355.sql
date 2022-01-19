USE pad;

UPDATE `pad`.`language_keys` SET `translate_value` = 'Report an Issue' WHERE (`id` = '1549');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Signaler un problème' WHERE (`id` = '1550');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_WORKING_CAPACITY_LABEL', `translate_value` = 'Working Capacity' WHERE (`id` = '1553');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_WORKING_CAPACITY_LABEL', `translate_value` = 'Capacité de Travail' WHERE (`id` = '1554');


ALTER TABLE `pad`.`port_operator_alerts` 
ADD COLUMN `name_reporter` VARCHAR(128) NOT NULL AFTER `date_resolution_estimate`,
ADD COLUMN `msisdn_reporter` VARCHAR(16) NOT NULL AFTER `name_reporter`,
ADD COLUMN `is_resolved` TINYINT(1) NOT NULL AFTER `msisdn_reporter`,
ADD COLUMN `date_resolution` DATETIME NULL AFTER `is_resolved`,
ADD COLUMN `resolution_description` VARCHAR(256) NOT NULL AFTER `date_resolution`,
ADD COLUMN `operator_id` INT NOT NULL AFTER `resolution_description`,
ADD COLUMN `date_created` DATETIME NOT NULL AFTER `operator_id`,
ADD COLUMN `date_edited` DATETIME NULL AFTER `date_created`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1569', '1', 'KEY_SCREEN_DATE_ISSUE_LABEL', 'Date Issue');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1570', '2', 'KEY_SCREEN_DATE_ISSUE_LABEL', 'Problème de date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1571', '1', 'KEY_SCREEN_REPORTER_NAME_LABEL', 'Reporter Name');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1572', '2', 'KEY_SCREEN_REPORTER_NAME_LABEL', 'Nom du journaliste');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1573', '1', 'KEY_SCREEN_REPORTER_MOBILE_LABEL', 'Reporter Mobile');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1574', '2', 'KEY_SCREEN_REPORTER_MOBILE_LABEL', 'Reporter Mobile');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1575', '1', 'KEY_SCREEN_IS_RESOLVED_LABEL', 'Is Resolved');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1576', '2', 'KEY_SCREEN_IS_RESOLVED_LABEL', 'Est résolu');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1577', '1', 'KEY_SCREEN_RESOLUTION_DATE_LABEL', 'Resolution Date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1578', '2', 'KEY_SCREEN_RESOLUTION_DATE_LABEL', 'Résolution de date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1579', '1', 'KEY_SCREEN_RESOLUTION_DESCRIPTION_LABEL', 'Resolution Description');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1580', '2', 'KEY_SCREEN_RESOLUTION_DESCRIPTION_LABEL', 'Description de la résolution');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1581', '1', 'KEY_SCREEN_EST_RESOLUTION_LABEL', 'Estimated Resolution');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1582', '2', 'KEY_SCREEN_EST_RESOLUTION_LABEL', 'Résolution Estimé');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1583', '1', 'KEY_SCREEN_RESOLVE_ISSUE_LABEL', 'Resolve Issue');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1584', '2', 'KEY_SCREEN_RESOLVE_ISSUE_LABEL', 'Résoudre le problème');

