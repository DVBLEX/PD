USE pad;

INSERT INTO `pad`.`system_parameters` (`id`, `system_environment`, `system_shortname`, `errors_from_email`, `errors_from_email_password`, `errors_to_email`, `password_forgot_email_limit`, `reg_email_code_send_limit`, `reg_email_verification_limit`, `reg_email_code_valid_minutes`, `reg_email_verification_valid_hours`, `reg_email_notification`, `login_lock_count_failed`, `login_lock_period`, `login_password_valid_period`, `allowed_hosts`, `client_id`, `client_secret`) VALUES ('1', 'TEST', 'PAD', 'errors.httpinterface@telclic.net', 'telclic p@55w0rd', 'errors@telclic.net', '3', '10', '3', '30', '4', 'rafael@telclic.net', '5', '6', '120', '127.0.0.1,213.79.53.175,195.218.123.202,0:0:0:0:0:0:0:1', '7e5327fbcf3a4c5f92f15gf5654t4g6rg1rf4f51f35337524612f10ed196fe3490a', 'e1a495ba94e40c1b3b0e2f9156df65e1f625dsfergfhdyrt6668c5aa6e');

ALTER TABLE `pad`.`email_templates` 
ADD COLUMN `type` INT(11) NOT NULL AFTER `id`;

ALTER TABLE `pad`.`email_templates` 
ADD COLUMN `user` VARCHAR(32) NOT NULL AFTER `config_id`;
