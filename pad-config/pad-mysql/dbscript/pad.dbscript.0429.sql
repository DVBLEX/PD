USE pad;

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `type` INT(11) NOT NULL AFTER `code`;

UPDATE pad.sessions SET type = 1 WHERE id > 0;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1739', '1', 'KEY_SCREEN_OPERATOR_TYPE_VIRTUAL_KIOSK_LABEL', 'Virtual Kiosk');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1740', '2', 'KEY_SCREEN_OPERATOR_TYPE_VIRTUAL_KIOSK_LABEL', 'Kiosque Virtuel');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1741', '1', 'KEY_SCREEN_SELECT_TYPE_MESSAGE', 'Please select a Type');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1742', '2', 'KEY_SCREEN_SELECT_TYPE_MESSAGE', 'Veuillez sélectionner un Type');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1743', '1', 'KEY_SCREEN_END_KIOSK_SESSION_CONFIRMATION_MESSAGE', 'Are you sure you want to end the session?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1744', '2', 'KEY_SCREEN_END_KIOSK_SESSION_CONFIRMATION_MESSAGE', 'Voulez-vous vraiment mettre fin à la session?');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1745', '1', 'KEY_RESPONSE_1095', 'The Ad-hoc trip can be booked up to 24 hours in advance. Please select a slot time within the next 24 hours');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1746', '2', 'KEY_RESPONSE_1095', 'Le voyage ad hoc peut être réservé jusqu\'à 24 heures à l\'avance. Veuillez sélectionner un créneau horaire dans les prochaines 24 heures');

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_allowed_for_virtual_kiosk_op` TINYINT(1) NOT NULL AFTER `is_allowed_for_parking_and_kiosk_op`;

UPDATE pad.port_operator_transaction_types SET is_allowed_for_virtual_kiosk_op = 1 WHERE is_direct_to_port = 1 AND id > 0;

UPDATE `pad`.`system_parameters` SET `in_transit_validity_minutes` = '1440' WHERE (`id` = '1');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1753', '1', 'KEY_SCREEN_PORT_ENTRY_MSG_AUTH_ENTRY', 'Payment successful! Please advise the driver they are authorised to proceed to the port entry at the appropriate time');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1754', '2', 'KEY_SCREEN_PORT_ENTRY_MSG_AUTH_ENTRY', 'Paiement réussi! Veuillez informer le chauffeur qu\'il est autorisé à se rendre à l\'entrée du port au moment opportun');
