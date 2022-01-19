USE pad;

ALTER TABLE `pad`.`operators`
CHANGE COLUMN `firstname` `first_name` VARCHAR(32) NOT NULL ,
CHANGE COLUMN `lastname` `last_name` VARCHAR(32) NOT NULL ;

DROP TABLE `pad`.`driver_missions`;
DROP TABLE `pad`.`vehicle_missions`;

ALTER TABLE `pad`.`vehicles`
CHANGE COLUMN `registration_country` `registration_country_iso` VARCHAR(2) NOT NULL AFTER `account_id`;

ALTER TABLE `pad`.`vehicles`
ADD COLUMN `operator_id` INT(11) NOT NULL AFTER `color`;

ALTER TABLE `pad`.`drivers`
CHANGE COLUMN `nationality` `nationality_country_iso` VARCHAR(2) NOT NULL ;

ALTER TABLE `pad`.`drivers`
ADD COLUMN `operator_id` INT(11) NOT NULL AFTER `licence_number`;

ALTER TABLE `pad`.`accounts`
CHANGE COLUMN `company_telephone_number` `company_telephone` VARCHAR(16) NOT NULL ;

UPDATE `pad`.`language_keys` SET `translate_value`='Company Telephone' WHERE `id`='361';
UPDATE `pad`.`language_keys` SET `translate_value`='Téléphone de l\'entreprise' WHERE `id`='362';
UPDATE `pad`.`language_keys` SET `translate_value`='Please enter Company Telephone' WHERE `id`='389';
UPDATE `pad`.`language_keys` SET `translate_value`='Veuillez entrer le numéro de téléphone de l\'entreprise' WHERE `id`='390';

ALTER TABLE `pad`.`accounts`
CHANGE COLUMN `company_registration_number` `company_registration` VARCHAR(64) NOT NULL ;

ALTER TABLE `pad`.`accounts`
CHANGE COLUMN `registration_country` `registration_country_iso` VARCHAR(2) NOT NULL ;
ALTER TABLE `pad`.`accounts`
CHANGE COLUMN `nationality` `nationality_country_iso` VARCHAR(2) NOT NULL ;

ALTER TABLE `pad`.`missions`
CHANGE COLUMN `port_operator_type` `port_operator_id` INT(11) NOT NULL ;
ALTER TABLE `pad`.`missions`
CHANGE COLUMN `trips` `count_trips` INT(11) NOT NULL ;
ALTER TABLE `pad`.`missions`
CHANGE COLUMN `operator_id_created` `operator_id` INT(11) NOT NULL ;
ALTER TABLE `pad`.`missions`
CHANGE COLUMN `trips_booked` `count_trips_booked` INT(11) NOT NULL ;
ALTER TABLE `pad`.`missions`
DROP COLUMN `reason_rejected`;

ALTER TABLE `pad`.`trips`
CHANGE COLUMN `operator_id_created` `operator_id` INT(11) NOT NULL ;
ALTER TABLE `pad`.`trips`
CHANGE COLUMN `date_slot` `date_slot_start` DATETIME NOT NULL ;

ALTER TABLE `pad`.`parking`
CHANGE COLUMN `operator_id_created` `operator_id` INT(11) NOT NULL ;

ALTER TABLE `pad`.`payments`
CHANGE COLUMN `operator_id_created` `operator_id` INT(11) NOT NULL ;
ALTER TABLE `pad`.`payments`
CHANGE COLUMN `amount` `amount_payment` DECIMAL(7,0) NOT NULL ;

ALTER TABLE `pad`.`port_access`
CHANGE COLUMN `operator_id_created` `operator_id` INT(11) NOT NULL ;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('617', '1', 'KEY_SCREEN_INVALID_CASH_GIVEN_AMOUNT_MESSAGE', 'Cash given cannot be less than fee payment');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('618', '2', 'KEY_SCREEN_INVALID_CASH_GIVEN_AMOUNT_MESSAGE', 'L\'argent donné ne peut être inférieur au paiement des frais');
