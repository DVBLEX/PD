USE pad;

ALTER TABLE `pad`.`drivers`
CHANGE COLUMN `licence_number` `licence_number` VARCHAR(32) NOT NULL ;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('991', '1', 'KEY_SCREEN_COUNTRY_ISSUE_LABEL', 'Country of Issue');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('992', '2', 'KEY_SCREEN_COUNTRY_ISSUE_LABEL', 'Pays d\'émission');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('993', '1', 'KEY_SCREEN_SELECT_COUNTRY_ISSUE_MESSAGE', 'Please select Country of Issue');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('994', '2', 'KEY_SCREEN_SELECT_COUNTRY_ISSUE_MESSAGE', 'Veuillez sélectionner le pays d\'émission');

ALTER TABLE `pad`.`drivers`
CHANGE COLUMN `nationality_country_iso` `issuing_country_iso` VARCHAR(2) NOT NULL ;

CREATE TABLE pad.`driver_associations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `date_deleted` datetime DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('995', '1', 'KEY_RESPONSE_1186', 'Driver has already been associated to this transporter account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('996', '2', 'KEY_RESPONSE_1186', 'Le pilote a déjà été associé à ce compte de transporteur');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('997', '1', 'KEY_SCREEN_DRIVER_ADDED_SUCCESS_MESSAGE', 'Driver successfully added');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('998', '2', 'KEY_SCREEN_DRIVER_ADDED_SUCCESS_MESSAGE', 'Pilote ajouté avec succès');

ALTER TABLE `pad`.`drivers`
DROP COLUMN `account_id`,
DROP INDEX `account_id_ik` ;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('999', '1', 'KEY_SCREEN_REJECTED_LABEL', 'Rejected');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1000', '2', 'KEY_SCREEN_REJECTED_LABEL', 'Rejeté');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1001', '1', 'KEY_SCREEN_DELETED_LABEL', 'Deleted');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1002', '2', 'KEY_SCREEN_DELETED_LABEL', 'Supprimé');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1003', '1', 'KEY_SCREEN_EDIT_DRIVER_LABEL', 'Edit Driver');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1004', '2', 'KEY_SCREEN_EDIT_DRIVER_LABEL', 'Modifier le pilote');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1005', '1', 'KEY_SCREEN_REMOVE_LABEL', 'Remove');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1006', '2', 'KEY_SCREEN_REMOVE_LABEL', 'Retirer');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1007', '1', 'KEY_SCREEN_DRIVER_REMOVE_ALERT_MESSAGE', 'Are you sure you want to remove the association to this driver?');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1008', '2', 'KEY_SCREEN_DRIVER_REMOVE_ALERT_MESSAGE', 'Êtes-vous sûr de vouloir supprimer l\'association à ce pilote?');
