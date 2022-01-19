USE pad;

DELETE FROM `pad`.`port_operator_transaction_types` WHERE (`id` = '1008');
DELETE FROM `pad`.`port_operator_transaction_types` WHERE (`id` = '1016');
DELETE FROM `pad`.`port_operator_transaction_types` WHERE (`id` = '1021');
DELETE FROM `pad`.`port_operator_transaction_types` WHERE (`id` = '1026');
DELETE FROM `pad`.`port_operator_transaction_types` WHERE (`id` = '1031');

DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1008');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1010');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1013');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1014');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1015');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1016');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1018');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1019');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1020');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1021');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1023');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1024');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1025');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1026');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1028');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1029');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1030');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1031');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1033');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1034');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1035');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1038');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1039');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1040');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1048');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1049');
DELETE FROM `pad`.`port_operator_trip_fee` WHERE (`id` = '1050');

DELETE FROM pad.booking_slot_limits_default WHERE id > 0;
ALTER TABLE pad.booking_slot_limits_default auto_increment = 1;
