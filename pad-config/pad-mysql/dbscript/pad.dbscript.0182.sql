USE pad;

CREATE TABLE pad.`anpr_parameters` (
  `id` int(11) NOT NULL,
  `last_entry_log_id` int(11) NOT NULL,
  `entry_log_page_size` int(11) NOT NULL,
  `entry_log_connect_timeout` int(11) NOT NULL,
  `entry_log_socket_timeout` int(11) NOT NULL,
  `entry_log_conn_request_timeout` int(11) NOT NULL,
  `date_edited` datetime NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`anpr_parameters` (`id`, `last_entry_log_id`, `entry_log_page_size`, `entry_log_connect_timeout`, `entry_log_socket_timeout`, `entry_log_conn_request_timeout`, `date_edited`) VALUES ('1', '8480', '200', '30000', '30000', '30000', NULL);

INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('106', 'anprEntryLogTimerTask', '2019-09-02 17:17:53', 'continuous', '60000', 'pad');
UPDATE `pad`.`system_timer_tasks` SET `period` = '5000' WHERE (`id` = '105');

UPDATE `pad`.`anpr_parameters` SET `entry_log_page_size` = '100' WHERE (`id` = '1');

ALTER TABLE `pad`.`anpr_parameters`
ADD COLUMN `default_connect_timeout` INT(11) NOT NULL AFTER `entry_log_conn_request_timeout`,
ADD COLUMN `default_socket_timeout` INT(11) NOT NULL AFTER `default_connect_timeout`,
ADD COLUMN `default_conn_request_timeout` INT(11) NOT NULL AFTER `default_socket_timeout`;

UPDATE `pad`.`anpr_parameters` SET `default_connect_timeout` = '3000', `default_socket_timeout` = '3000', `default_conn_request_timeout` = '3000' WHERE (`id` = '1');

INSERT INTO `pad`.`anpr_log` (`is_processed`, `request_type`, `mission_id`, `trip_id`, `vehicle_registration`, `parking_permission_id`, `date_valid_from`, `date_valid_to`, `priority`, `date_created`, `date_scheduled`, `retry_count`, `date_processed`, `response_code`, `response_text`) VALUES ('0', '100', '-1', '-1', ' ', '-1', '2019-09-05 10:11:58', '2019-09-05 10:11:58', '15', '2019-09-05 10:11:58', '2019-09-05 10:11:58', '0', NULL, '-1', ' ');
INSERT INTO `pad`.`anpr_scheduler` (`id`, `is_processed`, `request_type`, `mission_id`, `trip_id`, `vehicle_registration`, `date_valid_from`, `date_valid_to`, `priority`, `date_created`, `date_scheduled`, `retry_count`, `date_processed`, `response_code`, `response_text`) VALUES (LAST_INSERT_ID(), '0', '100', '-1', '-1', ' ', '2019-09-05 10:11:58', '2019-09-05 10:11:58', '15', '2019-09-05 10:11:58', '2019-09-05 10:11:58', '0', NULL, '-1', ' ');
