package com.pad.server.base.services.activitylog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.ActivityLog;

@Service
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public void saveActivityLog(ActivityLog activityLog) {

        hibernateTemplate.save(activityLog);
    }

    @Override
    public void saveActivityLog(long activityId, long operatorId) {

        saveActivityLog(new ActivityLog(activityId, operatorId));
    }

    @Override
    public void saveActivityLog(long activityId, long operatorId, long accountId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, accountId));
    }

    @Override
    public void saveActivityLogMission(long activityId, long operatorId, long missionId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, missionId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG,
            ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG));
    }

    @Override
    public void saveActivityLogTrip(long activityId, long operatorId, long tripId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, tripId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG,
            ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG));
    }

    @Override
    public void saveActivityLogParking(long activityId, long operatorId, long parkingId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, parkingId, ServerConstants.DEFAULT_LONG,
            ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG));
    }

    @Override
    public void saveActivityLogPortAccess(long activityId, long operatorId, long portAccessId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, portAccessId,
            ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG));
    }

    @Override
    public void saveActivityLogPortAccessWhitelist(long activityId, long operatorId, long portAccessWhitelistId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG,
            ServerConstants.DEFAULT_LONG, portAccessWhitelistId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG));
    }

    @Override
    public void saveActivityLogStatement(long activityId, long operatorId, long statementId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG,
            ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, statementId, ServerConstants.DEFAULT_LONG));
    }

    @Override
    public void saveActivityLogSession(long activityId, long operatorId, long sessionId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG,
            ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, ServerConstants.DEFAULT_LONG, sessionId));
    }

    @Override
    public void saveActivityLogOperator(long activityId, long operatorId, long newUpdatedOperatorId) {

        saveActivityLog(new ActivityLog(activityId, operatorId, ServerConstants.DEFAULT_LONG, newUpdatedOperatorId));
    }

    @Override
    public void saveActivityLogTrip(long activityId, long operatorId, String regNumber, long portOperatorId, int transactionType, String referenceNumber) {

        saveActivityLog(new ActivityLog(ServerConstants.ACTIVITY_LOG_TRIP_SEARCH, operatorId, regNumber, portOperatorId, transactionType, referenceNumber));

    }

    @Override
    public void saveActivityLogParkingAutoRelease(long activityId, long operatorId, long portOperatorId, int transactionType, boolean isAutoReleaseOn) {

        saveActivityLog(new ActivityLog(activityId, operatorId, portOperatorId, transactionType, isAutoReleaseOn));

    }

    @Override
    public void saveActivityLogParkingManualRelease(long activityId, long operatorId, long portOperatorId, int transactionType, int vehicleReleaseCount) {

        saveActivityLog(new ActivityLog(activityId, operatorId, portOperatorId, transactionType, vehicleReleaseCount));

    }

    @Override
    public void saveActivityLogWithObjectJson(long activityId, long operatorId, long accountId, String jsonObj) {
        ActivityLog activityLog = new ActivityLog(activityId, operatorId, accountId);
        activityLog.setObjectJson(jsonObj);
        saveActivityLog(activityLog);
    }


}
