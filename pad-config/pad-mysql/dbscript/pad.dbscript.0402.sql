USE pad;

ALTER TABLE `pad`.`sms_code_requests` 
ADD INDEX `date_code_sent_ik` (`date_code_sent` ASC),
ADD INDEX `date_verified_ik` (`date_verified` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`sms_log` 
ADD INDEX `transaction_id_ik` (`transaction_id` ASC),
ADD INDEX `channel_ik` (`channel` ASC);

ALTER TABLE `pad`.`sms_scheduler` 
ADD INDEX `trip_id_ik` (`trip_id` ASC),
ADD INDEX `channel_ik` (`channel` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `date_processed_ik` (`date_processed` ASC),
ADD INDEX `transaction_id_ik` (`transaction_id` ASC),
ADD INDEX `response_code_ik` (`response_code` ASC);

ALTER TABLE `pad`.`statements` 
ADD INDEX `operator_id_ik` (`operator_id` ASC);

ALTER TABLE `pad`.`system_checks` 
ADD INDEX `query_time_from_ik` (`query_time_from` ASC),
ADD INDEX `query_time_to_ik` (`query_time_to` ASC),
ADD INDEX `query_result_ik` (`query_result` ASC),
ADD INDEX `date_edited_ik` (`date_edited` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `type_ik` (`type` ASC);

ALTER TABLE `pad`.`system_timer_tasks` 
ADD INDEX `date_last_run_ik` (`date_last_run` ASC),
ADD INDEX `type_ik` (`type` ASC);

ALTER TABLE `pad`.`transporter_trips_statistics` 
ADD INDEX `date_statistics_ik` (`date_statistics` ASC),
ADD INDEX `account_id_ik` (`account_id` ASC),
ADD INDEX `account_payment_terms_type_ik` (`account_payment_terms_type` ASC),
ADD INDEX `port_operator_id_ik` (`port_operator_id` ASC),
ADD INDEX `transaction_type_ik` (`transaction_type` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `amount_total_trip_fee_ik` (`amount_total_trip_fee` ASC);

ALTER TABLE `pad`.`vehicle_counter` 
ADD INDEX `lane_id_ik` (`lane_id` ASC),
ADD INDEX `zone_id_ik` (`zone_id` ASC),
ADD INDEX `session_id_ik` (`session_id` ASC),
ADD INDEX `date_count_ik` (`date_count` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`vehicles` 
ADD INDEX `operator_id_ik` (`operator_id` ASC),
ADD INDEX `date_created_ik` (`date_created` ASC);

ALTER TABLE `pad`.`vehicles_anpr` 
ADD INDEX `date_created_ik` (`date_created` ASC),
ADD INDEX `date_response_ik` (`date_response` ASC);
