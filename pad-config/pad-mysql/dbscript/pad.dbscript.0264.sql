USE pad;

ALTER TABLE `pad`.`receipt_unique_urls` 
CHANGE COLUMN `unique_url` `number` VARCHAR(10) NOT NULL , RENAME TO  `pad`.`receipt_numbers` ;

CREATE TABLE `pad`.`invoice_numbers` (
  `invoice_id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(10) NOT NULL,
  PRIMARY KEY (`invoice_id`,`number`),
  UNIQUE KEY `number_UNIQUE` (`number`),
  UNIQUE KEY `invoice_id_UNIQUE` (`invoice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `pad`.`transporter_trips_statistics` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `date_statistics` DATE NOT NULL,
  `account_id` INT(11) NOT NULL,
  `port_operator_id` INT(11) NOT NULL,
  `transaction_type` INT(11) NOT NULL,
  `count_trips` INT(11) NOT NULL,
  `amount_total_trip_fee` DECIMAL(7,0) NOT NULL,
  `date_created` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`transporter_trips_statistics` 
ADD COLUMN `account_payment_terms_type` INT(11) NOT NULL AFTER `account_id`;