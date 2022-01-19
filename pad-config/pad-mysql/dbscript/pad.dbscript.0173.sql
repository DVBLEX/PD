USE pad;

CREATE TABLE pad.`vehicles_anpr` (
  `id` int(11) NOT NULL,
  `vehicle_registration` varchar(32) NOT NULL,
  `car_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_request` datetime NOT NULL,
  `date_response` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vehicle_registration_ik` (`vehicle_registration`),
  KEY `car_id_ik` (`car_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE pad.vehicles_anpr auto_increment = 1001;

ALTER TABLE `pad`.`vehicles_anpr`
ADD UNIQUE INDEX `vehicle_registration_UNIQUE` (`vehicle_registration` ASC);

ALTER TABLE `pad`.`vehicles`
DROP COLUMN `anpr_id`;
