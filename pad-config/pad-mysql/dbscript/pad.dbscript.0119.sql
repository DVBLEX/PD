USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('775', '1', 'KEY_SCREEN_OPERATOR_TYPE_PARKING_LABEL', 'Parking');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('776', '2', 'KEY_SCREEN_OPERATOR_TYPE_PARKING_LABEL', 'Parking');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('777', '1', 'KEY_SCREEN_OPERATOR_TYPE_PARKING_KIOSK_LABEL', 'Parking Kiosk');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('778', '2', 'KEY_SCREEN_OPERATOR_TYPE_PARKING_KIOSK_LABEL', 'Kiosque de stationnement');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('779', '1', 'KEY_SCREEN_OPERATOR_TYPE_PARKING_OFFICE_LABEL', 'Parking Office');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('780', '2', 'KEY_SCREEN_OPERATOR_TYPE_PARKING_OFFICE_LABEL', 'Bureau de stationnement');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('781', '1', 'KEY_SCREEN_OPERATOR_TYPE_PORT_AUTHORITY_LABEL', 'Port Authority');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('782', '2', 'KEY_SCREEN_OPERATOR_TYPE_PORT_AUTHORITY_LABEL', 'Autorité portuaire');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('783', '1', 'KEY_SCREEN_OPERATOR_TYPE_PORT_ENTRY_LABEL', 'Port Entry');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('784', '2', 'KEY_SCREEN_OPERATOR_TYPE_PORT_ENTRY_LABEL', 'Entrée du port');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('785', '1', 'KEY_SCREEN_OPERATOR_TYPE_PORT_EXIT_LABEL', 'Port Exit');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('786', '2', 'KEY_SCREEN_OPERATOR_TYPE_PORT_EXIT_LABEL', 'Sortie du port');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('787', '1', 'KEY_SCREEN_OPERATOR_TYPE_ADMIN_LABEL', 'Admin');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('788', '2', 'KEY_SCREEN_OPERATOR_TYPE_ADMIN_LABEL', 'Admin');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('789', '1', 'KEY_SCREEN_SELECT_OPERATOR_ROLE_MESSAGE', 'Please select Role');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('790', '2', 'KEY_SCREEN_SELECT_OPERATOR_ROLE_MESSAGE', 'Veuillez sélectionner un rôle');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('791', '1', 'KEY_SCREEN_SELECT_LANGUAGE_MESSAGE', 'Please select Language');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('792', '2', 'KEY_SCREEN_SELECT_LANGUAGE_MESSAGE', 'S\'il vous plaît sélectionner la langue');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('793', '1', 'KEY_SCREEN_LANGUAGE_LABEL', 'Language');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('794', '2', 'KEY_SCREEN_LANGUAGE_LABEL', 'La langue');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('795', '1', 'KEY_SCREEN_OPERATOR_ADDED_SUCCESS_MESSAGE', 'Operator saved successfully');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('796', '2', 'KEY_SCREEN_OPERATOR_ADDED_SUCCESS_MESSAGE', 'Opérateur enregistré avec succès');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('797', '1', 'KEY_SCREEN_ENTER_EMAIL_AND_OR_MOBILE_MESSAGE', 'Please enter Email and/or Mobile Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('798', '2', 'KEY_SCREEN_ENTER_EMAIL_AND_OR_MOBILE_MESSAGE', 'Veuillez saisir un email et / ou un numéro de mobile');

ALTER TABLE `pad`.`operators` 
ADD COLUMN `language_id` INT(11) NOT NULL AFTER `operator_id`;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('799', '1', 'KEY_SCREEN_PASSWORD_RESET_SENT_MESSAGE', 'A message for password reset has been sent to user');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('800', '2', 'KEY_SCREEN_PASSWORD_RESET_SENT_MESSAGE', 'Un message de réinitialisation du mot de passe a été envoyé à l\'utilisateur');
UPDATE `pad`.`language_keys` SET `translate_value`='A message for password reset has been sent to' WHERE `id`='799';
UPDATE `pad`.`language_keys` SET `translate_value`='Un message de réinitialisation du mot de passe a été envoyé à' WHERE `id`='800';
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('801', '1', 'KEY_SCREEN_PASSWORD_RESET_SEND_ERROR_MESSAGE', 'Error sending password reset message to');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('802', '2', 'KEY_SCREEN_PASSWORD_RESET_SEND_ERROR_MESSAGE', 'Erreur lors de l\'envoi du message de réinitialisation du mot de passe à');

UPDATE pad.operators SET language_id = 1;