USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('601', '1', 'KEY_SCREEN_VEHICLE_FOUND_MESSAGE', 'Vehicle found. To confirm exit, please tap on Proceed button');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('602', '2', 'KEY_SCREEN_VEHICLE_FOUND_MESSAGE', 'Véhicule trouvé. Pour confirmer la sortie, s\'il vous plaît appuyez sur le bouton Continuer');

ALTER TABLE `pad`.`parking`
ADD COLUMN `type` INT(11) NOT NULL AFTER `code`;

UPDATE `pad`.`parking` SET `type`='1' WHERE `id`>0;

UPDATE `pad`.`language_keys` SET `translate_value`='Vehicle not found. Please try again' WHERE `id`='427';
UPDATE `pad`.`language_keys` SET `translate_value`='Véhicule introuvable. Veuillez réessayer' WHERE `id`='428';