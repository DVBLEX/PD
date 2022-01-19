USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1033', '1', 'KEY_SCREEN_TAX_LABEL', 'Tax');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1034', '2', 'KEY_SCREEN_TAX_LABEL', 'Impôt');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1035', '1', 'KEY_SCREEN_VEHICLE_REG_LABEL', 'Vehicle REG');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1036', '2', 'KEY_SCREEN_VEHICLE_REG_LABEL', 'Véhicule REG');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1037', '1', 'KEY_SCREEN_EMAIL_RECEIPT_ACCOUNT_LABEL', 'Email receipt to account');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1038', '2', 'KEY_SCREEN_EMAIL_RECEIPT_ACCOUNT_LABEL', 'Email de réception au compte');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1039', '1', 'KEY_SCREEN_OTHER_EMAIL_LABEL', 'Other email');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1040', '2', 'KEY_SCREEN_OTHER_EMAIL_LABEL', 'Autre email');


ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `tax_percentage` DECIMAL(7,0) NOT NULL AFTER `client_secret`;

UPDATE `pad`.`system_parameters` SET `tax_percentage`='18' WHERE `id`='1';