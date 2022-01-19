package com.pad.server.base.services.system;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.BookingSlotCount;
import com.pad.server.base.entities.BookingSlotLimit;
import com.pad.server.base.entities.BookingSlotLimitsDefault;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.IndependentPortOperator;
import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorAlert;
import com.pad.server.base.entities.PortOperatorGate;
import com.pad.server.base.entities.PortOperatorIndependentPortOperator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.PortOperatorTripFee;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.entities.BookingSlotLimit.DaysOfWeek;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.BookingSlotLimitJson;
import com.pad.server.base.jsonentities.api.DayOfWeekJson;
import com.pad.server.base.jsonentities.api.IndependentPortOperatorJson;
import com.pad.server.base.jsonentities.api.ParkingReleaseStatsJson;
import com.pad.server.base.jsonentities.api.PortOperatorAlertJson;
import com.pad.server.base.jsonentities.api.PortOperatorJson;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.jsonentities.api.TripBookingTimeJson;
import com.pad.server.base.jsonentities.api.VehicleRegistrationJson;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.parking.ParkingService;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    private static final Logger                       logger                                  = Logger.getLogger(SystemServiceImpl.class);

    private SystemParameter                           systemParameter;

    private List<SystemParameter>                     systemParameterList                     = new CopyOnWriteArrayList<>();

    private List<PortOperator>                        portOperatorList                        = new CopyOnWriteArrayList<>();

    private List<PortOperatorTripFee>                 portOperatorTripFeeList                 = new CopyOnWriteArrayList<>();

    private List<IndependentPortOperator>             independentPortOperatorList             = new CopyOnWriteArrayList<>();

    private List<PortOperatorIndependentPortOperator> portOperatorIndependentPortOperatorList = new CopyOnWriteArrayList<>();

    private Map<Long, PortOperator>                   portOperatorMap                         = new ConcurrentHashMap<>();

    private Map<String, PortOperatorTripFee>          portOperatorTripFeeMap                  = new ConcurrentHashMap<>();

    @Autowired
    private HibernateTemplate                         hibernateTemplate;

    @Autowired
    private JdbcTemplate                              jdbcTemplate;

    @Autowired
    private SessionFactory                            sessionFactory;

    @Autowired
    private EmailService                              emailService;

    @Autowired
    private ParkingService                            parkingService;

    @Value("${system.environment}")
    private String                                    systemEnvironment;

    @Value("${system.shortname}")
    private String                                    systemShortname;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        logger.info(systemShortname + "###SYSTEM_ENVIRONMENT=" + systemEnvironment);

        systemParameterList = (List<SystemParameter>) hibernateTemplate.find("FROM SystemParameter");
        if (systemParameterList != null && !systemParameterList.isEmpty()) {
            systemParameter = systemParameterList.get(0);
            logger.info("init###" + systemParameter.toString());
        }

        portOperatorList = (List<PortOperator>) hibernateTemplate.find("FROM PortOperator");
        if (portOperatorList != null && !portOperatorList.isEmpty()) {
            for (PortOperator portOperator : portOperatorList) {
                logger.info("init###" + portOperator.toString());
                portOperatorMap.put(portOperator.getId(), portOperator);
            }
        }

        portOperatorTripFeeList = (List<PortOperatorTripFee>) hibernateTemplate.find("FROM PortOperatorTripFee");
        if (portOperatorTripFeeList != null && !portOperatorTripFeeList.isEmpty()) {
            for (PortOperatorTripFee portOperatorTripFee : portOperatorTripFeeList) {
                logger.info("init###" + portOperatorTripFee.toString());
                portOperatorTripFeeMap.put(portOperatorTripFee.getPortOperatorId() + "#" + portOperatorTripFee.getTransactionType(), portOperatorTripFee);
            }
        }

        independentPortOperatorList = (List<IndependentPortOperator>) hibernateTemplate.find("FROM IndependentPortOperator WHERE isActive=1");
        portOperatorIndependentPortOperatorList = (List<PortOperatorIndependentPortOperator>) hibernateTemplate.find("FROM PortOperatorIndependentPortOperator");
    }

    @Override
    public SystemParameter getSystemParameter() {
        return systemParameter;
    }

    @Override
    public void updateSystemParameter(SystemParameter systemParameter) {

        logger.info("updateSystemParameter#" + systemParameter.toString());

        hibernateTemplate.update(systemParameter);

        this.systemParameter = systemParameter;
    }

    @Override
    public PortOperator getPortOperatorFromMap(long portOperatorId) {
        return portOperatorMap.get(portOperatorId);
    }

    @Override
    public String getPortOperatorName(long portOperatorId) {
        return portOperatorMap.get(portOperatorId) == null ? "" : portOperatorMap.get(portOperatorId).getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updatePortOperatorTransactionType(PortOperatorTransactionType portOperatorTransactionType) {

        hibernateTemplate.update(portOperatorTransactionType);

        portOperatorList = (List<PortOperator>) hibernateTemplate.find("FROM PortOperator");
        if (portOperatorList != null && !portOperatorList.isEmpty()) {
            for (PortOperator portOperator : portOperatorList) {
                portOperatorMap.put(portOperator.getId(), portOperator);
            }
        }
    }

    @Override
    public BigDecimal getTripFeeAmount(long portOperatorId, int transactionType, String registrationCountryISO) throws PADValidationException {
        PortOperatorTripFee portOperatorTripFee = portOperatorTripFeeMap.get(portOperatorId + "#" + transactionType);

        if (portOperatorTripFee == null)
            throw new PADValidationException(ServerResponseConstants.TRIP_FEE_NOT_DEFINED_CODE, ServerResponseConstants.TRIP_FEE_NOT_DEFINED_TEXT, "#getTripFeeAmount");

        return registrationCountryISO.equalsIgnoreCase("SN") ? portOperatorTripFee.getTripFeeAmount() : portOperatorTripFee.getTripFeeOtherAmount();
    }

    @Override
    public BigDecimal getOperatorTripFeeAmount(long portOperatorId, int transactionType) throws PADValidationException {
        PortOperatorTripFee portOperatorTripFee = portOperatorTripFeeMap.get(portOperatorId + "#" + transactionType);

        if (portOperatorTripFee == null)
            throw new PADValidationException(ServerResponseConstants.TRIP_FEE_NOT_DEFINED_CODE, ServerResponseConstants.TRIP_FEE_NOT_DEFINED_TEXT, "#getOperatorTripFeeAmount");

        return portOperatorTripFee.getOperatorFeeAmount();
    }

    @Override
    public void updateSystemTimerTaskDateLastRun(long timerTaskId, Date dateLastRun) {

        jdbcTemplate.update("UPDATE system_timer_tasks SET date_last_run = ? WHERE id = ? ", dateLastRun, timerTaskId);
    }

    @Override
    public boolean isUsernameRegisteredAlready(String username) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM operators WHERE username = ? ", new Object[] { username }, Long.class) > 0l;
    }

    @Override
    public boolean isMsisdnRegisteredAlready(String msisdn) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM operators WHERE username = ? ", new Object[] { msisdn }, Long.class) > 0l;
    }

    @Override
    public boolean isCountRequestForgotPasswordUnderLimit(String username, int passwordForgotLimit) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM operators WHERE username = ? AND count_passwd_forgot_requests >= ? ",
            new Object[] { username, passwordForgotLimit }, Long.class) == 0l;
    }

    @Override
    public void updateOperatorPassForgotReportDate(long operatorId) {
        jdbcTemplate.update("UPDATE operators SET date_password_forgot_reported = ? WHERE id = ?", new Date(), operatorId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BookingSlotCount getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(long portOperatorId, int transactionType, Date dateSlot, int hourSlot) {

        BookingSlotCount bookingSlotCount = null;

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotCount> bookingSlotCountList = currentSession.createQuery(
            "FROM BookingSlotCount b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type and b.dateSlot=:date_slot and b.hourSlotFrom=:hour_slot_from")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("date_slot", dateSlot)
            .setParameter("hour_slot_from", hourSlot).list();

        if (bookingSlotCountList != null && !bookingSlotCountList.isEmpty()) {
            bookingSlotCount = bookingSlotCountList.get(0);
        }

        return bookingSlotCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BookingSlotCount> getBookingSlotCountListByPortOperatorAndTransactionTypeAndDateSlot(long portOperatorId, int transactionType, Date dateSlot) {

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotCount> bookingSlotCountList = currentSession
            .createQuery("FROM BookingSlotCount b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type and b.dateSlot=:date_slot")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("date_slot", dateSlot).list();

        return bookingSlotCountList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BookingSlotLimit> getBookingSlotLimitListByPortOperatorAndTransactionTypeAndDateSlot(long portOperatorId, int transactionType, Date dateSlot) {

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotLimit> bookingSlotLimitList = currentSession
            .createQuery("FROM BookingSlotLimit b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type and b.dateSlot=:date_slot")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("date_slot", dateSlot).list();

        return bookingSlotLimitList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BookingSlotLimit getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(long portOperatorId, int transactionType, Date dateSlot, int hourSlot) {

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotLimit> bookingSlotLimitList = currentSession.createQuery(
            "FROM BookingSlotLimit b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type and b.dateSlot=:date_slot and b.hourSlotFrom=:hour_slot_from")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("date_slot", dateSlot)
            .setParameter("hour_slot_from", hourSlot).list();

        return bookingSlotLimitList.size() == 1 ? bookingSlotLimitList.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BookingSlotLimitsDefault getDefaultBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(long portOperatorId, int transactionType, int dayOfWeekId,
        int hourSlot) {

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotLimitsDefault> defaultBookingSlotLimitList = currentSession.createQuery(
            "FROM BookingSlotLimitsDefault b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type and b.dayOfWeekId=:day_of_week_id and b.hourSlotFrom=:hour_slot_from")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("day_of_week_id", dayOfWeekId)
            .setParameter("hour_slot_from", hourSlot).list();

        return defaultBookingSlotLimitList.size() == 1 ? defaultBookingSlotLimitList.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BookingSlotLimitsDefault> getDefaultBookingSlotLimitByPortOperatorAndTransactionType(long portOperatorId, int transactionType) {

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotLimitsDefault> defaultBookingSlotLimitList = currentSession.createQuery(
            "FROM BookingSlotLimitsDefault b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type ORDER BY hourSlotFrom, FIELD(dayOfWeekId, 2, 3, 4, 5, 6, 7, 1)")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).list();

        return defaultBookingSlotLimitList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BookingSlotLimitsDefault> getDefaultBookingSlotLimitListByPortOperatorAndTransactionTypeAndDayOfWeek(long portOperatorId, int transactionType, int dayOfWeekId) {

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotLimitsDefault> defaultBookingSlotLimitList = currentSession
            .createQuery("FROM BookingSlotLimitsDefault b where b.portOperatorId=:port_operator_id and b.transactionType=:transaction_type and b.dayOfWeekId=:day_of_week_id")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("day_of_week_id", dayOfWeekId).list();

        return defaultBookingSlotLimitList;
    }

    @Override
    public void getBookingSlotLimit(BookingSlotLimitJson bookingSlotLimitJson, Map<String, List<BookingSlotLimitJson>> bookingLimitMap, List<DayOfWeekJson> daysWeekList) {

        List<BookingSlotLimitsDefault> bookingSlotLimitsDefaultList = getDefaultBookingSlotLimitByPortOperatorAndTransactionType(bookingSlotLimitJson.getPortOperatorId(),
            bookingSlotLimitJson.getTransactionType());

        if (bookingSlotLimitJson.getPeriod().equalsIgnoreCase("DEFAULT")) {

            for (BookingSlotLimitsDefault bookingSlotLimitsDefault : bookingSlotLimitsDefaultList) {

                BookingSlotLimitJson json = new BookingSlotLimitJson();

                BeanUtils.copyProperties(bookingSlotLimitsDefault, json);
                json.setIsAllowUpdate(Boolean.TRUE);

                if (bookingLimitMap.containsKey(json.getHourSlotFromString() + " - " + json.getHourSlotToString())) {
                    bookingLimitMap.get(json.getHourSlotFromString() + " - " + json.getHourSlotToString()).add(json);
                } else {
                    List<BookingSlotLimitJson> jsonList = new ArrayList<>();
                    jsonList.add(json);
                    bookingLimitMap.put(json.getHourSlotFromString() + " - " + json.getHourSlotToString(), jsonList);
                }
            }

            for (DaysOfWeek dayOfWeek : DaysOfWeek.values()) {
                daysWeekList.add(new DayOfWeekJson(dayOfWeek.id, dayOfWeek.label, ServerConstants.DEFAULT_STRING, Boolean.TRUE));
            }

        } else {

            LocalDate startLocalDate = LocalDate.parse(bookingSlotLimitJson.getPeriod().split(ServerConstants.STRING_SPLIT_DATE)[0]);
            LocalDate endLocalDate = LocalDate.parse(bookingSlotLimitJson.getPeriod().split(ServerConstants.STRING_SPLIT_DATE)[1]);

            List<LocalDate> localDates = ServerUtil.getDatesBetween(startLocalDate, endLocalDate);

            Map<DaysOfWeek, LocalDate> datesMap = new HashMap<>();

            for (LocalDate localDate : localDates) {
                DaysOfWeek dayOfWeek = DaysOfWeek.getDayOfWeekByValue(localDate.getDayOfWeek().name());

                datesMap.put(dayOfWeek, localDate);

                DayOfWeekJson weekJson = new DayOfWeekJson();
                weekJson.setId(dayOfWeek.id);
                weekJson.setLabel(dayOfWeek.label);
                weekJson.setDateString(localDate.format(DateTimeFormatter.ofPattern(ServerConstants.dateFormatddMMyyyy)));
                weekJson.setIsAllowUpdate(localDate.isAfter(LocalDate.now()));
                daysWeekList.add(weekJson);
            }

            for (BookingSlotLimitsDefault bookingSlotLimitsDefault : bookingSlotLimitsDefaultList) {

                BookingSlotLimitJson json = new BookingSlotLimitJson();

                BeanUtils.copyProperties(bookingSlotLimitsDefault, json);

                LocalDate localDateSlot = datesMap.get(DaysOfWeek.getDayOfWeekById(json.getDayOfWeekId()));
                LocalDateTime localDateTimeSlot = localDateSlot.atTime(json.getHourSlotFrom(), 0, 0);
                Date dateSlot = Date.from(localDateSlot.atStartOfDay(ZoneId.systemDefault()).toInstant());

                json.setIsAllowUpdate(localDateTimeSlot.isAfter(LocalDateTime.now().minusHours(1)));

                BookingSlotLimit slotLimit = getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(bookingSlotLimitJson.getPortOperatorId(),
                    bookingSlotLimitJson.getTransactionType(), dateSlot, json.getHourSlotFrom());

                if (slotLimit != null) {
                    json.setBookingLimit(slotLimit.getBookingLimit());
                }

                BookingSlotCount slotCount = getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(bookingSlotLimitJson.getPortOperatorId(),
                    bookingSlotLimitJson.getTransactionType(), dateSlot, json.getHourSlotFrom());

                json.setBookingCount(slotCount == null ? ServerConstants.ZERO_INT : slotCount.getTripsBookedCount());

                if (bookingLimitMap.containsKey(json.getHourSlotFromString() + " - " + json.getHourSlotToString())) {
                    bookingLimitMap.get(json.getHourSlotFromString() + " - " + json.getHourSlotToString()).add(json);
                } else {
                    List<BookingSlotLimitJson> jsonList = new ArrayList<>();
                    jsonList.add(json);
                    bookingLimitMap.put(json.getHourSlotFromString() + " - " + json.getHourSlotToString(), jsonList);
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getBookingSlotCountTotal(Date dateSlot, int hourSlot) {

        int bookingSlotCount = 0;

        Session currentSession = sessionFactory.getCurrentSession();

        List<BookingSlotCount> bookingSlotCountList = currentSession.createQuery("FROM BookingSlotCount b where b.dateSlot=:date_slot and b.hourSlotFrom=:hour_slot_from")
            .setParameter("date_slot", dateSlot).setParameter("hour_slot_from", hourSlot).list();

        if (bookingSlotCountList != null && !bookingSlotCountList.isEmpty()) {

            for (BookingSlotCount b : bookingSlotCountList) {
                bookingSlotCount = bookingSlotCount + b.getTripsBookedCount();
            }
        }

        return bookingSlotCount;
    }

    @Override
    public void saveBookingSlot(BookingSlotCount bookingSlotCount) {

        hibernateTemplate.save(bookingSlotCount);
    }

    @Override
    public void updateBookingSlot(BookingSlotCount bookingSlotCount) {

        hibernateTemplate.update(bookingSlotCount);
    }

    @Override
    public void saveBookingSlotLimit(BookingSlotLimitJson bookingSlotLimitJson) throws PADException {

        Session session = null;
        Transaction tx = null;

        try {

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            int slotTo = bookingSlotLimitJson.getHourSlotFrom() == 23 ? 24 : bookingSlotLimitJson.getHourSlotTo();
            for (int slotFrom = bookingSlotLimitJson.getHourSlotFrom(); slotFrom < slotTo; slotFrom++) {

                BookingSlotLimit bookingSlotLimit = getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(bookingSlotLimitJson.getPortOperatorId(),
                    bookingSlotLimitJson.getTransactionType(), bookingSlotLimitJson.getDateSlot(), slotFrom);

                if (bookingSlotLimit == null) {

                    bookingSlotLimit = new BookingSlotLimit();

                    BeanUtils.copyProperties(bookingSlotLimitJson, bookingSlotLimit);
                    bookingSlotLimit.setHourSlotFrom(slotFrom);
                    bookingSlotLimit.setHourSlotTo(slotFrom == 23 ? 0 : slotFrom + 1);

                    session.save(bookingSlotLimit);

                    session.flush();
                    session.clear();

                } else {
                    bookingSlotLimit.setBookingLimit(bookingSlotLimitJson.getBookingLimit());

                    hibernateTemplate.update(bookingSlotLimit);
                }

            }

            tx.commit();

        } catch (Exception e) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "saveBookingSlotLimit");
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    @Override
    public void saveBookingSlotLimitDefault(BookingSlotLimitJson bookingSlotLimitJson) throws PADException {

        for (int slotFrom = bookingSlotLimitJson.getHourSlotFrom(); slotFrom < bookingSlotLimitJson.getHourSlotTo(); slotFrom++) {

            BookingSlotLimitsDefault bookingSlotLimitDefault = getDefaultBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(
                bookingSlotLimitJson.getPortOperatorId(), bookingSlotLimitJson.getTransactionType(), bookingSlotLimitJson.getDayOfWeekId(), slotFrom);

            bookingSlotLimitDefault.setBookingLimit(bookingSlotLimitJson.getBookingLimit());

            hibernateTemplate.update(bookingSlotLimitDefault);

        }

    }

    @Override
    public List<NameValuePair> getBookingSlotLimitPeriods() {

        List<NameValuePair> pairs = new ArrayList<>();
        DateTimeFormatter nameFormatter = DateTimeFormatter.ofPattern(ServerConstants.dateFormatddMMyyyy);
        DateTimeFormatter valueFormatter = DateTimeFormatter.ofPattern(ServerConstants.dateFormatyyyyMMdd_ISO);

        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        for (int i = 0; i < ServerConstants.BOOKING_SLOT_LIMIT_PERIOD_WEEKS; i++) {

            String name = monday.format(nameFormatter) + " - " + sunday.format(nameFormatter);
            String value = monday.format(valueFormatter) + ServerConstants.STRING_SPLIT_DATE + sunday.format(valueFormatter);

            pairs.add(new NameValuePair(name, value));

            monday = monday.plusWeeks(1);
            sunday = sunday.plusWeeks(1);
        }

        return pairs;

    }

    @Override
    public List<TripBookingTimeJson> getTripBookingHoursAvailability(long portOperatorId, int transactionType, Date dateSlot, long languageId, long roleId) throws PADException {

        TripBookingTimeJson tripBookingTimeJson = null;
        List<TripBookingTimeJson> tripBookingTimeJsonList = new ArrayList<>();
        String[] quarterHours = { "00", "15", "30", "45" };

        PortOperator portOperator = getPortOperatorFromMap(portOperatorId);
        if (portOperator == null)
            throw new PADException(ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_TEXT, "");

        for (int i = 0; i < 24; i++) {

            if (roleId == ServerConstants.OPERATOR_ROLE_TRANSPORTER) {

                tripBookingTimeJson = new TripBookingTimeJson();
                if (i < 10) {
                    tripBookingTimeJson.setTime("0" + i + "00");
                    tripBookingTimeJson.setTimeFormat("0" + i + ":00");

                } else {
                    tripBookingTimeJson.setTime(i + "00");
                    tripBookingTimeJson.setTimeFormat(i + ":00");
                }

                tripBookingTimeJson.setIsTimeSlotDisabled(false);

                tripBookingTimeJsonList.add(tripBookingTimeJson);

            } else {

                for (int j = 0; j < 4; j++) {

                    tripBookingTimeJson = new TripBookingTimeJson();
                    if (i < 10) {
                        tripBookingTimeJson.setTime("0" + i + quarterHours[j]);
                        tripBookingTimeJson.setTimeFormat("0" + i + ":" + quarterHours[j]);

                    } else {
                        tripBookingTimeJson.setTime(i + quarterHours[j]);
                        tripBookingTimeJson.setTimeFormat(i + ":" + quarterHours[j]);
                    }

                    tripBookingTimeJson.setIsTimeSlotDisabled(false);

                    tripBookingTimeJsonList.add(tripBookingTimeJson);
                }
            }
        }

        if (systemParameter.getIsBookingLimitCheckEnabled()) {

            // get day of week id from slot date
            Calendar calendarDateSlot = Calendar.getInstance();
            calendarDateSlot.setTime(dateSlot);

            final List<BookingSlotLimit> bookingSlotLimitList = getBookingSlotLimitListByPortOperatorAndTransactionTypeAndDateSlot(portOperatorId, transactionType, dateSlot);
            final List<BookingSlotLimitsDefault> defaultBookingSlotLimitList = getDefaultBookingSlotLimitListByPortOperatorAndTransactionTypeAndDayOfWeek(portOperatorId,
                transactionType, calendarDateSlot.get(Calendar.DAY_OF_WEEK));
            final List<BookingSlotCount> bookingSlotCountList = getBookingSlotCountListByPortOperatorAndTransactionTypeAndDateSlot(portOperatorId, transactionType, dateSlot);

            if (defaultBookingSlotLimitList != null && !defaultBookingSlotLimitList.isEmpty()) {

                for (BookingSlotLimitsDefault defaultBookingSlotLimitEntity : defaultBookingSlotLimitList) {

                    if (defaultBookingSlotLimitEntity.getBookingLimit() == 0) {

                        tripBookingTimeJsonList = disableBookingHourSlot(tripBookingTimeJsonList, defaultBookingSlotLimitEntity.getHourSlotFrom(), languageId);

                    }

                    for (BookingSlotCount bookingSlotCountEntity : bookingSlotCountList) {

                        if (bookingSlotCountEntity.getHourSlotFrom() == defaultBookingSlotLimitEntity.getHourSlotFrom()
                            && bookingSlotCountEntity.getHourSlotTo() == defaultBookingSlotLimitEntity.getHourSlotTo()) {

                            if ((bookingSlotCountEntity.getTripsBookedCount() + 1) > defaultBookingSlotLimitEntity.getBookingLimit()) {

                                tripBookingTimeJsonList = disableBookingHourSlot(tripBookingTimeJsonList, bookingSlotCountEntity.getHourSlotFrom(), languageId);
                            }
                        }
                    }
                }
            }

            if (bookingSlotLimitList != null && !bookingSlotLimitList.isEmpty()) {

                for (BookingSlotLimit bookingSlotLimitEntity : bookingSlotLimitList) {

                    if (bookingSlotLimitEntity.getBookingLimit() == 0) {

                        tripBookingTimeJsonList = disableBookingHourSlot(tripBookingTimeJsonList, bookingSlotLimitEntity.getHourSlotFrom(), languageId);

                    } else {
                        tripBookingTimeJsonList = enableBookingHourSlot(tripBookingTimeJsonList, bookingSlotLimitEntity.getHourSlotFrom());
                    }

                    for (BookingSlotCount bookingSlotCountEntity : bookingSlotCountList) {

                        if (bookingSlotCountEntity.getHourSlotFrom() == bookingSlotLimitEntity.getHourSlotFrom()
                            && bookingSlotCountEntity.getHourSlotTo() == bookingSlotLimitEntity.getHourSlotTo()) {

                            if ((bookingSlotCountEntity.getTripsBookedCount() + 1) > bookingSlotLimitEntity.getBookingLimit()) {

                                tripBookingTimeJsonList = disableBookingHourSlot(tripBookingTimeJsonList, bookingSlotCountEntity.getHourSlotFrom(), languageId);
                            }
                        }
                    }
                }
            }
        }

        return tripBookingTimeJsonList;
    }

    private List<TripBookingTimeJson> disableBookingHourSlot(List<TripBookingTimeJson> tripBookingTimeJsonList, int hourSlotToDisable, long languageId) {

        String labelNA = languageId == ServerConstants.LANGUAGE_EN_ID ? " - Not Available" : " - Indisponible";

        for (int i = 0; i < tripBookingTimeJsonList.size(); i++) {

            TripBookingTimeJson tripBookingTimeJson = tripBookingTimeJsonList.get(i);

            int hourSlot = Integer.parseInt(tripBookingTimeJson.getTime().substring(0, 2));

            if (hourSlot == hourSlotToDisable) {
                tripBookingTimeJson.setTimeFormat(tripBookingTimeJson.getTimeFormat() + labelNA);
                tripBookingTimeJson.setIsTimeSlotDisabled(true);

                tripBookingTimeJsonList.set(i, tripBookingTimeJson);
            }
        }

        return tripBookingTimeJsonList;
    }

    private List<TripBookingTimeJson> enableBookingHourSlot(List<TripBookingTimeJson> tripBookingTimeJsonList, int hourSlotToEnable) {

        for (int i = 0; i < tripBookingTimeJsonList.size(); i++) {

            TripBookingTimeJson tripBookingTimeJson = tripBookingTimeJsonList.get(i);

            if (tripBookingTimeJson.getIsTimeSlotDisabled()) {

                int hourSlot = Integer.parseInt(tripBookingTimeJson.getTime().substring(0, 2));

                if (hourSlot == hourSlotToEnable) {
                    tripBookingTimeJson.setTimeFormat(tripBookingTimeJson.getTimeFormat().substring(0, 5));
                    tripBookingTimeJson.setIsTimeSlotDisabled(false);

                    tripBookingTimeJsonList.set(i, tripBookingTimeJson);
                }
            }
        }

        return tripBookingTimeJsonList;
    }

    @Override
    public void reserveBookingSlot(int portOperatorId, int transactionType, Date dateSlot) throws PADException, PADValidationException {

        if (getSystemParameter().getIsBookingLimitCheckEnabled()) {
            // check if there is an entry for the booking slot for this trip
            PortOperator portOperator = getPortOperatorFromMap(portOperatorId);
            if (portOperator == null)
                throw new PADException(ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.INVALID_PORT_OPERATOR_TYPE_TEXT, "");

            // get day of week id and hour of day from slot date
            Calendar calendarDateSlot = Calendar.getInstance();
            calendarDateSlot.setTime(dateSlot);

            int dayOfWeekId = calendarDateSlot.get(Calendar.DAY_OF_WEEK);
            int hourOfSlot = calendarDateSlot.get(Calendar.HOUR_OF_DAY);

            calendarDateSlot.add(Calendar.HOUR_OF_DAY, 1);

            int hourOfSlotPlus1 = calendarDateSlot.get(Calendar.HOUR_OF_DAY);

            BookingSlotCount bookingSlotCount = getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(portOperator.getId(), transactionType, dateSlot, hourOfSlot);

            if (bookingSlotCount == null) {
                // add the booking slot
                BookingSlotCount bookingSlot = new BookingSlotCount();
                bookingSlot.setPortOperatorId(portOperator.getId());
                bookingSlot.setTransactionType(transactionType);
                bookingSlot.setDateSlot(dateSlot);
                bookingSlot.setHourSlotFrom(hourOfSlot);
                bookingSlot.setHourSlotTo(hourOfSlotPlus1);
                bookingSlot.setTripsBookedCount(1);

                saveBookingSlot(bookingSlot);

            } else {
                // check whether trips booked for this slot for this port operator & transaction type exceed the total allowed number of booked trips per hour
                final BookingSlotLimit bookingSlotLimitEntity = getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(portOperatorId, transactionType, dateSlot,
                    hourOfSlot);

                final List<BookingSlotLimitsDefault> defaultBookingSlotLimitList = getDefaultBookingSlotLimitListByPortOperatorAndTransactionTypeAndDayOfWeek(portOperatorId,
                    transactionType, dayOfWeekId);

                if (bookingSlotLimitEntity != null) {

                    if ((bookingSlotCount.getTripsBookedCount() + 1) > bookingSlotLimitEntity.getBookingLimit())
                        throw new PADValidationException(ServerResponseConstants.BOOKING_TRIP_LIMIT_REACHED_FOR_PORT_OPERATOR_CODE,
                            ServerResponseConstants.BOOKING_TRIP_LIMIT_REACHED_FOR_PORT_OPERATOR_TEXT, "");

                } else if (defaultBookingSlotLimitList != null && !defaultBookingSlotLimitList.isEmpty()) {

                    for (BookingSlotLimitsDefault b : defaultBookingSlotLimitList) {
                        if (hourOfSlot >= b.getHourSlotFrom() && hourOfSlotPlus1 <= b.getHourSlotTo()) {

                            if ((bookingSlotCount.getTripsBookedCount() + 1) > b.getBookingLimit())
                                throw new PADValidationException(ServerResponseConstants.BOOKING_TRIP_LIMIT_REACHED_FOR_PORT_OPERATOR_CODE,
                                    ServerResponseConstants.BOOKING_TRIP_LIMIT_REACHED_FOR_PORT_OPERATOR_TEXT, "");

                            break;
                        }
                    }
                }

                // check whether trips booked for this hour of day for all operators exceed the global number of booked trips per hour
                if ((getBookingSlotCountTotal(dateSlot, hourOfSlot)) + 1 > getSystemParameter().getBookingLimitPerHour())
                    throw new PADValidationException(ServerResponseConstants.BOOKING_TRIP_SLOT_LIMIT_REACHED_CODE, ServerResponseConstants.BOOKING_TRIP_SLOT_LIMIT_REACHED_TEXT,
                        "");

                // increment booking slot counter
                bookingSlotCount.setTripsBookedCount(bookingSlotCount.getTripsBookedCount() + 1);

                updateBookingSlot(bookingSlotCount);
            }
        }
    }

    @Override
    public Map<Long, List<PortOperatorTransactionTypeJson>> getPortOperatorTransactionTypesMap(long loggedOperatorRoleId) {

        Map<Long, List<PortOperatorTransactionTypeJson>> portOperatorTransactionTypesMap = new ConcurrentHashMap<>();

        List<PortOperatorTransactionTypeJson> portOperatorTransactionTypesList = null;

        for (PortOperator portOperator : portOperatorList) {

            portOperatorTransactionTypesList = new ArrayList<>();

            for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

                PortOperatorGate portOperatorGate = portOperatorTransactionType.getPortOperatorGate();

                portOperatorTransactionTypesList.add(new PortOperatorTransactionTypeJson(portOperator.getName(), portOperator.getNameShort(), portOperatorGate.getId(),
                    portOperatorGate.getGateNumber(), portOperatorGate.getGateNumberShort(), portOperatorTransactionType.getTransactionType(),
                    portOperatorTransactionType.getTranslateKey(), portOperatorTransactionType.getIsAllowedForParkingAndKioskOp(),
                    portOperatorTransactionType.getIsAllowedForVirtualKioskOp(), portOperatorTransactionType.getIsDirectToPort(),
                    portOperatorTransactionType.getIsAllowMultipleEntries(), portOperatorTransactionType.getIsTripCancelSystem(),
                    portOperatorTransactionType.getMissionCancelSystemAfterMinutes(), portOperatorTransactionType.getTripCancelSystemAfterMinutes()));
            }

            if (portOperatorTransactionTypesList.size() > 0) {
                portOperatorTransactionTypesMap.put(portOperator.getId(), portOperatorTransactionTypesList);
            }
        }

        return portOperatorTransactionTypesMap;
    }

    @Override
    public Map<Long, List<ParkingReleaseStatsJson>> getParkingReleaseStatsMap() throws SQLException {

        Map<Long, List<ParkingReleaseStatsJson>> getParkingReleaseStatsMap = new ConcurrentHashMap<>();

        List<ParkingReleaseStatsJson> parkingReleaseStatsList = null;
        List<VehicleRegistrationJson> releasedVehicleRegistrationList = null;

        int parkingSessionsCount = 0;
        int vehiclesAlreadyReleasedCount = 0; // in the last 60 minutes
        int bookingLimitCount = 0;

        ParkingReleaseStatsJson parkingReleaseStatsJson = null;
        BookingSlotLimit bookingSlotLimitEntity = null;
        BookingSlotLimitsDefault bookingSlotLimitDefaultEntity = null;

        Calendar calendarDateToday = Calendar.getInstance();
        calendarDateToday.setTime(new Date());

        Calendar calendarDateTodayMinusSixtyMin = Calendar.getInstance();
        calendarDateTodayMinusSixtyMin.setTime(calendarDateToday.getTime());
        calendarDateTodayMinusSixtyMin.add(Calendar.SECOND, -3600);

        int hourOfDay = calendarDateToday.get(Calendar.HOUR_OF_DAY);
        int dayOfWeekId = calendarDateToday.get(Calendar.DAY_OF_WEEK);

        final List<Parking> parkingList = parkingService.getActiveParkingList(ServerConstants.DEFAULT_INT, ServerConstants.DEFAULT_INT, true);

        for (PortOperator portOperator : portOperatorList) {

            parkingReleaseStatsList = new ArrayList<>();

            for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

                if (portOperatorTransactionType.getIsAllowedForParkingAndKioskOp()) {

                    releasedVehicleRegistrationList = new ArrayList<>();
                    parkingSessionsCount = 0;
                    vehiclesAlreadyReleasedCount = 0;
                    bookingLimitCount = 0;

                    for (Parking parking : parkingList) {

                        if (portOperator.getId() == parking.getPortOperatorId() && portOperatorTransactionType.getTransactionType() == parking.getTransactionType()) {

                            if (parking.getStatus() == ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE) {

                                long secondsSinceRelease = (calendarDateToday.getTime().getTime() - parking.getDateSmsExit().getTime()) / 1000;

                                if (secondsSinceRelease > 1800l) {
                                    releasedVehicleRegistrationList.add(new VehicleRegistrationJson(parking.getVehicleRegistration(), "#ff0000"));
                                } else if (secondsSinceRelease > 900l && secondsSinceRelease <= 1800l) {
                                    releasedVehicleRegistrationList.add(new VehicleRegistrationJson(parking.getVehicleRegistration(), "#ff751a"));
                                } else {
                                    releasedVehicleRegistrationList.add(new VehicleRegistrationJson(parking.getVehicleRegistration(), "#000000"));
                                }
                            }

                            parkingSessionsCount++;
                        }
                    }

                    vehiclesAlreadyReleasedCount = parkingService.getVehicleReleasedCountInLastXMinutes(portOperator.getId(), portOperatorTransactionType.getTransactionType(),
                        calendarDateTodayMinusSixtyMin.getTime());

                    bookingSlotLimitEntity = getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(portOperator.getId(),
                        portOperatorTransactionType.getTransactionType(), calendarDateToday.getTime(), hourOfDay);

                    if (bookingSlotLimitEntity != null) {
                        bookingLimitCount = bookingSlotLimitEntity.getBookingLimit();

                    } else {
                        bookingSlotLimitDefaultEntity = getDefaultBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(portOperator.getId(),
                            portOperatorTransactionType.getTransactionType(), dayOfWeekId, hourOfDay);

                        if (bookingSlotLimitDefaultEntity != null) {
                            bookingLimitCount = bookingSlotLimitDefaultEntity.getBookingLimit();
                        }
                    }

                    parkingReleaseStatsJson = new ParkingReleaseStatsJson();
                    parkingReleaseStatsJson.setTransactionType(portOperatorTransactionType.getTransactionType());
                    parkingReleaseStatsJson.setTranslateKey(portOperatorTransactionType.getTranslateKey());
                    parkingReleaseStatsJson.setTranslateKeyShort(portOperatorTransactionType.getTranslateKeyShort());
                    parkingReleaseStatsJson.setParkingSessionsCount(parkingSessionsCount);
                    parkingReleaseStatsJson.setIsAutoReleaseOn(portOperatorTransactionType.getIsAutoReleaseParking());
                    parkingReleaseStatsJson.setBookingLimitCount(bookingLimitCount);
                    parkingReleaseStatsJson.setVehiclesAlreadyReleasedCount(vehiclesAlreadyReleasedCount);
                    parkingReleaseStatsJson.setReleasedVehicleRegistrationList(releasedVehicleRegistrationList);

                    parkingReleaseStatsList.add(parkingReleaseStatsJson);
                }
            }

            if (parkingReleaseStatsList.size() > 0) {
                getParkingReleaseStatsMap.put(portOperator.getId(), parkingReleaseStatsList);
            }
        }

        return getParkingReleaseStatsMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getPortOperatorGateNumberById(long id) {

        String gateNumber = "";

        if (id > 0) {
            List<PortOperatorGate> portOperatorGateList = (List<PortOperatorGate>) hibernateTemplate.findByNamedParam("FROM PortOperatorGate WHERE id = :id", "id", id);

            if (portOperatorGateList != null && !portOperatorGateList.isEmpty()) {
                gateNumber = portOperatorGateList.get(0).getGateNumber();
            }
        }
        return gateNumber;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getPortOperatorGateNumberShortById(long id) {

        String gateNumberShort = "";

        if (id > 0) {
            List<PortOperatorGate> portOperatorGateList = (List<PortOperatorGate>) hibernateTemplate.findByNamedParam("FROM PortOperatorGate WHERE id = :id", "id", id);

            if (portOperatorGateList != null && !portOperatorGateList.isEmpty()) {
                gateNumberShort = portOperatorGateList.get(0).getGateNumberShort();
            }
        }
        return gateNumberShort;
    }

    @SuppressWarnings("unchecked")
    @Override
    public long getPortOperatorAnprZoneIdById(long id) {

        long anprZoneId = -1;

        if (id > 0) {
            List<PortOperatorGate> portOperatorGateList = (List<PortOperatorGate>) hibernateTemplate.findByNamedParam("FROM PortOperatorGate WHERE id = :id", "id", id);

            if (portOperatorGateList != null && !portOperatorGateList.isEmpty()) {
                anprZoneId = portOperatorGateList.get(0).getAnprZoneId();
            }
        }
        return anprZoneId;
    }

    @Override
    public List<PortOperatorJson> getPortOperatorJsonList() {

        List<PortOperatorJson> portOperatorJsonList = new ArrayList<>();

        for (PortOperator portOperator : portOperatorList) {
            List<IndependentPortOperatorJson> independentPortOperatorJsonList = new ArrayList<>();

            portOperatorIndependentPortOperatorList.stream()
                .filter(portOperatorIndependentPortOperator -> portOperatorIndependentPortOperator.getPortOperatorId() == portOperator.getId())
                .forEach(portOperatorIndependentPortOperator -> independentPortOperatorList.stream()
                    .filter(independentPortOperator -> independentPortOperator.getId() == portOperatorIndependentPortOperator.getIndependentPortOperatorId())
                    .map(independentPortOperator -> new IndependentPortOperatorJson(independentPortOperator.getCode(), independentPortOperator.getName(),
                        independentPortOperator.getNameShort()))
                    .forEach(independentPortOperatorJsonList::add));

            portOperatorJsonList
                .add(new PortOperatorJson(portOperator.getId(), portOperator.getName(), portOperator.getNameShort(), portOperator.getIsActive(), independentPortOperatorJsonList));
        }

        return portOperatorJsonList;
    }

    @Override
    public List<PortOperator> getPortOperators() {

        return portOperatorList;
    }

    @Override
    public List<IndependentPortOperator> getIndependentPortOperators() {

        return independentPortOperatorList;
    }

    @Override
    public PortOperatorTransactionType getPortOperatorTransactionTypeEntity(long portOperatorId, int transactionType) throws PADException {

        PortOperatorTransactionType portOperatorTransactionTypeEntity = null;

        PortOperator portOperator = getPortOperatorFromMap(portOperatorId);

        for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

            if (portOperatorTransactionType.getTransactionType() == transactionType) {
                portOperatorTransactionTypeEntity = portOperatorTransactionType;
                break;
            }
        }

        if (portOperatorTransactionTypeEntity == null)
            throw new PADException(ServerResponseConstants.INVALID_TRANSACTION_TYPE_CODE, ServerResponseConstants.INVALID_TRANSACTION_TYPE_TEXT,
                "#getPortOperatorTransactionTypeEntity");

        return portOperatorTransactionTypeEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PortOperatorTransactionType> getPortOperatorTransactionTypes() {

        Session currentSession = sessionFactory.getCurrentSession();

        List<PortOperatorTransactionType> portOperatorTransactionTypeList = currentSession.createQuery("FROM PortOperatorTransactionType").list();

        return portOperatorTransactionTypeList;
    }

    @Override
    public void savePortOperatorAlert(PortOperatorAlert portOperatorAlert) {

        portOperatorAlert.setDateCreated(new Date());

        hibernateTemplate.save(portOperatorAlert);

        // TODO MANAGE BOOKING LIMITS

        sendIssueAlertEmail(portOperatorAlert, ServerConstants.EMAIL_PORT_OPERATOR_ISSUE_ALERT_CREATED_TEMPLATE_TYPE);
    }

    private void sendIssueAlertEmail(PortOperatorAlert portOperatorAlert, long emailType) {
        Email scheduledEmail = new Email();
        scheduledEmail.setEmailTo(getSystemParameter().getAgsOperationsEmail());
        scheduledEmail.setLanguageId(ServerConstants.LANGUAGE_FR_ID);
        scheduledEmail.setAccountId(ServerConstants.DEFAULT_LONG);
        scheduledEmail.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduledEmail.setTripId(ServerConstants.DEFAULT_LONG);

        HashMap<String, Object> params = new HashMap<>();
        params.put("transactionType", ServerUtil.getTransactionTypeName(portOperatorAlert.getTransactionType(), ServerConstants.LANGUAGE_FR_ID));
        params.put("portOperator", getPortOperatorNameById(portOperatorAlert.getPortOperatorId()));
        params.put("workingCapacity", portOperatorAlert.getWorkingCapacity() + "%");
        params.put("description", portOperatorAlert.getDescription());
        params.put("resolutionDescription", portOperatorAlert.getResolutionDescription());

        try {
            params.put("issueDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portOperatorAlert.getDateIssue()));
        } catch (ParseException e) {
            params.put("issueDate", ServerConstants.DEFAULT_STRING);
        }

        if (portOperatorAlert.getDateResolutionEstimate() != null) {
            try {
                params.put("estimateResolutionDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portOperatorAlert.getDateResolutionEstimate()));
            } catch (ParseException e) {
                params.put("estimateResolutionDate", ServerConstants.DEFAULT_STRING);
            }
        } else {
            params.put("estimateResolutionDate", ServerConstants.DEFAULT_STRING);
        }

        if (portOperatorAlert.getDateResolution() != null) {
            try {
                params.put("resolutionDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portOperatorAlert.getDateResolution()));
            } catch (ParseException e) {
                params.put("resolutionDate", ServerConstants.DEFAULT_STRING);
            }
        } else {
            params.put("resolutionDate", ServerConstants.DEFAULT_STRING);
        }

        try {
            emailService.scheduleEmailByType(scheduledEmail, emailType, params);
        } catch (PADException e) {
            logger.info("savePortOperatorAlert#scheduleEmailByType###Exception ", e);
        }
    }

    @Override
    public long getIssueCount(Long portOperatorId) {

        PreparedJDBCQuery query = getIssueQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, portOperatorId);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getIssueQuery(int queryType, Long portOperatorId) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append(" SELECT COUNT(id) ");
            query.append(" FROM pad.port_operator_alerts ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT * ");
            query.append(" FROM pad.port_operator_alerts ");
        }

        query.append(" WHERE port_operator_id = ? ");
        query.addQueryParameter(portOperatorId);

        return query;
    }

    @Override
    public List<PortOperatorAlertJson> getIssueList(Long portOperatorId, String sortColumn, boolean sortAsc, int startLimit, int endLimit) {

        final List<PortOperatorAlertJson> issueList = new ArrayList<>();

        PreparedJDBCQuery query = getIssueQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, portOperatorId);

        query.setSortParameters(sortColumn, sortAsc, "port_operator_alerts", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
        query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                PortOperatorAlertJson alertJson = new PortOperatorAlertJson();
                alertJson.setCode(rs.getString("code"));
                alertJson.setPortOperatorId(rs.getLong("port_operator_id"));
                alertJson.setTransactionType(rs.getInt("transaction_type"));
                alertJson.setWorkingCapacityPercentage(rs.getInt("working_capacity"));
                alertJson.setDescription(rs.getString("description"));
                alertJson.setNameReporter(rs.getString("name_reporter"));
                alertJson.setMsisdnReporter(rs.getString("msisdn_reporter"));
                alertJson.setIsResolved(rs.getBoolean("is_resolved"));
                alertJson.setResolutionDescription(rs.getString("resolution_description"));

                try {
                    alertJson.setIssueDateString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_issue")));
                } catch (Exception e) {
                    alertJson.setIssueDateString(ServerConstants.DEFAULT_STRING);
                }

                try {
                    alertJson.setEstimatedResolutionDateString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_resolution_estimate")));
                } catch (Exception e) {
                    alertJson.setEstimatedResolutionDateString(ServerConstants.DEFAULT_STRING);
                }

                try {
                    alertJson.setDateResolutionString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_resolution")));
                } catch (Exception e) {
                    alertJson.setDateResolutionString(ServerConstants.DEFAULT_STRING);
                }

                try {
                    alertJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_created")));
                } catch (Exception e) {
                    alertJson.setDateCreatedString(ServerConstants.DEFAULT_STRING);
                }

                issueList.add(alertJson);
            }
        }, query.getQueryParameters());

        return issueList;
    }

    @Override
    public void updatePortOperatorAlert(PortOperatorAlert portOperatorAlert) {

        portOperatorAlert.setDateEdited(new Date());

        hibernateTemplate.update(portOperatorAlert);

        // TODO MANAGE BOOKING LIMITS

        sendIssueAlertEmail(portOperatorAlert, ServerConstants.EMAIL_PORT_OPERATOR_ISSUE_ALERT_UPDATED_TEMPLATE_TYPE);

    }

    @Override
    public void resolvePortOperatorAlert(PortOperatorAlert portOperatorAlert) {

        portOperatorAlert.setIsResolved(true);
        portOperatorAlert.setDateEdited(new Date());

        hibernateTemplate.update(portOperatorAlert);

        // TODO MANAGE BOOKING LIMITS

        sendIssueAlertEmail(portOperatorAlert, ServerConstants.EMAIL_PORT_OPERATOR_ISSUE_ALERT_RESOLVED_TEMPLATE_TYPE);

    }

    @SuppressWarnings("unchecked")
    @Override
    public PortOperatorAlert getPortOperatorAlertByCode(String code) {

        PortOperatorAlert portOperatorAlert = null;

        List<PortOperatorAlert> portOperatorAlertList = (List<PortOperatorAlert>) hibernateTemplate.findByNamedParam("FROM PortOperatorAlert WHERE code = :code", "code", code);

        if (portOperatorAlertList != null && !portOperatorAlertList.isEmpty()) {
            portOperatorAlert = portOperatorAlertList.get(0);
        }

        return portOperatorAlert;
    }

    @Override
    public String getPortOperatorNameById(long portOperatorId) {

        PortOperator portOperator = getPortOperatorFromMap(portOperatorId);

        return portOperator == null ? "" : portOperator.getName();
    }

    @Override
    public String getPortOperatorShortNameById(long portOperatorId) {

        PortOperator portOperator = getPortOperatorFromMap(portOperatorId);

        return portOperator == null ? "" : portOperator.getNameShort();
    }

    @Override
    public IndependentPortOperator getIndependentPortOperatorByCode(String code) {

        if (StringUtils.isNotBlank(code)) {
            for (IndependentPortOperator independentPortOperator : getIndependentPortOperators()) {
                if (independentPortOperator.getCode().equalsIgnoreCase(code))
                    return independentPortOperator;
            }
        }

        return null;
    }

    @Override
    public String getIndependentPortOperatorNameByCode(String code) {

        if (StringUtils.isNotBlank(code)) {
            for (IndependentPortOperator independentPortOperator : getIndependentPortOperators()) {
                if (independentPortOperator.getCode().equalsIgnoreCase(code))
                    return independentPortOperator.getName();
            }
        }

        return "";
    }

    @Override
    public IndependentPortOperator getIndependentPortOperatorById(long independentPortOperatorId) {

        for (IndependentPortOperator independentPortOperator : getIndependentPortOperators()) {
            if (independentPortOperator.getId() == independentPortOperatorId)
                return independentPortOperator;
        }

        return null;
    }

    @Override
    public String getIndependentPortOperatorCodeById(long independentPortOperatorId) {

        for (IndependentPortOperator independentPortOperator : getIndependentPortOperators()) {
            if (independentPortOperator.getId() == independentPortOperatorId)
                return independentPortOperator.getCode();
        }

        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortOperatorGate getPortOperatorGateById(long id) {

        PortOperatorGate portOperatorGate = null;

        if (id > 0) {
            List<PortOperatorGate> portOperatorGateList = (List<PortOperatorGate>) hibernateTemplate.findByNamedParam("FROM PortOperatorGate WHERE id = :id", "id", id);

            if (portOperatorGateList != null && !portOperatorGateList.isEmpty()) {
                portOperatorGate = portOperatorGateList.get(0);
            }
        }
        return portOperatorGate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PortOperatorGate> getPortOperatorGates() {

        return (List<PortOperatorGate>) hibernateTemplate.find("FROM PortOperatorGate");
    }
}
