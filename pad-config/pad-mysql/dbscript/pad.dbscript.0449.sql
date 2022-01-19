USE pad;

-- CREATE TABLE pad.transporter_trips_statistics_20201130 AS SELECT * FROM pad.transporter_trips_statistics;

-- SELECT COUNT(1) FROM pad.transporter_trips_statistics;
-- SELECT COUNT(1) FROM pad.transporter_trips_statistics_20201130;

-- TRUNCATE `pad`.`transporter_trips_statistics`;

-- ALTER TABLE `pad`.`transporter_trips_statistics` 
-- AUTO_INCREMENT = 1 ;


-- ALTER TABLE `pad`.`transporter_trips_statistics` 
-- DROP COLUMN `account_payment_terms_type`,
-- ADD COLUMN `vehicle_registration_country_iso` VARCHAR(2) NOT NULL AFTER `transaction_type`,
-- DROP INDEX `account_payment_terms_type_ik` ;
-- ;

-- ALTER TABLE `pad`.`transporter_trips_statistics` 
-- AUTO_INCREMENT = 1 ,
-- ADD INDEX `vehicle_registration_country_iso_ik` (`vehicle_registration_country_iso` ASC) VISIBLE;
-- ALTER TABLE `pad`.`transporter_trips_statistics` ALTER INDEX `date_created_ik` INVISIBLE;


-- INSERT INTO pad.transporter_trips_statistics
-- (date_statistics, account_id, port_operator_id, 
--  transaction_type, vehicle_registration_country_iso, count_trips, 
--  amount_total_trip_fee, date_created)
-- SELECT DATE(t.date_fee_paid), t.account_id, t.port_operator_id, t.transaction_type, t.vehicle_registration_country_iso, COUNT(1), sum(t.amount_fee), now() 
-- FROM pad.trips t
-- WHERE t.date_fee_paid IS NOT NULL AND t.date_fee_paid < '2020-11-30 00:00:00' AND t.date_fee_paid >= '2020-04-01 00:00:00' 
-- GROUP BY t.account_id, t.port_operator_id, t.transaction_type, t.vehicle_registration_country_iso
-- ORDER BY DATE(t.date_fee_paid), t.account_id, t.port_operator_id, t.transaction_type, t.vehicle_registration_country_iso;