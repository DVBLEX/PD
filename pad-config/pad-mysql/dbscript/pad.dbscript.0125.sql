USE pad;

CREATE TABLE `pad`.`sessions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(64) NOT NULL,
  `status` INT(11) NOT NULL,
  `kiosk_operator_id` INT(11) NOT NULL,
  `operator_id` INT(11) NOT NULL,
  `lane_number` INT(11) NOT NULL,
  `cash_bag_number` INT(11) NOT NULL,
  `amount_start` DECIMAL(7,0) NOT NULL,
  `amount_end` DECIMAL(7,0) NOT NULL,
  `amount_collected` DECIMAL(7,0) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_start` DATETIME NULL,
  `date_end` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('857', '1', 'KEY_SCREEN_KIOSK_SESSIONS_LABEL', 'Kiosk Sessions');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('858', '2', 'KEY_SCREEN_KIOSK_SESSIONS_LABEL', 'Sessions Kiosque');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('859', '1', 'KEY_SCREEN_KIOSK_OPERATOR_NAME_LABEL', 'Kiosk Operator Name');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('860', '2', 'KEY_SCREEN_KIOSK_OPERATOR_NAME_LABEL', 'Nom de l\'opérateur du kiosque');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('861', '1', 'KEY_SCREEN_LANE_NUMBER_LABEL', 'Lane Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('862', '2', 'KEY_SCREEN_LANE_NUMBER_LABEL', 'Numéro de voie');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('863', '1', 'KEY_SCREEN_CASH_BAG_NUMBER_LABEL', 'Cash Bag Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('864', '2', 'KEY_SCREEN_CASH_BAG_NUMBER_LABEL', 'Numéro de sac de caisse');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('865', '1', 'KEY_SCREEN_AMOUNT_START_LABEL', 'Amount Start');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('866', '2', 'KEY_SCREEN_AMOUNT_START_LABEL', 'Montant Début');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('867', '1', 'KEY_SCREEN_AMOUNT_END_LABEL', 'Amount End');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('868', '2', 'KEY_SCREEN_AMOUNT_END_LABEL', 'Montant Fin');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('869', '1', 'KEY_SCREEN_AMOUNT_COLLECTED_LABEL', 'Amount Collected');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('870', '2', 'KEY_SCREEN_AMOUNT_COLLECTED_LABEL', 'Montant perçu');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('871', '1', 'KEY_SCREEN_VALIDATE_SESSION_LABEL', 'Validate Session');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('872', '2', 'KEY_SCREEN_VALIDATE_SESSION_LABEL', 'Valider la session');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('873', '1', 'KEY_SCREEN_NO_SESSION_RETURNED_MESSAGE', 'No sessions returned');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('874', '2', 'KEY_SCREEN_NO_SESSION_RETURNED_MESSAGE', 'Aucune session retournée');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('875', '1', 'KEY_SCREEN_STATUS_ASSIGNED_LABEL', 'Assigned');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('876', '2', 'KEY_SCREEN_STATUS_ASSIGNED_LABEL', 'Attribué');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('877', '1', 'KEY_SCREEN_STATUS_VALIDATED_LABEL', 'Validated');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('878', '2', 'KEY_SCREEN_STATUS_VALIDATED_LABEL', 'Validé');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('879', '1', 'KEY_SCREEN_AMOUNT_END_EXPECTED_LABEL', 'Amount End Expected');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('880', '2', 'KEY_SCREEN_AMOUNT_END_EXPECTED_LABEL', 'Montant Fin Attendu');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('881', '1', 'KEY_SCREEN_CREATE_KIOSK_SESSION_LABEL', 'Create Kiosk Session');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('882', '2', 'KEY_SCREEN_CREATE_KIOSK_SESSION_LABEL', 'Créer une session de kiosque');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('883', '1', 'KEY_SCREEN_VALIDATE_KIOSK_SESSION_LABEL', 'Validate Kiosk Session');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('884', '2', 'KEY_SCREEN_VALIDATE_KIOSK_SESSION_LABEL', 'Valider la session du kiosque');

UPDATE `pad`.`email_config` SET `smtp_host`='send.one.com', `smtp_port`='587', `smtp_socket_factory_port`='587', `smtp_starttls_enable`='1' WHERE `id`='1';

UPDATE pad.email_templates SET email_from = 'no-reply@agsparking.com', email_from_password = 'GDSbvgz#64xTEbja#g4JGwdgbPeG5jS9' WHERE id > 0;

ALTER TABLE `pad`.`sessions` 
CHANGE COLUMN `cash_bag_number` `cash_bag_number` VARCHAR(16) NOT NULL ;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('885', '1', 'KEY_SCREEN_SELECT_LANE_NUMBER_MESSAGE', 'Please select Lane Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('886', '2', 'KEY_SCREEN_SELECT_LANE_NUMBER_MESSAGE', 'Veuillez sélectionner le numéro de voie');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('887', '1', 'KEY_SCREEN_SELECT_START_AMOUNT_MESSAGE', 'Please select Amount Start');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('888', '2', 'KEY_SCREEN_SELECT_START_AMOUNT_MESSAGE', 'Veuillez sélectionner Montant Début');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('889', '1', 'KEY_SCREEN_ENTERT_CASH_BAG_NUMBER_MESSAGE', 'Please enter Cash Bag Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('890', '2', 'KEY_SCREEN_ENTERT_CASH_BAG_NUMBER_MESSAGE', 'Veuillez entrer le numéro du sac de paiement');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('891', '1', 'KEY_RESPONSE_1176', 'An active kiosk session for this operator already exists. Please validate the existing active session first.');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('892', '2', 'KEY_RESPONSE_1176', 'Une session de kiosque active pour cet opérateur existe déjà. Veuillez d\'abord valider la session active existante.');

UPDATE `pad`.`language_keys` SET `translate_value`='An active kiosk session for this operator already exists.' WHERE `id`='891';
UPDATE `pad`.`language_keys` SET `translate_value`='Une session de kiosque active pour cet opérateur existe déjà.' WHERE `id`='892';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('893', '1', 'KEY_SCREEN_NO_ACTIVE_KIOSK_SESSION_MESSAGE', 'No active kiosk session found for this operator account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('894', '2', 'KEY_SCREEN_NO_ACTIVE_KIOSK_SESSION_MESSAGE', 'Aucune session de kiosque active trouvée pour ce compte opérateur');

UPDATE `pad`.`language_keys` SET `translate_value`='No session assigned to this operator account' WHERE `id`='893';
UPDATE `pad`.`language_keys` SET `translate_value`='Aucune session assignée à ce compte opérateur' WHERE `id`='894';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('895', '1', 'KEY_SCREEN_END_KIOSK_SESSION_LABEL', 'End Session');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('896', '2', 'KEY_SCREEN_END_KIOSK_SESSION_LABEL', 'Fin de session');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('897', '1', 'KEY_SCREEN_END_KIOSK_SESSION_COMPLETE_MESSAGE', 'Kiosk Session Complete');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('898', '2', 'KEY_SCREEN_END_KIOSK_SESSION_COMPLETE_MESSAGE', 'Session de kiosque terminée');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('899', '1', 'KEY_SCREEN_CONFIRM_LANE_NUMBER_LABEL', 'Confirm Lane Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('900', '2', 'KEY_SCREEN_CONFIRM_LANE_NUMBER_LABEL', 'Confirmer le numéro de voie');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('901', '1', 'KEY_SCREEN_CONFIRM_LABEL', 'Confirm');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('902', '2', 'KEY_SCREEN_CONFIRM_LABEL', 'Confirmer');



