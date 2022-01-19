USE pad;

INSERT INTO `pad`.`system_checks` (`id`, `name`, `is_active`, `type`, `query_time_from`, `query_time_to`, `query`, `query_params`, `config_params`, `query_result`, `query_execution_time`, `comparison_operator`, `comparison_value`, `comparison_result`, `date_edited`, `date_created`) VALUES ('113', 'ACCOUNT_AMOUNT_HOLD_CHECK', '1', '1', '00:00:00', '23:59:59', 'SELECT COUNT(1) FROM accounts WHERE status = 2 AND is_deleted = 0 AND (amount_hold < 0 OR amount_hold > 200000)', '', '', '0', '2', '2', '0', '0', '2020-10-22 16:40:25', '2020-10-22 16:40:25');
