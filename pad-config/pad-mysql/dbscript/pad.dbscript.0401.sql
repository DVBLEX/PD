USE pad;

DELETE FROM pad.statements WHERE account_id = -1 AND id > 0;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1699', '1', 'KEY_SCREEN_VEHICLE_ALREADY_PROCESSED_ENTRY_ANPR_MESSAGE', 'This vehicle has been checked in by the automated systems, please instruct the driver to enter the parking.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1700', '2', 'KEY_SCREEN_VEHICLE_ALREADY_PROCESSED_ENTRY_ANPR_MESSAGE', 'Ce véhicule a été enregistré par les systèmes automatisés, veuillez demander au chauffeur d\'entrer dans le parking.');
