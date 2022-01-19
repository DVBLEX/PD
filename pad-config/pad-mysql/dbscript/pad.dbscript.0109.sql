USE pad;

ALTER TABLE `pad`.`accounts`
ADD COLUMN `amount_hold` DECIMAL(7,0) NOT NULL AFTER `amount_overdraft_limit`;

ALTER TABLE `pad`.`system_parameters`
ADD COLUMN `max_amount_hold` DECIMAL(7,0) NOT NULL AFTER `trip_amount_fee`;

UPDATE `pad`.`system_parameters` SET `max_amount_hold`='6000' WHERE `id`='1';

UPDATE `pad`.`language_keys` SET `translate_value`='Proceed without payment' WHERE `id`='523';
UPDATE `pad`.`language_keys` SET `translate_value`='Procéder sans paiement' WHERE `id`='524';

UPDATE `pad`.`language_keys` SET `translate_value`='Se déconnecter' WHERE `id`='12';
UPDATE `pad`.`language_keys` SET `translate_value`='Nouveau Mot de passe' WHERE `id`='20';
UPDATE `pad`.`language_keys` SET `translate_value`='Confirme le mot de passe' WHERE `id`='22';
UPDATE `pad`.`language_keys` SET `translate_value`='Veuillez vérifier votre boîte de réception et suivre les instructions' WHERE `id`='42';
UPDATE `pad`.`language_keys` SET `translate_value`='Créer un nouveau mot de passe' WHERE `id`='58';
UPDATE `pad`.`language_keys` SET `translate_value`='L\'ancien mot de passe saisi est invalide' WHERE `id`='62';
UPDATE `pad`.`language_keys` SET `translate_value`='Résoudre le captcha' WHERE `id`='88';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer le mot de passe valide  ' WHERE `id`='92';
UPDATE `pad`.`language_keys` SET `translate_value`='Confirmer si l\'activité non autorisée doit être signalé' WHERE `id`='96';
UPDATE `pad`.`language_keys` SET `translate_value`='Examinez les détails attentivement. Si vous souhaitez le modifier, veuillez sélectionner l’étape appropriée.' WHERE `id`='148';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer la date de début' WHERE `id`='158';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer la date de fin' WHERE `id`='160';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer Prénom' WHERE `id`='182';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer le nom de famille' WHERE `id`='184';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer Email' WHERE `id`='186';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer un numéro de téléphone valide' WHERE `id`='188';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer le numéro de licence' WHERE `id`='192';
UPDATE `pad`.`language_keys` SET `translate_value`='Est actif' WHERE `id`='204';
UPDATE `pad`.`language_keys` SET `translate_value`='Effacer' WHERE `id`='302';
UPDATE `pad`.`language_keys` SET `translate_value`='N\'a pas reçu le courrier électronique du code de vérification' WHERE `id`='336';
UPDATE `pad`.`language_keys` SET `translate_value`='Numéro de téléphone' WHERE `id`='362';
UPDATE `pad`.`language_keys` SET `translate_value`='Voulez-vous rafraichirr ce site? Les modifications que vous avez apportées peuvent ne pas être enregistrées' WHERE `id`='372';
UPDATE `pad`.`language_keys` SET `translate_value`='S\'il vous plaît entrer le numéro de téléphone' WHERE `id`='390';
UPDATE `pad`.`language_keys` SET `translate_value`='Véhicule introuvable. Essayez encore s\'il vous plait' WHERE `id`='428';
UPDATE `pad`.`language_keys` SET `translate_value`='Entrer le numéro d\'immatriculation du véhicule' WHERE `id`='432';
UPDATE `pad`.`language_keys` SET `translate_value`='Déposer un conteneur à l\'exportation' WHERE `id`='478';
UPDATE `pad`.`language_keys` SET `translate_value`='Collecter un conteneur à l\'importation' WHERE `id`='482';
UPDATE `pad`.`language_keys` SET `translate_value`='Collecter vide' WHERE `id`='484';
UPDATE `pad`.`language_keys` SET `translate_value`='Refuser' WHERE `id`='542';
UPDATE `pad`.`language_keys` SET `translate_value`='Refus effectué' WHERE `id`='546';
UPDATE `pad`.`language_keys` SET `translate_value`='Changement requis' WHERE `id`='552';
UPDATE `pad`.`language_keys` SET `translate_value`='Sélectionnez Cash donné' WHERE `id`='554';
