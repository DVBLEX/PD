USE pad;


ALTER TABLE `pad`.`anpr_log` 
ADD COLUMN `car_id` INT(11) NOT NULL AFTER `vehicle_registration`;

