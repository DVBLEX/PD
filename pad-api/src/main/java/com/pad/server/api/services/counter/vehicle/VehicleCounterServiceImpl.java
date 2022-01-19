package com.pad.server.api.services.counter.vehicle;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.VehicleCounter;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterEventJson;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterRequestJson;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.session.SessionService;

@Service
@Transactional
public class VehicleCounterServiceImpl implements VehicleCounterService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private LaneService    laneService;

    @Override
    public void log(VehicleCounterRequestJson vehicleCounterRequestJson) throws PADException {

        try {

            Lane lane = laneService.getLaneByDeviceId(vehicleCounterRequestJson.getDeviceId());

            if (lane == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "DeviceId not found.");

            SecurityUtil.validateApiRemoteAddr(lane.getAllowedHosts());

            Session session = null;
            Transaction tx = null;
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            for (VehicleCounterEventJson eventJson : vehicleCounterRequestJson.getEvents()) {

                if (StringUtils.isBlank(eventJson.getType()))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event type is misssin.");

                if (!eventJson.getType().equals(VehicleCounterRequestJson.TYPE_AUTOMATIC) && !eventJson.getType().equals(VehicleCounterRequestJson.TYPE_MANUAL)
                    && !eventJson.getType().equals(VehicleCounterRequestJson.TYPE_UNKNOWN) && !eventJson.getType().equals(VehicleCounterRequestJson.TYPE_HEARTBEAT))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event type is invalid.");

                if (StringUtils.isBlank(eventJson.getDateCount()))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event dateCount is missing.");

                Date dateCount = null;
                try {
                    dateCount = ServerUtil.parseDate(ServerConstants.dateFormatyyyyMMddHHmmss, eventJson.getDateCount());
                } catch (Exception e) {
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event dateCount is invalid.");
                }

                long sessionId = ServerConstants.DEFAULT_INT;
                if (lane.getZoneId() == ServerConstants.ZONE_ID_PARKING) {
                    sessionId = sessionService.getSessionIdByLaneNumberAndDateStartEnd(lane.getLaneNumber(), dateCount);
                }

                VehicleCounter vehicleCounter = new VehicleCounter();
                vehicleCounter.setDeviceId(vehicleCounterRequestJson.getDeviceId());
                vehicleCounter.setDeviceName(lane.getDeviceName());
                vehicleCounter.setLaneId(lane.getLaneId());
                vehicleCounter.setLaneNumber(lane.getLaneNumber());
                vehicleCounter.setZoneId(lane.getZoneId());
                vehicleCounter.setSessionId(sessionId);
                vehicleCounter.setType(eventJson.getType());
                vehicleCounter.setDateCount(dateCount);
                vehicleCounter.setDateCreated(new Date());

                session.save(vehicleCounter);

                session.flush();
                session.clear();
            }

            tx.commit();

            lane.setDateLastRequest(new Date());
            laneService.updateLane(lane);

        } catch (PADException pade) {
            throw pade;
        } catch (Exception e) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "log");
        }

    }

}
