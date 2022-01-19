USE pad;

UPDATE pad.payments SET code = SHA2(REPLACE(id, '-', ''), 256) WHERE code = '' AND id > 0;

UPDATE pad.online_payments SET payment_id = -1 WHERE payment_id = 0 AND id > 0;

DELETE from pad.statements where account_id = -1 AND id > 0;
ALTER TABLE `pad`.`payments` 
DROP INDEX `amount_credit_ik` ,
ADD INDEX `amount_due_ik` (`amount_due` ASC),
DROP INDEX `amount_debit_ik` ,
ADD INDEX `amount_payment_ik` (`amount_payment` ASC),
DROP INDEX `amount_running_balance_ik` ,
ADD INDEX `amount_change_due_ik` (`amount_change_due` ASC),
ADD UNIQUE INDEX `code_uk` (`code` ASC),
ADD INDEX `mission_id_ik` (`mission_id` ASC),
ADD INDEX `trip_id_ik` (`trip_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `payment_option_ik` (`payment_option` ASC),
ADD INDEX `response_code_ik` (`response_code` ASC),
ADD INDEX `date_response_ik` (`date_response` ASC);

ALTER TABLE `pad`.`online_payments` 
ADD UNIQUE INDEX `code_uk` (`code` ASC),
ADD INDEX `client_id_ik` (`client_id` ASC),
ADD INDEX `aggregator_id_ik` (`aggregator_id` ASC),
ADD INDEX `account_id_ik` (`account_id` ASC),
ADD INDEX `mission_id_ik` (`mission_id` ASC),
ADD INDEX `trip_id_ik` (`trip_id` ASC),
ADD INDEX `driver_id_ik` (`driver_id` ASC),
ADD INDEX `payment_id_ik` (`payment_id` ASC),
ADD INDEX `mno_id_ik` (`mno_id` ASC),
ADD INDEX `msisdn_ik` (`msisdn` ASC),
ADD INDEX `amount_ik` (`amount` ASC),
ADD INDEX `amount_aggregator_ik` (`amount_aggregator` ASC),
ADD INDEX `fee_aggregator_ik` (`fee_aggregator` ASC),
ADD INDEX `reference_aggregator_ik` (`reference_aggregator` ASC),
ADD INDEX `date_request_ik` (`date_request` ASC),
ADD INDEX `date_response_ik` (`date_response` ASC),
ADD INDEX `date_response_aggregator_ik` (`date_response_aggregator` ASC),
ADD INDEX `date_callback_response_ik` (`date_callback_response` ASC),
ADD INDEX `date_payment_aggregator_ik` (`date_payment_aggregator` ASC),
ADD INDEX `status_aggregator_ik` (`status_aggregator` ASC),
ADD INDEX `response_code_ik` (`response_code` ASC);

ALTER TABLE `pad`.`trips` 
DROP INDEX `amount_fee` ,
ADD INDEX `amount_fee_ik` (`amount_fee` ASC),
DROP INDEX `date_slot_start` ,
ADD INDEX `date_slot_requested_ik` (`date_slot_requested` ASC),
ADD INDEX `type_ik` (`type` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `parking_permission_id_ik` (`parking_permission_id` ASC),
ADD INDEX `parking_permission_id_parking_entry_first_ik` (`parking_permission_id_parking_entry_first` ASC),
ADD INDEX `parking_permission_id_parking_entry_ik` (`parking_permission_id_parking_entry` ASC),
ADD INDEX `parking_permission_id_parking_exit_ik` (`parking_permission_id_parking_exit` ASC),
ADD INDEX `parking_permission_id_port_entry_ik` (`parking_permission_id_port_entry` ASC),
ADD INDEX `parking_permission_id_port_exit_ik` (`parking_permission_id_port_exit` ASC),
ADD INDEX `reference_number_ik` (`reference_number` ASC),
ADD INDEX `container_id_ik` (`container_id` ASC),
ADD INDEX `driver_msisdn_ik` (`driver_msisdn` ASC),
ADD INDEX `operator_id_created_ik` (`operator_id_created` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `lane_session_id_ik` (`lane_session_id` ASC),
ADD INDEX `is_fee_paid_ik` (`is_fee_paid` ASC),
ADD INDEX `operator_amount_fee_ik` (`operator_amount_fee` ASC),
ADD INDEX `date_fee_paid_ik` (`date_fee_paid` ASC),
ADD INDEX `date_slot_approved_ik` (`date_slot_approved` ASC),
ADD INDEX `date_entry_parking_ik` (`date_entry_parking` ASC),
ADD INDEX `date_exit_parking_ik` (`date_exit_parking` ASC),
ADD INDEX `date_entry_port_ik` (`date_entry_port` ASC),
ADD INDEX `date_exit_port_ik` (`date_exit_port` ASC),
DROP INDEX `date_created` ;

ALTER TABLE `pad`.`missions` 
ADD INDEX `status_ik` (`status` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC);
