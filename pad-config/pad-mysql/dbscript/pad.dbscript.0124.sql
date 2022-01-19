USE pad;

CREATE TABLE pad.`statements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `type` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `trip_id` int(11) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `amount_credit` decimal(7,0) NOT NULL,
  `amount_debit` decimal(7,0) NOT NULL,
  `amount_running_balance` decimal(7,0) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_uk` (`code`),
  KEY `account_id_ik` (`account_id`),
  KEY `date_created_ik` (`date_created`),
  KEY `type_ik` (`type`),
  KEY `mission_id_ik` (`mission_id`),
  KEY `trip_id_ik` (`trip_id`),
  KEY `amount_credit_ik` (`amount_credit`),
  KEY `amount_debit_ik` (`amount_debit`),
  KEY `amount_running_balance_ik` (`amount_running_balance`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `pad`.`payments`
DROP COLUMN `trip_id`,
DROP COLUMN `mission_id`,
CHANGE COLUMN `type` `lane_session_id` INT(11) NOT NULL ,
CHANGE COLUMN `amount_credit` `amount_due` DECIMAL(7,0) NOT NULL ,
CHANGE COLUMN `amount_debit` `amount_payment` DECIMAL(7,0) NOT NULL ,
CHANGE COLUMN `amount_running_balance` `amount_change_due` DECIMAL(7,0) NOT NULL ,
DROP INDEX `trip_id_ik` ,
DROP INDEX `mission_id_ik` ;

ALTER TABLE `pad`.`payments`
ADD INDEX `lane_session_id_ik` (`lane_session_id` ASC);

ALTER TABLE `pad`.`payments` 
ADD COLUMN `payment_option` INT(11) NOT NULL AFTER `operator_id`;

ALTER TABLE `pad`.`payments` 
DROP COLUMN `code`,
DROP INDEX `code_uk` ;
