USE pad;

ALTER TABLE `pad`.`system_parameters`
CHANGE COLUMN `finance_session_initial_float_min_amount` `finance_session_initial_float_min_amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `finance_session_initial_float_max_amount` `finance_session_initial_float_max_amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `maximum_overdraft_limit_min_amount` `maximum_overdraft_limit_min_amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `maximum_overdraft_limit_max_amount` `maximum_overdraft_limit_max_amount` DECIMAL(12,0) NOT NULL ;
