package com.pad.server.web.restcontrollers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.StatsJson;
import com.pad.server.base.services.statistics.StatisticsService;

@RestController
@RequestMapping("/statistics")
public class StatisticsRESTController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getStatisticsData(HttpServletResponse response, @RequestBody StatsJson statsJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (statsJson.getReportType() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (statsJson.getMonthId() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        if (statsJson.getYear() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MONTH, statsJson.getMonthId() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, statsJson.getYear());

        final Date dateFrom = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);

        final Date dateTo = calendar.getTime();

        List<List<StatsJson>> reportData = new ArrayList<>();
        List<StatsJson> statsJsonList = null;

        switch (statsJson.getReportType()) {
            case ServerConstants.REPORT_TYPE_PARKING_ENTRY_COUNTS:

                reportData.add(statisticsService.getParkingStatsByDateRange(dateFrom, dateTo));

                statsJsonList = new ArrayList<>();

                statsJson = new StatsJson();
                statsJson = statisticsService.getStatsTotalsByCurrentMonthAndYear(statsJson);
                statsJson.setStatsDataMap(statisticsService.getStatisticsMapForLineChart(ServerConstants.REPORT_TYPE_PARKING_ENTRY_COUNTS, reportData.get(0), dateFrom));

                statsJsonList.add(statsJson);
                reportData.add(statsJsonList);

                break;

            case ServerConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR_COUNTS:

                reportData.add(statisticsService.getPortStatsByDateRange(dateFrom, dateTo));

                statsJsonList = new ArrayList<>();

                statsJson = new StatsJson();
                statsJson = statisticsService.getStatsTotalsByCurrentMonthAndYear(statsJson);
                statsJson.setStatsDataMap(statisticsService.getStatisticsMapForLineChart(ServerConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR_COUNTS, reportData.get(0), dateFrom));

                statsJsonList.add(statsJson);
                reportData.add(statsJsonList);

                break;

            default:
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(reportData);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
