USE pad;

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `agsparking_anpr_entry_lane1_id` INT(11) NOT NULL AFTER `date_edited`,
ADD COLUMN `agsparking_anpr_entry_lane2_id` INT(11) NOT NULL AFTER `agsparking_anpr_entry_lane1_id`,
ADD COLUMN `agsparking_anpr_entry_lane3_id` INT(11) NOT NULL AFTER `agsparking_anpr_entry_lane2_id`,
ADD COLUMN `agsparking_anpr_entry_lane4_id` INT(11) NOT NULL AFTER `agsparking_anpr_entry_lane3_id`,
ADD COLUMN `agsparking_anpr_entry_lane5_id` INT(11) NOT NULL AFTER `agsparking_anpr_entry_lane4_id`,
ADD COLUMN `agsparking_anpr_exit_lane6_id` INT(11) NOT NULL AFTER `agsparking_anpr_entry_lane5_id`,
ADD COLUMN `agsparking_anpr_exit_lane7_id` INT(11) NOT NULL AFTER `agsparking_anpr_exit_lane6_id`,
ADD COLUMN `port_anpr_entry_gate_mole2_id` INT(11) NOT NULL AFTER `agsparking_anpr_exit_lane7_id`,
ADD COLUMN `port_anpr_entry_gate_mole3_id` INT(11) NOT NULL AFTER `port_anpr_entry_gate_mole2_id`,
ADD COLUMN `port_anpr_entry_gate_mole4_id` INT(11) NOT NULL AFTER `port_anpr_entry_gate_mole3_id`,
ADD COLUMN `port_anpr_entry_gate_mole8_id` INT(11) NOT NULL AFTER `port_anpr_entry_gate_mole4_id`,
ADD COLUMN `port_anpr_exit_gate_mole1_id` INT(11) NOT NULL AFTER `port_anpr_entry_gate_mole8_id`,
ADD COLUMN `port_anpr_exit_gate_mole4_id` INT(11) NOT NULL AFTER `port_anpr_exit_gate_mole1_id`,
ADD COLUMN `port_anpr_exit_gate_mole8_id` INT(11) NOT NULL AFTER `port_anpr_exit_gate_mole4_id`;

UPDATE `pad`.`anpr_parameters` SET `agsparking_anpr_entry_lane1_id` = '10003', `agsparking_anpr_entry_lane2_id` = '10004', `agsparking_anpr_entry_lane3_id` = '10005', `agsparking_anpr_entry_lane4_id` = '10006', `agsparking_anpr_entry_lane5_id` = '10007', `agsparking_anpr_exit_lane6_id` = '10008', `agsparking_anpr_exit_lane7_id` = '10009', `port_anpr_entry_gate_mole2_id` = '10014', `port_anpr_entry_gate_mole3_id` = '10015', `port_anpr_entry_gate_mole4_id` = '10012', `port_anpr_entry_gate_mole8_id` = '10010', `port_anpr_exit_gate_mole1_id` = '10016', `port_anpr_exit_gate_mole4_id` = '10013', `port_anpr_exit_gate_mole8_id` = '10011' WHERE (`id` = '1');

ALTER TABLE `pad`.`anpr_parameters` 
CHANGE COLUMN `parking_permission_hours_in_future` `parking_permission_hours_in_future` INT(11) NOT NULL AFTER `port_anpr_exit_gate_mole8_id`,
CHANGE COLUMN `parking_permission_hours_prior_slot_date` `parking_permission_hours_prior_slot_date` INT(11) NOT NULL AFTER `parking_permission_hours_in_future`,
CHANGE COLUMN `parking_permission_hours_after_slot_date` `parking_permission_hours_after_slot_date` INT(11) NOT NULL AFTER `parking_permission_hours_prior_slot_date`,
CHANGE COLUMN `parking_permission_hours_after_exit_date` `parking_permission_hours_after_exit_date` INT(11) NOT NULL AFTER `parking_permission_hours_after_slot_date`,
CHANGE COLUMN `date_edited` `date_edited` DATETIME NULL DEFAULT NULL AFTER `parking_permission_hours_after_exit_date`;
