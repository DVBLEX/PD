USE pad;

INSERT INTO `pad`.`port_operators` (`id`, `name`, `name_short`, `date_created`) VALUES ('10', 'ISTAMCO', 'ISTAM', '2019-12-19 10:16:00');

INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `port_operator_id`, `date_created`) VALUES ('1018', 'Môle 3', 'ML3', '10002', 'mole2zone', '10', '2019-12-19 10:18:37');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `port_operator_id`, `date_created`) VALUES ('1019', 'Môle 4', 'ML4', '10002', 'mole2zone', '10', '2019-12-19 10:18:37');

INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`, `translate_key`) VALUES ('1043', '10', '1', 'KEY_SCREEN_DROP_OFF_EXPORT_LABEL');
INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`, `translate_key`) VALUES ('1044', '10', '3', 'KEY_SCREEN_PICK_UP_IMPORT_LABEL');

INSERT INTO `pad`.`port_operator_trip_fee` (`id`, `port_operator_id`, `transaction_type`, `trip_amount_fee`, `operator_id`, `date_created`, `date_edited`) VALUES ('1046', '10', '1', '0', '-1', '2019-09-10 10:34:29', '2019-09-10 10:34:29');
INSERT INTO `pad`.`port_operator_trip_fee` (`id`, `port_operator_id`, `transaction_type`, `trip_amount_fee`, `operator_id`, `date_created`, `date_edited`) VALUES ('1047', '10', '3', '0', '-1', '2019-09-10 10:34:29', '2019-09-10 10:34:29');
INSERT INTO `pad`.`port_operator_trip_fee` (`id`, `port_operator_id`, `transaction_type`, `trip_amount_fee`, `operator_id`, `date_created`, `date_edited`) VALUES ('1048', '10', '5', '0', '-1', '2019-09-10 10:34:29', '2019-09-10 10:34:29');
INSERT INTO `pad`.`port_operator_trip_fee` (`id`, `port_operator_id`, `transaction_type`, `trip_amount_fee`, `operator_id`, `date_created`, `date_edited`) VALUES ('1049', '10', '6', '0', '-1', '2019-09-10 10:34:29', '2019-09-10 10:34:29');
INSERT INTO `pad`.`port_operator_trip_fee` (`id`, `port_operator_id`, `transaction_type`, `trip_amount_fee`, `operator_id`, `date_created`, `date_edited`) VALUES ('1050', '10', '7', '0', '-1', '2019-09-10 10:34:29', '2019-09-10 10:34:29');
