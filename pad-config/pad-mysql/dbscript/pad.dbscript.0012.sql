USE pad;

UPDATE `pad`.`language_keys` SET `translate_value`='Identifiez-vous' WHERE `id`='30';
UPDATE `pad`.`language_keys` SET `translate_value`='Adresse e-mail' WHERE `id`='32';
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('97', '1', 'KEY_SCREEN_CREATE_ACCOUNT_MESSAGE', 'Create Account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('98', '2', 'KEY_SCREEN_CREATE_ACCOUNT_MESSAGE', 'Créer un compte');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('101', '1', 'KEY_SCREEN_SUCCESS_LABEL', 'Success');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('102', '2', 'KEY_SCREEN_SUCCESS_LABEL', 'Succès');
