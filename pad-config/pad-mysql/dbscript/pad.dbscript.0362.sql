USE pad;

ALTER TABLE `pad`.`port_operator_trip_fee` 
ADD COLUMN `operator_amount_fee` DECIMAL(7,0) NOT NULL AFTER `trip_amount_fee`;
ALTER TABLE `pad`.`trips` 
ADD COLUMN `operator_amount_fee` DECIMAL(7,0) NOT NULL AFTER `amount_fee`;

-- WARNING. RUN BELOW TWO QUERIES ON THE LIVE NOT SOONER THAN 15th SEPTEMBER 
UPDATE pad.port_operator_trip_fee SET operator_amount_fee = 3000, date_edited = now() WHERE port_operator_id IN (1,2,3,5,6,7) AND id > 0;
UPDATE pad.port_operator_trip_fee SET operator_amount_fee = 1000, date_edited = now() WHERE port_operator_id NOT IN (1,2,3,5,6,7) AND id > 0;
