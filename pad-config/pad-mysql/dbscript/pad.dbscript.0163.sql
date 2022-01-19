USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1043', '1', 'KEY_SCREEN_ENTERED_PORT_LABEL', 'Entered Port');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1044', '2', 'KEY_SCREEN_ENTERED_PORT_LABEL', 'Port entré');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1045', '1', 'KEY_SCREEN_DENIED_PORT_ENTRY_LABEL', 'Denied Port Entry');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1046', '2', 'KEY_SCREEN_DENIED_PORT_ENTRY_LABEL', 'Entrée au port refusée');

UPDATE `pad`.`language_keys` SET `translate_value`='Autre' WHERE `id`='814';
