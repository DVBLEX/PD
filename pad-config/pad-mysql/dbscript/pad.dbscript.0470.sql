USE pad;

UPDATE `pad`.`language_keys` SET `translate_value` = 'Invalid First Name. Special characters are not allowed' WHERE (`id` = '1815');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Prénom invalide. Les caractères spéciaux ne sont pas autorisés' WHERE (`id` = '1816');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Invalid Last Name. Special characters are not allowed' WHERE (`id` = '1817');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Nom de famille invalide. Les caractères spéciaux ne sont pas autorisés' WHERE (`id` = '1818');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1819', '1', 'KEY_RESPONSE_1093', 'Invalid First/Last Staff User name. Special characters are not allowed');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1820', '2', 'KEY_RESPONSE_1093', 'Nom/prénom de l\'utilisateur non valide. Les caractères spéciaux ne sont pas autorisés');
