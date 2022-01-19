USE pad;

ALTER TABLE `pad`.`parking_statistics` 
ADD INDEX `date_ik` (`date` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC);

ALTER TABLE `pad`.`port_access` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `driver_msisdn_ik` (`driver_msisdn` ASC),
ADD INDEX `entry_lane_id_ik` (`entry_lane_id` ASC),
ADD INDEX `exit_lane_id_ik` (`exit_lane_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC);

ALTER TABLE `pad`.`port_access_whitelist` 
ADD UNIQUE INDEX `code_uk` (`code` ASC),
ADD INDEX `status_ik` (`status` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `date_from_ik` (`date_from` ASC),
ADD INDEX `date_to_ik` (`date_to` ASC),
ADD INDEX `vehicle_registration_ik` (`vehicle_registration` ASC),
ADD INDEX `parking_permission_id_ik` (`parking_permission_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`port_operator_alerts` 
ADD UNIQUE INDEX `code_uk` (`code` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `date_issue_ik` (`date_issue` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `date_resolution_estimate_ik` (`date_resolution_estimate` ASC);

ALTER TABLE `pad`.`port_operator_gates` 
ADD INDEX `anpr_zone_id_ik` (`anpr_zone_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC);

ALTER TABLE `pad`.`port_operator_trip_fee` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC);

ALTER TABLE `pad`.`port_operator_trips_api` 
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `vehicle_reg_number_ik` (`vehicle_reg_number` ASC),
ADD INDEX `date_slot_from_ik` (`date_slot_from` ASC),
ADD INDEX `date_slot_to_ik` (`date_slot_to` ASC),
ADD INDEX `date_request_ik` (`date_request` ASC),
ADD INDEX `trip_id_ik` (`trip_id` ASC),
ADD INDEX `trip_code_ik` (`trip_code` ASC),
ADD INDEX `response_code_ik` (`response_code` ASC),
ADD INDEX `date_response_ik` (`date_response` ASC);

ALTER TABLE `pad`.`port_statistics` 
ADD INDEX `date_ik` (`date` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC);

ALTER TABLE `pad`.`receipt` 
ADD INDEX `account_id_ik` (`account_id` ASC),
ADD INDEX `payment_id_ik` (`payment_id` ASC),
ADD INDEX `msisdn_ik` (`msisdn` ASC),
ADD INDEX `payment_option_ik` (`payment_option` ASC),
ADD INDEX `total_amount_ik` (`total_amount` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`sessions` 
ADD UNIQUE INDEX `code_uk` (`code` ASC),
ADD INDEX `status_ik` (`status` ASC),
ADD INDEX `kiosk_operator_id_ik` (`kiosk_operator_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `lane_id_ik` (`lane_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `date_start_ik` (`date_start` ASC),
ADD INDEX `date_end_ik` (`date_end` ASC);
