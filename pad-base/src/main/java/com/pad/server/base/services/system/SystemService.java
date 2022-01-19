package com.pad.server.base.services.system;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pad.server.base.entities.BookingSlotCount;
import com.pad.server.base.entities.BookingSlotLimit;
import com.pad.server.base.entities.BookingSlotLimitsDefault;
import com.pad.server.base.entities.IndependentPortOperator;
import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorAlert;
import com.pad.server.base.entities.PortOperatorGate;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.BookingSlotLimitJson;
import com.pad.server.base.jsonentities.api.DayOfWeekJson;
import com.pad.server.base.jsonentities.api.ParkingReleaseStatsJson;
import com.pad.server.base.jsonentities.api.PortOperatorAlertJson;
import com.pad.server.base.jsonentities.api.PortOperatorJson;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.jsonentities.api.TripBookingTimeJson;

public interface SystemService {

    public SystemParameter getSystemParameter();

    public void updateSystemParameter(SystemParameter systemParameter);

    public PortOperator getPortOperatorFromMap(long portOperatorId);

    public String getPortOperatorName(long portOperatorId);

    public void updatePortOperatorTransactionType(PortOperatorTransactionType portOperatorTransactionType);

    public BigDecimal getTripFeeAmount(long portOperatorId, int transactionType, String registrationCountryISO) throws PADValidationException;

    public BigDecimal getOperatorTripFeeAmount(long portOperatorId, int transactionType) throws PADValidationException;

    public void updateSystemTimerTaskDateLastRun(long timerTaskId, Date dateLastRun);

    public boolean isUsernameRegisteredAlready(String username);

    public boolean isMsisdnRegisteredAlready(String msisdn);

    public boolean isCountRequestForgotPasswordUnderLimit(String username, int passwordForgotLimit);

    public void updateOperatorPassForgotReportDate(long operatorId);

    public BookingSlotCount getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(long portOperatorId, int transactionType, Date dateSlot, int hourSlot);

    public List<BookingSlotCount> getBookingSlotCountListByPortOperatorAndTransactionTypeAndDateSlot(long portOperatorId, int transactionType, Date dateSlot);

    public List<BookingSlotLimit> getBookingSlotLimitListByPortOperatorAndTransactionTypeAndDateSlot(long portOperatorId, int transactionType, Date dateSlot);

    public BookingSlotLimit getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(long portOperatorId, int transactionType, Date dateSlot, int hourSlot);

    public BookingSlotLimitsDefault getDefaultBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(long portOperatorId, int transactionType, int dayOfWeekId,
        int hourSlot);

    public List<BookingSlotLimitsDefault> getDefaultBookingSlotLimitListByPortOperatorAndTransactionTypeAndDayOfWeek(long portOperatorId, int transactionType, int dayOfWeekId);

    public List<BookingSlotLimitsDefault> getDefaultBookingSlotLimitByPortOperatorAndTransactionType(long portOperatorId, int transactionType);

    public void getBookingSlotLimit(BookingSlotLimitJson bookingSlotLimitJson, Map<String, List<BookingSlotLimitJson>> bookingLimitMap, List<DayOfWeekJson> daysWeekList);

    public int getBookingSlotCountTotal(Date dateSlot, int hourSlot);

    public void saveBookingSlot(BookingSlotCount bookingSlotCount);

    public void saveBookingSlotLimit(BookingSlotLimitJson bookingSlotLimitJson) throws PADException;

    public void saveBookingSlotLimitDefault(BookingSlotLimitJson bookingSlotLimitJson) throws PADException;

    public void updateBookingSlot(BookingSlotCount bookingSlotCount);

    public List<NameValuePair> getBookingSlotLimitPeriods();

    public List<TripBookingTimeJson> getTripBookingHoursAvailability(long portOperatorId, int transactionType, Date dateSlot, long languageId, long roleId) throws PADException;

    public void reserveBookingSlot(int portOperatorId, int transactionType, Date dateSlot) throws PADException, PADValidationException;

    public Map<Long, List<PortOperatorTransactionTypeJson>> getPortOperatorTransactionTypesMap(long loggedOperatorRoleId);

    public Map<Long, List<ParkingReleaseStatsJson>> getParkingReleaseStatsMap() throws SQLException;

    public String getPortOperatorGateNumberById(long id);

    public String getPortOperatorGateNumberShortById(long id);

    public long getPortOperatorAnprZoneIdById(long id);

    public List<PortOperatorJson> getPortOperatorJsonList();

    public List<PortOperator> getPortOperators();

    public List<IndependentPortOperator> getIndependentPortOperators();

    public PortOperatorTransactionType getPortOperatorTransactionTypeEntity(long portOperatorId, int transactionType) throws PADException;

    public List<PortOperatorTransactionType> getPortOperatorTransactionTypes();

    public void savePortOperatorAlert(PortOperatorAlert portOperatorAlert);

    public void updatePortOperatorAlert(PortOperatorAlert portOperatorAlert);

    public void resolvePortOperatorAlert(PortOperatorAlert portOperatorAlert);

    public PortOperatorAlert getPortOperatorAlertByCode(String code);

    public long getIssueCount(Long portOperatorId) throws Exception;

    public List<PortOperatorAlertJson> getIssueList(Long portOperatorId, String sortColumn, boolean sortAsc, int startLimit, int endLimit);

    public String getPortOperatorNameById(long portOperatorId);

    public String getPortOperatorShortNameById(long portOperatorId);

    public IndependentPortOperator getIndependentPortOperatorByCode(String code);

    public String getIndependentPortOperatorNameByCode(String code);

    public IndependentPortOperator getIndependentPortOperatorById(long independentPortOperatorId);

    public String getIndependentPortOperatorCodeById(long independentPortOperatorId);

    public PortOperatorGate getPortOperatorGateById(long id);

    public List<PortOperatorGate> getPortOperatorGates();
}
