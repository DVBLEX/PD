USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('967', '1', 'KEY_SCREEN_WHITELIST_CREATED_SUCCESS_MESSAGE', 'Night Run successfully created');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('968', '2', 'KEY_SCREEN_WHITELIST_CREATED_SUCCESS_MESSAGE', 'Night Run créé avec succès');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('969', '1', 'KEY_RESPONSE_1184', 'Night Run with these Date and Time already exists');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('970', '2', 'KEY_RESPONSE_1184', 'Night Run avec ces date et heure existe déjà');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('971', '1', 'KEY_SCREEN_SELECT_DATE_FROM_MESSAGE', 'Please select Date From');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('972', '2', 'KEY_SCREEN_SELECT_DATE_FROM_MESSAGE', 'S\'il vous plaît sélectionner la date de');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('973', '1', 'KEY_SCREEN_SELECT_TIME_FROM_MESSAGE', 'Please select Time From');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('974', '2', 'KEY_SCREEN_SELECT_TIME_FROM_MESSAGE', 'S\'il vous plaît sélectionner le temps de');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('975', '1', 'KEY_SCREEN_SELECT_DATE_TO_MESSAGE', 'Please select Date To');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('976', '2', 'KEY_SCREEN_SELECT_DATE_TO_MESSAGE', 'S\'il vous plaît sélectionner la date à');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('977', '1', 'KEY_SCREEN_SELECT_TIME_TO_MESSAGE', 'Please select Time To');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('978', '2', 'KEY_SCREEN_SELECT_TIME_TO_MESSAGE', 'S\'il vous plaît  sélectionner le temps jusqu\'à');

ALTER TABLE `pad`.`port_access_whitelist` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;