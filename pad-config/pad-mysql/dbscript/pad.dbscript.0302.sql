USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1421', '1', 'KEY_SCREEN_LANES_LABEL', 'Lanes');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1422', '2', 'KEY_SCREEN_LANES_LABEL', 'Voies');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1423', '1', 'KEY_SCREEN_LANE_ID_LABEL', 'Lane ID');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1424', '2', 'KEY_SCREEN_LANE_ID_LABEL', 'Identifiant de Voie');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1425', '1', 'KEY_SCREEN_ZONE_ID_LABEL', 'Zone ID');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1426', '2', 'KEY_SCREEN_ZONE_ID_LABEL', 'Identifiant de Zone');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1427', '1', 'KEY_SCREEN_ALLOWED_HOSTS_LABEL', 'Allowed Hosts');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1428', '2', 'KEY_SCREEN_ALLOWED_HOSTS_LABEL', 'Hôtes autorisés');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1429', '1', 'KEY_SCREEN_DATE_LAST_REQUEST_LABEL', 'Date Last Request');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1430', '2', 'KEY_SCREEN_DATE_LAST_REQUEST_LABEL', 'Date dernière demande');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1431', '1', 'KEY_SCREEN_ADD_LANE_LABEL', 'Add Lane');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1432', '2', 'KEY_SCREEN_ADD_LANE_LABEL', 'Ajouter une voie');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1433', '1', 'KEY_SCREEN_UPDATE_LANE_LABEL', 'Update Lane');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1434', '2', 'KEY_SCREEN_UPDATE_LANE_LABEL', 'Mettre à jour la voie');

ALTER TABLE `pad`.`lanes` 
ADD COLUMN `is_active` TINYINT(1) NOT NULL AFTER `date_last_request`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1435', '1', 'KEY_SCREEN_LANE_SAVED_MESSAGE', 'Lane saved successfully');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1436', '2', 'KEY_SCREEN_LANE_SAVED_MESSAGE', 'Lane enregistré avec succès');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1437', '1', 'KEY_SCREEN_LANE_UPDATED_MESSAGE', 'Lane updated successfully');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1438', '2', 'KEY_SCREEN_LANE_UPDATED_MESSAGE', 'Lane mis à jour avec succès');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1439', '1', 'KEY_RESPONSE_1199', 'Lane ID already exist');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1440', '2', 'KEY_RESPONSE_1199', 'L\'identifiant de voie existe déjà');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1441', '1', 'KEY_RESPONSE_1180', 'Lane/Device mapping already exist');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1442', '2', 'KEY_RESPONSE_1180', 'Le mappage de voie / périphérique existe déjà');

-- UPDATE `pad`.`language_keys` SET `translate_value` = 'Device is already mapped to another lane' WHERE (`id` = '1441');
-- UPDATE `pad`.`language_keys` SET `translate_value` = 'L\'appareil est déjà mappé sur une autre voie' WHERE (`id` = '1442');
