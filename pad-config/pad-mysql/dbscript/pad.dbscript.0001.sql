CREATE DATABASE `pad`;
USE pad;

CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `type` int(11) NOT NULL,
  `company_name` varchar(64) NOT NULL,
  `company_telephone_number` varchar(16) NOT NULL,
  `registration_country` varchar(2) NOT NULL,
  `company_address_1` varchar(64) NOT NULL,
  `company_address_2` varchar(64) NOT NULL,
  `company_address_3` varchar(64) NOT NULL,
  `company_address_4` varchar(64) NOT NULL,
  `is_email_verified` tinyint(1) NOT NULL,
  `date_email_verified` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  `date_deleted` datetime DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `vehicles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `type` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `vehicle_registration` varchar(32) NOT NULL,
  `colour` varchar(32) NOT NULL,
  `make` varchar(32) NOT NULL,
  `model` varchar(32) NOT NULL,
  `axel_count` int(11) NOT NULL,
  `registration_country` varchar(2) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `drivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `type` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `title` varchar(16) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  `msisdn` varchar(16) NOT NULL,
  `nationality` varchar(2) NOT NULL,
  `licence_number` varchar(64) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `missions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `type` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `is_fee_paid` tinyint(1) NOT NULL,
  `date_fee_paid` datetime DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `vehicle_missions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `account_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `mission_type` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `vehicle_type` int(11) NOT NULL,
  `vehicle_registration` varchar(32) NOT NULL,
  `vehicle_colour` varchar(32) NOT NULL,
  `vehicle_make` varchar(32) NOT NULL,
  `vehicle_model` varchar(32) NOT NULL,
  `vehicle_axel_count` int(11) NOT NULL,
  `registration_country` varchar(2) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `driver_missions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `account_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `mission_type` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `driver_type` int(11) NOT NULL,
  `driver_title` varchar(16) NOT NULL,
  `driver_first_name` varchar(64) NOT NULL,
  `driver_last_name` varchar(64) NOT NULL,
  `driver_email` varchar(64) NOT NULL,
  `driver_msisdn` varchar(16) NOT NULL,
  `driver_nationality` varchar(2) NOT NULL,
  `driver_licence_number` varchar(64) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `payments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `account_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `operators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `account_id` int(11) NOT NULL,
  `firstname` varchar(32) NOT NULL,
  `lastname` varchar(32) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `salt` varchar(128) NOT NULL,
  `role_id` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  `is_locked` tinyint(1) NOT NULL,
  `login_failure_count` int(1) NOT NULL,
  `date_locked` datetime DEFAULT NULL,
  `date_last_login` datetime DEFAULT NULL,
  `date_last_attempt` datetime DEFAULT NULL,
  `operator_id` int(11) NOT NULL,
  `count_passwd_forgot_email` int(11) NOT NULL,
  `date_last_passwd_forgot_email` datetime(3) DEFAULT NULL,
  `date_password_forgot_reported` datetime(3) DEFAULT NULL,
  `date_last_password` datetime(3) NOT NULL,
  `date_last_passwd_set_up_email` datetime(3) DEFAULT NULL,
  `is_credentials_expired` tinyint(1) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_uk` (`username`),
  KEY `account_id_ik` (`account_id`),
  KEY `role_id_ik` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `system_parameters` (
  `id` int(11) NOT NULL,
  `system_environment` varchar(8) NOT NULL,
  `system_shortname` varchar(8) NOT NULL,
  `errors_from_email` varchar(64) NOT NULL,
  `errors_from_email_password` varchar(64) NOT NULL,
  `errors_to_email` varchar(64) NOT NULL,
  `password_forgot_email_limit` int(11) NOT NULL,
  `reg_email_code_send_limit` int(11) NOT NULL,
  `reg_email_verification_limit` int(11) NOT NULL,
  `reg_email_code_valid_minutes` int(11) NOT NULL,
  `reg_email_verification_valid_hours` int(11) NOT NULL,
  `reg_email_notification` varchar(512) DEFAULT NULL,
  `login_lock_count_failed` int(11) NOT NULL,
  `login_lock_period` int(11) NOT NULL,
  `login_password_valid_period` int(11) NOT NULL,
  `allowed_hosts` varchar(128) NOT NULL,
  `client_id` varchar(128) NOT NULL,
  `client_secret` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `email_config` (
  `id` int(11) NOT NULL,
  `smtp_host` varchar(64) NOT NULL,
  `smtp_auth` varchar(8) NOT NULL,
  `smtp_port` varchar(8) NOT NULL,
  `smtp_socket_factory_port` varchar(8) DEFAULT NULL,
  `smtp_socket_factory_class` varchar(64) DEFAULT NULL,
  `smtp_socket_factory_fallback` varchar(8) DEFAULT NULL,
  `smtp_starttls_enable` tinyint(1) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `email_code_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(64) NOT NULL,
  `code` varchar(5) NOT NULL,
  `token` varchar(64) DEFAULT NULL,
  `count_code_sent` int(11) NOT NULL,
  `count_verified` int(11) NOT NULL,
  `date_code_sent` datetime NOT NULL,
  `date_verified` datetime DEFAULT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `email_templates` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `account_id` int(11) NOT NULL,
  `config_id` int(11) NOT NULL,
  `email_from` varchar(64) DEFAULT NULL,
  `email_from_password` varchar(64) DEFAULT NULL,
  `email_from_alias` varchar(45) NOT NULL DEFAULT '',
  `email_bcc` varchar(256) DEFAULT NULL,
  `subject` varchar(128) NOT NULL,
  `template` varchar(8192) NOT NULL,
  `message` varchar(8192) NOT NULL,
  `variables` varchar(128) NOT NULL,
  `priority` int(11) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `email_scheduler` (
  `id` int(11) NOT NULL,
  `is_processed` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `config_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `email_to` varchar(256) NOT NULL,
  `email_bcc` varchar(256) DEFAULT NULL,
  `subject` varchar(128) NOT NULL,
  `message` varchar(8192) NOT NULL,
  `channel` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_scheduled` datetime NOT NULL,
  `retry_count` int(11) NOT NULL,
  `date_processed` datetime DEFAULT NULL,
  `response_code` varchar(128) NOT NULL,
  `response_text` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date_scheduled_ik` (`date_scheduled`),
  KEY `is_processed_ik` (`is_processed`),
  KEY `priority_ik` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `email_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_processed` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `config_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `mission_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `email_to` varchar(256) NOT NULL,
  `email_bcc` varchar(256) DEFAULT NULL,
  `subject` varchar(128) NOT NULL,
  `channel` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_scheduled` datetime NOT NULL,
  `retry_count` int(11) NOT NULL,
  `date_processed` datetime DEFAULT NULL,
  `response_code` varchar(128) NOT NULL,
  `response_text` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date_scheduled_ik` (`date_scheduled`),
  KEY `mission_id_ik` (`mission_id`),
  KEY `template_id_ik` (`template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
