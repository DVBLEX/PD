USE pad;

UPDATE `pad`.`port_operator_trip_fee` SET `trip_amount_fee_other` = '2000', `operator_amount_fee` = '3000' WHERE (`id` = '1017');
UPDATE `pad`.`port_operator_trip_fee` SET `trip_amount_fee` = '5000' WHERE (`id` = '1022');
