USE pad;

ALTER TABLE `pad`.`statements` 
ADD COLUMN `notes` VARCHAR(256) NOT NULL AFTER `amount_running_balance`;

ALTER TABLE `pad`.`payments` 
ADD COLUMN `notes` VARCHAR(256) NOT NULL AFTER `amount_change_due`;


INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('50', 'Account Debit', '2020-10-06 12:00:00');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('51', 'Account Credit', '2020-10-06 12:00:00');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1663', '1', 'KEY_SCREEN_CREDIT_NOTE_LABEL', 'Credit Note');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1664', '2', 'KEY_SCREEN_CREDIT_NOTE_LABEL', 'Note de crédit');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1665', '1', 'KEY_SCREEN_CASH_REFUND_LABEL', 'Cash Refund');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1666', '2', 'KEY_SCREEN_CASH_REFUND_LABEL', 'Remboursement en espèces');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1667', '1', 'KEY_SCREEN_NOTES_LABEL', 'Notes');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1668', '2', 'KEY_SCREEN_NOTES_LABEL', 'Remarques');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1669', '1', 'KEY_SCREEN_ENTER_NOTES_MESSAGE', 'Please enter Notes');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1670', '2', 'KEY_SCREEN_ENTER_NOTES_MESSAGE', 'Veuillez saisir des remarques');
