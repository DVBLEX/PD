USE pad;

INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('3', ' DAKAR TERMINAL', '2019-08-02 14:48:00');
INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('4', 'VIVO ENERGY', '2019-08-02 14:48:00');
INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('5', 'SENSTOCK', '2019-08-02 14:48:00');
INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('6', 'ORYX', '2019-08-02 14:48:00');
INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('7', 'ERES', '2019-08-02 14:48:00');
INSERT INTO `pad`.`port_operators` (`id`, `name`, `date_created`) VALUES ('8', 'TOUS MANUTENTIONNAIRES', '2019-08-02 14:48:00');

ALTER TABLE `pad`.`port_operator_gates`
CHANGE COLUMN `gate_number` `gate_number` VARCHAR(64) NOT NULL ;

UPDATE `pad`.`port_operator_gates` SET `gate_number`='Container Terminal' WHERE `id`='1003';
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1004', 'Mole 2', '3', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1005', 'Môle 9', '4', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1006', 'Jetée Nord', '4', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1007', 'Môle 9', '5', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1008', 'Jetée Nord', '5', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1009', 'Môle 9', '6', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1010', 'Jetée Nord', '6', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1011', 'Môle 9', '7', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1012', 'Jetée Nord', '7', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1013', 'Môle 1', '8', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1014', 'Môle 3', '8', '2019-08-02 15:37:00');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1015', 'Môle 4', '8', '2019-08-02 15:37:00');

UPDATE `pad`.`port_operator_gates` SET `gate_number`='Môle 2' WHERE `id`='1004';
