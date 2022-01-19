USE pad;

CREATE TABLE `pad`.`port_operator_trip_fee` (
  `id` INT(11) NOT NULL,
  `port_operator_id` INT(11) NOT NULL,
  `transaction_type` INT(11) NOT NULL,
  `trip_amount_fee` DECIMAL(7,0) NOT NULL,
  `operator_id` INT(11) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_edited` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `pad`.`port_operator_trip_fee` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;


INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1001,1,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1002,1,2,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1003,1,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1004,1,4,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1005,1,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1006,2,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1007,2,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1008,2,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1009,2,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1010,2,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1011,3,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1012,3,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1013,3,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1014,3,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1015,3,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1016,4,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1017,4,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1018,4,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1019,4,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1020,4,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1021,5,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1022,5,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1023,5,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1024,5,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1025,5,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1026,6,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1027,6,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1028,6,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1029,6,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1030,6,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1031,7,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1032,7,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1033,7,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1034,7,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1035,7,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1036,8,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1037,8,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1038,8,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1039,8,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1040,8,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1041,9,1,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1042,9,3,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1043,9,5,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1044,9,6,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');
INSERT INTO `port_operator_trip_fee` (`id`,`port_operator_id`,`transaction_type`,`trip_amount_fee`,`operator_id`,`date_created`,`date_edited`) VALUES (1045,9,7,0,-1,'2019-09-10 10:34:29','2019-09-10 10:34:29');


ALTER TABLE `pad`.`system_parameters` 
DROP COLUMN `trip_amount_fee`;
