USE pad;

-- ALTER TABLE `pad`.`email_scheduler` 
-- ADD INDEX `mission_id` (`mission_id` ASC);

ALTER TABLE `pad`.`invoice` 
ADD UNIQUE INDEX `code_uk` (`code` ASC),
ADD INDEX `account_id_ik` (`account_id` ASC),
ADD INDEX `date_due_ik` (`date_due` ASC),
ADD INDEX `date_payment_ik` (`date_payment` ASC),
ADD INDEX `total_amount_ik` (`total_amount` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`lanes` 
ADD INDEX `lane_id_ik` (`lane_id` ASC),
ADD INDEX `zone_id_ik` (`zone_id` ASC),
ADD INDEX `device_id_ik` (`device_id` ASC),
ADD INDEX `date_last_request_ik` (`date_last_request` ASC);

ALTER TABLE `pad`.`operators` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `email_ik` (`email` ASC),
ADD INDEX `msisdn_ik` (`msisdn` ASC),
ADD INDEX `username_ik` (`username` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `language_id_ik` (`language_id` ASC),
ADD INDEX `date_last_password_ik` (`date_last_password` ASC),
ADD INDEX `date_last_passwd_set_up_ik` (`date_last_passwd_set_up` ASC);

ALTER TABLE `pad`.`parking` 
ADD INDEX `port_access_whitelist_id_ik` (`port_access_whitelist_id` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC),
ADD INDEX `driver_msisdn_ik` (`driver_msisdn` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `entry_lane_id_ik` (`entry_lane_id` ASC),
ADD INDEX `exit_lane_id_ik` (`exit_lane_id` ASC),
ADD INDEX `date_sms_exit_ik` (`date_sms_exit` ASC);
