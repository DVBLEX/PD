USE pad;

INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1007', 'Môle 10', 'ML10', '10002', 'mole2zone', '2020-12-04 15:55:12');

UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1003' WHERE (`id` = '1039');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1007' WHERE (`id` = '1036');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1007' WHERE (`id` = '1037');
