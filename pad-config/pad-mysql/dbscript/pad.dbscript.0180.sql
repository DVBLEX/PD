USE pad;

INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('9', 'TOTAL', '2019-09-02 15:48:00');

INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1016', 'Môle 9', '9', '2019-09-02 15:51:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1017', 'Jetée Nord', '9', '2019-09-02 15:51:00');

INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`) VALUES ('1041', '9', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`) VALUES ('1042', '9', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`) VALUES ('1043', '9', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`) VALUES ('1044', '9', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`) VALUES ('1045', '9', '7');
