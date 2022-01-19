USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1271', '1', 'KEY_SCREEN_RECEIPT_NUMBER_LABEL', 'Receipt Number');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1272', '2', 'KEY_SCREEN_RECEIPT_NUMBER_LABEL', 'Numéro de reçu');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1273', '1', 'KEY_SCREEN_PAYMENTS_LABEL', 'Payments');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1274', '2', 'KEY_SCREEN_PAYMENTS_LABEL', 'Paiements');


ALTER TABLE `pad`.`receipt` 
ADD COLUMN `first_name` VARCHAR(64) NOT NULL AFTER `payment_id`,
ADD COLUMN `last_name` VARCHAR(64) NOT NULL AFTER `first_name`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1275', '1', 'KEY_SCREEN_PAYER_NAME_LABEL', 'Payer Name');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1276', '2', 'KEY_SCREEN_PAYER_NAME_LABEL', 'Nom du Payeur');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1277', '1', 'KEY_SCREEN_RECEIPT_LINK_EXPIRE_LABEL', 'The link to download the receipt expires at');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1278', '2', 'KEY_SCREEN_RECEIPT_LINK_EXPIRE_LABEL', 'Le lien pour télécharger le reçu expire à');

