USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1479', '1', 'KEY_RESPONSE_1165', 'The mission cannot be canceled');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1480', '2', 'KEY_RESPONSE_1165', 'La mission ne peut pas être annulée');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1481', '1', 'KEY_SCREEN_MISSION_CANCEL_ALERT_MESSAGE', 'Are you sure you want to cancel this mission?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1482', '2', 'KEY_SCREEN_MISSION_CANCEL_ALERT_MESSAGE', 'Voulez-vous vraiment annuler cette mission?');
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('39', 'Mission Cancel', '2020-06-22 12:00:00');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('29', '13', 'Mission Created Notification - EN', '1', '-1', '1', 'AGS', '${referenceLabel} : ${referenceNumber} has been cancelled.', ' ', '10', '-1', '2019-05-23 10:11:20', '2019-05-23 10:11:20');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('30', '13', 'Mission Created Notification - FR', '2', '-1', '1', 'AGS', '${referenceLabel} : ${referenceNumber} a été annulé.', ' ', '10', '-1', '2019-05-23 10:11:20', '2019-05-23 10:11:20');
UPDATE `pad`.`sms_templates` SET `name` = 'Mission Cancelled Notification - EN' WHERE (`id` = '29');
UPDATE `pad`.`sms_templates` SET `name` = 'Mission Cancelled Notification - FR' WHERE (`id` = '30');

