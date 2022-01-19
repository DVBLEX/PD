USE pad;

ALTER TABLE `pad`.`port_operator_trip_fee` 
ADD COLUMN `trip_amount_fee_other` DECIMAL(7,0) NOT NULL COMMENT 'alternative trip fee that will be used only in particular scenarios. e.g. for non pertroleum and non senegalese trucks' AFTER `trip_amount_fee`;

-- RUN BELOW 2 QUERIES ON THE LIVE ON 15TH SEP
UPDATE pad.port_operator_trip_fee SET trip_amount_fee_other = trip_amount_fee, date_edited = now() WHERE id > 0;
UPDATE pad.port_operator_trip_fee SET trip_amount_fee_other = 6000, date_edited = now() WHERE port_operator_id IN (1,2,3,5,8,10) AND id > 0;

ALTER TABLE `pad`.`trips` ADD COLUMN `vehicle_registration_country_iso` VARCHAR(2) NOT NULL AFTER `vehicle_registration`;

UPDATE pad.trips SET vehicle_registration_country_iso = "SN" WHERE vehicle_id = -1 AND id > 0;

UPDATE pad.trips t SET t.vehicle_registration_country_iso = (SELECT v.registration_country_iso FROM pad.vehicles v WHERE t.vehicle_id = v.id) WHERE t.vehicle_id != -1 AND t.id > 0;
