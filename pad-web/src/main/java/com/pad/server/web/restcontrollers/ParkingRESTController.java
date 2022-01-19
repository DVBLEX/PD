package com.pad.server.web.restcontrollers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Session;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.ParkingJson;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.web.services.padanpr.PadAnprService;

@RestController
@RequestMapping("/parking")
public class ParkingRESTController {

    @Autowired
    private AnprBaseService    anprBaseService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private LaneService        laneService;

    @Autowired
    private OperatorService    operatorService;

    @Autowired
    private PadAnprService     padAnprService;

    @Autowired
    private ParkingService     parkingService;

    @Autowired
    private PortAccessService  portAccessService;

    @Autowired
    private SessionService     sessionService;

    @Autowired
    private SmsService         smsService;

    @Autowired
    private SystemService      systemService;

    @Autowired
    private TripService        tripService;
    
    @Autowired
    private HttpSession        session;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getParkingCount(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Date dateEntryFrom = null;
        Date dateEntryTo = null;

        String timeEntryFrom = "00:00";
        String timeEntryTo = "00:00";

        if (!StringUtils.isBlank(parkingJson.getTimeEntryFromString())) {
            timeEntryFrom = parkingJson.getTimeEntryFromString();
        }

        if (!StringUtils.isBlank(parkingJson.getTimeEntryToString())) {
            timeEntryTo = parkingJson.getTimeEntryToString();
        }

        if (!StringUtils.isBlank(parkingJson.getDateEntryFromString())) {
            try {
                dateEntryFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parkingJson.getDateEntryFromString() + " " + timeEntryFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(parkingJson.getDateEntryToString())) {
            try {
                dateEntryTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parkingJson.getDateEntryToString() + " " + timeEntryTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        long count = parkingService.getParkingCount(parkingJson.getPortOperator(), parkingJson.getTransactionType(), parkingJson.getVehicleRegistration(), parkingJson.getType(),
            parkingJson.getStatus(), parkingJson.getIsInTransit(), dateEntryFrom, dateEntryTo);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getParkingList(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Date dateEntryFrom = null;
        Date dateEntryTo = null;

        String timeEntryFrom = "00:00";
        String timeEntryTo = "00:00";

        if (!StringUtils.isBlank(parkingJson.getTimeEntryFromString())) {
            timeEntryFrom = parkingJson.getTimeEntryFromString();
        }

        if (!StringUtils.isBlank(parkingJson.getTimeEntryToString())) {
            timeEntryTo = parkingJson.getTimeEntryToString();
        }

        if (!StringUtils.isBlank(parkingJson.getDateEntryFromString())) {
            try {
                dateEntryFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parkingJson.getDateEntryFromString() + " " + timeEntryFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(parkingJson.getDateEntryToString())) {
            try {
                dateEntryTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parkingJson.getDateEntryToString() + " " + timeEntryTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        List<ParkingJson> parkings = parkingService.getParkingList(parkingJson.getPortOperator(), parkingJson.getTransactionType(), parkingJson.getVehicleRegistration(),
            parkingJson.getType(), parkingJson.getStatus(), parkingJson.getIsInTransit(), dateEntryFrom, dateEntryTo, parkingJson.getSortColumn(), parkingJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(parkingJson.getCurrentPage(), parkingJson.getPageCount()), parkingJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(parkings);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/export", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InputStreamResource> exportParkingList(HttpServletResponse response, @FormParam("portOperator") final Integer portOperator,
        @FormParam("transactionType") final Integer transactionType, @FormParam("vehicleRegistration") final String vehicleRegistration, @FormParam("type") final Integer type,
        @FormParam("status") final Integer status, @FormParam("isInTransit") final Boolean isInTransit, @FormParam("dateEntryFromString") final String dateEntryFromString,
        @FormParam("dateEntryToString") final String dateEntryToString, @FormParam("timeEntryFromString") final String timeEntryFromString,
        @FormParam("timeEntryToString") final String timeEntryToString, @FormParam("sortColumn") final String sortColumn, @FormParam("sortAsc") final boolean sortAsc,
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

        List<ParkingJson> parkings = parkingService.getParkingList(portOperator, transactionType, vehicleRegistration, type, status, isInTransit, dateEntryFrom, dateEntryTo,
            sortColumn, sortAsc, ServerUtil.getStartLimitPagination(currentPage, pageCount), pageCount);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        parkingService.exportParkingList(parkings, byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("", "AGS_PAD_PARKING_SESSIONS_" + ServerUtil.formatDate(ServerConstants.EXPORT_yyyyMMddHHmmss, new Date()) + ".xlsx");

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/portoperator/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getParkingCountByPortOperator(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        long countDPWorldVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_DPWORLD);
        long countTVSVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_TVS);
        long countDakarTerminalVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_DAKAR_TERMINAL);
        long countVivoEnergyVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_VIVO_ENERGY);
        long countSenstockVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_SENSTOCK);
        long countOryxVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_ORYX);
        long countEresVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_ERES);
        long countTMNorthVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_TM_NORTH);
        long countTMSouthVehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_TM_SOUTH);
        long countMole10Vehicles = parkingService.getParkingCountByPortOperator(ServerConstants.PORT_OPERATOR_MOLE_10);

        long countVehicleExitOnly = parkingService.getVehicleExitOnlyCount();
        long countVehicleInTransit = parkingService.getVehicleInTransitCount();

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
        countsList.add(countVehicleExitOnly);
        countsList.add(countVehicleInTransit);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(countsList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/entered/vehicle/registration/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity findEnteredVehicleRegistration(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<ParkingJson> parkingJsonList = new ArrayList<>();

        ParkingJson parkingJsonSession = parkingService.getParkingSessionByVehicleReg(parkingJson.getVehicleRegistration());

        if (parkingJsonSession != null) {
            parkingJsonList.add(parkingJsonSession);
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(parkingJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/exit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity vehicleExit(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        // TODO get laneId and pass to processVehicleExit

        parkingService.processVehicleExit(null, parkingJson.getCode(), ServerConstants.DEFAULT_LONG, parkingJson.getVehicleRegistration(), loggedOperator.getId(),
            ServerConstants.DEFAULT_LONG, new Date());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/entered/vehicle/registration/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getEnteredVehicleRegistrationList(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<String> vehicleRegistrationList = parkingService.getEnteredVehicleRegistrationList();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicleRegistrationList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used to show list of vehicles allowed port entry at port entry login
    @RequestMapping(value = "/exited/vehicle/registration/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getExitedVehicleRegistrationList(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        // fetch all trips that have exited the parking area after being authorised in the last x number of hours
        List<String> vehicleRegistrationList = parkingService.getExitedVehicleRegistrationList();

        // search all approved trips that are on direct to port missions
        List<String> vehicleRegistrationListFromApprovedDirectToPortTrips = tripService.getVehicleRegistrationListFromApprovedDirectToPortTrips();

        if (vehicleRegistrationListFromApprovedDirectToPortTrips != null && !vehicleRegistrationListFromApprovedDirectToPortTrips.isEmpty()) {

            for (String vehicleReg : vehicleRegistrationListFromApprovedDirectToPortTrips) {

                vehicleRegistrationList.add(vehicleReg);
            }
        }

        vehicleRegistrationList.removeAll(portAccessService.getEnteredVehicleRegistrationList());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicleRegistrationList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used from port entry login to get trip details for vehicle reg eligible for port entry
    @RequestMapping(value = "/exited/vehicle/registration/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity findExitedVehicleRegistration(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<ParkingJson> parkingJsonList = parkingService.findExitedVehicleByRegNumber(ServerUtil.formatVehicleRegNumber(parkingJson.getVehicleRegistration()));
        
        Object zoneAttribute = session.getAttribute(ServerConstants.SESSION_ATTRIBUTE_SELECTED_ZONE);
        String selectedZone = zoneAttribute instanceof String ? (String) zoneAttribute : "";
        Long gateId = null;
        
        if (!parkingJsonList.isEmpty()) {
            gateId = parkingJsonList.get(0).getGateId();
        }
        
        SystemParameter systemParameter = systemService.getSystemParameter();
        
        if (Boolean.TRUE.equals(systemParameter.getIsPortEntryFiltering())
            && Boolean.FALSE.equals(parkingJson.getIsPortEntryReadOnly())
            && !portAccessService.isPortEntryAllowedInZone(selectedZone, gateId)) {
            throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE, ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT, "");
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(parkingJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveParking(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(parkingJson.getTripCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        String tripCode = parkingService.processVehicleEntry(parkingJson.getTripCode(), loggedOperator.getId(), parkingJson.getEntryLaneId(), new Date());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setData(tripCode);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/exitonly", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveParkingExitOnly(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        Session session = null;
        Trip trip = null;
        Trip tripTmp = null;
        PortAccessWhitelistJson portAccessWhitelistJson = null;

        Date dateToday = new Date();

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(parkingJson.getVehicleRegistration()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (parkingJson.getVehicleRegistration().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_32)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        if (StringUtils.isBlank(parkingJson.getDriverMobile()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        session = sessionService.getLastSessionByKioskOperatorId(loggedOperator.getId());

        if (session == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

        String vehicleRegNumber = ServerUtil.formatVehicleRegNumber(parkingJson.getVehicleRegistration());
        String driverMsisdn = ServerUtil.getValidNumber(parkingJson.getDriverMobile(), "Parking#saveParkingExitOnly#");

        // check if vehicle exists on the system. if it does, check if its assigned on any approved trip. If it is and the trip is not direct to port, deny this request
        List<Trip> tripList = tripService.getTripsByVehicleRegNumberAndStatus(vehicleRegNumber, ServerConstants.TRIP_STATUS_APPROVED, ServerConstants.DEFAULT_INT);

        if (tripList != null && tripList.size() > 0) {
            for (Trip t : tripList) {

                if (t.getIsDirectToPort()) {
                    continue;

                } else
                    throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ASSOCIATED_WITH_TRIP_CODE,
                        ServerResponseConstants.VEHICLE_ALREADY_ASSOCIATED_WITH_TRIP_TEXT, "");
            }
        }

        Lane lane = laneService.getLaneByLaneId(parkingJson.getEntryLaneId());

        // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
        if (lane == null && parkingJson.getEntryLaneId() > ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#4");

        // check if vehicle already entered the parking area
        if (parkingService.getParkingByVehicleRegistrationAndStatus(vehicleRegNumber, ServerConstants.PARKING_STATUS_ENTRY) != null)
            throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PARKING_AREA_CODE, ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PARKING_AREA_TEXT,
                "");

        if (StringUtils.isNotBlank(parkingJson.getTripCode())) {
            // trip code is available, that means there exists a trip with status "approved" or "in transit"
            trip = tripService.getTripByCode(parkingJson.getTripCode());

        } else {
            // no trip code, kiosk operator created the exit only session manually
            // check if theres a port entry whitelist for this vehicle. if there is then disable the corresponding port entry ANPR permission
            portAccessWhitelistJson = portAccessService.getVehicleWhitelisted(vehicleRegNumber);
        }

        Parking parking = new Parking();
        parking.setCode(SecurityUtil.generateUniqueCode());
        parking.setType(ServerConstants.PARKING_TYPE_EXIT_ONLY);

        if (trip == null && portAccessWhitelistJson == null) {
            parking.setStatus(ServerConstants.PARKING_STATUS_ENTRY);

        } else {
            parking.setStatus(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);
        }

        parking.setIsEligiblePortEntry(false);
        parking.setTripId(trip == null ? ServerConstants.DEFAULT_LONG : trip.getId());
        parking.setMissionId(ServerConstants.DEFAULT_LONG);
        parking.setPortAccessWhitelistId(portAccessWhitelistJson == null ? ServerConstants.DEFAULT_LONG : portAccessWhitelistJson.getId());
        parking.setVehicleId(ServerConstants.DEFAULT_LONG);
        parking.setDriverId(ServerConstants.DEFAULT_LONG);
        parking.setPortOperatorId(ServerConstants.DEFAULT_LONG);
        parking.setPortOperatorGateId(ServerConstants.DEFAULT_LONG);
        parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_NORMAL);
        parking.setVehicleRegistration(vehicleRegNumber);
        parking.setVehicleColor(StringUtils.isBlank(parkingJson.getVehicleColor()) ? "" : parkingJson.getVehicleColor());
        parking.setDriverMsisdn(driverMsisdn);
        parking.setOperatorId(loggedOperator.getId());
        parking.setEntryLaneId(parkingJson.getEntryLaneId());
        parking.setEntryLaneNumber(lane.getLaneNumber());
        parking.setExitLaneId(ServerConstants.DEFAULT_LONG);
        parking.setExitLaneNumber(ServerConstants.DEFAULT_INT);
        parking.setDateEntry(dateToday);
        parking.setDateCreated(dateToday);
        parking.setDateEdited(parking.getDateCreated());

        parkingService.saveParking(parking);

        AnprParameter anprParameter = anprBaseService.getAnprParameter();

        if (trip == null) {

            Mission missionTmp = new Mission();
            missionTmp.setId(parking.getMissionId());

            tripTmp = new Trip();
            tripTmp.setId(parking.getTripId());
            tripTmp.setMission(missionTmp);
            tripTmp.setVehicleRegistration(parking.getVehicleRegistration());
            tripTmp.setDateSlotApproved(dateToday);

            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT, anprParameter.getAnprZoneIdAgsparking(), tripTmp, null, null,
                dateToday);

        } else {
            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT, anprParameter.getAnprZoneIdAgsparking(), trip, null, null,
                dateToday);
        }

        if (trip != null) {

            if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG && trip.getIsDirectToPort()) {
                // temporarily, update parking permission status to disabled for vehicle at port area
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED, ServerConstants.DEFAULT_LONG, trip, null,
                    null, dateToday);

            } else if (trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT && trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                // temporarily, update parking permission status to disabled for vehicle at port area
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED, ServerConstants.DEFAULT_LONG, trip, null,
                    null, dateToday);
            }

            if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT) {

                trip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PARKING);
                trip.setParkingEntryCount(trip.getParkingEntryCount() + 1);

                if (trip.getDateEntryParking() == null) {
                    trip.setDateEntryParking(dateToday);
                }

                tripService.updateTrip(trip);
            }

        } else if (portAccessWhitelistJson != null) {

            tripTmp.setParkingPermissionIdPortEntry(portAccessWhitelistJson.getParkingPermissionId());

            // temporarily, update parking permission status to disabled for vehicle at port area
            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED, ServerConstants.DEFAULT_LONG, tripTmp, null,
                null, dateToday);
        }

        session.setExitOnlySessionCount(session.getExitOnlySessionCount() + 1);

        sessionService.updateSession(session);

        activityLogService.saveActivityLogParking(ServerConstants.ACTIVITY_LOG_PARKING_EXIT_ONLY, loggedOperator.getId(), parking.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/send/exit/sms", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity sendExitSms(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (parkingJson.getExitParkingCodes() == null || parkingJson.getExitParkingCodes().length == 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "sendExitSms#1");

        if (parkingJson.getAddSecondsSchedule() < 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "sendExitSms#2");

        for (String code : parkingJson.getExitParkingCodes()) {

            Parking parking = parkingService.getParkingByCode(code);

            if (parking == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "sendExitSms#5");

            Trip trip = tripService.getTripById(parking.getTripId());

            if (trip == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

            if (trip.getStatus() != ServerConstants.TRIP_STATUS_ENTERED_PARKING)
                throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_EXITED_PARKING_AREA_CODE, ServerResponseConstants.VEHICLE_ALREADY_EXITED_PARKING_AREA_TEXT,
                    "");

            if (!smsService.isParkingExitSmsEligibleToSend(parking.getTripId()))
                throw new PADValidationException(ServerResponseConstants.PARKING_EXIT_SMS_WAS_ALREADY_SENT_CODE, ServerResponseConstants.PARKING_EXIT_SMS_WAS_ALREADY_SENT_TEXT,
                    "");

            parkingService.sendExitParkingSms(trip, parking, parkingJson.getAddSecondsSchedule());

            activityLogService.saveActivityLogParking(ServerConstants.ACTIVITY_LOG_PARKING_EXIT_SMS_SEND, loggedOperator.getId(), parking.getId());
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/toggle/auto/release", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity toggleAutoRelease(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (parkingJson.getPortOperator() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "toggleAutoRelease#1");

        if (parkingJson.getTransactionType() == ServerConstants.DEFAULT_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "toggleAutoRelease#2");

        PortOperator portOperator = systemService.getPortOperatorFromMap(parkingJson.getPortOperator());

        if (portOperator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "toggleAutoRelease#3");

        for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

            if (portOperatorTransactionType.getTransactionType() == parkingJson.getTransactionType()) {

                portOperatorTransactionType.setIsAutoReleaseParking(parkingJson.getIsAutoReleaseOn());

                PortOperatorTransactionTypeJson portOperatorTransactionTypeJson = new PortOperatorTransactionTypeJson(parkingJson.getTransactionType(),
                    parkingJson.getPortOperator(), portOperatorTransactionType.getIsAllowedForParkingAndKioskOp(), portOperatorTransactionType.getIsAllowedForVirtualKioskOp(),
                    portOperatorTransactionType.getIsAutoReleaseParking(), portOperatorTransactionType.getIsDirectToPort(), portOperatorTransactionType.getIsAllowMultipleEntries(),
                    portOperatorTransactionType.getIsTripCancelSystem(), portOperatorTransactionType.getMissionCancelSystemAfterMinutes(), portOperatorTransactionType.getTripCancelSystemAfterMinutes(),
                    portOperatorTransactionType.getPortOperatorGate() == null ? null : portOperatorTransactionType.getPortOperatorGate().getId());

                padAnprService.updateTransactionTypeFlagPadanpr(portOperatorTransactionTypeJson);

                systemService.updatePortOperatorTransactionType(portOperatorTransactionType);

                activityLogService.saveActivityLogParkingAutoRelease(ServerConstants.ACTIVITY_LOG_TOGGLE_PARKING_AUTO_RELEASE, loggedOperator.getId(), portOperator.getId(),
                    portOperatorTransactionType.getTransactionType(), portOperatorTransactionType.getIsAutoReleaseParking());

                break;
            }
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/trigger/manual/release", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity triggerManualRelease(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (parkingJson.getPortOperator() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "triggerManualRelease#1");

        if (parkingJson.getTransactionType() == ServerConstants.DEFAULT_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "triggerManualRelease#2");

        if (parkingJson.getReleaseCount() == null || parkingJson.getReleaseCount() < 1 || parkingJson.getReleaseCount() > 20)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "triggerManualRelease#3");

        if (parkingJson.getVehiclesAlreadyReleasedCount() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "triggerManualRelease#4");

        if (parkingJson.getBookingLimitCount() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "triggerManualRelease#5");

        int vehicleReleaseLimit = (int) Math.ceil(parkingJson.getBookingLimitCount() / 4f);

        if (parkingJson.getVehiclesAlreadyReleasedCount() > (int) Math.ceil(vehicleReleaseLimit / 2f))
            throw new PADValidationException(ServerResponseConstants.MAX_AMOUNT_VEHICLE_RELEASE_REACHED_CODE, ServerResponseConstants.MAX_AMOUNT_VEHICLE_RELEASE_REACHED_TEXT,
                "triggerManualRelease#6");

        PortOperator portOperator = systemService.getPortOperatorFromMap(parkingJson.getPortOperator());

        if (portOperator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "triggerManualRelease#7");

        int releaseCount = 0;

        final List<Parking> parkingList = parkingService.getActiveParkingList(parkingJson.getPortOperator(), parkingJson.getTransactionType(), false);

        for (Parking parking : parkingList) {

            if (parking.getStatus() == ServerConstants.PARKING_STATUS_ENTRY && releaseCount < parkingJson.getReleaseCount()) {

                Trip trip = tripService.getTripById(parking.getTripId());

                parkingService.sendExitParkingSms(trip, parking, 0);

                releaseCount++;
            }
        }

        activityLogService.saveActivityLogParkingManualRelease(ServerConstants.ACTIVITY_LOG_TRIGGER_PARKING_MANUAL_RELEASE, loggedOperator.getId(), portOperator.getId(),
            parkingJson.getTransactionType(), parkingJson.getReleaseCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setData(releaseCount);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/parkingsupervisor/readonly/flag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateParkingSupervisorReadOnlyFlag(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (parkingJson.getIsParkingSupervisorReadOnlyEnabled() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "updateParkingSupervisorReadOnlyFlag#1");

        SystemParameter systemParameter = systemService.getSystemParameter();
        systemParameter.setIsParkingSupervisorReadonlyEnabled(parkingJson.getIsParkingSupervisorReadOnlyEnabled());

        systemService.updateSystemParameter(systemParameter);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setData(systemParameter.getIsBookingLimitCheckEnabled());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/vehicle/state", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateVehicleState(HttpServletResponse response, @RequestBody ParkingJson parkingJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(parkingJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateVehicleState#1");

        if (parkingJson.getVehicleState() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateVehicleState#2");

        Parking parking = parkingService.getParkingByCode(parkingJson.getCode());
        if (parking == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "parking is null");

        switch (parkingJson.getVehicleState()) {

            case ServerConstants.VEHICLE_PARKING_STATE_BROKEN_DOWN:
                parking.setStatus(ServerConstants.PARKING_STATUS_ENTRY);
                parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_BROKEN_DOWN);
                break;

            case ServerConstants.VEHICLE_PARKING_STATE_CLAMPED:
                parking.setStatus(ServerConstants.PARKING_STATUS_ENTRY);
                parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_CLAMPED);
                break;

            case ServerConstants.VEHICLE_PARKING_STATE_UNRESPONSIVE:
                parking.setStatus(ServerConstants.PARKING_STATUS_ENTRY);
                parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_UNRESPONSIVE);
                break;

            default:
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateVehicleState#3");
        }

        parking.setDateEdited(new Date());
        parkingService.updateParking(parking);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
