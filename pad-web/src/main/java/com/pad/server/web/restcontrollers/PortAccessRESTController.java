package com.pad.server.web.restcontrollers;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortAccess;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.PortAccessJson;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@RestController
@RequestMapping("/port/access")
public class PortAccessRESTController {

    @Autowired
    private OperatorService    operatorService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private AnprBaseService    anprBaseService;

    @Autowired
    private SystemService      systemService;

    @Autowired
    private TripService        tripService;

    @Autowired
    private ParkingService     parkingService;

    @Autowired
    private PortAccessService  portAccessService;

    @Autowired
    private HttpSession        session;

    @RequestMapping(value = "/entry/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPortEntryCount(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Date dateEntryFrom = null;
        Date dateEntryTo = null;

        String timeEntryFrom = "00:00";
        String timeEntryTo = "00:00";

        if (!StringUtils.isBlank(portAccessJson.getTimeEntryFromString())) {
            timeEntryFrom = portAccessJson.getTimeEntryFromString();
        }

        if (!StringUtils.isBlank(portAccessJson.getTimeEntryToString())) {
            timeEntryTo = portAccessJson.getTimeEntryToString();
        }

        if (!StringUtils.isBlank(portAccessJson.getDateEntryFromString())) {
            try {
                dateEntryFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portAccessJson.getDateEntryFromString() + " " + timeEntryFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(portAccessJson.getDateEntryToString())) {
            try {
                dateEntryTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portAccessJson.getDateEntryToString() + " " + timeEntryTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        long count = portAccessService.getPortEntryCount(portAccessJson.getPortOperator(), portAccessJson.getVehicleRegistration(), portAccessJson.getReferenceNumber(),
            portAccessJson.getTransactionType(), portAccessJson.getStatus(), dateEntryFrom, dateEntryTo);

        List<Long> res = new ArrayList<>();
        res.add(count);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/entry/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPortEntryList(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Date dateEntryFrom = null;
        Date dateEntryTo = null;

        String timeEntryFrom = "00:00";
        String timeEntryTo = "00:00";

        if (!StringUtils.isBlank(portAccessJson.getTimeEntryFromString())) {
            timeEntryFrom = portAccessJson.getTimeEntryFromString();
        }

        if (!StringUtils.isBlank(portAccessJson.getTimeEntryToString())) {
            timeEntryTo = portAccessJson.getTimeEntryToString();
        }

        if (!StringUtils.isBlank(portAccessJson.getDateEntryFromString())) {
            try {
                dateEntryFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portAccessJson.getDateEntryFromString() + " " + timeEntryFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(portAccessJson.getDateEntryToString())) {
            try {
                dateEntryTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portAccessJson.getDateEntryToString() + " " + timeEntryTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        List<PortAccessJson> portAccessList = portAccessService.getPortEntryList(portAccessJson.getPortOperator(), portAccessJson.getVehicleRegistration(),
            portAccessJson.getReferenceNumber(), portAccessJson.getTransactionType(), portAccessJson.getStatus(), dateEntryFrom, dateEntryTo, portAccessJson.getSortColumn(),
            portAccessJson.getSortAsc(), ServerUtil.getStartLimitPagination(portAccessJson.getCurrentPage(), portAccessJson.getPageCount()), portAccessJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(portAccessList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/entry/export", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InputStreamResource> exportEntryList(HttpServletResponse response, @FormParam("portOperator") final Integer portOperator,
        @FormParam("transactionType") final Integer transactionType, @FormParam("vehicleRegistration") final String vehicleRegistration,
        @FormParam("referenceNumber") final String referenceNumber, @FormParam("status") final Integer status,
        @FormParam("dateEntryFromString") final String dateEntryFromString, @FormParam("dateEntryToString") final String dateEntryToString,
        @FormParam("timeEntryFromString") final String timeEntryFromString, @FormParam("timeEntryToString") final String timeEntryToString,
        @FormParam("sortColumn") final String sortColumn, @FormParam("sortAsc") final boolean sortAsc,
        @FormParam("currentPage") final Integer currentPage, @FormParam("pageCount") final Integer pageCount) throws PADException, Exception {

        Date dateEntryFrom = null;
        Date dateEntryTo = null;

        String timeEntryFrom = "00:00";
        String timeEntryTo = "00:00";

        if (!StringUtils.isBlank(timeEntryFromString)) {
            timeEntryFrom = timeEntryFromString;
        }

        if (!StringUtils.isBlank(timeEntryToString)) {
            timeEntryTo = timeEntryToString;
        }

        if (!StringUtils.isBlank(dateEntryFromString)) {
            try {
                dateEntryFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, dateEntryFromString + " " + timeEntryFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(dateEntryToString)) {
            try {
                dateEntryTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, dateEntryToString + " " + timeEntryTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        List<PortAccessJson> portAccessList = portAccessService.getPortEntryList(portOperator, vehicleRegistration, referenceNumber, transactionType, status,
            dateEntryFrom, dateEntryTo, sortColumn, sortAsc, ServerUtil.getStartLimitPagination(currentPage, pageCount), pageCount);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        portAccessService.exportPortEntryList(portAccessList, byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("", "AGS_PAD_PORT_ENTRY_LIST_" + ServerUtil.formatDate(ServerConstants.EXPORT_yyyyMMddHHmmss, new Date()) + ".xlsx");

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/entry/portoperator/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPortEntryCountByPortOperator(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        long countDPWorldVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_DPWORLD);
        long countTVSVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_TVS);
        long countDakarTerminalVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_DAKAR_TERMINAL);
        long countVivoEnergyVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_VIVO_ENERGY);
        long countSenstockVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_SENSTOCK);
        long countOryxVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_ORYX);
        long countEresVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_ERES);
        long countTMNorthVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_TM_NORTH);
        long countTMSouthVehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_TM_SOUTH);
        long countMole10Vehicles = portAccessService.getPortEntryCountByPortOperator(ServerConstants.PORT_OPERATOR_MOLE_10);

        List<Long> countsList = new ArrayList<>();
        countsList.add(countDPWorldVehicles);
        countsList.add(countTVSVehicles);
        countsList.add(countDakarTerminalVehicles);
        countsList.add(countVivoEnergyVehicles);
        countsList.add(countSenstockVehicles);
        countsList.add(countOryxVehicles);
        countsList.add(countEresVehicles);
        countsList.add(countTMNorthVehicles);
        countsList.add(countTMSouthVehicles);
        countsList.add(countMole10Vehicles);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(countsList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/entry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity vehiclePortEntry(HttpServletResponse response, @FormParam("tripCode") String tripCode, @FormParam("parkingCode") String parkingCode,
        @FormParam("transactionType") int transactionType, @FormParam("selectedZone") String selectedZone) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Trip trip = tripService.getTripByCode(tripCode);

        if (trip == null)
            throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE, ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT, "#1");

        if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && trip.getLaneSessionId() != ServerConstants.DEFAULT_LONG)
            throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE, ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT, "#2");

        // TODO get laneId and pass to processUrgentTripPortEntry / processVehiclePortEntry

        String decodedZone = java.net.URLDecoder.decode(selectedZone, StandardCharsets.UTF_8.name());
        if (trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries()) {

            portAccessService.processUrgentTripPortEntry(trip.getCode(), trip.getVehicleRegistration(), ServerConstants.DEFAULT_LONG, loggedOperator.getId(),
                ServerConstants.DEFAULT_LONG, new Date(), decodedZone);

        } else {
            portAccessService.processVehiclePortEntry(tripCode, parkingCode, loggedOperator.getId(), ServerConstants.DEFAULT_LONG, new Date(), decodedZone);
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/whitelist/entry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity vehicleWhitelistPortEntry(HttpServletResponse response, @FormParam("portOperatorId") Integer portOperatorId, @FormParam("gateId") Long gateId,
        @FormParam("vehicleRegNumber") String vehicleRegNumber, @FormParam("selectedZone") String selectedZone) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        // TODO get laneId and pass to processWhitelistedVehiclePortEntry
        String decodedZone = java.net.URLDecoder.decode(selectedZone, StandardCharsets.UTF_8.name());
        portAccessService.processWhitelistedVehiclePortEntry(vehicleRegNumber, ServerConstants.DEFAULT_LONG, portOperatorId, gateId, loggedOperator.getId(),
            ServerConstants.DEFAULT_LONG, new Date(), decodedZone);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/deny", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity vehiclePortDeny(HttpServletResponse response, @FormParam("tripCode") String tripCode, @FormParam("parkingCode") String parkingCode,
        @FormParam("reasonDeny") String reasonDeny) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        Parking parking = null;

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripCode))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (StringUtils.isBlank(reasonDeny))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        Trip trip = tripService.getTripByCode(tripCode);

        if (trip == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT)
            throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PORT_AREA_CODE, ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PORT_AREA_TEXT, "");

        if (!trip.getIsDirectToPort()) {

            if (StringUtils.isBlank(parkingCode))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

            parking = parkingService.getParkingByCode(parkingCode);

            if (parking == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#5");
        }

        // TODO get laneId and set to portAccess

        PortAccess portAccess = new PortAccess();
        portAccess.setCode(SecurityUtil.generateUniqueCode());
        portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_DENY);
        portAccess.setParkingId(parking != null ? parking.getId() : ServerConstants.DEFAULT_LONG);
        portAccess.setTripId(trip.getId());
        portAccess.setMissionId(trip.getMission() == null ? ServerConstants.DEFAULT_LONG : trip.getMission().getId());
        portAccess.setVehicleId(trip.getVehicleId());
        portAccess.setDriverId(trip.getDriverId());
        portAccess.setPortOperatorId(trip.getPortOperatorId());
        portAccess.setPortOperatorGateId(trip.getPortOperatorGateId());
        portAccess.setVehicleRegistration(trip.getVehicleRegistration());
        portAccess.setDriverMsisdn(trip.getDriverMsisdn());
        portAccess.setReasonDeny(reasonDeny);
        portAccess.setDateDeny(new Date());
        portAccess.setOperatorIdEntry(ServerConstants.DEFAULT_LONG);
        portAccess.setOperatorGate(ServerConstants.DEFAULT_STRING);
        portAccess.setOperatorId(loggedOperator.getId());
        portAccess.setEntryLaneId(ServerConstants.DEFAULT_LONG);
        portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
        portAccess.setDateEntry(null);
        portAccess.setDateExit(null);
        portAccess.setDateCreated(new Date());
        portAccess.setDateEdited(new Date());

        portAccessService.savePortAccess(portAccess);

        trip.setStatus(ServerConstants.TRIP_STATUS_DENIED_PORT_ENTRY);

        tripService.updateTrip(trip);

        if (parking != null) {
            parking.setIsEligiblePortEntry(false);

            parkingService.updateParking(parking);
        }

        activityLogService.saveActivityLogPortAccess(ServerConstants.ACTIVITY_LOG_PORT_DENY, loggedOperator.getId(), portAccess.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/exit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity vehiclePortExit(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        // TODO get laneId and pass to processVehiclePortExit

        portAccessService.processVehiclePortExit(portAccessJson.getCode(), portAccessJson.getVehicleRegistration(), loggedOperator.getId(), ServerConstants.DEFAULT_LONG,
            new Date());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/entered/vehicle/registration/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getEnteredVehicleRegistrationList(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<String> vehicleRegistrationList = portAccessService.getEnteredVehicleRegistrationList();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicleRegistrationList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/entered/vehicle/registration/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity findEnteredVehicleRegistration(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        String vehicleRegistration = ServerUtil.formatVehicleRegNumber(portAccessJson.getVehicleRegistration());

        portAccessJson = portAccessService.findEnteredVehicleByRegNumber(vehicleRegistration);

        List<PortAccessJson> portAccessJsonList = new ArrayList<>();
        portAccessJsonList.add(portAccessJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(portAccessJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/whitelist/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPortWhitelistCount(HttpServletResponse response, @RequestBody PortAccessWhitelistJson portAccessWhitelistJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Date dateFrom = null;
        Date dateTo = null;

        if (!StringUtils.isBlank(portAccessWhitelistJson.getDateFromString())) {
            try {
                dateFrom = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, portAccessWhitelistJson.getDateFromString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }

            Calendar calendarDayToday = Calendar.getInstance();
            calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
            calendarDayToday.set(Calendar.MINUTE, 0);
            calendarDayToday.set(Calendar.SECOND, 0);
            calendarDayToday.set(Calendar.MILLISECOND, 0);

            if (dateFrom.before(calendarDayToday.getTime()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
        }

        if (!StringUtils.isBlank(portAccessWhitelistJson.getDateToString())) {
            try {
                dateTo = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, portAccessWhitelistJson.getDateToString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (dateTo.before(dateFrom))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        long count = portAccessService.getWhitelistCount(portAccessWhitelistJson.getPortOperatorId(), dateFrom, dateTo, portAccessWhitelistJson.getVehicleRegistration());

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;

    }

    @RequestMapping(value = "/whitelist/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPortWhitelistList(HttpServletResponse response, @RequestBody PortAccessWhitelistJson portAccessWhitelistJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Date dateFrom = null;
        Date dateTo = null;

        if (!StringUtils.isBlank(portAccessWhitelistJson.getDateFromString())) {
            try {
                dateFrom = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, portAccessWhitelistJson.getDateFromString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }

            Calendar calendarDayToday = Calendar.getInstance();
            calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
            calendarDayToday.set(Calendar.MINUTE, 0);
            calendarDayToday.set(Calendar.SECOND, 0);
            calendarDayToday.set(Calendar.MILLISECOND, 0);

            if (dateFrom.before(calendarDayToday.getTime()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
        }

        if (!StringUtils.isBlank(portAccessWhitelistJson.getDateToString())) {
            try {
                dateTo = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, portAccessWhitelistJson.getDateToString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (dateTo.before(dateFrom))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        List<PortAccessWhitelistJson> whitelists = portAccessService.getWhitelistList(portAccessWhitelistJson.getPortOperatorId(), dateFrom, dateTo,
            portAccessWhitelistJson.getVehicleRegistration(), portAccessWhitelistJson.getSortColumn(), portAccessWhitelistJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(portAccessWhitelistJson.getCurrentPage(), portAccessWhitelistJson.getPageCount()), portAccessWhitelistJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(whitelists);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/whitelist/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity createWhitelist(HttpServletResponse response, @RequestBody PortAccessWhitelistJson portAccessWhitelistJson)
        throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Date dateFrom = null;
        Date dateTo = null;

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        StringBuilder existingVehiclesOnWhitelist = new StringBuilder();
        ArrayList<String> existingVehiclesOnWhitelistArrayList = new ArrayList<>();

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getPortOperatorId() != ServerConstants.PORT_OPERATOR_DPWORLD)
            throw new PADException(ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_TEXT, "invalid portOperatorType#1");

        if (portAccessWhitelistJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

        if (loggedOperator.getPortOperatorId() != ServerConstants.PORT_OPERATOR_DPWORLD)
            throw new PADException(ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_TEXT, "invalid portOperatorType#2");

        if (portAccessWhitelistJson.getGateId() == null)
            throw new PADException(ServerResponseConstants.MISSING_GATE_ID_CODE, ServerResponseConstants.MISSING_GATE_ID_TEXT, "");

        if (StringUtils.isBlank(portAccessWhitelistJson.getDateFromString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_FROM_CODE, ServerResponseConstants.MISSING_DATE_FROM_TEXT, "");

        if (StringUtils.isBlank(portAccessWhitelistJson.getTimeFromString()))
            throw new PADException(ServerResponseConstants.MISSING_TIME_FROM_CODE, ServerResponseConstants.MISSING_TIME_FROM_TEXT, "");

        if (StringUtils.isBlank(portAccessWhitelistJson.getDateToString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_TO_CODE, ServerResponseConstants.MISSING_DATE_TO_TEXT, "");

        if (StringUtils.isBlank(portAccessWhitelistJson.getTimeToString()))
            throw new PADException(ServerResponseConstants.MISSING_TIME_TO_CODE, ServerResponseConstants.MISSING_TIME_TO_TEXT, "");

        if (portAccessWhitelistJson.getVehicleRegistrationArray() == null || portAccessWhitelistJson.getVehicleRegistrationArray().length == 0)
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_ARRAY_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_ARRAY_TEXT, "");

        try {
            dateFrom = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyyHHmm,
                portAccessWhitelistJson.getDateFromString() + " " + portAccessWhitelistJson.getTimeFromString());
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
        }

        Calendar calendarDayToday = Calendar.getInstance();
        calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarDayToday.set(Calendar.MINUTE, 0);
        calendarDayToday.set(Calendar.SECOND, 0);
        calendarDayToday.set(Calendar.MILLISECOND, 0);

        if (dateFrom.before(calendarDayToday.getTime()))
            throw new PADValidationException(ServerResponseConstants.MISSION_DATE_IN_PAST_CODE, ServerResponseConstants.MISSION_DATE_IN_PAST_TEXT, "");

        try {
            dateTo = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyyHHmm, portAccessWhitelistJson.getDateToString() + " " + portAccessWhitelistJson.getTimeToString());
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
        }

        if (dateTo.before(dateFrom))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        for (String regNum : portAccessWhitelistJson.getVehicleRegistrationArray()) {

            if (portAccessService.isExistWhitlistForDates(dateFrom, dateTo, regNum)) {

                existingVehiclesOnWhitelist.append(regNum + ", ");
                existingVehiclesOnWhitelistArrayList.add(regNum);

                continue;

            } else {

                String vehicleRegistration = ServerUtil.formatVehicleRegNumber(regNum);

                PortAccessWhitelist whitelist = new PortAccessWhitelist();
                whitelist.setCode(SecurityUtil.generateUniqueCode());
                whitelist.setStatus(ServerConstants.PORT_ACCESS_WHITELIST_STATUS_ACTIVE);
                whitelist.setPortOperatorId(portAccessWhitelistJson.getPortOperatorId());
                whitelist.setPortOperatorGateId(portAccessWhitelistJson.getGateId());
                whitelist.setDateFrom(dateFrom);
                whitelist.setDateTo(dateTo);
                whitelist.setVehicleRegistration(vehicleRegistration);
                whitelist.setParkingPermissionId(ServerConstants.DEFAULT_LONG);
                whitelist.setOperatorId(loggedOperator.getId());
                whitelist.setDateCreated(new Date());

                portAccessService.savePortAccessWhitelist(whitelist);

                Mission missionTmp = new Mission();
                missionTmp.setId(ServerConstants.DEFAULT_LONG);
                missionTmp.setDateMissionStart(dateFrom);
                missionTmp.setDateMissionEnd(dateTo);

                Trip tripTmp = new Trip();
                tripTmp.setId(ServerConstants.DEFAULT_LONG);
                tripTmp.setMission(missionTmp);
                tripTmp.setVehicleRegistration(vehicleRegistration);

                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_WHITELISTED,
                    systemService.getPortOperatorAnprZoneIdById(whitelist.getPortOperatorGateId()), tripTmp, whitelist, null, new Date());

                activityLogService.saveActivityLogPortAccessWhitelist(ServerConstants.ACTIVITY_LOG_PORT_WHITELIST_ADD, loggedOperator.getId(), whitelist.getId());
            }
        }

        if (existingVehiclesOnWhitelistArrayList.size() == portAccessWhitelistJson.getVehicleRegistrationArray().length) {
            // all vehicles are already part of a whitelist

            apiResponse.setResponseCode(ServerResponseConstants.VEHICLE_REGISTRATION_ALREADY_WHITELISTED_CODE);
            apiResponse.setResponseText(ServerResponseConstants.VEHICLE_REGISTRATION_ALREADY_WHITELISTED_TEXT);
            apiResponse.setData("[" + existingVehiclesOnWhitelist.toString() + "]");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

            response.setStatus(HttpServletResponse.SC_OK);
        }

        return apiResponse;
    }

    @RequestMapping(value = "/whitelist/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity deleteWhitelist(HttpServletResponse response, @RequestBody PortAccessWhitelistJson portAccessWhitelistJson)
        throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (portAccessWhitelistJson.getCode() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        PortAccessWhitelist portAccessWhitelist = portAccessService.getPortAccessWhitelistByCode(portAccessWhitelistJson.getCode());

        if (portAccessWhitelist == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        portAccessService.deletePortAccessWhitelist(portAccessWhitelist);

        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, null, portAccessWhitelist, null,
            new Date(System.currentTimeMillis() + 5l * 1000l));

        activityLogService.saveActivityLogPortAccessWhitelist(ServerConstants.ACTIVITY_LOG_PORT_WHITELIST_DELETE, loggedOperator.getId(), portAccessWhitelist.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/checkLastEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity checkLastVehicleEntry(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(portAccessJson.getVehicleRegistration()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Vehicle registration is null");

        PortAccessJson lastPortAccessJson = new PortAccessJson();
        PortAccess lastPortAccess = portAccessService.getLastPortAccessByRegNumber(portAccessJson.getVehicleRegistration());
        if (lastPortAccess != null) {
            Date lastDateEntry = lastPortAccess.getDateEntry();
            LocalDateTime localDateTime = LocalDateTime.now();
            Instant dateNow = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            lastPortAccessJson.setCode(lastPortAccess.getCode());
            lastPortAccessJson.setMinutesFromLastPortEntry(MINUTES.between(lastDateEntry.toInstant(), dateNow));
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(Collections.singletonList(lastPortAccessJson));

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/entry/selectZone", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity selectPortEntryOperatorZone(HttpServletResponse response, @RequestBody PortAccessJson portAccessJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (!StringUtils.isBlank(portAccessJson.getSelectedZone())) {
            String selectedZone = portAccessJson.getSelectedZone();
            session.setAttribute(ServerConstants.SESSION_ATTRIBUTE_SELECTED_ZONE, selectedZone);
            activityLogService.saveActivityLogWithObjectJson(ServerConstants.ACTIVITY_LOG_PORT_ENTRY_ZONE_SELECTED,
                loggedOperator.getId(), loggedOperator.getAccountId(),selectedZone);
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
