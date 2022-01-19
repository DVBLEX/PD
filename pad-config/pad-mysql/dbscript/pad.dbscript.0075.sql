USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('581', '1', 'KEY_NAVBAR_PARKING_EXIT_ONLY', 'Exit Only');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('582', '2', 'KEY_NAVBAR_PARKING_EXIT_ONLY', 'Sortie seulement');

ALTER TABLE `pad`.`parking`
ADD COLUMN `vehicle_color` VARCHAR(32) NOT NULL AFTER `vehicle_registration`;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('583', '1', 'KEY_RESPONSE_1153', 'Vehicle already associated with trip. Proceed to payment');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('584', '2', 'KEY_RESPONSE_1153', 'Véhicule déjà associé au voyage. Procéder au paiement');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('585', '1', 'KEY_SCREEN_ENTER_VEHICLE_MAKE_MESSAGE', 'Please enter make');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('586', '2', 'KEY_SCREEN_ENTER_VEHICLE_MAKE_MESSAGE', 'S\'il vous plaît entrer marque');

