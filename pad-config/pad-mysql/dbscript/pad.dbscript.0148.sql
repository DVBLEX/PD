USE pad;

CREATE TABLE `pad`.`booking_slot_counts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `port_operator_id` INT(11) NOT NULL,
  `date_slot` DATE NOT NULL,
  `hour_slot_from` INT(11) NOT NULL,
  `hour_slot_to` INT(11) NOT NULL,
  `trips_booked_count` INT(11) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `pad`.`booking_slot_limits` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `port_operator_id` INT(11) NOT NULL,
  `day_of_week_id` INT(11) NOT NULL,
  `day_of_week_name` VARCHAR(12) NOT NULL,
  `hour_slot_from` INT(11) NOT NULL,
  `hour_slot_to` INT(11) NOT NULL,
  `booking_limit` INT(11) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'captures the booking limits per port operator per day of week per hour slot';

ALTER TABLE `pad`.`system_parameters`
ADD COLUMN `booking_limit_per_hour` INT(11) NOT NULL AFTER `trip_slot_start_range_days`;

UPDATE `pad`.`system_parameters` SET `booking_limit_per_hour`='100' WHERE `id`='1';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1009', '1', 'KEY_RESPONSE_1188', 'Trip booking limit reached for this port operator for the selected date and time');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1010', '2', 'KEY_RESPONSE_1188', 'Limite de réservation de voyage atteinte pour cet opérateur portuaire pour la date et l\'heure sélectionnées');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1011', '1', 'KEY_RESPONSE_1189', 'Trip booking limit reached for the selected date and time');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1012', '2', 'KEY_RESPONSE_1189', 'Limite de réservation de voyage atteinte pour la date et l\'heure sélectionnées');

ALTER TABLE `pad`.`system_parameters`
ADD COLUMN `is_booking_limit_check_enabled` TINYINT(1) NOT NULL COMMENT 'flag to enable/disable the checks for booking limits once a trip has been booked.' AFTER `booking_limit_per_hour`;

UPDATE `pad`.`system_parameters` SET `is_booking_limit_check_enabled`='1' WHERE `id`='1';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1013', '1', 'KEY_SCREEN_TRIP_BOOKING_LIMIT_CHECK_LABEL', 'Trip Booking Limit Check');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1014', '2', 'KEY_SCREEN_TRIP_BOOKING_LIMIT_CHECK_LABEL', 'Vérification de limite de réservation de voyage');
