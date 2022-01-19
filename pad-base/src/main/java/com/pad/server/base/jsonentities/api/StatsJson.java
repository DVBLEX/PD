package com.pad.server.base.jsonentities.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsJson {

    private Integer            reportType;
    private Integer            monthId;
    private Integer            year;

    private String             dateEntryString;

    List<List<Integer>>        statsDataList;
    Map<String, List<Integer>> statsDataMap;
    List<PortOperatorGateJson> uniqueGateNumberJsonList;
    Map<String, Integer>       portEntryGateCountsMap;
    Map<String, Integer>       operatorCountsMap;

    private int                dayTotalCountParkingEntry;
    private int                monthTotalCountParkingEntry;
    private int                yearTotalCountParkingEntry;

    private int                dayTotalCountPortEntry;
    private int                monthTotalCountPortEntry;
    private int                yearTotalCountPortEntry;

    private int                countInTheParking;
    private int                countInTransit;
    private int                countEnteredPortLastHour;

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDateEntryString() {
        return dateEntryString;
    }

    public void setDateEntryString(String dateEntryString) {
        this.dateEntryString = dateEntryString;
    }

    public List<List<Integer>> getStatsDataList() {
        return statsDataList;
    }

    public void setStatsDataList(List<List<Integer>> statsDataList) {
        this.statsDataList = statsDataList;
    }

    public Map<String, List<Integer>> getStatsDataMap() {
        return statsDataMap;
    }

    public void setStatsDataMap(Map<String, List<Integer>> statsDataMap) {
        this.statsDataMap = statsDataMap;
    }

    public List<PortOperatorGateJson> getUniqueGateNumberJsonList() {
        return uniqueGateNumberJsonList;
    }

    public void setUniqueGateNumberJsonList(List<PortOperatorGateJson> uniqueGateNumberJsonList) {
        this.uniqueGateNumberJsonList = uniqueGateNumberJsonList;
    }

    public Map<String, Integer> getPortEntryGateCountsMap() {
        return portEntryGateCountsMap;
    }

    public void setPortEntryGateCountsMap(Map<String, Integer> portEntryGateCountsMap) {
        this.portEntryGateCountsMap = portEntryGateCountsMap;
    }

    public Map<String, Integer> getOperatorCountsMap() {
        return operatorCountsMap;
    }

    public void setOperatorCountsMap(Map<String, Integer> operatorCountsMap) {
        this.operatorCountsMap = operatorCountsMap;
    }

    public int getDayTotalCountParkingEntry() {
        return dayTotalCountParkingEntry;
    }

    public void setDayTotalCountParkingEntry(int dayTotalCountParkingEntry) {
        this.dayTotalCountParkingEntry = dayTotalCountParkingEntry;
    }

    public int getMonthTotalCountParkingEntry() {
        return monthTotalCountParkingEntry;
    }

    public void setMonthTotalCountParkingEntry(int monthTotalCountParkingEntry) {
        this.monthTotalCountParkingEntry = monthTotalCountParkingEntry;
    }

    public int getYearTotalCountParkingEntry() {
        return yearTotalCountParkingEntry;
    }

    public void setYearTotalCountParkingEntry(int yearTotalCountParkingEntry) {
        this.yearTotalCountParkingEntry = yearTotalCountParkingEntry;
    }

    public int getDayTotalCountPortEntry() {
        return dayTotalCountPortEntry;
    }

    public void setDayTotalCountPortEntry(int dayTotalCountPortEntry) {
        this.dayTotalCountPortEntry = dayTotalCountPortEntry;
    }

    public int getMonthTotalCountPortEntry() {
        return monthTotalCountPortEntry;
    }

    public void setMonthTotalCountPortEntry(int monthTotalCountPortEntry) {
        this.monthTotalCountPortEntry = monthTotalCountPortEntry;
    }

    public int getYearTotalCountPortEntry() {
        return yearTotalCountPortEntry;
    }

    public void setYearTotalCountPortEntry(int yearTotalCountPortEntry) {
        this.yearTotalCountPortEntry = yearTotalCountPortEntry;
    }

    public int getCountInTheParking() {
        return countInTheParking;
    }

    public void setCountInTheParking(int countInTheParking) {
        this.countInTheParking = countInTheParking;
    }

    public int getCountInTransit() {
        return countInTransit;
    }

    public void setCountInTransit(int countInTransit) {
        this.countInTransit = countInTransit;
    }

    public int getCountEnteredPortLastHour() {
        return countEnteredPortLastHour;
    }

    public void setCountEnteredPortLastHour(int countEnteredPortLastHour) {
        this.countEnteredPortLastHour = countEnteredPortLastHour;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReportJson [reportType=");
        builder.append(reportType);
        builder.append(", monthId=");
        builder.append(monthId);
        builder.append(", year=");
        builder.append(year);
        builder.append("]");
        return builder.toString();
    }

}
