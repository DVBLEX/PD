package com.pad.server.base.services.lane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.jsonentities.api.LaneJson;
import com.pad.server.base.services.anpr.AnprBaseService;

@Service
@Transactional
public class LaneServiceImpl implements LaneService {

    private static final Logger logger = Logger.getLogger(LaneServiceImpl.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Override
    public long getCount(LaneJson laneJson) {

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, laneJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getQuery(int queryType, LaneJson laneJson) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append(" SELECT COUNT(l.id) ");
        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query.append(" SELECT l.* ");
        }

        query.append(" FROM pad.lanes l ");
        query.append(" WHERE (1=1) ");

        return query;
    }

    @Override
    public List<LaneJson> getList(LaneJson laneJson) {

        final List<LaneJson> laneList = new ArrayList<>();
        try {

            PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, laneJson);

            if (laneJson.getPageCount() != ServerConstants.DEFAULT_INT) {
                query.setSortParameters(laneJson.getSortColumn(), laneJson.getSortAsc(), "l", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
                query.setLimitParameters(laneJson.getCurrentPage(), laneJson.getPageCount());
            }

            jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    LaneJson json = new LaneJson();
                    json.setId(rs.getInt("l.id"));
                    json.setLaneId(rs.getInt("l.lane_id"));
                    json.setLaneNumber(rs.getInt("l.lane_number"));
                    json.setZoneId(rs.getInt("l.zone_id"));
                    json.setDeviceId(rs.getString("l.device_id"));
                    json.setDeviceName(rs.getString("l.device_name"));
                    json.setAllowedHosts(rs.getString("l.allowed_hosts"));
                    json.setIsActive(rs.getBoolean("l.is_active"));
                    json.setPrinterIp(rs.getString("l.printer_ip"));
                    json.setDateLastRequest(rs.getTimestamp("l.date_last_request"));

                    laneList.add(json);
                }
            }, query.getQueryParameters());

        } catch (Exception e) {
            logger.error("getList###Exception: ", e);
        }

        return laneList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Lane getLaneByDeviceId(String deviceId) {
        Lane lane = null;

        List<Lane> laneList = (List<Lane>) hibernateTemplate.findByNamedParam("FROM Lane WHERE deviceId = :deviceId", "deviceId", deviceId);

        if (laneList != null && !laneList.isEmpty()) {
            lane = laneList.get(0);
        }
        return lane;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Lane getLaneByLaneId(long laneId) {
        Lane lane = null;

        List<Lane> laneList = (List<Lane>) hibernateTemplate.findByNamedParam("FROM Lane WHERE laneId = :laneId", "laneId", laneId);

        if (laneList != null && !laneList.isEmpty()) {
            lane = laneList.get(0);
        }

        return lane;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Lane getLaneById(long id) {
        Lane lane = null;

        List<Lane> laneList = (List<Lane>) hibernateTemplate.findByNamedParam("FROM Lane WHERE id = :id", "id", id);

        if (laneList != null && !laneList.isEmpty()) {
            lane = laneList.get(0);
        }

        return lane;
    }

    @Override
    public void updateLane(Lane lane) {
        hibernateTemplate.update(lane);
    }

    @Override
    public void saveLane(Lane lane) {
        hibernateTemplate.save(lane);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaneJson> getParkingEntryLanes() {

        List<LaneJson> lanes = new ArrayList<>();

        long parkingZoneId = anprBaseService.getAnprParameter().getAnprZoneIdAgsparking();
        for (Lane lane : (List<Lane>) hibernateTemplate.findByNamedParam("FROM Lane WHERE zoneId = :zoneId", "zoneId", parkingZoneId)) {
            LaneJson laneJson = new LaneJson();
            laneJson.setLaneId(lane.getLaneId());
            laneJson.setLaneNumber(lane.getLaneNumber());

            lanes.add(laneJson);
        }

        return lanes;
    }

}
