DROP TABLE IF EXISTS `email_log`;
CREATE TABLE `email_log`
(
    `id`              int          NOT NULL AUTO_INCREMENT,
    `is_processed`    int          NOT NULL,
    `type`            int          NOT NULL,
    `config_id`       int          NOT NULL,
    `language_id`     int          NOT NULL,
    `account_id`      int          NOT NULL,
    `mission_id`      int          NOT NULL,
    `trip_id`         int          NOT NULL,
    `template_id`     int          NOT NULL,
    `priority`        int          NOT NULL,
    `email_to`        varchar(256) NOT NULL,
    `email_bcc`       varchar(256) DEFAULT NULL,
    `subject`         varchar(128) NOT NULL,
    `channel`         int          NOT NULL,
    `attachment_path` varchar(256) NOT NULL,
    `date_created`    datetime     NOT NULL,
    `date_scheduled`  datetime     NOT NULL,
    `retry_count`     int          NOT NULL,
    `date_processed`  datetime     DEFAULT NULL,
    `response_code`   varchar(128) NOT NULL,
    `response_text`   varchar(256) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `email_scheduler`;
CREATE TABLE `email_scheduler`
(
    `id`              int           NOT NULL,
    `is_processed`    int           NOT NULL,
    `type`            int           NOT NULL,
    `config_id`       int           NOT NULL,
    `language_id`     int           NOT NULL,
    `account_id`      int           NOT NULL,
    `mission_id`      int           NOT NULL,
    `trip_id`         int           NOT NULL,
    `template_id`     int           NOT NULL,
    `priority`        int           NOT NULL,
    `email_to`        varchar(256)  NOT NULL,
    `email_bcc`       varchar(256) DEFAULT NULL,
    `subject`         varchar(128)  NOT NULL,
    `message`         varchar(8192) NOT NULL,
    `channel`         int           NOT NULL,
    `attachment_path` varchar(256)  NOT NULL,
    `date_created`    datetime      NOT NULL,
    `date_scheduled`  datetime      NOT NULL,
    `retry_count`     int           NOT NULL,
    `date_processed`  datetime     DEFAULT NULL,
    `response_code`   varchar(128)  NOT NULL,
    `response_text`   varchar(256) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `email_templates`;
CREATE TABLE `email_templates`
(
    `id`                  int           NOT NULL,
    `type`                int           NOT NULL,
    `name`                varchar(64)   NOT NULL,
    `language_id`         int           NOT NULL,
    `config_id`           int           NOT NULL,
    `user`                varchar(32)   NOT NULL,
    `email_from`          varchar(64)  DEFAULT NULL,
    `email_from_password` varchar(64)  DEFAULT NULL,
    `email_bcc`           varchar(256) DEFAULT NULL,
    `subject`             varchar(128)  NOT NULL,
    `template`            varchar(8192) NOT NULL,
    `message`             varchar(8192) NOT NULL,
    `variables`           varchar(128)  NOT NULL,
    `priority`            int           NOT NULL,
    `operator_id`         int           NOT NULL,
    `date_created`        datetime      NOT NULL,
    `date_edited`         datetime      NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `email_config`;
CREATE TABLE `email_config`
(
    `id`                           int         NOT NULL,
    `smtp_host`                    varchar(64) NOT NULL,
    `smtp_auth`                    varchar(8)  NOT NULL,
    `smtp_port`                    varchar(8)  NOT NULL,
    `smtp_starttls_enable`         varchar(8)  DEFAULT NULL,
    `smtp_ssl_protocols`           varchar(8)  NOT NULL,
    `operator_id`                  int         NOT NULL,
    `date_created`                 datetime    NOT NULL,
    `date_edited`                  datetime    NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `operators`;
CREATE TABLE `operators`
(
    `id`                              int          NOT NULL AUTO_INCREMENT,
    `code`                            varchar(64)  NOT NULL,
    `account_id`                      int          NOT NULL,
    `port_operator_id`                int          NOT NULL,
    `independent_port_operator_id`    int          NOT NULL,
    `first_name`                      varchar(32)  NOT NULL,
    `last_name`                       varchar(32)  NOT NULL,
    `email`                           varchar(64)  NOT NULL,
    `msisdn`                          varchar(16)  NOT NULL,
    `username`                        varchar(64)  NOT NULL,
    `password`                        varchar(128) NOT NULL,
    `role_id`                         int          NOT NULL,
    `is_active`                       boolean      NOT NULL,
    `is_deleted`                      boolean      NOT NULL,
    `is_locked`                       boolean      NOT NULL,
    `login_failure_count`             int          NOT NULL,
    `date_locked`                     datetime DEFAULT NULL,
    `date_last_login`                 datetime DEFAULT NULL,
    `date_last_attempt`               datetime DEFAULT NULL,
    `operator_id`                     int          NOT NULL,
    `language_id`                     int          NOT NULL,
    `count_passwd_forgot_requests`    int          NOT NULL,
    `date_last_passwd_forgot_request` datetime DEFAULT NULL,
    `date_password_forgot_reported`   datetime DEFAULT NULL,
    `date_last_password`              datetime     NOT NULL,
    `date_last_passwd_set_up`         datetime DEFAULT NULL,
    `is_credentials_expired`          boolean      NOT NULL,
    `date_created`                    datetime     NOT NULL,
    `date_edited`                     datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sms_log`;
CREATE TABLE `sms_log`
(
    `id`             int          NOT NULL AUTO_INCREMENT,
    `is_processed`   int          NOT NULL,
    `type`           int          DEFAULT NULL,
    `config_id`      varchar(11)  NOT NULL,
    `language_id`    int          NOT NULL,
    `account_id`     int          NOT NULL,
    `mission_id`     int          NOT NULL,
    `trip_id`        int          NOT NULL,
    `template_id`    int          NOT NULL,
    `priority`       int          NOT NULL,
    `msisdn`         varchar(32)  NOT NULL,
    `source_addr`    varchar(32)  NOT NULL,
    `message`        varchar(320) NOT NULL,
    `channel`        int          NOT NULL,
    `date_created`   datetime     NOT NULL,
    `date_scheduled` datetime     NOT NULL,
    `retry_count`    int          NOT NULL,
    `date_processed` datetime     DEFAULT NULL,
    `transaction_id` int          NOT NULL,
    `response_code`  int          NOT NULL,
    `response_text`  varchar(256) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sms_scheduler`;
CREATE TABLE `sms_scheduler`
(
    `id`             int          NOT NULL AUTO_INCREMENT,
    `is_processed`   int          NOT NULL,
    `type`           int          DEFAULT NULL,
    `config_id`      varchar(11)  NOT NULL,
    `language_id`    int          NOT NULL,
    `account_id`     int          NOT NULL,
    `mission_id`     int          NOT NULL,
    `trip_id`        int          NOT NULL,
    `template_id`    int          NOT NULL,
    `priority`       int          NOT NULL,
    `msisdn`         varchar(32)  NOT NULL,
    `source_addr`    varchar(32)  NOT NULL,
    `message`        varchar(320) NOT NULL,
    `channel`        int          NOT NULL,
    `date_created`   datetime     NOT NULL,
    `date_scheduled` datetime     NOT NULL,
    `retry_count`    int          NOT NULL,
    `date_processed` datetime     DEFAULT NULL,
    `transaction_id` int          NOT NULL,
    `response_code`  int          NOT NULL,
    `response_text`  varchar(256) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sms_templates`;
CREATE TABLE `sms_templates`
(
    `id`           int          NOT NULL,
    `type`         int          NOT NULL,
    `name`         varchar(64)  NOT NULL,
    `language_id`  int          NOT NULL,
    `config_id`    int          NOT NULL,
    `source_addr`  varchar(32)  NOT NULL,
    `message`      varchar(640) NOT NULL,
    `variables`    varchar(128) NOT NULL,
    `priority`     int          NOT NULL,
    `operator_id`  int          NOT NULL,
    `date_created` datetime     NOT NULL,
    `date_edited`  datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sms_config`;
CREATE TABLE `sms_config`
(
    `id`           int          NOT NULL,
    `url`          varchar(128) NOT NULL,
    `username`     varchar(32)  NOT NULL,
    `password`     varchar(32)  NOT NULL,
    `operator_id`  int          NOT NULL,
    `date_created` datetime     NOT NULL,
    `date_edited`  datetime     NOT NULL,
    PRIMARY KEY (`id`)
);
