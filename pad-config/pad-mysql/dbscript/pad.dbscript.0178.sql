USE pad;


ALTER TABLE `pad`.`anpr_log` 
CHANGE COLUMN `car_id` `parking_permission_id` INT(11) NOT NULL ;

