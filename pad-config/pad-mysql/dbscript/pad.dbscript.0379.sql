USE pad;

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `finance_account_topup_min_amount` DECIMAL(7,0) NOT NULL AFTER `kiosk_account_topup_max_amount`,
ADD COLUMN `finance_account_topup_max_amount` DECIMAL(7,0) NOT NULL AFTER `finance_account_topup_min_amount`;

UPDATE `pad`.`system_parameters` SET `finance_account_topup_max_amount` = '5000' WHERE (`id` = '1');
