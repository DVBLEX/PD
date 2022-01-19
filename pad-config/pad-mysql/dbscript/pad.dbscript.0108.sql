USE pad;

ALTER TABLE `pad`.`accounts`
ADD COLUMN `payment_terms_type` INT(11) NOT NULL COMMENT 'prepay or postpay' AFTER `special_tax_status`,
ADD COLUMN `max_negative_amount_balance_allowed` DECIMAL(7,0) NOT NULL AFTER `amount_balance`;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('745', '1', 'KEY_SCREEN_PAYMENT_TERMS_TYPE_LABEL', 'Payment Terms Type');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('746', '2', 'KEY_SCREEN_PAYMENT_TERMS_TYPE_LABEL', 'Conditions de paiement Type');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('747', '1', 'KEY_SCREEN_PAYMENT_TERMS_TYPE_PREPAY_LABEL', 'Prepay');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('748', '2', 'KEY_SCREEN_PAYMENT_TERMS_TYPE_PREPAY_LABEL', 'Pré-paiement');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('749', '1', 'KEY_SCREEN_PAYMENT_TERMS_TYPE_POSTPAY_LABEL', 'Postpay');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('750', '2', 'KEY_SCREEN_PAYMENT_TERMS_TYPE_POSTPAY_LABEL', 'post-paiement');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('751', '1', 'KEY_SCREEN_MAX_ACCOUNT_DEBT_ALLOWED_LABEL', 'Maximum Debt Allowed');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('752', '2', 'KEY_SCREEN_MAX_ACCOUNT_DEBT_ALLOWED_LABEL', 'Dette maximale autorisée');

UPDATE `pad`.`language_keys` SET `translate_value`='Maximum Debt Amount Allowed' WHERE `id`='751';
UPDATE `pad`.`language_keys` SET `translate_value`='Montant maximal de la dette autorisé' WHERE `id`='752';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('753', '1', 'KEY_SCREEN_SELECT_PAYMENT_TERMS_TYPE_MESSAGE', 'Please select Payment Terms Type');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('754', '2', 'KEY_SCREEN_SELECT_PAYMENT_TERMS_TYPE_MESSAGE', 'Veuillez sélectionner les conditions de paiement Type');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('755', '1', 'KEY_SCREEN_SELECT_MAX_ACCOUNT_DEBT_ALLOWED_MESSAGE', 'Please select Maximum Debt Amount Allowed');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('756', '2', 'KEY_SCREEN_SELECT_MAX_ACCOUNT_DEBT_ALLOWED_MESSAGE', 'Veuillez sélectionner le montant maximum de la dette autorisé');

ALTER TABLE `pad`.`accounts`
CHANGE COLUMN `max_negative_amount_balance_allowed` `amount_overdraft_limit` DECIMAL(7,0) NOT NULL ;

UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_MAX_AMOUNT_OVERDRAFT_LIMIT_LABEL', `translate_value`='Maximum Overdraft Limit' WHERE `id`='751';
UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_MAX_AMOUNT_OVERDRAFT_LIMIT_LABEL', `translate_value`='Limite maximale de découvert' WHERE `id`='752';
UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_SELECT_MAX_AMOUNT_OVERDRAFT_LIMIT_MESSAGE', `translate_value`='Please select Maximum Overdraft Limit' WHERE `id`='755';
UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_SELECT_MAX_AMOUNT_OVERDRAFT_LIMIT_MESSAGE', `translate_value`='Veuillez sélectionner la limite de découvert maximum' WHERE `id`='756';
