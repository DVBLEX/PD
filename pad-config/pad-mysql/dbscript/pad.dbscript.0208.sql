USE pad;

UPDATE `pad`.`online_payment_parameters` SET `default_connect_timeout` = '30000', `default_socket_timeout` = '30000', `default_conn_request_timeout` = '30000' WHERE (`id` = '1');
UPDATE `pad`.`online_payment_parameters` SET `default_connect_timeout` = '30000', `default_socket_timeout` = '30000', `default_conn_request_timeout` = '30000' WHERE (`id` = '2');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Exportation' WHERE (`id` = '478');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Importation' WHERE (`id` = '482');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Exportation Urgente' WHERE (`id` = '926');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Importation Urgente' WHERE (`id` = '928');
