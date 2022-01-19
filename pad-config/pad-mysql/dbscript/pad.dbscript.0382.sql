USE pad;

ALTER TABLE `pad`.`api_remote_addr` 
DROP INDEX `api_remote_addr_fk_01` ,
ADD INDEX `operator_id_ik` (`operator_id` ASC),
DROP INDEX `api_remote_addr_ik_01` ,
ADD INDEX `remote_addr_ik` (`remote_addr` ASC);

ALTER TABLE `pad`.`booking_slot_counts` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `date_slot_ik` (`date_slot` ASC),
ADD INDEX `hour_slot_from_ik` (`hour_slot_from` ASC),
ADD INDEX `hour_slot_to_ik` (`hour_slot_to` ASC);

ALTER TABLE `pad`.`booking_slot_limits` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `date_slot_ik` (`date_slot` ASC),
ADD INDEX `day_of_week_id_ik` (`day_of_week_id` ASC),
ADD INDEX `hour_slot_from_ik` (`hour_slot_from` ASC),
ADD INDEX `hour_slot_to_ik` (`hour_slot_to` ASC);

ALTER TABLE `pad`.`booking_slot_limits_default` 
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `day_of_week_id_ik` (`day_of_week_id` ASC),
ADD INDEX `hour_slot_from_ik` (`hour_slot_from` ASC),
ADD INDEX `hour_slot_to_ik` (`hour_slot_to` ASC);

ALTER TABLE `pad`.`driver_associations` 
ADD INDEX `status_ik` (`status` ASC),
ADD INDEX `driver_id_ik` (`driver_id` ASC),
ADD INDEX `account_id_ik` (`account_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `date_deleted_ik` (`date_deleted` ASC);

ALTER TABLE `pad`.`drivers` 
ADD INDEX `msisdn_ik` (`msisdn` ASC),
ADD INDEX `language_id_ik` (`language_id` ASC),
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`email_code_requests` 
ADD UNIQUE INDEX `token_uk` (`token` ASC),
ADD INDEX `count_code_sent_ik` (`count_code_sent` ASC),
ADD INDEX `count_verified_ik` (`count_verified` ASC),
ADD INDEX `date_code_sent_ik` (`date_code_sent` ASC);
