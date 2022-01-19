USE pad;

UPDATE `pad`.`language_keys` SET `translate_value` = 'This mission is valid from' WHERE (`id` = '1451');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Cette mission est disponible du' WHERE (`id` = '1452');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1821', '1', 'KEY_SCREEN_BETWEEN_HOURS_MESSAGE', 'between the hours of');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1822', '2', 'KEY_SCREEN_BETWEEN_HOURS_MESSAGE', 'entre');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1823', '1', 'KEY_SCREEN_AND_MESSAGE', 'and');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1824', '2', 'KEY_SCREEN_AND_MESSAGE', 'et');

