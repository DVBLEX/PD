USE pad;

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `exit_prematurely_validity_minutes` INT NOT NULL AFTER `in_transit_validity_minutes`;

UPDATE `pad`.`system_parameters` SET `exit_prematurely_validity_minutes` = '60' WHERE (`id` = '1');


-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1789', '1', 'KEY_SCREEN_EXITED_PREMATURELY_EXPIRED_LABEL', 'Exited Prematurely Expired');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1790', '2', 'KEY_SCREEN_EXITED_PREMATURELY_EXPIRED_LABEL', 'Sortie prématurément expirée');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1791', '1', 'KEY_SCREEN_EXITED_PARKING_PREMATURELY_EXPIRED_LABEL', 'Exited Parking Prematurely Expired');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1792', '2', 'KEY_SCREEN_EXITED_PARKING_PREMATURELY_EXPIRED_LABEL', 'Parking sorti prématurément expiré');


UPDATE `pad`.`port_operator_trip_fee` SET `trip_amount_fee` = '3000', `operator_amount_fee` = '6000' WHERE (`id` = '1051');
