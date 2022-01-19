USE pad;

ALTER TABLE `pad`.`drivers` 
ADD COLUMN `language_id` INT(11) NOT NULL AFTER `licence_number`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1311', '1', 'KEY_SCREEN_WOLOF_LABEL', 'Wolof');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1312', '2', 'KEY_SCREEN_WOLOF_LABEL', 'Wolof');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1313', '1', 'KEY_SCREEN_BAMBARA_LABEL', 'Bambara');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1314', '2', 'KEY_SCREEN_BAMBARA_LABEL', 'Bambara');

UPDATE pad.drivers set language_id = 2;

INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('25', '1', 'Exit Parking - WO', '3', '-1', '1', 'AGS', '${referenceLabel} : ${referenceNumber} - Ban baay bi parking area léegi-léegi ak jiitu ci bi port', '', '10', '-1', '2019-03-04 14:10:10', '2019-03-04 14:10:10');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('26', '1', 'Exit Parking - BM', '4', '-1', '1', 'AGS', '${referenceLabel} : ${referenceNumber} - Please leave the parking area now and proceed to the port.', '', '10', '-1', '2019-03-04 14:10:10', '2019-03-04 14:10:10');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('27', '6', 'Trip Approved Notification Driver - WO', '3', '-1', '1', 'AGS', 'Salaam aleekum ${driverName}, ${referenceLabel} : ${referenceNumber} a been scheduled pur ${slotDateTime} pur vehicle ${vehicleReg}. ${parkingOrPortMessage}.', ' ', '10', '-1', '2019-05-23 10:11:20', '2019-05-23 10:11:20');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('28', '6', 'Trip Approved Notification Driver - BM', '4', '-1', '1', 'AGS', 'Bonjour ${driverName}, ${referenceLabel} : ${referenceNumber} a été programmé pour ${slotDateTime} pour véhicule ${vehicleReg}. ${parkingOrPortMessage}.', ' ', '10', '-1', '2019-05-23 10:11:20', '2019-05-23 10:11:20');

ALTER TABLE `pad`.`parking` 
ADD COLUMN `driver_language_id` INT(11) NOT NULL AFTER `driver_msisdn`;

UPDATE pad.parking SET driver_language_id = 2;

UPDATE pad.drivers SET language_id = 2;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `driver_language_id` INT(11) NOT NULL AFTER `driver_msisdn`;

UPDATE pad.trips SET driver_language_id = 2;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1327', '1', 'KEY_SCREEN_DRIVER_LANGUAGE_LABEL', 'Driver Language');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1328', '2', 'KEY_SCREEN_DRIVER_LANGUAGE_LABEL', 'Langue du pilote');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1329', '1', 'KEY_SCREEN_ENTER_VALID_LANGUAGE_MESSAGE', 'Please enter valid language');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1330', '2', 'KEY_SCREEN_ENTER_VALID_LANGUAGE_MESSAGE', 'Veuillez entrer une langue valide');

-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DRIVER_DETAILS_UPDATED_MESSAGE', `translate_value` = 'Driver details were successfully updated' WHERE (`id` = '769');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DRIVER_DETAILS_UPDATED_MESSAGE', `translate_value` = 'Les détails du pilote ont été mis à jour avec succès' WHERE (`id` = '770');
