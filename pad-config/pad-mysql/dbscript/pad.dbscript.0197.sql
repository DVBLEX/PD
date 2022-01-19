USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1149', '1', 'KEY_SCREEN_CHEQUE_LABEL', 'Cheque');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1150', '2', 'KEY_SCREEN_CHEQUE_LABEL', 'Chèque');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1151', '1', 'KEY_SCREEN_BANK_TRANSFER_LABEL', 'Bank Transfer');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1152', '2', 'KEY_SCREEN_BANK_TRANSFER_LABEL', 'Virement');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1153', '1', 'KEY_SCREEN_PAYMENT_TYPE_LABEL', 'Payment Type');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1154', '2', 'KEY_SCREEN_PAYMENT_TYPE_LABEL', 'Type de Paiement');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1155', '1', 'KEY_SCREEN_INVOICE_PAYMENT_SUCCESS_MESSAGE', 'Invoice payment confirmation saved successfully');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1156', '2', 'KEY_SCREEN_INVOICE_PAYMENT_SUCCESS_MESSAGE', 'Confirmation du paiement de la facture enregistrée avec succès');


ALTER TABLE `pad`.`invoice` 
ADD COLUMN `type_payment` INT(2) NOT NULL AFTER `date_payment`,
ADD COLUMN `operator_id` INT(11) NOT NULL AFTER `path`,
ADD COLUMN `date_edited` DATETIME NOT NULL AFTER `date_created`;

