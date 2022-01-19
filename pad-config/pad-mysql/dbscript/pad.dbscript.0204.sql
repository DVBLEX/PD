USE pad;

CREATE TABLE `pad`.`online_payments` (
  `id` INT(11) NOT NULL,
  `client_id` INT(11) NOT NULL,
  `aggregator_id` INT(11) NOT NULL,
  `account_id` INT(11) NOT NULL,
  `mission_id` INT(11) NOT NULL,
  `trip_id` INT(11) NOT NULL,
  `driver_id` INT(11) NOT NULL,
  `first_name` VARCHAR(64) NOT NULL,
  `last_name` VARCHAR(64) NOT NULL,
  `mno_id` INT(11) NOT NULL,
  `msisdn` VARCHAR(16) NOT NULL,
  `currency_code` VARCHAR(3) NOT NULL,
  `amount` DECIMAL(7,0) NOT NULL,
  `amount_aggregator` DECIMAL(7,0) NOT NULL,
  `fee_aggregator` DECIMAL(7,0) NOT NULL,
  `reference_aggregator` VARCHAR(32) NOT NULL,
  `date_request` DATETIME NOT NULL,
  `date_response` DATETIME NULL,
  `date_response_aggregator` DATETIME NULL,
  `status_aggregator` VARCHAR(32) NOT NULL,
  `client_id_aggregator` VARCHAR(32) NOT NULL,
  `gu_id_aggregator` VARCHAR(32) NOT NULL,
  `response_code` INT(11) NOT NULL,
  `request_hash` VARCHAR(128) NOT NULL,
  `response_hash` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE `online_payment_parameters` (
  `id` int(11) NOT NULL,
  `mno_id` int(11) NOT NULL,
  `default_connect_timeout` int(11) NOT NULL,
  `default_socket_timeout` int(11) NOT NULL,
  `default_conn_request_timeout` int(11) NOT NULL,
  `date_edited` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`online_payment_parameters` (`id`, `mno_id`, `default_connect_timeout`, `default_socket_timeout`, `default_conn_request_timeout`, `date_edited`) VALUES ('1', '101', '3', '3', '3', '2019-10-18 15:18:03');
INSERT INTO `pad`.`online_payment_parameters` (`id`, `mno_id`, `default_connect_timeout`, `default_socket_timeout`, `default_conn_request_timeout`, `date_edited`) VALUES ('2', '102', '3', '3', '3', '2019-10-18 15:18:03');

ALTER TABLE `pad`.`online_payment_parameters` 
CHANGE COLUMN `date_edited` `date_edited` DATETIME NOT NULL ,
ADD COLUMN `is_active` TINYINT(1) NOT NULL AFTER `default_conn_request_timeout`;

UPDATE `pad`.`online_payment_parameters` SET `is_active`='1' WHERE `id`='1';
UPDATE `pad`.`online_payment_parameters` SET `is_active`='1' WHERE `id`='2';


ALTER TABLE `pad`.`online_payments` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `pad`.`online_payments` 
ADD COLUMN `transaction` VARCHAR(32) NOT NULL AFTER `reference_aggregator`,
ADD COLUMN `date_callback_response` DATETIME NULL AFTER `date_response_aggregator`,
ADD COLUMN `date_payment_aggregator` DATETIME NULL AFTER `date_callback_response`,
ADD COLUMN `response_callback_hash` VARCHAR(128) NOT NULL AFTER `response_hash`;

ALTER TABLE `pad`.`online_payments` 
CHANGE COLUMN `transaction` `transaction_aggregator` VARCHAR(32) NOT NULL ;