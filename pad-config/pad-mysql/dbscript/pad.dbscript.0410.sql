USE pad;

ALTER TABLE `pad`.`system_parameters`
ADD COLUMN `maximum_overdraft_limit_min_amount` INT NOT NULL AFTER `finance_session_initial_float_max_amount`,
ADD COLUMN `maximum_overdraft_limit_max_amount` INT NOT NULL AFTER `maximum_overdraft_limit_min_amount`;

UPDATE `pad`.`system_parameters` SET `maximum_overdraft_limit_min_amount` = '0' WHERE (`id` = '1');
UPDATE `pad`.`system_parameters` SET `maximum_overdraft_limit_max_amount` = '1000000' WHERE (`id` = '1');
