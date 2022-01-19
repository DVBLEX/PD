USE pad;

CREATE TABLE `pad`.`port_entry_zones`
(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL,
    `date_created` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`port_entry_zones` (`id`, `name`, `date_created`) VALUES ('1', 'Môle 3', '2021-03-09 14:30');
INSERT INTO `pad`.`port_entry_zones` (`id`, `name`, `date_created`) VALUES ('2', 'Môle 10', '2021-03-09 14:30');
INSERT INTO `pad`.`port_entry_zones` (`id`, `name`, `date_created`) VALUES ('3', 'North Zone', '2021-03-09 14:30');

CREATE TABLE `pad`.`port_entry_zones_port_operator_gates` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `port_entry_zone_id` INT(11) NOT NULL,
    `port_operator_gate_id` INT(11) NOT NULL,
    `date_created` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `port_entry_zone_id_ik` (`port_entry_zone_id` ASC),
    INDEX `port_operator_gate_id_ik` (`port_operator_gate_id` ASC)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`port_entry_zones_port_operator_gates` (`id`, `port_entry_zone_id`, `port_operator_gate_id`, `date_created`) VALUES ('1', '1', '1003', '2021-03-09 14:30');
INSERT INTO `pad`.`port_entry_zones_port_operator_gates` (`id`, `port_entry_zone_id`, `port_operator_gate_id`, `date_created`) VALUES ('2', '1', '1007', '2021-03-09 14:30');
INSERT INTO `pad`.`port_entry_zones_port_operator_gates` (`id`, `port_entry_zone_id`, `port_operator_gate_id`, `date_created`) VALUES ('3', '3', '1004', '2021-03-09 14:30');
INSERT INTO `pad`.`port_entry_zones_port_operator_gates` (`id`, `port_entry_zone_id`, `port_operator_gate_id`, `date_created`) VALUES ('4', '3', '1005', '2021-03-09 14:30');
