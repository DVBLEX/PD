package com.pad.server.api.services.trip;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.PortOperatorTripsApi;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.MissionJson;
import com.pad.server.base.jsonentities.api.TripApiJson;
import com.pad.server.base.jsonentities.api.TripApiResponseJson;
import com.pad.server.base.jsonentities.api.TripJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.vehicle.VehicleService;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    private static final Logger                           logger = Logger.getLogger(TripServiceImpl.class);

    @Autowired
    private HibernateTemplate                             hibernateTemplate;

    @Autowired
    private AccountService                                accountService;

    @Autowired
    private EmailService                                  emailService;

    @Autowired
    private SmsService                                    smsService;

    @Autowired
    private MissionService                                missionService;

    @Autowired
    private SystemService                                 systemService;

    @Autowired
    private com.pad.server.base.services.trip.TripService tripService;

    @Autowired
    private VehicleService                                vehicleService;

    @Autowired
    private OperatorService                               operatorService;

    @Value("${system.url}")
    private String                                        systemUrl;

    @Value("${system.url.login.tp}")
    private String                                        loginTransporterUrl;

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public TripApiResponseJson addTripApi(TripApiJson tripApiJson, Operator operator) throws PADException, PADValidationException, Exception {

        PortOperator portOperator = systemService.getPortOperatorFromMap(operator.getPortOperatorId());

        if (portOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "portOperator is null");

        List<PortOperatorTransactionType> transactionTypeList = portOperator.getPortOperatorTransactionTypesList();

        PortOperatorTransactionType portOperatorTransactionType = transactionTypeList.stream()
            .filter(e -> tripApiJson.getTransactionType().equalsIgnoreCase(e.getTransactionTypeCode())).findAny().orElse(null);

        if (portOperatorTransactionType == null)
            throw new PADException(ServerResponseConstants.INVALID_TRANSACTION_TYPE_CODE, ServerResponseConstants.INVALID_TRANSACTION_TYPE_TEXT, "invalid transaction type");

        Date dateMissionStart;
        Date dateMissionEnd;

        try {
            dateMissionStart = ServerUtil.parseDate(ServerConstants.dateFormatyyyyMMddHHmm, tripApiJson.getDateSlotFrom());
        } catch (ParseException e) {
            throw new PADException(ServerResponseConstants.INVALID_DATE_SLOT_FROM_CODE, ServerResponseConstants.INVALID_DATE_SLOT_FROM_TEXT, "invalid date slot from");
        }
        try {
            dateMissionEnd = ServerUtil.parseDate(ServerConstants.dateFormatyyyyMMddHHmm, tripApiJson.getDateSlotTo());
        } catch (ParseException e) {
            throw new PADException(ServerResponseConstants.INVALID_DATE_SLOT_TO_CODE, ServerResponseConstants.INVALID_DATE_SLOT_TO_TEXT, "invalid date slot to");
        }

        if (dateMissionEnd.before(dateMissionStart))
            throw new PADException(ServerResponseConstants.INVALID_DATE_SLOT_TO_CODE, ServerResponseConstants.INVALID_DATE_SLOT_TO_TEXT, "invalid date slot range");

        Account account = accountService.getAccountByCompanyShortName(tripApiJson.getTransporterShortName());

        if (account == null) {

            // Email to portOperator
            emailService.sendTransporterShortNameErrorEmailNotification(tripApiJson, portOperatorTransactionType.getTransactionType(), operator.getLanguageId(),
                operator.getPortOperatorId(), operator.getEmail(), ServerConstants.EMAIL_TRANSPORTER_SHORT_NAME_MAPPING_ERROR_TEMPLATE_TYPE);

            // Email to AGS team
            emailService.sendTransporterShortNameErrorEmailNotification(tripApiJson, portOperatorTransactionType.getTransactionType(), ServerConstants.LANGUAGE_FR_ID,
                operator.getPortOperatorId(), systemService.getSystemParameter().getAgsOperationsEmail(),
                ServerConstants.EMAIL_TRANSPORTER_SHORT_NAME_MAPPING_ERROR_AGS_TEAM_TEMPLATE_TYPE);

            throw new PADException(ServerResponseConstants.INVALID_TRANSPORTER_SHORT_NAME_CODE, ServerResponseConstants.INVALID_TRANSPORTER_SHORT_NAME_TEXT,
                "invalid transporter short name");
        }

        MissionJson missionJson = new MissionJson();
        missionJson.setAccountCodes(new String[] { account.getCode() });
        missionJson.setPortOperatorId((int) operator.getPortOperatorId());
        missionJson.setReferenceNumber(tripApiJson.getReferenceNumber());
        missionJson.setTransactionType(portOperatorTransactionType.getTransactionType());
        missionJson.setGateId(portOperatorTransactionType.getPortOperatorGate().getId());
        missionJson.setDateMissionStart(dateMissionStart);
        missionJson.setDateMissionEnd(dateMissionEnd);

        missionService.createMission(missionJson, operator.getId(), true);

        Vehicle vehicle = vehicleService.getVehicleByAccountIdAndRegNumber(account.getId(), tripApiJson.getVehicleRegNumber());

        String vehicleCode = ServerConstants.DEFAULT_STRING;
        if (vehicle == null) {
            vehicleCode = addVehicle(account, tripApiJson.getVehicleRegNumber(), operator.getId());
        } else {
            vehicleCode = vehicle.getCode();
        }

        TripJson tripJson = new TripJson();
        tripJson.setTransactionType(portOperatorTransactionType.getTransactionType());
        tripJson.setPortOperatorId((int) operator.getPortOperatorId());
        tripJson.setVehicleCode(vehicleCode);
        tripJson.setContainerId(tripApiJson.getContainerId());
        tripJson.setContainerType(tripApiJson.getContainerType());
        tripJson.setDriverCode(ServerConstants.DEFAULT_STRING);
        tripJson.setMissionCode(missionJson.getCode());
        tripJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, dateMissionStart));
        tripJson.setTimeSlotString(ServerUtil.formatDate(ServerConstants.timeFormatHHmm, dateMissionStart));

        String tripCode = tripService.addTrip(tripJson, account.getId(), operator.getId());

        HashMap<String, Object> params = new HashMap<>();
        params.put("referenceLabel", account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID ? "BAD / Réservation Export" : "BAD / Booking Export");
        params.put("accountName", account.getFirstName());
        params.put("portOperator", systemService.getPortOperatorNameById(missionJson.getPortOperatorId()));
        params.put("referenceNumber", missionJson.getReferenceNumber());
        params.put("transactionType", ServerUtil.getTransactionTypeName(missionJson.getTransactionType(), account.getLanguageId()));
        params.put("startDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, missionJson.getDateMissionStart()));
        params.put("endDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, missionJson.getDateMissionEnd()));
        params.put("loginPageUrl", systemUrl + loginTransporterUrl);

        sendNotification(account, params, ServerConstants.EMAIL_MISSION_TRIP_ADDED_API_NOTIFICATION_TEMPLATE_TYPE,
            ServerConstants.SMS_MISSION_TRIP_ADDED_API_NOTIFICATION_TEMPLATE_TYPE);

        return new TripApiResponseJson(tripCode, tripJson.getReferenceNumber());
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public TripApiResponseJson updateTripApi(TripApiJson tripApiJson, Operator operator) throws PADException, Exception {

        PortOperator portOperator = systemService.getPortOperatorFromMap(operator.getPortOperatorId());

        if (portOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "portOperator is null");

        Trip trip = tripService.getTripByReferenceNumber(tripApiJson.getReferenceNumber());

        if (trip == null)
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "");

        Account account = accountService.getAccountById(trip.getAccountId());

        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        TripJson tripJson = new TripJson();
        tripJson.setActionType(ServerConstants.ACTION_TYPE_UPDATE_TRIP_API);
        tripJson.setCode(trip.getCode());

        if (StringUtils.isNotBlank(tripApiJson.getContainerId())) {

            if (!tripApiJson.getContainerId().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
                throw new PADException(ServerResponseConstants.INVALID_CONTAINER_ID_CODE, ServerResponseConstants.INVALID_CONTAINER_ID_TEXT, "invalid container id");

            if (tripApiJson.getContainerId().length() < ServerConstants.CONTAINER_ID_VALIDATION_LENGTH_MIN
                || tripApiJson.getContainerId().length() > ServerConstants.CONTAINER_ID_VALIDATION_LENGTH_MAX)
                throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                    "invalid container id lenght");

            tripJson.setContainerId(tripApiJson.getContainerId());
        }

        if (StringUtils.isNotBlank(tripApiJson.getContainerType())) {

            if (!tripApiJson.getTransactionType().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
                throw new PADException(ServerResponseConstants.INVALID_TRANSACTION_TYPE_CODE, ServerResponseConstants.INVALID_TRANSACTION_TYPE_TEXT, "invalid transaction type");

            if (tripApiJson.getContainerType().length() < ServerConstants.CONTAINER_TYPE_VALIDATION_LENGTH_MIN
                || tripApiJson.getContainerType().length() > ServerConstants.CONTAINER_TYPE_VALIDATION_LENGTH_MAX)
                throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                    "invalid container id lenght");

            tripJson.setContainerType(tripApiJson.getContainerType());
        }

        if (StringUtils.isNotBlank(tripApiJson.getDateSlotFrom())) {
            tripJson.setDateMissionStartString(tripApiJson.getDateSlotFrom());
            tripJson.setDateMissionEndString(tripApiJson.getDateSlotTo());
        }

        if (StringUtils.isNotBlank(tripApiJson.getVehicleRegNumber())) {

            if (!tripApiJson.getVehicleRegNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
                throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                    "invalid vehicle reg number");

            if (tripApiJson.getVehicleRegNumber().length() < ServerConstants.REGNUMBER_VALIDATION_LENGTH_MIN
                || tripApiJson.getVehicleRegNumber().length() > ServerConstants.REGNUMBER_VALIDATION_LENGTH_MAX)
                throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                    "invalid vehicle reg number lenght");

            tripApiJson.setVehicleRegNumber(ServerUtil.formatVehicleRegNumber(tripApiJson.getVehicleRegNumber()));

            Vehicle vehicle = vehicleService.getVehicleByAccountIdAndRegNumber(account.getId(), tripApiJson.getVehicleRegNumber());

            if (vehicle == null) {
                String vehicleCode = addVehicle(account, tripApiJson.getVehicleRegNumber(), operator.getId());
                tripJson.setVehicleCode(vehicleCode);
            } else {
                tripJson.setVehicleCode(vehicle.getCode());
            }
        }

        tripService.updateTrip(tripJson, operator.getId());

        HashMap<String, Object> params = new HashMap<>();
        params.put("referenceLabel", account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID ? "BAD / Réservation Export" : "BAD / Booking Export");
        params.put("accountName", account.getFirstName());
        params.put("portOperator", systemService.getPortOperatorNameById(trip.getPortOperatorId()));
        params.put("referenceNumber", trip.getReferenceNumber());
        params.put("transactionType", ServerUtil.getTransactionTypeName(trip.getMission().getTransactionType(), account.getLanguageId()));
        params.put("startDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getMission().getDateMissionStart()));
        params.put("endDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getMission().getDateMissionEnd()));
        params.put("loginPageUrl", systemUrl + loginTransporterUrl);

        sendNotification(account, params, ServerConstants.EMAIL_MISSION_TRIP_UPDATED_API_NOTIFICATION_TEMPLATE_TYPE,
            ServerConstants.SMS_MISSION_TRIP_UPDATED_API_NOTIFICATION_TEMPLATE_TYPE);

        return new TripApiResponseJson(trip.getCode(), trip.getReferenceNumber());
    }

    @Override
    public TripApiResponseJson cancelTripApi(TripApiJson tripApiJson, Operator operator) throws PADException, Exception {

        Trip trip = tripService.getTripByReferenceNumber(tripApiJson.getReferenceNumber());

        if (trip == null)
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "");

        tripService.cancelTrip(trip.getCode(), operator);

        return new TripApiResponseJson(trip.getCode(), trip.getReferenceNumber());
    }

    private void sendNotification(Account account, HashMap<String, Object> params, long emailType, long smsType) throws PADException {

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
            // email transporter

            Email scheduledEmail = new Email();
            scheduledEmail.setEmailTo(transporter.getUsername());
            scheduledEmail.setLanguageId(account.getLanguageId());
            scheduledEmail.setAccountId(account.getId());
            scheduledEmail.setMissionId(ServerConstants.DEFAULT_LONG);
            scheduledEmail.setTripId(ServerConstants.DEFAULT_LONG);

            emailService.scheduleEmailByType(scheduledEmail, emailType, params);

        } else {
            // send sms to individual transporter
            Sms scheduledSms = new Sms();
            scheduledSms.setLanguageId(account.getLanguageId());
            scheduledSms.setAccountId(account.getId());
            scheduledSms.setMissionId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setTripId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setMsisdn(transporter.getUsername());

            smsService.scheduleSmsByType(scheduledSms, smsType, params);
        }
    }

    private String addVehicle(Account account, String regNumber, long operatorId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCode(SecurityUtil.generateUniqueCode());
        vehicle.setAccount(account);
        vehicle.setRegistrationCountryISO(ServerConstants.DEFAULT_STRING);
        vehicle.setVehicleRegistration(regNumber);
        vehicle.setMake(ServerConstants.DEFAULT_STRING);
        vehicle.setColor(ServerConstants.DEFAULT_STRING);
        vehicle.setOperatorId(operatorId);
        vehicle.setIsAddedApi(true);
        vehicle.setIsApproved(false);
        vehicle.setIsActive(false);
        vehicle.setDateCreated(new Date());
        vehicle.setDateEdited(vehicle.getDateCreated());

        vehicleService.saveVehicle(vehicle);

        return vehicle.getCode();
    }

    @Override
    public PortOperatorTripsApi saveRequest(TripApiJson tripApiJson, String type) {

        PortOperatorTripsApi portOperatorTripsApi = new PortOperatorTripsApi();

        try {

            portOperatorTripsApi.setType(type.toUpperCase());
            portOperatorTripsApi.setReferenceNumber(tripApiJson.getReferenceNumber() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getReferenceNumber());
            portOperatorTripsApi.setTransactionType(tripApiJson.getTransactionType() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getTransactionType());
            portOperatorTripsApi.setTransporterShortName(tripApiJson.getTransporterShortName() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getTransporterShortName());
            portOperatorTripsApi.setVehicleRegNumber(tripApiJson.getVehicleRegNumber() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getVehicleRegNumber());
            portOperatorTripsApi.setContainerId(tripApiJson.getContainerId() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getContainerId());
            portOperatorTripsApi.setContainerType(tripApiJson.getContainerType() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getContainerType());
            portOperatorTripsApi.setDateSlotFrom(tripApiJson.getDateSlotFrom() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getDateSlotFrom());
            portOperatorTripsApi.setDateSlotTo(tripApiJson.getDateSlotTo() == null ? ServerConstants.DEFAULT_STRING : tripApiJson.getDateSlotTo());
            portOperatorTripsApi.setDateRequest(new Date());
            portOperatorTripsApi.setTripId(ServerConstants.DEFAULT_LONG);
            portOperatorTripsApi.setTripCode(ServerConstants.DEFAULT_STRING);
            portOperatorTripsApi.setResponseCode(ServerConstants.DEFAULT_INT);
            portOperatorTripsApi.setResponseText(ServerConstants.DEFAULT_STRING);
            portOperatorTripsApi.setDateResponse(null);

            hibernateTemplate.save(portOperatorTripsApi);

        } catch (Exception e) {
            logger.error("saveRequest###Exception: ", e);
        }

        return portOperatorTripsApi;
    }

    @Override
    public void saveResponse(long portOperatorTripsApiId, int responseCode, String responseText, String tripCode) {

        try {
            PortOperatorTripsApi portOperatorTripsApi = hibernateTemplate.get(PortOperatorTripsApi.class, portOperatorTripsApiId);

            Trip trip = tripService.getTripByCode(tripCode);

            portOperatorTripsApi.setTripId(trip == null ? ServerConstants.DEFAULT_LONG : trip.getId());
            portOperatorTripsApi.setTripCode(tripCode);
            portOperatorTripsApi.setResponseCode(responseCode);
            portOperatorTripsApi.setResponseText(responseText);
            portOperatorTripsApi.setDateResponse(new Date());

            hibernateTemplate.update(portOperatorTripsApi);

        } catch (Exception e) {
            logger.error("saveResponse###Exception: ", e);
        }
    }

}
