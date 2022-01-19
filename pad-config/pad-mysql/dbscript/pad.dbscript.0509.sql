USE pad;

CREATE TABLE transporter_trips_statistics_20210507
SELECT * FROM pad.transporter_trips_statistics;

DELETE FROM pad.transporter_trips_statistics;

ALTER TABLE `pad`.`transporter_trips_statistics` 
ADD COLUMN `operator_total_amount_fee` DECIMAL(12,0) NOT NULL AFTER `transporter_total_amount_fee`,
CHANGE COLUMN `date_statistics` `date_fee_paid` DATE NOT NULL ,
CHANGE COLUMN `amount_total_trip_fee` `transporter_total_amount_fee` DECIMAL(12,0) NOT NULL ;

ALTER TABLE `pad`.`transporter_trips_statistics` 
ADD INDEX `operator_total_anmount_fee_ik` (`operator_total_amount_fee` ASC);
ALTER TABLE `pad`.`transporter_trips_statistics` RENAME INDEX `amount_total_trip_fee_ik` TO `transporter_total_amount_fee_ik`;


 INSERT INTO pad.transporter_trips_statistics
 ( 
  date_fee_paid, account_id, port_operator_id, 
  transaction_type, vehicle_registration_country_iso, count_trips, 
  transporter_total_amount_fee, operator_total_amount_fee, date_created 
 )
 SELECT 
  DATE(t.date_fee_paid), t.account_id, t.port_operator_id, 
  t.transaction_type, t.vehicle_registration_country_iso, COUNT(1), 
  sum(t.amount_fee), sum(t.operator_amount_fee), now() 
 FROM pad.trips t 
 WHERE t.date_fee_paid IS NOT NULL 
 GROUP BY DATE(t.date_fee_paid), t.account_id, t.port_operator_id, t.transaction_type, t.vehicle_registration_country_iso
 ORDER BY DATE(t.date_fee_paid);