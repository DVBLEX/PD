USE pad;


ALTER TABLE `pad`.`port_operator_trips_api` 
ADD COLUMN `trip_id` INT NOT NULL AFTER `date_request`;
