USE pad;

UPDATE `pad`.`language_keys` SET `translate_value` = 'Please enter Amount End Final Float' WHERE (`id` = '905');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Veuillez entrer le montant final du flotteur' WHERE (`id` = '906');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Please confirm that the cash amount in the bag has been counted' WHERE (`id` = '903');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Veuillez confirmer que le montant en espèces dans le sac a été compté' WHERE (`id` = '904');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Confirm that the cash amount in the bag has been counted' WHERE (`id` = '907');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Confirmer que le montant en espèces dans le sac a été compté' WHERE (`id` = '908');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1227', '1', 'KEY_SCREEN_REASON_LABEL', 'Reason');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1228', '2', 'KEY_SCREEN_REASON_LABEL', 'Raison');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1229', '1', 'KEY_SCREEN_UNEXPECTED_AMOUNT_RETURNED_REASON_MESSAGE', 'Amount returned is less than expected amount. Please enter the reason for this');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1230', '2', 'KEY_SCREEN_UNEXPECTED_AMOUNT_RETURNED_REASON_MESSAGE', 'Le montant retourné est inférieur au montant attendu. S\'il vous plaît entrer la raison de cela');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Amount Returned' WHERE (`id` = '867');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Montant retourné' WHERE (`id` = '868');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Please enter Amount Returned' WHERE (`id` = '905');
UPDATE `pad`.`language_keys` SET `translate_value` = 'S\'il vous plaît entrer le Montant Retourné' WHERE (`id` = '906');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Montant Retourné' WHERE (`id` = '868');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1231', '1', 'KEY_SCREEN_ENTER_REASON_MESSAGE', 'Please enter a Reason');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1232', '2', 'KEY_SCREEN_ENTER_REASON_MESSAGE', 'S\'il vous plaît entrer une raison');

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `reason_amount_unexpected` VARCHAR(128) NOT NULL AFTER `amount_collected`;

UPDATE `pad`.`language_keys` SET `translate_value` = 'Amount returned is different than expected amount. Please enter the reason for this.' WHERE (`id` = '1229');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Le montant retourné est différent du montant attendu. S\'il vous plaît entrer la raison pour cela.' WHERE (`id` = '1230');
