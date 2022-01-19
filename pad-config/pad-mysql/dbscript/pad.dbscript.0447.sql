USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1785', '1', 'KEY_SCREEN_DIFFERENCE_LABEL', 'Difference');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1786', '2', 'KEY_SCREEN_DIFFERENCE_LABEL', 'Différence');

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `validation_step` INT(11) NOT NULL AFTER `cash_amount_end`;

UPDATE pad.sessions SET validation_step = 2 WHERE status = 4 AND id > 0;

ALTER TABLE `pad`.`sessions` 
ADD INDEX `cash_amount_start_ik` (`cash_amount_start` ASC),
ADD INDEX `cash_amount_end_ik` (`cash_amount_end` ASC),
ADD INDEX `no_account_cash_transaction_count_ik` (`no_account_cash_transaction_count` ASC),
ADD INDEX `no_account_online_transaction_count_ik` (`no_account_online_transaction_count` ASC),
ADD INDEX `account_cash_transaction_count_ik` (`account_cash_transaction_count` ASC),
ADD INDEX `account_online_transaction_count_ik` (`account_online_transaction_count` ASC),
ADD INDEX `account_deduct_transaction_count_ik` (`account_deduct_transaction_count` ASC),
ADD INDEX `exit_only_session_count_ik` (`exit_only_session_count` ASC),
ADD INDEX `adhoc_trips_created_count_ik` (`adhoc_trips_created_count` ASC),
ADD INDEX `adhoc_trips_cancelled_count_ik` (`adhoc_trips_cancelled_count` ASC),
ADD INDEX `no_account_cash_transaction_total_amount_ik` (`no_account_cash_transaction_total_amount` ASC),
ADD INDEX `no_account_online_transaction_total_amount_ik` (`no_account_online_transaction_total_amount` ASC),
ADD INDEX `account_cash_transaction_total_amount_ik` (`account_cash_transaction_total_amount` ASC),
ADD INDEX `account_online_transaction_total_amount_ik` (`account_online_transaction_total_amount` ASC),
ADD INDEX `account_deduct_transaction_total_amount_ik` (`account_deduct_transaction_total_amount` ASC),
ADD INDEX `cash_change_given_total_amount_ik` (`cash_change_given_total_amount` ASC);
