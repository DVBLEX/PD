USE pad;

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `agsparking_entry_lane1_video_feed_url` VARCHAR(128) NOT NULL AFTER `iis_server_state`,
ADD COLUMN `agsparking_entry_lane2_video_feed_url` VARCHAR(128) NOT NULL AFTER `agsparking_entry_lane1_video_feed_url`,
ADD COLUMN `agsparking_entry_lane3_video_feed_url` VARCHAR(128) NOT NULL AFTER `agsparking_entry_lane2_video_feed_url`,
ADD COLUMN `agsparking_entry_lane4_video_feed_url` VARCHAR(128) NOT NULL AFTER `agsparking_entry_lane3_video_feed_url`,
ADD COLUMN `agsparking_entry_lane5_video_feed_url` VARCHAR(128) NOT NULL AFTER `agsparking_entry_lane4_video_feed_url`;

UPDATE `pad`.`anpr_parameters` SET `agsparking_entry_lane2_video_feed_url` = 'http://10.11.8.102:9901/video.mjpeg', `agsparking_entry_lane3_video_feed_url` = 'http://10.11.8.103:9901/video.mjpeg', `agsparking_entry_lane4_video_feed_url` = 'http://10.11.8.104:9901/video.mjpeg' WHERE (`id` = '1');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1377', '1', 'KEY_SCREEN_VIDEO_FEED_URL_LABEL', 'Video Feed URL');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1378', '2', 'KEY_SCREEN_VIDEO_FEED_URL_LABEL', 'URL du flux vidéo');