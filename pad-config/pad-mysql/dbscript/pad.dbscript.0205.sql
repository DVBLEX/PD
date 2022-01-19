USE pad;

ALTER TABLE `pad`.`online_payments` 
ADD COLUMN `code` VARCHAR(64) NOT NULL AFTER `id`;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1171', '1', 'KEY_RESPONSE_1401', 'Unable to connect to the payment provider to process the payment. Please try again');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1172', '2', 'KEY_RESPONSE_1401', 'Impossible de se connecter au fournisseur de paiement pour traiter le paiement. Veuillez réessayer');

ALTER TABLE `pad`.`online_payments` 
ADD COLUMN `error_aggregator` VARCHAR(128) NOT NULL AFTER `status_aggregator`;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1173', '1', 'KEY_RESPONSE_1403', 'Unable to process the payment at this time. Please try again');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1174', '2', 'KEY_RESPONSE_1403', 'Impossible de traiter le paiement à ce moment. Veuillez réessayer');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Unable to connect to the payment provider to process the payment. Please use an alternative payment method' WHERE (`id` = '1171');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Unable to process the payment at this time. Please use an alternative payment method' WHERE (`id` = '1173');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Impossible de se connecter au fournisseur de paiement pour traiter le paiement. Veuillez utiliser un autre mode de paiement' WHERE (`id` = '1172');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Impossible de traiter le paiement à ce moment. Veuillez utiliser un autre mode de paiement' WHERE (`id` = '1174');
