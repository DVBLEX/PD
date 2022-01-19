USE pad;

INSERT INTO `pad`.`system_checks` (`id`, `name`, `is_active`, `type`, `is_query`, `query_time_from`, `query_time_to`, `query`, `query_params`, `query_result`, `query_execution_time`, `comparison_operator`, `comparison_value`, `comparison_result`, `date_edited`, `date_created`) VALUES ('1', 'ANPR_QUEUE_CHECK', '1', '1', '1', '00:00:00', '23:59:59', 'SELECT COUNT(1) FROM anpr_scheduler WHERE date_scheduled < SUBTIME(now(), \'00:06:00\')', '', '0', '0', '2', '0', '0', '2019-09-02 11:18:08', '2019-09-02 11:18:08');

