USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1357', '1', 'KEY_SCREEN_PARKING_RELEASE_LABEL', 'Parking Release');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1358', '2', 'KEY_SCREEN_PARKING_RELEASE_LABEL', 'Libération de Stationnement');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Limites de Réservation' WHERE (`id` = '1332');

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `translate_key_short` VARCHAR(128) NOT NULL AFTER `translate_key`;

UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL' WHERE (`id` = '1001');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL' WHERE (`id` = '1006');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL' WHERE (`id` = '1011');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL' WHERE (`id` = '1036');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL' WHERE (`id` = '1043');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EMPTY_SHORT_LABEL' WHERE (`id` = '1002');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1003');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1007');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1012');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1017');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1022');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1027');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1032');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1037');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1044');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_PICK_UP_EMPTY_SHORT_LABEL' WHERE (`id` = '1004');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key_short` = 'KEY_SCREEN_URGENT_PICK_UP_IMPORT_SHORT_LABEL' WHERE (`id` = '1009');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1361', '1', 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL', 'Exp.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1362', '2', 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL', 'Exp.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1363', '1', 'KEY_SCREEN_DROP_OFF_EMPTY_SHORT_LABEL', 'D.Emp');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1364', '2', 'KEY_SCREEN_DROP_OFF_EMPTY_SHORT_LABEL', 'D.Vide');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1365', '1', 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL', 'Imp.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1366', '2', 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL', 'Imp.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1367', '1', 'KEY_SCREEN_PICK_UP_EMPTY_SHORT_LABEL', 'C.Emp');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1368', '2', 'KEY_SCREEN_PICK_UP_EMPTY_SHORT_LABEL', 'C.Vide');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1369', '1', 'KEY_SCREEN_URGENT_PICK_UP_IMPORT_SHORT_LABEL', 'Urg-Imp');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1370', '2', 'KEY_SCREEN_URGENT_PICK_UP_IMPORT_SHORT_LABEL', 'Imp-Urg');

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `is_parking_supervisor_readonly_enabled` TINYINT(1) NOT NULL COMMENT 'flag to enable/disable the ability for parking supervisor operator to start/pause parking auto-release and manual release functionality' AFTER `is_booking_limit_check_enabled`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1371', '1', 'KEY_SCREEN_READONLY_LABEL', 'Read-only');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1372', '2', 'KEY_SCREEN_READONLY_LABEL', 'Lecture-seulement');
