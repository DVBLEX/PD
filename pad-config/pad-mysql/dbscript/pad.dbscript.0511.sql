USE pad;

ALTER TABLE `pad`.`email_config` 
DROP COLUMN `smtp_socket_factory_fallback`,
DROP COLUMN `smtp_socket_factory_class`,
DROP COLUMN `smtp_socket_factory_port`,
ADD COLUMN `smtp_ssl_protocols` VARCHAR(16) NOT NULL AFTER `smtp_starttls_enable`;
	
UPDATE `pad`.`email_config` SET `smtp_ssl_protocols` = 'TLSv1.2' WHERE (`id` = '-1');
UPDATE `pad`.`email_config` SET `smtp_ssl_protocols` = 'TLSv1.2' WHERE (`id` = '1');
	
ALTER TABLE `pad`.`email_config` 
CHANGE COLUMN `smtp_starttls_enable` `smtp_starttls_enable` VARCHAR(8) NULL DEFAULT NULL ;

UPDATE `pad`.`email_config` SET `smtp_port` = '587', `smtp_starttls_enable` = 'true' WHERE (`id` = '-1');
UPDATE `pad`.`email_config` SET `smtp_port` = '587', `smtp_starttls_enable` = 'true' WHERE (`id` = '1');
