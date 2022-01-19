USE pad;

-- WARNING. RUN BELOW TWO QUERIES ON THE LIVE ON NOT SOONER THAN 15th SEPTEMBER 
UPDATE pad.port_operator_trip_fee SET trip_amount_fee = 5000, date_edited = now() WHERE port_operator_id IN (4,6,7) AND id > 0;
UPDATE pad.port_operator_trip_fee SET trip_amount_fee = 1500, date_edited = now() WHERE port_operator_id NOT IN (4,6,7) AND id > 0;
