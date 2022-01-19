USE pad;

-- ********************************************
-- ***** ALREADY RAN ON LIVE DATABASE *********
-- ********************************************

ALTER TABLE `pad`.`accounts` 
CHANGE COLUMN `amount_balance` `amount_balance` DECIMAL(12,0) NOT NULL;

ALTER TABLE `pad`.`accounts` 
CHANGE COLUMN `amount_overdraft_limit` `amount_overdraft_limit` DECIMAL(12,0) NOT NULL;

ALTER TABLE `pad`.`accounts` 
CHANGE COLUMN `amount_hold` `amount_hold` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`account_balances` 
CHANGE COLUMN `amount_balance` `amount_balance` DECIMAL(12,0) NOT NULL;

ALTER TABLE `pad`.`account_balances` 
CHANGE COLUMN `amount_overdraft_limit` `amount_overdraft_limit` DECIMAL(12,0) NOT NULL;

ALTER TABLE `pad`.`account_balances` 
CHANGE COLUMN `amount_hold` `amount_hold` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`invoice` 
CHANGE COLUMN `total_amount` `total_amount` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`online_payments` 
CHANGE COLUMN `amount` `amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_aggregator` `amount_aggregator` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `fee_aggregator` `fee_aggregator` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`parking_statistics` 
CHANGE COLUMN `amount_total_trip_fee` `amount_total_trip_fee` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`payments` 
CHANGE COLUMN `amount_due` `amount_due` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_payment` `amount_payment` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_change_due` `amount_change_due` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`port_operator_trip_fee` 
CHANGE COLUMN `trip_amount_fee` `trip_amount_fee` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `trip_amount_fee_other` `trip_amount_fee_other` DECIMAL(12,0) NOT NULL COMMENT 'alternative trip fee that will be used only in particular scenarios. e.g. for non pertroleum and non senegalese trucks' ,
CHANGE COLUMN `operator_amount_fee` `operator_amount_fee` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`port_statistics` 
CHANGE COLUMN `amount_total_trip_fee` `amount_total_trip_fee` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`receipt` 
CHANGE COLUMN `total_amount` `total_amount` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`sessions` 
CHANGE COLUMN `amount_start` `amount_start` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_end` `amount_end` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_collected` `amount_collected` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`statements` 
CHANGE COLUMN `amount_credit` `amount_credit` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_debit` `amount_debit` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `amount_running_balance` `amount_running_balance` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`transporter_trips_statistics` 
CHANGE COLUMN `amount_total_trip_fee` `amount_total_trip_fee` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`trips` 
CHANGE COLUMN `amount_fee` `amount_fee` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `operator_amount_fee` `operator_amount_fee` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`system_parameters` 
CHANGE COLUMN `tax_percentage` `tax_percentage` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `kiosk_account_topup_min_amount` `kiosk_account_topup_min_amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `kiosk_account_topup_max_amount` `kiosk_account_topup_max_amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `finance_account_topup_min_amount` `finance_account_topup_min_amount` DECIMAL(12,0) NOT NULL ,
CHANGE COLUMN `finance_account_topup_max_amount` `finance_account_topup_max_amount` DECIMAL(12,0) NOT NULL ;
