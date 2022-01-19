package com.pad.server.web.restcontrollers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.OperatorJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.web.security.MyUserDetails;

@RestController
@RequestMapping("/operator")
public class OperatorRESTController {

    @Autowired
    private ActivityLogService    activityLogService;

    @Autowired
    private OperatorService       operatorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getOperatorList(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<OperatorJson> operatorList = operatorService.getSystemOperatorList(operatorJson.getFirstName(), operatorJson.getLastName(), operatorJson.getRoleId(),
            loggedOperator.getAccountId(), Boolean.TRUE, operatorJson.getSortColumn(), operatorJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(operatorJson.getCurrentPage(), operatorJson.getPageCount()), operatorJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(operatorList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getOperatorCount(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        long operatorCount = operatorService.getSystemOperatorCount(operatorJson.getFirstName(), operatorJson.getLastName(), operatorJson.getRoleId(),
            loggedOperator.getAccountId());

        List<Long> countsList = new ArrayList<>();
        countsList.add(operatorCount);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(countsList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/password/change", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity changePassword(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        MyUserDetails authUser = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Operator operator = operatorService.getOperator(authUser.getUsername());

        if (!passwordEncoder.matches(operatorJson.getCurrentPassword(), operator.getPassword()))
            throw new PADValidationException(ServerResponseConstants.INVALID_CURRENT_PASSWORD_CODE, ServerResponseConstants.INVALID_CURRENT_PASSWORD_TEXT,
                "RegexFormatCheck#Email");
        else if (!operatorJson.getPassword().matches(ServerConstants.REGEX_PASSWORD))
            throw new PADValidationException(ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_CODE, ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_TEXT,
                "RegexFormatCheck#Password");
        else if (!operatorJson.getPassword().equals(operatorJson.getConfirmPassword()))
            throw new PADValidationException(ServerResponseConstants.MISMATCH_PASSWORD_CODE, ServerResponseConstants.MISMATCH_PASSWORD_TEXT, "Mismatch#Password");
        else {

            operator.setPassword(passwordEncoder.encode(operatorJson.getPassword()));
            operator.setIsCredentialsExpired(false);
            operator.setDateLastPassword(new Date());
            operator.setLoginFailureCount(0);
            operator.setCountPasswdForgotRequests(0);
            operator.setOperatorId(authUser.getId());
            operator.setDateEdited(new Date());
            operatorService.updateOperator(operator);

            activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_OPERATOR_PASSWORD_CHANGE, operator.getId());

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

            response.setStatus(HttpServletResponse.SC_OK);

            return apiResponse;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity addOperator(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        String msisdn = "";

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(operatorJson.getFirstName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#firstName is missing");

        if (StringUtils.isBlank(operatorJson.getLastName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#lastName is missing");

        if (!operatorJson.getFirstName().matches(ServerConstants.REGEX_NAME))
            throw new PADValidationException(ServerResponseConstants.INVALID_FIRST_LAST_STAFF_USER_NAME_CODE, ServerResponseConstants.INVALID_FIRST_LAST_STAFF_USER_NAME_TEXT, "RegexFormatCheck#FirstName");

        if (!operatorJson.getLastName().matches(ServerConstants.REGEX_NAME))
            throw new PADValidationException(ServerResponseConstants.INVALID_FIRST_LAST_STAFF_USER_NAME_CODE, ServerResponseConstants.INVALID_FIRST_LAST_STAFF_USER_NAME_TEXT, "RegexFormatCheck#LastName");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_ADMIN && StringUtils.isBlank(operatorJson.getMsisdn()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#msisdn is missing");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_TRANSPORTER && StringUtils.isBlank(operatorJson.getEmail()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#email is missing");

        if (StringUtils.isNotBlank(operatorJson.getEmail())) {

            if (operatorJson.getEmail().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64 || !operatorJson.getEmail().matches(ServerConstants.REGEX_EMAIL))
                throw new PADValidationException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");
        }

        if (StringUtils.isNotBlank(operatorJson.getMsisdn())) {

            msisdn = ServerUtil.getValidNumber(operatorJson.getMsisdn(), "addOperator");

            if (!msisdn.matches(ServerConstants.REGEX_MSISDN))
                throw new PADValidationException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "RegexFormatCheck#Msisdn");
        }

        if (operatorJson.getRoleId() == null || operatorJson.getRoleId() < 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#roleId is invalid");

        if (operatorJson.getRoleId() == ServerConstants.OPERATOR_ROLE_PORT_OPERATOR && (operatorJson.getPortOperatorId() == null || operatorJson.getPortOperatorId() < 0))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#portOperatorId is invalid");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_TRANSPORTER && operatorJson.getRoleId() != ServerConstants.OPERATOR_ROLE_TRANSPORTER)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "#roleId is invalid for transporter");

        if (StringUtils.isBlank(operatorJson.getLanguage()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#language is null");

        if (!operatorJson.getLanguage().equals(ServerConstants.LANGUAGE_EN_ISO_CODE) && !operatorJson.getLanguage().equals(ServerConstants.LANGUAGE_FR_ISO_CODE))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#language is invalid");

        Long languageId = ServerUtil.getLanguageIdByCode(operatorJson.getLanguage());

        operatorService.addOperator(loggedOperator.getAccountId(), operatorJson, msisdn, languageId, loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateOperator(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        String msisdn = "";

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(operatorJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "operatorCode is invalid");

        if (StringUtils.isBlank(operatorJson.getFirstName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#firstName is missing");

        if (StringUtils.isBlank(operatorJson.getLastName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#lastName is missing");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_ADMIN && StringUtils.isBlank(operatorJson.getMsisdn()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#msisdn is missing");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_TRANSPORTER && StringUtils.isBlank(operatorJson.getEmail()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#email is missing");

        if (StringUtils.isNotBlank(operatorJson.getEmail())) {

            if (operatorJson.getEmail().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64 || !operatorJson.getEmail().matches(ServerConstants.REGEX_EMAIL))
                throw new PADValidationException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");
        }

        if (StringUtils.isNotBlank(operatorJson.getMsisdn())) {

            msisdn = ServerUtil.getValidNumber(operatorJson.getMsisdn(), "updateOperator");

            if (!msisdn.matches(ServerConstants.REGEX_MSISDN))
                throw new PADValidationException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "RegexFormatCheck#Msisdn");
        }

        if (operatorJson.getActive() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "isActive is invalid");

        Operator operator = operatorService.getOperatorByCode(operatorJson.getCode());

        if (operator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "operator is null");

        operator.setFirstName(operatorJson.getFirstName());
        operator.setLastName(operatorJson.getLastName());

        operator.setEmail(operatorJson.getEmail());
        operator.setMsisdn(msisdn);

        if (!operatorJson.getActive() && operator.getIsActive()) {
            operator.setIsActive(false);
            operator.setIsDeleted(true);

        } else if (operatorJson.getActive() && !operator.getIsActive()) {
            operator.setIsActive(true);
            operator.setIsDeleted(false);
        }

        operator.setOperatorId(loggedOperator.getId());
        operator.setDateEdited(new Date());

        operatorService.updateOperator(operator);

        activityLogService.saveActivityLogOperator(ServerConstants.ACTIVITY_LOG_OPERATOR_UPDATE, loggedOperator.getId(), operator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity unlockOperator(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(operatorJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "operatorCode is invalid");

        Operator operator = operatorService.getOperatorByCode(operatorJson.getCode());

        if (operator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "operator is null");

        operatorService.unlockOperator(operator);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/password/reset/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity passwordResetSend(HttpServletResponse response, @RequestBody OperatorJson operatorJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(operatorJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#code is missing");

        Operator operator = operatorService.getOperatorByCode(operatorJson.getCode());

        if (operator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "operator is null");

        try {
            if (StringUtils.isNotBlank(operator.getEmail())) {
                operatorService.sendPasswdSetUpEmail(operator.getEmail(), operator.getLanguageId(), operator);

            } else if (StringUtils.isNotBlank(operator.getMsisdn())) {
                operatorService.sendPasswdSetUpSms(operator.getMsisdn(), operator.getLanguageId(), operator);
            }
        } catch (UnsupportedEncodingException e) {
            throw new PADException(ServerResponseConstants.FAILURE_CODE, ServerResponseConstants.FAILURE_TEXT, "UnsupportedEncodingException");
        }

        operatorService.unlockOperator(operator);

        activityLogService.saveActivityLogOperator(ServerConstants.ACTIVITY_LOG_OPERATOR_RESET_PASSWORD_SEND, loggedOperator.getId(), operator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/getKioskOperatorNames", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ApiResponseJsonEntity getKioskOperatorNames(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Map<String, String> kioskOperatorNamesMap = operatorService.getKioskOperatorNamesMap();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataMap(kioskOperatorNamesMap);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
