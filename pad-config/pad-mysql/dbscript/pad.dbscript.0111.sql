USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('763', '1', 'KEY_RESPONSE_1172', 'Vehicle has already been added to this account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('764', '2', 'KEY_RESPONSE_1172', 'Le véhicule a déjà été ajouté à ce compte');

UPDATE `pad`.`language_keys` SET `translate_value`='Please enter Make' WHERE `id`='585';
UPDATE `pad`.`language_keys` SET `translate_value`='S\'il vous plaît entrer Marque' WHERE `id`='586';
