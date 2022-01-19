package com.pad.server.web.restcontrollers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Session;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.SessionJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.session.SessionService;

@RestController
@RequestMapping("/session")
public class SessionRESTController {

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private LaneService        laneService;

    @Autowired
    private OperatorService    operatorService;

    @Autowired
    private SessionService     sessionService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getSessionCount(HttpServletResponse response, @RequestBody SessionJson sessionJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        long count = sessionService.getSessionCount(sessionJson);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;

    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getSessionList(HttpServletResponse response, @RequestBody SessionJson sessionJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<SessionJson> sessions = sessionService.getSessionList(sessionJson, sessionJson.getSortColumn(), sessionJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(sessionJson.getCurrentPage(), sessionJson.getPageCount()), sessionJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(sessions);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity addSession(HttpServletResponse response, @RequestBody SessionJson sessionJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Lane lane = null;

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(sessionJson.getKioskOperatorCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#kioskOperatorCode is missing");

        if (sessionJson.getType() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "type is null");

        if (sessionJson.getType() != ServerConstants.SESSION_TYPE_PARKING && sessionJson.getType() != ServerConstants.SESSION_TYPE_VIRTUAL)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "type is invalid");

        if (sessionJson.getType() == ServerConstants.SESSION_TYPE_PARKING) {

            if (sessionJson.getLaneId() == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "laneId is null");

            if (sessionJson.getLaneId() < 0)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "laneId is invalid");

            lane = laneService.getLaneByLaneId(sessionJson.getLaneId());

            if (lane == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane is null");
        }

        if (StringUtils.isBlank(sessionJson.getCashAmountStartString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#cashAmountStartString is missing");

        if (StringUtils.isBlank(sessionJson.getCashBagNumber()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#cashBagNumber is missing");

        if (sessionJson.getCashBagNumber().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#cashBagNumber is invalid");

        Operator kioskOperator = operatorService.getOperatorByCode(sessionJson.getKioskOperatorCode());
        if (kioskOperator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#kioskOperator is null");

        BigDecimal cashAmountStart = null;
        try {
            cashAmountStart = new BigDecimal(sessionJson.getCashAmountStartString());

        } catch (NumberFormatException nfe) {
            throw new PADValidationException(ServerResponseConstants.INVALID_AMOUNT_CODE, ServerResponseConstants.INVALID_AMOUNT_TEXT, "");
        }

        if (cashAmountStart.compareTo(BigDecimal.ZERO) < 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#cashAmountStart is invalid");

        if (sessionJson.getType() == ServerConstants.SESSION_TYPE_PARKING) {

            sessionService.addSession(kioskOperator.getId(), sessionJson.getType(), lane.getLaneId(), lane.getLaneNumber(), cashAmountStart, sessionJson.getCashBagNumber(),
                loggedOperator.getId());

        } else {

            // TODO Temporary work around to allow the virtual kiosk to print receipts. It'll be improved.
            int laneIdNumberVirtual = 999;

            sessionService.addSession(kioskOperator.getId(), sessionJson.getType(), laneIdNumberVirtual, laneIdNumberVirtual, cashAmountStart, sessionJson.getCashBagNumber(),
                loggedOperator.getId());
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/end", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity endSession(HttpServletResponse response, @RequestBody SessionJson sessionJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (StringUtils.isBlank(sessionJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#session code is missing");

        Session session = sessionService.getSessionByCode(sessionJson.getCode());

        if (session == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#session is null");

        final int sessionStatus = session.getStatus();
        if (sessionStatus != ServerConstants.SESSION_STATUS_START && sessionStatus != ServerConstants.SESSION_STATUS_ASSIGNED)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#unexpected session status");

        session.setStatus(sessionStatus == ServerConstants.SESSION_STATUS_ASSIGNED ? ServerConstants.SESSION_STATUS_VALIDATED : ServerConstants.SESSION_STATUS_END);
        session.setDateEnd(new Date());

        sessionService.updateSession(session);

        activityLogService.saveActivityLogSession(ServerConstants.ACTIVITY_LOG_SESSION_END, loggedOperator.getId(), session.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity validateSession(HttpServletResponse response, @RequestBody SessionJson sessionJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (StringUtils.isBlank(sessionJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#session code is missing");

        List<SessionJson> sessionJsonList = new ArrayList<>();
        SessionJson sessionJsonFromDB = new SessionJson();

        Session session = sessionService.getSessionByCode(sessionJson.getCode());

        if (session == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#session is null");

        if (session.getStatus() != ServerConstants.SESSION_STATUS_END)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#unexpected session status");

        BigDecimal cashAmountEnd = null;
        try {
            cashAmountEnd = new BigDecimal(sessionJson.getCashAmountEndString());

        } catch (NumberFormatException nfe) {
            throw new PADValidationException(ServerResponseConstants.INVALID_AMOUNT_CODE, ServerResponseConstants.INVALID_AMOUNT_TEXT, "");
        }

        if (cashAmountEnd.compareTo(BigDecimal.ZERO) < 0)
            throw new PADValidationException(ServerResponseConstants.AMOUNT_END_LESS_THAN_AMOUNT_START_CODE, ServerResponseConstants.AMOUNT_END_LESS_THAN_AMOUNT_START_TEXT, "#1");

        if (cashAmountEnd.compareTo(session.getCashAmountStart()) < 0)
            throw new PADValidationException(ServerResponseConstants.AMOUNT_END_LESS_THAN_AMOUNT_START_CODE, ServerResponseConstants.AMOUNT_END_LESS_THAN_AMOUNT_START_TEXT, "#2");

        BeanUtils.copyProperties(session, sessionJsonFromDB);

        sessionJsonList.add(sessionJsonFromDB);

        BigDecimal amountCashEndExpected = session.getCashAmountStart().add(session.getNoAccountCashTransactionTotalAmount()).add(session.getAccountCashTransactionTotalAmount());

        try {
            sessionService.performKioskSessionValidationChecks(session);

        } catch (PADException pade) {

            apiResponse.setResponseCode(ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_CODE);
            apiResponse.setResponseText(pade.getResponseSource());
            apiResponse.setDataList(sessionJsonList);

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return apiResponse;
        }

        sessionJsonList = new ArrayList<>();

        sessionJsonFromDB.setCashAmountEndExpected(amountCashEndExpected);
        sessionJsonFromDB.setCashAmountStartToEndDifference(cashAmountEnd.subtract(amountCashEndExpected));

        sessionJsonList.add(sessionJsonFromDB);

        if (StringUtils.isBlank(sessionJson.getReasonAmountUnexpected()) && session.getValidationStep() == ServerConstants.SESSION_VALIDATION_STEP_NOT_VALIDATED) {
            // the first validation request

            session.setCashAmountEnd(cashAmountEnd);

            if (cashAmountEnd.compareTo(amountCashEndExpected) != 0) {

                session.setValidationStep(ServerConstants.SESSION_VALIDATION_STEP_PARTIALLY_VALIDATED);

                sessionService.updateSession(session);

                apiResponse.setResponseCode(ServerResponseConstants.AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_CODE);
                apiResponse.setResponseText(ServerResponseConstants.AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_TEXT);
                apiResponse.setDataList(sessionJsonList);

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return apiResponse;
            }

            session.setValidationStep(ServerConstants.SESSION_VALIDATION_STEP_VALIDATED);
            session.setStatus(ServerConstants.SESSION_STATUS_VALIDATED);

            sessionService.updateSession(session);

        } else if (StringUtils.isBlank(sessionJson.getReasonAmountUnexpected()) && session.getValidationStep() == ServerConstants.SESSION_VALIDATION_STEP_PARTIALLY_VALIDATED
            && cashAmountEnd.compareTo(session.getCashAmountEnd()) == 0 && cashAmountEnd.compareTo(amountCashEndExpected) != 0) {
            // the first validation request retried - this can happen if the session was not completely validated but finance operator already set the cash amount end value

            apiResponse.setResponseCode(ServerResponseConstants.AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_CODE);
            apiResponse.setResponseText(ServerResponseConstants.AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_TEXT);
            apiResponse.setDataList(sessionJsonList);

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return apiResponse;

        } else if (!StringUtils.isBlank(sessionJson.getReasonAmountUnexpected()) && session.getValidationStep() == ServerConstants.SESSION_VALIDATION_STEP_PARTIALLY_VALIDATED) {
            // the second validation request, if the cash amount end expected was different
            session.setReasonAmountUnexpected(
                sessionJson.getReasonAmountUnexpected().length() > 128 ? sessionJson.getReasonAmountUnexpected().substring(0, 127) : sessionJson.getReasonAmountUnexpected());

            session.setValidationStep(ServerConstants.SESSION_VALIDATION_STEP_VALIDATED);
            session.setStatus(ServerConstants.SESSION_STATUS_VALIDATED);

            sessionService.updateSession(session);

        } else
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#session cannot be validated");

        activityLogService.saveActivityLogSession(ServerConstants.ACTIVITY_LOG_SESSION_VALIDATE, loggedOperator.getId(), session.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(sessionJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
