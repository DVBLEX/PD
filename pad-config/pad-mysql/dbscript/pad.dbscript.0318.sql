USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1455', '1', 'KEY_SCREEN_SHORTEN_VEHICLE_REGISTRATION_NUMBER_LABEL', 'V.R.N.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1456', '2', 'KEY_SCREEN_SHORTEN_VEHICLE_REGISTRATION_NUMBER_LABEL', 'Immatriculation');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1457', '1', 'KEY_SCREEN_REQUEST_RECEIVED_LABEL', 'Request Received');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1458', '2', 'KEY_SCREEN_REQUEST_RECEIVED_LABEL', 'Demande reçue');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1459', '1', 'KEY_SCREEN_PROCESSED_BY_LABEL', 'Processed By');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1460', '2', 'KEY_SCREEN_PROCESSED_BY_LABEL', 'Traité par');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1461', '1', 'KEY_SCREEN_PROCESSED_DATE_LABEL', 'Processed Date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1462', '2', 'KEY_SCREEN_PROCESSED_DATE_LABEL', 'Date de traitement');

ALTER TABLE `pad`.`trips` 
CHANGE COLUMN `date_deny` `date_approved_denied` DATETIME NULL DEFAULT NULL ;
