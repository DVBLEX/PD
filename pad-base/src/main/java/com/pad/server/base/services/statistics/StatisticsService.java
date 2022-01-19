package com.pad.server.base.services.statistics;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pad.server.base.entities.ParkingStatistics;
import com.pad.server.base.entities.PortStatistics;
import com.pad.server.base.jsonentities.api.StatsJson;

public interface StatisticsService {

    public void saveParkingStatistics(ParkingStatistics parkingStatistics);

    public void savePortStatistics(PortStatistics portStatistics);

    public void updateParkingStatistics(ParkingStatistics parkingStatistics);

    public void updatePortStatistics(PortStatistics portStatistics);

    public ParkingStatistics getParkingStatsByPortOperatorAndTransactionType(Date dateYesterday, long portOperatorId, int transactionType);

    public PortStatistics getPortStatsByPortOperatorAndTransactionType(Date dateYesterday, long portOperatorId, int transactionType);

    public List<StatsJson> getParkingStatsByDateRange(Date dateFrom, Date dateTo);

    public List<StatsJson> getPortStatsByDateRange(Date dateFrom, Date dateTo);

    public Map<String, List<Integer>> getStatisticsMapForLineChart(int reportType, List<StatsJson> statsJsonList, Date dateFrom);

    public StatsJson getStatsTotalsByCurrentMonthAndYear(StatsJson statsJson);
}
