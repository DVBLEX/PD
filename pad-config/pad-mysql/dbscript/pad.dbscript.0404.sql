USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `transaction_type` INT(11) NOT NULL AFTER `port_operator_id`;

UPDATE pad.trips t, pad.missions m SET t.transaction_type = m.transaction_type WHERE t.mission_id = m.id AND t.id > 0;
