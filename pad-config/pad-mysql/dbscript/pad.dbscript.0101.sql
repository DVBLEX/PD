USE pad;

ALTER TABLE `pad`.`missions` 
DROP COLUMN `customs_delivery_order`,
DROP COLUMN `sales_delivery_order`;

-- DELETE FROM `pad`.`language_keys` WHERE `id`='287';
-- DELETE FROM `pad`.`language_keys` WHERE `id`='288';
-- DELETE FROM `pad`.`language_keys` WHERE `id`='289';
-- DELETE FROM `pad`.`language_keys` WHERE `id`='290';
-- DELETE FROM `pad`.`language_keys` WHERE `id`='295';
-- DELETE FROM `pad`.`language_keys` WHERE `id`='293';

ALTER TABLE `pad`.`missions` 
CHANGE COLUMN `pregate_number` `reference_number` VARCHAR(32) NOT NULL ;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('693', '1', 'KEY_SCREEN_BAD_NUMBER_LABEL', 'BAD Number');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('694', '2', 'KEY_SCREEN_BAD_NUMBER_LABEL', 'Nombre BAD');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('695', '1', 'KEY_SCREEN_REFERENCE_NUMBER_LABEL', 'Reference Number');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('696', '2', 'KEY_SCREEN_REFERENCE_NUMBER_LABEL', 'Numéro de réference');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('697', '1', 'KEY_SCREEN_ENTER_BAD_NUMBER_MESSAGE', 'Please enter BAD Number');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('698', '2', 'KEY_SCREEN_ENTER_BAD_NUMBER_MESSAGE', 'S\'il vous plaît entrer le nombre BAD');

-- DELETE FROM `pad`.`language_keys` WHERE `id`='435';
-- DELETE FROM `pad`.`language_keys` WHERE `id`='436';

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('701', '1', 'KEY_RESPONSE_1165', 'Mission not found for this BAD number. Please enter driver mobile number and slot datetime to create the ad hoc trip');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('702', '2', 'KEY_RESPONSE_1165', 'Mission non trouvée pour cette grossesse. Veuillez saisir le numéro de portable du conducteur et la date et l’heure pour créer le voyage ad hoc.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('703', '1', 'KEY_RESPONSE_1166', 'Mission found for this BAD number. Please enter driver mobile number and slot datetime to create the ad hoc trip');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('704', '2', 'KEY_RESPONSE_1166', 'Mission trouvée pour cette grossesse. Veuillez saisir le numéro de portable du conducteur et la date et l’heure pour créer le voyage ad hoc.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('705', '1', 'KEY_RESPONSE_1167', 'A mission with this BAD number already exists');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('706', '2', 'KEY_RESPONSE_1167', 'Une mission avec cette BAD existe déjà');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('707', '1', 'KEY_SCREEN_CONTAINER_ID_LABEL', 'Container ID');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('708', '2', 'KEY_SCREEN_CONTAINER_ID_LABEL', 'Identifiant du conteneur');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('709', '1 ', 'KEY_SCREEN_ENTER_CONTAINER_ID_MESSAGE', 'Please enter Container ID');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('710', '2', 'KEY_SCREEN_ENTER_CONTAINER_ID_MESSAGE', 'Veuillez entrer l\'ID du conteneur');

-- UPDATE `pad`.`language_keys` SET `translate_value`='Vehicle was not found. Please search by pregate number or BAD number' WHERE `id`='455';
-- UPDATE `pad`.`language_keys` SET `translate_value`='Le véhicule n\'a pas été trouvé. Veuillez rechercher par numéro de prégate ou par numéro BAD' WHERE `id`='456';

-- UPDATE `pad`.`language_keys` SET `translate_value`='More than 1 trip found for this vehicle. Please select Pregate Number or BAD Number' WHERE `id`='547';
-- UPDATE `pad`.`language_keys` SET `translate_value`='Plus d\'un voyage trouvé pour ce véhicule. S\'il vous plaît sélectionnez le numéro Pregate ou le numéro BAD' WHERE `id`='548';

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('711', '1', 'KEY_RESPONSE_1169', 'A mission with this Container ID already exists.');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('712', '2', 'KEY_RESPONSE_1169', 'Une mission avec cet ID de conteneur existe déjà.');


ALTER TABLE `pad`.`missions` 
ADD COLUMN `container_id` VARCHAR(32) NOT NULL AFTER `reference_number`;


-- UPDATE `pad`.`language_keys` SET `translate_value`='More than 1 trip found for this vehicle. Please select Operator and Transaction Type' WHERE `id`='547';
-- UPDATE `pad`.`language_keys` SET `translate_value`='Plus d\'un voyage trouvé pour ce véhicule. Veuillez sélectionner le type d\'opérateur et de transaction' WHERE `id`='548';