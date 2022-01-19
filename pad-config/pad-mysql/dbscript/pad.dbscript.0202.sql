USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1163', '1', 'KEY_SCREEN_PARKING_ENTRY_MSG_URGENT_MISSION', 'Please advise the driver to go directly to the parking exit gate and then head to the port entry. The driver is authorised to proceed to the port entry');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1164', '2', 'KEY_SCREEN_PARKING_ENTRY_MSG_URGENT_MISSION', 'Veuillez conseiller au chauffeur de se rendre directement à la porte de sortie du parking puis de se diriger vers l\'entrée du port. Le pilote est autorisé à se rendre à l\'entrée du port');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1165', '1', 'KEY_RESPONSE_1149', 'Account balance is low. Please top up the account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1166', '2', 'KEY_RESPONSE_1149', 'Le solde du compte est faible. Veuillez recharger le compte');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Vehicle is not authorized port entry' WHERE (`id` = '615');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Le véhicule n\'est pas autorisé à entrer au port' WHERE (`id` = '616');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1167', '1', 'KEY_SCREEN_EXIT_CLOSED_BY_SYSTEM_LABEL', 'Exit - closed by system');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1168', '2', 'KEY_SCREEN_EXIT_CLOSED_BY_SYSTEM_LABEL', 'Sortie - fermée par le système');
