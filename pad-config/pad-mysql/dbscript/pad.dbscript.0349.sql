USE pad;

CREATE TABLE `pad`.`port_operator_trips_api` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(16) NOT NULL,
  `reference_number` VARCHAR(16) NOT NULL,
  `transaction_type` VARCHAR(8) NOT NULL,
  `transporter_short_name` VARCHAR(16) NOT NULL,
  `vehicle_reg_number` VARCHAR(16) NOT NULL,
  `container_id` VARCHAR(16) NOT NULL,
  `container_type` VARCHAR(16) NOT NULL,
  `date_slot_from` VARCHAR(32) NOT NULL,
  `date_slot_to` VARCHAR(32) NOT NULL,
  `date_request` DATETIME NOT NULL,
  `trip_code` VARCHAR(64) NOT NULL,
  `response_code` INT NOT NULL,
  `response_text` VARCHAR(512) NOT NULL,
  `date_response` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`port_operator_trips_api` 
CHANGE COLUMN `response_text` `response_text` VARCHAR(1024) NOT NULL ;

