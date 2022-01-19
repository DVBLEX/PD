package com.pad.server.web.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Session;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.web.security.MyUserDetails;

@Controller
public class PADController {

    private static final Logger logger = Logger.getLogger(PADController.class);

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private SessionService      sessionService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TripService         tripService;

    @Value("${system.environment}")
    private String              environment;

    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public ModelAndView padIndex(HttpServletRequest request) {

        String responseSource = "padIndex#";
        responseSource = responseSource + request.getRemoteAddr() + "#username=" + SecurityUtil.getSystemUsername();

        StringBuilder builder = new StringBuilder();
        builder.append(responseSource);
        builder.append("#Request: ");
        builder.append("[]");
        logger.info(builder.toString());

        ModelAndView mav = new ModelAndView();

        if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
            mav.addObject("isTestEnvironment", true);
        } else {
            mav.addObject("isTestEnvironment", false);
        }

        MyUserDetails authUser = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // add role specific details
        mav.addObject("isTransporterOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_TRANSPORTER);
        mav.addObject("isPortOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PORT_OPERATOR);
        mav.addObject("isParkingOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_OPERATOR);
        mav.addObject("isParkingKioskOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR);
        mav.addObject("isParkingOfficeOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_OFFICE_OPERATOR);
        mav.addObject("isPortAuthorityOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PORT_AUTHORITY_OPERATOR);
        mav.addObject("isPortEntryOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PORT_ENTRY_OPERATOR);
        mav.addObject("isPortExitOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PORT_EXIT_OPERATOR);
        mav.addObject("isParkingSupervisorOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_SUPERVISOR_OPERATOR);
        mav.addObject("isFinanceOperator", authUser.getRole() == ServerConstants.OPERATOR_ROLE_FINANCE_OPERATOR);
        mav.addObject("isAdmin", authUser.getRole() == ServerConstants.OPERATOR_ROLE_ADMIN);

        mav.addObject("nameUser", authUser.getUsername());

        if (authUser.getFirstname().length() > 10) {
            mav.addObject("displayUsername", (authUser.getFirstname()).substring(0, 8) + "...");
        } else {
            mav.addObject("displayUsername", authUser.getFirstname());
        }

        mav.addObject("operatorCode", authUser.getCode());
        mav.addObject("operatorLanguage", ServerUtil.getLanguageCodeById(authUser.getLanguageId()));
        mav.addObject("dropOffEmptyNightMissionStartTime", systemService.getSystemParameter().getDropOffEmptyNightMissionStartTime());
        mav.addObject("dropOffEmptyNightMissionEndTime", systemService.getSystemParameter().getDropOffEmptyNightMissionEndTime());
        mav.addObject("dropOffEmptyTriangleMissionStartTime", systemService.getSystemParameter().getDropOffEmptyTriangleMissionStartTime());
        mav.addObject("dropOffEmptyTriangleMissionEndTime", systemService.getSystemParameter().getDropOffEmptyTriangleMissionEndTime());

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_ADMIN) {
            mav.addObject("isBookingLimitCheckEnabled", systemService.getSystemParameter().getIsBookingLimitCheckEnabled());
            mav.addObject("autoReleaseExitCapacityPercentage", systemService.getSystemParameter().getAutoReleaseExitCapacityPercentage());
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_PORT_OPERATOR) {
            mav.addObject("portOperatorId", authUser.getPortOperatorId());
            if (authUser.getIndependentPortOperatorId() != ServerConstants.DEFAULT_LONG) {
                mav.addObject("independentPortOperatorId", authUser.getIndependentPortOperatorId());
                mav.addObject("independentPortOperatorCode", systemService.getIndependentPortOperatorCodeById(authUser.getIndependentPortOperatorId()));
            }
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_TRANSPORTER) {
            mav.addObject("transporterAccountNumber", authUser.getAccountNumber());
            mav.addObject("transporterAccountStatus", authUser.getAccountStatus());
            mav.addObject("transporterAccountType", authUser.getAccountType());
            mav.addObject("transporterAccountPaymentTermsType", authUser.getAccountPaymentTermsType());
            mav.addObject("pendingTripsTransporterCount", tripService.getTripCountByAccountIdAndStatus(authUser.getAccountId(), ServerConstants.TRIP_STATUS_PENDING));
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR || authUser.getRole() == ServerConstants.OPERATOR_ROLE_TRANSPORTER) {
            mav.addObject("taxPercentage", systemService.getSystemParameter().getTaxPercentage());
            mav.addObject("kioskFeeMinAmount", systemService.getSystemParameter().getKioskAccountTopupMinAmount());
            mav.addObject("kioskFeeMaxAmount", systemService.getSystemParameter().getKioskAccountTopupMaxAmount());
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_FINANCE_OPERATOR) {
            mav.addObject("taxPercentage", systemService.getSystemParameter().getTaxPercentage());
            mav.addObject("financeFeeMinAmount", systemService.getSystemParameter().getFinanceAccountTopupMinAmount());
            mav.addObject("financeFeeMaxAmount", systemService.getSystemParameter().getFinanceAccountTopupMaxAmount());
            mav.addObject("financeInitialFloatMinAmount", systemService.getSystemParameter().getFinanceSessionInitialFloatMinAmount());
            mav.addObject("financeInitialFloatMaxAmount", systemService.getSystemParameter().getFinanceSessionInitialFloatMaxAmount());
            mav.addObject("maximumOverdraftLimitMinAmount", systemService.getSystemParameter().getMaximumOverdraftLimitMinAmount());
            mav.addObject("maximumOverdraftLimitMaxAmount", systemService.getSystemParameter().getMaximumOverdraftLimitMaxAmount());
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR) {

            Session session = sessionService.getLastSessionByKioskOperatorId(authUser.getId());

            if (session == null) {
                // no kiosk session found. Don't allow kiosk operator access to kiosk functionality
                mav.addObject("isKioskSessionAllowed", false);

            } else if (session.getStatus() == ServerConstants.SESSION_STATUS_ASSIGNED || session.getStatus() == ServerConstants.SESSION_STATUS_START) {

                if (session.getStatus() == ServerConstants.SESSION_STATUS_ASSIGNED) {

                    session.setStatus(ServerConstants.SESSION_STATUS_START);
                    session.setDateStart(new Date());

                    sessionService.updateSession(session);
                }

                mav.addObject("isKioskSessionAllowed", true);
                mav.addObject("kioskSessionType", session.getType());
                mav.addObject("kioskSessionCode", session.getCode());
                mav.addObject("kioskSessionLaneNumber", session.getLaneNumber());
                mav.addObject("kioskSessionLaneId", session.getLaneId());
                mav.addObject("kioskSessionAmountCollected", session.getNoAccountCashTransactionTotalAmount().add(session.getAccountCashTransactionTotalAmount()).toString()); // cash
                mav.addObject("entryLaneVideoFeedUrl", anprBaseService.getEntryLaneVideoFeddUrlByLaneNumber(session.getLaneNumber()));

            } else {
                // no active kiosk session. Don't allow kiosk operator access to kiosk functionality
                mav.addObject("isKioskSessionAllowed", false);
            }
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_PARKING_OFFICE_OPERATOR) {
            mav.addObject("tripsPendingApprovalCount", tripService.getTripCountByStatus(ServerConstants.TRIP_STATUS_PENDING_APPROVAL));
            mav.addObject("maximumOverdraftLimitMinAmount", systemService.getSystemParameter().getMaximumOverdraftLimitMinAmount());
            mav.addObject("maximumOverdraftLimitMaxAmount", systemService.getSystemParameter().getMaximumOverdraftLimitMaxAmount());
        }

        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_ADMIN || authUser.getRole() == ServerConstants.OPERATOR_ROLE_PORT_ENTRY_OPERATOR) {
            mav.addObject("isPortEntryFiltering", systemService.getSystemParameter().getIsPortEntryFiltering());
        }

        mav.setViewName("index");

        return mav;
    }
}
