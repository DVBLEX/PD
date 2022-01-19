USE pad;

INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('38', 'Manual Release Vehicles at Parking', '2020-03-09 17:41:14');

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `vehicle_release_count` INT(11) NOT NULL AFTER `is_auto_release_parking`;

UPDATE pad.activity_log SET vehicle_release_count = -1 where id > 0;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1335', '1', 'KEY_RESPONSE_1126', 'More than 50% of released vehicles did not exit the parking area. Further manual release is currently not allowed');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1336', '2', 'KEY_RESPONSE_1126', 'Plus de 50% des véhicules libérés ne sont pas sortis du parking. Aucune autre version manuelle n\'est actuellement autorisée');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1337', '1', 'KEY_SCREEN_RELEASE_LABEL', 'Release');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1338', '2', 'KEY_SCREEN_RELEASE_LABEL', 'Libération');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1339', '1', 'KEY_SCREEN_NO_DATA_MESSAGE', 'No Data');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1340', '2', 'KEY_SCREEN_NO_DATA_MESSAGE', 'Pas de Données');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1341', '1', 'KEY_SCREEN_START_AUTO_RELEASE_LABEL', 'Start Auto Release');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1342', '2', 'KEY_SCREEN_START_AUTO_RELEASE_LABEL', 'Lancer la Libération Automatique');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1343', '1', 'KEY_SCREEN_PAUSE_AUTO_RELEASE_LABEL', 'Pause Auto Release');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1344', '2', 'KEY_SCREEN_PAUSE_AUTO_RELEASE_LABEL', 'Suspendre la Libération Automatique');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1345', '1', 'KEY_SCREEN_MANUAL_RELEASE_LABEL', 'Trigger Manual Release');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1346', '2', 'KEY_SCREEN_MANUAL_RELEASE_LABEL', 'Déclenchement Manuel du Déclencheur');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1347', '1', 'KEY_SCREEN_MANUAL_RELEASE_CONFIRMATION_PART1_MESSAGE', 'Are you sure you want to manually release up to');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1348', '2', 'KEY_SCREEN_MANUAL_RELEASE_CONFIRMATION_PART1_MESSAGE', 'Voulez-vous vraiment libérer manuellement jusqu\'à');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1349', '1', 'KEY_SCREEN_MANUAL_RELEASE_CONFIRMATION_PART2_MESSAGE', 'vehicles from parking area');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1350', '2', 'KEY_SCREEN_MANUAL_RELEASE_CONFIRMATION_PART2_MESSAGE', 'véhicules du parking');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1351', '1', 'KEY_SCREEN_VEHICLES_ALREADY_RELEASED_LABEL', 'Vehicles in parking already released');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1352', '2', 'KEY_SCREEN_VEHICLES_ALREADY_RELEASED_LABEL', 'Véhicules en stationnement déjà libérés');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1353', '1', 'KEY_SCREEN_NO_MORE_VEHICLES_TO_RELEASE_MESSAGE', 'There were no more vehicles to release for this port operator and transaction type');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1354', '2', 'KEY_SCREEN_NO_MORE_VEHICLES_TO_RELEASE_MESSAGE', 'Il n\'y avait plus de véhicules à libérer pour cet opérateur portuaire et ce type de transaction');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1355', '1', 'KEY_SCREEN_VEHICLES_WERE_MANUALLY_RELEASED_MESSAGE', 'vehicles were manually released for');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1356', '2', 'KEY_SCREEN_VEHICLES_WERE_MANUALLY_RELEASED_MESSAGE', 'véhicules ont été libérés manuellement pour');
