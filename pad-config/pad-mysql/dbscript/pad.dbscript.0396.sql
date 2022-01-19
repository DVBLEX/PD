USE pad;

ALTER TABLE `pad`.`system_parameters`
ADD COLUMN `finance_session_initial_float_min_amount` INT NOT NULL AFTER `finance_account_topup_max_amount`,
ADD COLUMN `finance_session_initial_float_max_amount` INT NOT NULL AFTER `finance_session_initial_float_min_amount`;

UPDATE `pad`.`system_parameters` SET `finance_session_initial_float_min_amount` = '0' WHERE (`id` = '1');
UPDATE `pad`.`system_parameters` SET `finance_session_initial_float_max_amount` = '500000' WHERE (`id` = '1');
