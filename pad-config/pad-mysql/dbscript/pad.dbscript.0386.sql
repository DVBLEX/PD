USE pad;

UPDATE `pad`.`system_parameters` SET `finance_account_topup_min_amount` = '500', `finance_account_topup_max_amount` = '5000000' WHERE (`id` = '1');

UPDATE `pad`.`system_parameters` SET `kiosk_account_topup_max_amount` = '100000' WHERE (`id` = '1');