INSERT INTO `pad`.`email_templates` (`id`, `type`, `name`, `account_id`, `language_id`, `config_id`, `user`, `email_from`, `email_from_password`, `email_bcc`, `subject`, `template`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('22', '12', 'Mission Cancelled Notification - EN', '-1', '1', '-1', '', 'errors.httpinterface@telclic.net', 'telclic p@55w0rd', '', 'AGS PAD - Mission Created - ${referenceNumber}', '<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>PAD</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><div style=\"margin:0;background-color:#f3f3f4\"><div style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><tbody><tr><td align=\"center\"><table width=\"600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#f3f3f4\" align=\"center\" style=\"border-spacing:0\"><tbody><tr><td width=\"600\" bgcolor=\"#f3f3f4\" style=\"border-collapse:collapse\"><table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#f3f3f4\" style=\"border-spacing:0;margin-bottom:0;margin-left:auto;margin-right:auto;margin-top:40px\"><tbody><tr><td width=\"600\" height=\"100\" bgcolor=\"#ffffff\" style=\"border-collapse:collapse;background:#ffffff;border-radius:3px 3px 0 0\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-spacing:0;margin:0 auto\"><tbody><tr><td><img src=\"https://apps.agsparking.com/pad/app/img/ags_logo.png\" alt=\"\" style=\"height: 100%;\" /></td></tr><tr><td><h4 style=\"color: #337AB7;font-size: 30px; margin-top: 10px;\">Port Access Dakar</h4></td></tr></tbody></table></td></tr><tr><td width=\"600\" bgcolor=\"#ffffff\">\n                                                ${templateBody}\n                                             </td></tr><tr><td style=\"background-color:#ffffff;border-radius:0 0 3px 3px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9;border-bottom:1px solid #e8e8e9\">\n                                                &nbsp;\n                                             </td></tr><tr><td width=\"600\" height=\"20\" style=\"border-collapse:collapse\">&nbsp;</td></tr><tr style=\"font-size:12px;color:#999999\"><td width=\"600\" align=\"left\" style=\"border-collapse:collapse\"><p style=\"line-height:21px;margin-bottom:15px;margin-left:0;margin-right:0;margin-top:28px\">CONFIDENTIAL! This email contains confidential information and is intended for the authorised recipient only. You may neither use nor edit the email including attachments, nor make them accessible to third parties in any manner whatsoever. Any unauthorised copying, disclosure or distribution of the material in this email is strictly forbidden. Thank you for your co-operation. If you received this message by mistake, please delete this email.\n                                                   \n                                  <br><br>\n                                                   SAVE PAPER - Please do not print this e-mail unless absolutely necessary\n                                                \n                                    </p></td></tr><tr><td width=\"600\" height=\"50\" style=\"border-collapse:collapse\">&nbsp;</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div></div></body></html>', '<table width=\"600\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" style=\"width:100%;padding:32px 50px 32px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9\"><tbody><tr><td><p style=\"font-family:\'Helvetica Neue\';font-size:26px;font-weight:normal;color:#545457;margin:1em 0\">Hello ${accountName},</p></td></tr><tr><td><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%\"><tbody><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#545457;line-height:22px;margin-bottom:1.3em\">Please note, the below mission has been cancelled. </p></td></tr><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#545457;\"><table><tr><td>Port Operator</td><td>${portOperator}</td></tr><tr><td>BAD Number</td><td>${referenceNumber}</td></tr><tr><td>Transaction Type</td><td>${transactionType}</td></tr><tr><td>Start Date</td><td>${startDate}</td></tr><tr><td>End Date</td><td>${endDate}</td></tr></table></p></td></tr><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#707074;line-height:22px;margin-bottom:1em\">Kind Regards,<br><strong>PAD Support Team</strong><br>https://www.agsparking.com</p></td></tr></tbody></table></td></tr></tbody></table>', '', '15', '-1', '2019-05-23 11:52:30', '2019-05-23 11:52:30');
INSERT INTO `pad`.`email_templates` (`id`, `type`, `name`, `account_id`, `language_id`, `config_id`, `user`, `email_from`, `email_from_password`, `email_bcc`, `subject`, `template`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('23', '12', 'Mission Cancelled Notification - FR', '-1', '2', '-1', '', 'errors.httpinterface@telclic.net', 'telclic p@55w0rd', '', 'AGS PAD - Mission Créée - ${referenceNumber}', '<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>PAD</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><div style=\"margin:0;background-color:#f3f3f4\"><div style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><tbody><tr><td align=\"center\"><table width=\"600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#f3f3f4\" align=\"center\" style=\"border-spacing:0\"><tbody><tr><td width=\"600\" bgcolor=\"#f3f3f4\" style=\"border-collapse:collapse\"><table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#f3f3f4\" style=\"border-spacing:0;margin-bottom:0;margin-left:auto;margin-right:auto;margin-top:40px\"><tbody><tr><td width=\"600\" height=\"100\" bgcolor=\"#ffffff\" style=\"border-collapse:collapse;background:#ffffff;border-radius:3px 3px 0 0\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-spacing:0;margin:0 auto\"><tbody><tr><td><img src=\"https://apps.agsparking.com/pad/app/img/ags_logo.png\" alt=\"\" style=\"height: 100%;\" /></td></tr><tr><td><h4 style=\"color: #337AB7;font-size: 30px; margin-top: 10px;\">Port Access Dakar</h4></td></tr></tbody></table></td></tr><tr><td width=\"600\" bgcolor=\"#ffffff\">\n                                                ${templateBody}\n                                             </td></tr><tr><td style=\"background-color:#ffffff;border-radius:0 0 3px 3px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9;border-bottom:1px solid #e8e8e9\">\n                                                &nbsp;\n                                             </td></tr><tr><td width=\"600\" height=\"20\" style=\"border-collapse:collapse\">&nbsp;</td></tr><tr style=\"font-size:12px;color:#999999\"><td width=\"600\" align=\"left\" style=\"border-collapse:collapse\"><p style=\"line-height:21px;margin-bottom:15px;margin-left:0;margin-right:0;margin-top:28px\">CONFIDENTIEL! Cet email contient des informations confidentielles et est destiné au destinataire autorisé uniquement. Vous ne pouvez ni utiliser ni modifier le courrier électronique, y compris les pièces jointes, ni le rendre accessible à des tiers de quelque manière que ce soit. Toute copie, divulgation ou distribution non autorisée du contenu de ce courrier électronique est strictement interdite. Merci de votre collaboration. Si vous avez reçu ce message par erreur, veuillez supprimer cet email.\n                                                   \n                                  <br><br>\n                                                  SAVE PAPER - N\'imprimez pas cet e-mail sauf en cas de nécessité absolue\n                                                \n                                    </p></td></tr><tr><td width=\"600\" height=\"50\" style=\"border-collapse:collapse\">&nbsp;</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div></div></body></html>', '<table width=\"600\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" style=\"width:100%;padding:32px 50px 32px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9\"><tbody><tr><td><p style=\"font-family:\'Helvetica Neue\';font-size:26px;font-weight:normal;color:#545457;margin:1em 0\">Bonjour ${accountName},</p></td></tr><tr><td><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%\"><tbody><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#545457;line-height:22px;margin-bottom:1.3em\">Veuillez noter que la mission ci-dessous a été annulée.</p></td></tr><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#545457;\"><table><tr><td>Opérateur Portuaire</td><td>${portOperator}</td></tr><tr><td>Nombre de BAD</td><td>${referenceNumber}</td></tr><tr><td>Type de transaction</td><td>${transactionType}</td></tr><tr><td>Date de début</td><td>${startDate}</td></tr><tr><td>Date de fin</td><td>${endDate}</td></tr></table></p></td></tr><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#707074;line-height:22px;margin-bottom:1em\">\n Sincères amitiés,<br><strong>PAD Support Team</strong><br>https://www.agsparking.com</p></td></tr></tbody></table></td></tr></tbody></table>', '', '15', '-1', '2019-05-23 11:52:30', '2019-05-23 11:52:30');
UPDATE `pad`.`email_templates` SET `subject` = 'AGS PAD - Mission Cancelled - ${referenceNumber}' WHERE (`id` = '22');
UPDATE `pad`.`email_templates` SET `subject` = 'AGS PAD - Mission Annulé - ${referenceNumber}' WHERE (`id` = '23');