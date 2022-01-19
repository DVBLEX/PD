USE pad;

ALTER TABLE `pad`.`trips`
ADD COLUMN `date_slot_assigned` DATETIME NULL AFTER `date_slot_start`;

ALTER TABLE `pad`.`trips` 
CHANGE COLUMN `date_slot_start` `date_slot_requested` DATETIME NOT NULL ;

ALTER TABLE `pad`.`trips` 
CHANGE COLUMN `date_slot_assigned` `date_slot_approved` DATETIME NULL DEFAULT NULL ;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('815', '1', 'KEY_SCREEN_APPROVED_BY_PORT_OPERATOR_LABEL', 'Approved by port operator');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('816', '2', 'KEY_SCREEN_APPROVED_BY_PORT_OPERATOR_LABEL', 'Approuvé par l\'opérateur du port');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('817', '1', 'KEY_SCREEN_DENIED_BY_PORT_OPERATOR_LABEL', 'Denied by port operator');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('818', '2', 'KEY_SCREEN_DENIED_BY_PORT_OPERATOR_LABEL', 'Refusé par l\'opérateur du port');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('819', '1', 'KEY_SCREEN_COMPLETED_LABEL', 'Completed');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('820', '2', 'KEY_SCREEN_COMPLETED_LABEL', 'Terminé');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('821', '1', 'KEY_SCREEN_SLOT_TIME_FROM_LABEL', 'Slot Time From');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('822', '2', 'KEY_SCREEN_SLOT_TIME_FROM_LABEL', 'Heure de créneau horaire de');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('823', '1', 'KEY_SCREEN_TO_LABEL', 'To');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('824', '2', 'KEY_SCREEN_TO_LABEL', 'À');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('825', '1', 'KEY_SCREEN_SLOT_DATE_FROM_LABEL', 'Slot Date From');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('826', '2', 'KEY_SCREEN_SLOT_DATE_FROM_LABEL', 'Date du créneau');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('827', '1', 'KEY_SCREEN_NO_TRIP_RETURNED_MESSAGE', 'No trips returned');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('828', '2', 'KEY_SCREEN_NO_TRIP_RETURNED_MESSAGE', 'Aucun voyage rendu');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('829', '1', 'KEY_SCREEN_REQUESTED_SLOT_DATETIME_LABEL', 'Requested Slot Date');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('830', '2', 'KEY_SCREEN_REQUESTED_SLOT_DATETIME_LABEL', 'Date de créneau demandé');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('831', '1', 'KEY_SCREEN_SELECT_TRIP_LABEL', 'Select trip');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('832', '2', 'KEY_SCREEN_SELECT_TRIP_LABEL', 'Sélectionnez un voyage');

UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_APPROVE_LABEL', `translate_value`='Approve' WHERE `id`='309';
UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_APPROVE_LABEL', `translate_value`='Approuver' WHERE `id`='310';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('833', '1', 'KEY_SCREEN_SELECT_AT_LEAST_ONE_TRIP_MESSAGE', 'Please select at least one trip');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('834', '2', 'KEY_SCREEN_SELECT_AT_LEAST_ONE_TRIP_MESSAGE', 'Veuillez sélectionner au moins un voyage');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('835', '1', 'KEY_SCREEN_SELECTED_TRIPS_APPROVED_MESSAGE', 'The selected trips have been approved');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('836', '2', 'KEY_SCREEN_SELECTED_TRIPS_APPROVED_MESSAGE', 'Les voyages sélectionnés ont été approuvés');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('837', '1', 'KEY_SCREEN_SELECTED_TRIPS_NUMBER_MESSAGE', 'Number of trips selected');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('838', '2', 'KEY_SCREEN_SELECTED_TRIPS_NUMBER_MESSAGE', 'Nombre de voyages sélectionnés');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('839', '1', 'KEY_SCREEN_SELECTED_TRIPS_DENIED_MESSAGE', 'The selected trips have been denied');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('840', '2', 'KEY_SCREEN_SELECTED_TRIPS_DENIED_MESSAGE', 'Les voyages sélectionnés ont été refusés');

UPDATE `pad`.`language_keys` SET `translate_value`='Please enter reason for denial' WHERE `id`='403';
UPDATE `pad`.`language_keys` SET `translate_value`='Veuillez saisir le motif du refus' WHERE `id`='404';

ALTER TABLE `pad`.`trips` 
ADD COLUMN `reason_deny` VARCHAR(128) NOT NULL AFTER `date_slot_approved`,
ADD COLUMN `date_deny` DATETIME NULL DEFAULT NULL AFTER `reason_deny`;


