USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('917', '1', 'KEY_SCREEN_TRIP_ID_LABEL', 'Trip Id');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('918', '2', 'KEY_SCREEN_TRIP_ID_LABEL', 'Identifiant de Voyage');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('919', '1', 'KEY_SCREEN_ENTER_TRIP_ID_MESSAGE', 'Please enter Trip Id');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('920', '2', 'KEY_SCREEN_ENTER_TRIP_ID_MESSAGE', 'Veuillez entrer votre identifiant de voyage');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('921', '1', 'KEY_RESPONSE_1178', 'A mission with this Trip Id exists');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('922', '2', 'KEY_RESPONSE_1178', 'Une mission avec cet identifiant de voyage existe');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('923', '1', 'KEY_SCREEN_SEARCH_BY_TRIP_ID_MESSAGE', 'Please search by Trip Id');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('924', '2', 'KEY_SCREEN_SEARCH_BY_TRIP_ID_MESSAGE', 'Veuillez rechercher par ID de voyage');

ALTER TABLE `pad`.`trips` 
ADD COLUMN `reference_number` VARCHAR(32) NOT NULL DEFAULT '' AFTER `driver_id`;
