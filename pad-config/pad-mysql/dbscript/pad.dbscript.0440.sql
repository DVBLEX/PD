USE pad;

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `no_account_cash_transaction_count` INT(11) NOT NULL AFTER `reason_amount_unexpected`,
ADD COLUMN `no_account_online_transaction_count` INT(11) NOT NULL AFTER `no_account_cash_transaction_count`,
ADD COLUMN `account_cash_transaction_count` INT(11) NOT NULL AFTER `no_account_online_transaction_count`,
ADD COLUMN `account_online_transaction_count` INT(11) NOT NULL AFTER `account_cash_transaction_count`,
ADD COLUMN `account_deduct_transaction_count` INT(11) NOT NULL AFTER `account_online_transaction_count`;

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `exit_only_session_count` INT(11) NOT NULL AFTER `account_deduct_transaction_count`;

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `adhoc_trips_created_count` INT(11) NOT NULL AFTER `exit_only_session_count`,
ADD COLUMN `adhoc_trips_cancelled_count` INT(11) NOT NULL AFTER `adhoc_trips_created_count`;

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `no_account_cash_transaction_total_amount` DECIMAL(12,0) NOT NULL AFTER `date_end`,
ADD COLUMN `no_account_online_transaction_total_amount` DECIMAL(12,0) NOT NULL AFTER `no_account_cash_transaction_total_amount`,
ADD COLUMN `account_cash_transaction_total_amount` DECIMAL(12,0) NOT NULL AFTER `no_account_online_transaction_total_amount`,
ADD COLUMN `account_online_transaction_total_amount` DECIMAL(12,0) NOT NULL AFTER `account_cash_transaction_total_amount`,
ADD COLUMN `account_deduct_transaction_total_amount` DECIMAL(12,0) NOT NULL AFTER `account_online_transaction_total_amount`,
ADD COLUMN `cash_change_given_total_amount` DECIMAL(12,0) NOT NULL AFTER `account_deduct_transaction_total_amount`;

ALTER TABLE `pad`.`sessions` 
CHANGE COLUMN `date_start` `date_start` DATETIME NULL DEFAULT NULL AFTER `cash_change_given_total_amount`,
CHANGE COLUMN `date_end` `date_end` DATETIME NULL DEFAULT NULL AFTER `date_start`,
CHANGE COLUMN `date_created` `date_created` DATETIME NOT NULL AFTER `date_end`;

ALTER TABLE `pad`.`sessions` 
DROP COLUMN `amount_collected`;

ALTER TABLE `pad`.`sessions` 
CHANGE COLUMN `amount_start` `cash_amount_start` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_end` `cash_amount_end` DECIMAL(12,0) NOT NULL ;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1765', '1', 'KEY_RESPONSE_1094', 'A data field recorded for this kiosk session has failed to meet a validation check');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1766', '2', 'KEY_RESPONSE_1094', 'Un champ de données enregistré pour cette session de kiosque n\'a pas satisfait à un contrôle de validation');

UPDATE `pad`.`language_keys` SET `translate_value` = 'The amount returned is different than the expected amount. Please enter the reason for this.' WHERE (`id` = '1229');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1769', '1', 'KEY_SCREEN_COUNT_LABEL', 'Count');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1770', '2', 'KEY_SCREEN_COUNT_LABEL', 'Compter');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1771', '1', 'KEY_SCREEN_TOTAL_TRANSACTION_AMOUNT_LABEL', 'Transaction Amount (Total)');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1772', '2', 'KEY_SCREEN_TOTAL_TRANSACTION_AMOUNT_LABEL', 'Montant de la Transaction (Total)');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1773', '1', 'KEY_SCREEN_ONLINE_LABEL', 'Online');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1774', '2', 'KEY_SCREEN_ONLINE_LABEL', 'En ligne');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1775', '1', 'KEY_SCREEN_AD_HOC_TRIPS_CREATED_LABEL', 'Adhoc Trips Created');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1776', '2', 'KEY_SCREEN_AD_HOC_TRIPS_CREATED_LABEL', 'Voyages ad hoc Créés');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1777', '1', 'KEY_SCREEN_AD_HOC_TRIPS_CANCELLED_LABEL', 'Ad Hoc Trips Cancelled');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1778', '2', 'KEY_SCREEN_AD_HOC_TRIPS_CANCELLED_LABEL', 'Voyages Ad Hoc Annulés');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Ad Hoc Trips Created' WHERE (`id` = '1775');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Voyages Ad Hoc Créés' WHERE (`id` = '1776');
