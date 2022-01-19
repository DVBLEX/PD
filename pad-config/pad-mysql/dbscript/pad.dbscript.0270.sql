USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1301', '1', 'KEY_RESPONSE_1198', 'This vehicle has already been booked on another trip');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1302', '2', 'KEY_RESPONSE_1198', 'Ce véhicule a déjà été réservé pour un autre voyage');
ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `booking_check_date_slot_approved_in_advance_hours` INT(11) NOT NULL AFTER `receipt_lock_period`,
ADD COLUMN `booking_check_date_slot_approved_afterwards_hours` INT(11) NOT NULL AFTER `booking_check_date_slot_approved_in_advance_hours`;
UPDATE `pad`.`system_parameters` SET `booking_check_date_slot_approved_in_advance_hours` = '2', `booking_check_date_slot_approved_afterwards_hours` = '2' WHERE (`id` = '1');
