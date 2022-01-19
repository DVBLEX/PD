USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `container_type` VARCHAR(4) NOT NULL AFTER `container_id`;

INSERT INTO `pad`.`email_templates` (`id`, `type`, `name`, `account_id`, `language_id`, `config_id`, `user`, `email_from`, `email_from_password`, `email_bcc`, `subject`, `template`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('33', '18', 'Mission and Trip Updated API Notification - EN', '-1', '1', '1', 'jeffrey@telclic.net', 'no-reply@agsparking.com', 'EQhhFGQ3', '', 'AGS PAD - Mission and Trip Updated by ${portOperator} - ${referenceNumber}', '<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>PAD</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><div style=\"margin:0;background-color:#f3f3f4\"><div style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><tbody><tr><td align=\"center\"><table width=\"600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#f3f3f4\" align=\"center\" style=\"border-spacing:0\"><tbody><tr><td width=\"600\" bgcolor=\"#f3f3f4\" style=\"border-collapse:collapse\"><table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#f3f3f4\" style=\"border-spacing:0;margin-bottom:0;margin-left:auto;margin-right:auto;margin-top:40px\"><tbody><tr><td width=\"600\" height=\"100\" bgcolor=\"#ffffff\" style=\"border-collapse:collapse;background:#ffffff;border-radius:3px 3px 0 0\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-spacing:0;margin:0 auto\"><tbody><tr><td><img src=\"https://apps.agsparking.com/pad/app/img/ags_logo.png\" alt=\"\" style=\"height: 100%;\" /></td></tr><tr><td><h4 style=\"color: #337AB7;font-size: 30px; margin-top: 10px;\">Port Access Dakar</h4></td></tr></tbody></table></td></tr><tr><td width=\"600\" bgcolor=\"#ffffff\">\n                                                ${templateBody}\n                                             </td></tr><tr><td style=\"background-color:#ffffff;border-radius:0 0 3px 3px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9;border-bottom:1px solid #e8e8e9\">\n                                                &nbsp;\n                                             </td></tr><tr><td width=\"600\" height=\"20\" style=\"border-collapse:collapse\">&nbsp;</td></tr><tr style=\"font-size:12px;color:#999999\"><td width=\"600\" align=\"left\" style=\"border-collapse:collapse\"><p style=\"line-height:21px;margin-bottom:15px;margin-left:0;margin-right:0;margin-top:28px\">CONFIDENTIAL! This email contains confidential information and is intended for the authorised recipient only. You may neither use nor edit the email including attachments, nor make them accessible to third parties in any manner whatsoever. Any unauthorised copying, disclosure or distribution of the material in this email is strictly forbidden. Thank you for your co-operation. If you received this message by mistake, please delete this email.\n                                                   \n                                  <br><br>\n                                                   SAVE PAPER - Please do not print this e-mail unless absolutely necessary\n                                                \n                                    </p></td></tr><tr><td width=\"600\" height=\"50\" style=\"border-collapse:collapse\">&nbsp;</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div></div></body></html>', '<table width=\"600\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" style=\"width:100%;padding:32px 50px 32px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9\"><tbody><tr><td><p style=\"font-family:\'Helvetica Neue\';font-size:26px;font-weight:normal;color:#545457;margin:1em 0\">Hello ${accountName},</p></td></tr><tr><td><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%\"><tbody><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#545457;line-height:22px;margin-bottom:1.3em\">Please note, a mission and trip has been updated on your account. Please \n                  \n                  <a href=\"${loginPageUrl}\"><strong>click here</strong></a> to login and approve or reject the trip.\n                \n                </p></td></tr><tr><td><table style=\"font-family:Helvetica;font-size:16px;color:#545457;\"><tr><td>Port Operator</td><td>${portOperator}</td></tr><tr><td>BAD Number</td><td>${referenceNumber}</td></tr><tr><td>Transaction Type</td><td>${transactionType}</td></tr><tr><td>Start Date</td><td>${startDate}</td></tr><tr><td>End Date</td><td>${endDate}</td></tr></table></td></tr><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#707074;line-height:22px;margin-bottom:1em\">Kind Regards,\n                  \n                  <br><strong>PAD Support Team</strong><br>https://www.agsparking.com\n                    \n                    </p></td></tr></tbody></table></td></tr></tbody></table>', '', '15', '-1', '2019-05-23 11:52:30', '2019-05-23 11:52:30');
INSERT INTO `pad`.`email_templates` (`id`, `type`, `name`, `account_id`, `language_id`, `config_id`, `user`, `email_from`, `email_from_password`, `email_bcc`, `subject`, `template`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('34', '18', 'Mission and Trip Updated API Notification - FR', '-1', '2', '1', 'jeffrey@telclic.net', 'no-reply@agsparking.com', 'EQhhFGQ3', '', 'AGS PAD - Mission et Voyage Actualisé par ${portOperator} - ${referenceNumber}', '<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>PAD</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/></head><body style=\"margin: 0; padding: 0;\"><div style=\"margin:0;background-color:#f3f3f4\"><div style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f3f3f4;font-size:14px;font-family:\'Helvetica Neue\',Helvetica,Arial,sans-serif;color:#333333;padding-bottom:0;padding-left:0;padding-right:0;padding-top:0;margin-bottom:0;margin-left:0;margin-right:0;margin-top:0\"><tbody><tr><td align=\"center\"><table width=\"600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#f3f3f4\" align=\"center\" style=\"border-spacing:0\"><tbody><tr><td width=\"600\" bgcolor=\"#f3f3f4\" style=\"border-collapse:collapse\"><table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#f3f3f4\" style=\"border-spacing:0;margin-bottom:0;margin-left:auto;margin-right:auto;margin-top:40px\"><tbody><tr><td width=\"600\" height=\"100\" bgcolor=\"#ffffff\" style=\"border-collapse:collapse;background:#ffffff;border-radius:3px 3px 0 0\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-spacing:0;margin:0 auto\"><tbody><tr><td><img src=\"https://apps.agsparking.com/pad/app/img/ags_logo.png\" alt=\"\" style=\"height: 100%;\" /></td></tr><tr><td><h4 style=\"color: #337AB7;font-size: 30px; margin-top: 10px;\">Port Access Dakar</h4></td></tr></tbody></table></td></tr><tr><td width=\"600\" bgcolor=\"#ffffff\">\n                                                ${templateBody}\n                                             </td></tr><tr><td style=\"background-color:#ffffff;border-radius:0 0 3px 3px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9;border-bottom:1px solid #e8e8e9\">\n                                                &nbsp;\n                                             </td></tr><tr><td width=\"600\" height=\"20\" style=\"border-collapse:collapse\">&nbsp;</td></tr><tr style=\"font-size:12px;color:#999999\"><td width=\"600\" align=\"left\" style=\"border-collapse:collapse\"><p style=\"line-height:21px;margin-bottom:15px;margin-left:0;margin-right:0;margin-top:28px\">CONFIDENTIEL! Cet email contient des informations confidentielles et est destiné au destinataire autorisé uniquement. Vous ne pouvez ni utiliser ni modifier le courrier électronique, y compris les pièces jointes, ni le rendre accessible à des tiers de quelque manière que ce soit. Toute copie, divulgation ou distribution non autorisée du contenu de ce courrier électronique est strictement interdite. Merci de votre collaboration. Si vous avez reçu ce message par erreur, veuillez supprimer cet email.\n                                                   \n                                  <br><br>\n                                                  SAVE PAPER - N\'imprimez pas cet e-mail sauf en cas de nécessité absolue\n                                                \n                                    </p></td></tr><tr><td width=\"600\" height=\"50\" style=\"border-collapse:collapse\">&nbsp;</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div></div></body></html>', '<table width=\"600\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" style=\"width:100%;padding:32px 50px 32px;border-left:1px solid #e8e8e9;border-right:1px solid #e8e8e9\"><tbody><tr><td><p style=\"font-family:\'Helvetica Neue\';font-size:26px;font-weight:normal;color:#545457;margin:1em 0\">Bonjour ${accountName},</p></td></tr><tr><td><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%\"><tbody><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#545457;line-height:22px;margin-bottom:1.3em\">Veuillez noter qu\'une mission et un nouveau voyage ont été actualisé sur votre compte. S\'il vous plaît \n                  <a href=\"${loginPageUrl}\"><strong>cliquez ici</strong></a> pour vous connecter et approuver ou rejeter le voyage.\n                </p></td></tr><tr><td><table style=\"font-family:Helvetica;font-size:16px;color:#545457;\"><tr><td>Opérateur Portuaire</td><td>${portOperator}</td></tr><tr><td>Nombre de BAD</td><td>${referenceNumber}</td></tr><tr><td>Type de transaction</td><td>${transactionType}</td></tr><tr><td>Date de début</td><td>${startDate}</td></tr><tr><td>Date de fin</td><td>${endDate}</td></tr></table></td></tr><tr><td><p style=\"font-family:Helvetica;font-size:16px;color:#707074;line-height:22px;margin-bottom:1em\">\n Sincères amitiés,\n                  <br><strong>PAD Support Team</strong><br>https://www.agsparking.com\n                    </p></td></tr></tbody></table></td></tr></tbody></table>', '', '15', '-1', '2019-05-23 11:52:30', '2019-05-23 11:52:30');

INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('35', '16', 'Mission and Trip Updated API Notification - EN', '1', '-1', '1', 'AGS', '${referenceLabel} : ${referenceNumber} has been updated on your account by ${portOperator}. Please login and approve or reject the trip. ', ' ', '10', '-1', '2019-05-23 10:11:20', '2019-05-23 10:11:20');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('36', '16', 'Mission and Trip Updated API Notification - FR', '2', '-1', '1', 'AGS', '${referenceLabel} : ${referenceNumber} a été actualisé sur votre compte par ${portOperator}. Veuillez vous connecter et approuver ou rejeter le voyage.', ' ', '10', '-1', '2019-05-23 10:11:20', '2019-05-23 10:11:20');

-- DROP TABLE pad.dpw_trips;