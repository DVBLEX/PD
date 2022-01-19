USE pad;

UPDATE `pad`.`system_parameters` SET `booking_check_date_slot_approved_in_advance_hours` = '3', `booking_check_date_slot_approved_afterwards_hours` = '3' WHERE (`id` = '1');
UPDATE `pad`.`anpr_parameters` SET `parking_permission_hours_prior_slot_date` = '1' WHERE (`id` = '1');
