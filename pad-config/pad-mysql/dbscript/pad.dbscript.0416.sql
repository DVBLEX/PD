USE pad;

CREATE TABLE pad.`independent_port_operators` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(64) NOT NULL,
`name_short` varchar(16) NOT NULL,
`date_created` datetime NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('AFRICA CARGO CENTRAL', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('CSTTAO', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('BOLLORE AFRICA LOGISTIC', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('DIAMOND SHIPPING SERVICE', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('GMT SHIPPING', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('ISTAMCO', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('INTEGRAL LOGISTIX', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('ICS', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('I T S SENEGAL', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('MARITALIA', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('MLT', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('SAFRET (SENEMER)', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('SIMAR', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('SNTT', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('SNAT', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('SOMICOA', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('TIMAR', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('THOCOMAR', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('TRANSEXPRESS', '', now());
INSERT INTO `pad`.`independent_port_operators` (`name`, `name_short`, `date_created`) VALUES ('R-LOGISTIC', '', now());

ALTER TABLE `pad`.`independent_port_operators` 
ADD COLUMN `code` VARCHAR(64) NOT NULL AFTER `id`;

UPDATE pad.independent_port_operators SET code = SHA2(REPLACE(uuid() + '' + id, '-', ''), 256) WHERE id > 0;

ALTER TABLE `pad`.`independent_port_operators` 
ADD UNIQUE INDEX `code_uk` (`code` ASC);

UPDATE `pad`.`port_operators` SET `name` = 'TM NORTH', `name_short` = 'TM-N' WHERE (`id` = '8');

INSERT INTO `pad`.`port_operators` (`id`, `name`, `name_short`, `date_created`) VALUES ('9', 'TM SOUTH', 'TM-S', '2020-10-27 10:41:00');

INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`, `transaction_type_code`, `port_operator_gate_id`, `translate_key`, `translate_key_short`, `is_allowed_for_parking_and_kiosk_op`, `is_auto_release_parking`, `is_direct_to_port`, `is_allow_multiple_entries`, `port_transit_duration_minutes`, `mission_cancel_system_after_minutes`) VALUES ('1038', '9', '1', '', '1003', 'KEY_SCREEN_DROP_OFF_EXPORT_LABEL', 'KEY_SCREEN_DROP_OFF_EXPORT_SHORT_LABEL', '0', '0', '1', '0', '15', '180');
INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`, `transaction_type_code`, `port_operator_gate_id`, `translate_key`, `translate_key_short`, `is_allowed_for_parking_and_kiosk_op`, `is_auto_release_parking`, `is_direct_to_port`, `is_allow_multiple_entries`, `port_transit_duration_minutes`, `mission_cancel_system_after_minutes`) VALUES ('1039', '9', '3', '', '1004', 'KEY_SCREEN_PICK_UP_IMPORT_LABEL', 'KEY_SCREEN_PICK_UP_IMPORT_SHORT_LABEL', '0', '0', '1', '0', '15', '180');

ALTER TABLE `pad`.`missions` 
ADD COLUMN `independent_port_operator_id` INT(11) NOT NULL AFTER `port_operator_id`;

UPDATE pad.missions SET independent_port_operator_id = -1 WHERE id > 0;

ALTER TABLE `pad`.`missions` 
ADD INDEX `independent_port_operator_id` (`independent_port_operator_id` ASC);

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1709', '1', 'KEY_SCREEN_INDEPENDENT_PORT_OPERATOR_LABEL', 'Independent Port Operator');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1710', '2', 'KEY_SCREEN_INDEPENDENT_PORT_OPERATOR_LABEL', 'Opérateur Portuaire Indépendant');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1711', '1', 'KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE', 'Please enter a valid Independent Port Operator');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1712', '2', 'KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE', 'Veuillez saisir un Opérateur Portuaire Indépendant valide');
