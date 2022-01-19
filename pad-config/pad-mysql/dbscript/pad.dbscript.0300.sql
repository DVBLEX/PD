USE pad;

ALTER TABLE `pad`.`parking` 
ADD COLUMN `vehicle_state` INT(11) NOT NULL AFTER `port_operator_gate_id`;

UPDATE pad.parking SET vehicle_state = 1 WHERE id > 0;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1409', '1', 'KEY_SCREEN_VEHICLE_STATE_LABEL', 'Vehicle State');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1410', '2', 'KEY_SCREEN_VEHICLE_STATE_LABEL', 'État du Véhicule');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1411', '1', 'KEY_SCREEN_VEHICLE_STATE_NORMAL_LABEL', 'Normal');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1412', '2', 'KEY_SCREEN_VEHICLE_STATE_NORMAL_LABEL', 'Ordinaire');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1413', '1', 'KEY_SCREEN_VEHICLE_STATE_BROKEN_DOWN_LABEL', 'Broken Down');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1414', '2', 'KEY_SCREEN_VEHICLE_STATE_BROKEN_DOWN_LABEL', 'En Panne');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1415', '1', 'KEY_SCREEN_VEHICLE_STATE_CLAMPED_LABEL', 'Clamped');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1416', '2', 'KEY_SCREEN_VEHICLE_STATE_CLAMPED_LABEL', 'Serré');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1417', '1', 'KEY_SCREEN_VEHICLE_STATE_UNRESPONSIVE_LABEL', 'Unresponsive');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1418', '2', 'KEY_SCREEN_VEHICLE_STATE_UNRESPONSIVE_LABEL', 'Ne Répond Pas');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1419', '1', 'KEY_SCREEN_SELECT_VEHICLE_STATE_MESSAGE', 'Please select Vehicle State');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1420', '2', 'KEY_SCREEN_SELECT_VEHICLE_STATE_MESSAGE', 'Veuillez sélectionner l\'état du véhicule');
