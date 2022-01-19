USE pad;

CREATE TABLE pad.`languages` (
  `id` int(11) NOT NULL,
  `language` varchar(8) NOT NULL,
  `btn_description` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pad.languages(id, language, btn_description) VALUES(1, 'en', 'English');
INSERT INTO pad.languages(id, language, btn_description) VALUES(2, 'fr', 'French');

CREATE TABLE pad.`language_keys` (
  `id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `translate_key` varchar(128) NOT NULL,
  `translate_value` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `language_keys_uk` (`language_id`,`translate_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pad.language_keys VALUES(1, 1, 'KEY_NAVBAR_HOME', 'Home');
INSERT INTO pad.language_keys VALUES(2, 2, 'KEY_NAVBAR_HOME', 'Home');

INSERT INTO pad.language_keys VALUES(3, 1, 'KEY_NAVBAR_MISSIONS', 'Missions');
INSERT INTO pad.language_keys VALUES(4, 2, 'KEY_NAVBAR_MISSIONS', 'Missions');

INSERT INTO pad.language_keys VALUES(5, 1, 'KEY_NAVBAR_VEHICLES', 'Vehicles');
INSERT INTO pad.language_keys VALUES(6, 2, 'KEY_NAVBAR_VEHICLES', 'Véhicules');

INSERT INTO pad.language_keys VALUES(7, 1, 'KEY_NAVBAR_DRIVERS', 'Drivers');
INSERT INTO pad.language_keys VALUES(8, 2, 'KEY_NAVBAR_DRIVERS', 'Chauffeurs');