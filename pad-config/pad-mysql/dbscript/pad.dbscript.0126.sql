USE pad;

DELETE FROM `pad`.`email_config` WHERE `id`='1';
INSERT INTO `pad`.`email_config` (`id`, `smtp_host`, `smtp_auth`, `smtp_port`, `smtp_starttls_enable`, `operator_id`, `date_created`, `date_edited`) VALUES ('1', 'send.one.com', 'true', '587', '1', '-1', '2019-05-31 09:29:00', '2019-05-31 09:29:00');
