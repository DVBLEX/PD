package com.pad.server.anpr.tasks;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.anpr.services.anpr.AnprService;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.email.EmailService;

public class AnprTaskExecutor implements Runnable {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AnprTaskExecutor.class);

    private AnprService         anprService;

    private EmailService        emailService;

    private Anpr                anpr;

    public AnprTaskExecutor(Anpr anpr, AnprService anprService, EmailService emailService) {
        this.anpr = anpr;
        this.anprService = anprService;
        this.emailService = emailService;
    }

    @Override
    public void run() {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            final Date dateToday = calendar.getTime();

            switch (anpr.getRequestType()) {
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_REENTRY_AFTER_PREM_EXIT:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_WHITELISTED:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT:
                    if (anpr.getDateValidTo().before(dateToday))
                        throw new PADException(ServerResponseConstants.PARKING_PERMISSION_EXPIRED_CODE, "Parking Permission Expired", "AnprTaskExecutor#run");
                    else {
                        anprService.createWhiteList(anpr);
                    }
                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY:
                case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT:
                case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY:
                case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT:
                    anprService.deleteWhiteList(anpr);
                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_DISABLED:
                case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED:
                case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED:
                case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED:
                case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED:
                    anprService.updateWhiteListStatus(anpr);
                    break;

                default:
                    throw new PADException();
            }

            anpr.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
            anprService.updateAnpr(anpr);
            anprService.deleteScheduledAnpr(anpr.getId());

        } catch (PADException pade) {

            anpr.setResponseCode(pade.getResponseCode());
            anpr.setResponseText(pade.getResponseText());

            if (pade.getResponseCode() == ServerResponseConstants.PARKING_PERMISSION_ID_NOT_FOUND_CODE
                || pade.getResponseCode() == ServerResponseConstants.PARKING_PERMISSION_EXPIRED_CODE) {
                // PARKING_PERMISSION_ID_NOT_FOUND_CODE: the parking permission ID does not exist on parkIT, so no need to retry
                // PARKING_PERMISSION_EXPIRED_CODE: the parking permission's request "valid to" is in the past so no need to create a parking permission
                anpr.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
                anprService.updateAnpr(anpr);
                anprService.deleteScheduledAnpr(anpr.getId());

            } else if (pade.getResponseCode() == ServerResponseConstants.PARKING_PERMISSION_ID_CANNOT_DELETE_STILL_IN_USE_CODE && anpr.getRetryCount() == 3) {
                // PARKING_PERMISSION_ID_CANNOT_DELETE_STILL_IN_USE_CODE: the parking permission's delete request cannot be fulfilled as currently ANPR has the vehicle reg in its
                // occupied parking spots table
                // schedule the deletion request after 3 days since parkIT is configured to do auto-lead outs after 3 days
                anpr.setRetryCount(anpr.getRetryCount() + 1);
                anpr.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                anpr.setDateScheduled(new Date(anpr.getDateScheduled().getTime() + (60l * 60l * 24l * 3l * 1000l))); // date scheduled + 3 days
                anprService.updateScheduledAnpr(anpr);

            } else if (pade.getResponseCode() == ServerResponseConstants.PARKING_PERMISSION_ID_CANNOT_DELETE_STILL_IN_USE_CODE && anpr.getRetryCount() > 3) {
                // PARKING_PERMISSION_ID_CANNOT_DELETE_STILL_IN_USE_CODE: the parking permission's delete request cannot be fulfilled as currently ANPR has the vehicle reg in its
                // occupied parking spots table
                // the permission could not be deleted after retrying after 3 days so stop trying to delete it
                anpr.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
                anprService.updateAnpr(anpr);
                anprService.deleteScheduledAnpr(anpr.getId());

            } else {
                anpr.setRetryCount(anpr.getRetryCount() + 1);

                if (anpr.getRetryCount() == 1) {
                    anpr.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                    anpr.setDateScheduled(new Date(anpr.getDateScheduled().getTime() + 10l * 1000l));
                    anprService.updateScheduledAnpr(anpr);
                } else {
                    anpr.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                    anpr.setDateScheduled(new Date(anpr.getDateScheduled().getTime() + 60l * 1000l));
                    anprService.updateScheduledAnpr(anpr);
                }
            }

        } catch (ParseException pe) {

            anpr.setResponseCode(ServerResponseConstants.PARKING_PERMISSION_DATETIME_FORMAT_PARSE_ERROR);
            anpr.setResponseText("Unable to parse date and time for parking permission");

            anpr.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
            anprService.updateAnpr(anpr);
            anprService.deleteScheduledAnpr(anpr.getId());

            emailService.sendSystemEmail("Anpr TaskExecutor Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprTaskExecutor#run###Exception:<br />anprId: " + anpr.getId()
                + "<br />Response Code: " + anpr.getResponseCode() + "<br />Response Text: " + anpr.getResponseText());
        }
    }
}
