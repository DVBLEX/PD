USE pad;

ALTER TABLE `pad`.`vehicles_anpr` 
DROP INDEX `vehicle_registration_UNIQUE` ,
ADD UNIQUE INDEX `vehicle_registration_uk` (`vehicle_registration` ASC),
DROP INDEX `vehicle_registration_ik` ;
