package com.pad.server.base.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;

public class ServerUtil {

    public static Date parseDate(String dateFormat, String dateString) throws ParseException {

        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateString);
    }

    public static String formatDate(String dateFormat, Date date) throws ParseException {

        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date);
    }

    public static String restrictLength(String str, int length) {

        return ((str != null && str.length() > length) ? str.substring(0, length) : str);
    }

    public static int getStartLimitPagination(int currentPage, int pageBookingCount) {
        return currentPage > 1 ? (currentPage - 1) * pageBookingCount : 0;
    }

    public static String[] getValidEmails(String[] emailsStrings, String responseSource) throws PADException {
        Arrays.stream(emailsStrings).forEach(e -> e = e.trim());
        Pattern pattern = Pattern.compile(ServerConstants.REGEX_EMAIL);
        for (String email : emailsStrings) {
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches())
                throw new PADException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, responseSource + "1#");
        }
        return emailsStrings;
    }

    public static boolean isMsisdn(String msisdn) {
        return StringUtils.isNotBlank(msisdn) && (msisdn.startsWith(ServerConstants.DIAL_CODE_SENEGAL + ServerConstants.MSISDN_PREFIX_MALI_SENEGAL)
            || msisdn.startsWith(ServerConstants.DIAL_CODE_MALI + ServerConstants.MSISDN_PREFIX_MALI_SENEGAL));
    }

    public static String getValidNumber(String numberString, String responseSource) throws PADException, PADValidationException {

        numberString = numberString.toUpperCase().trim();
        numberString = numberString.replaceAll(" ", "");
        numberString = numberString.replaceAll("-", "");
        numberString = numberString.replaceAll(",", "");
        numberString = numberString.replaceAll("#", "");
        numberString = numberString.replaceAll("_", "");
        numberString = numberString.replaceAll("=", "");
        numberString = numberString.replaceAll(":", "");
        numberString = numberString.replaceAll("!", "");
        numberString = numberString.replaceAll("\\+", "");
        numberString = numberString.replaceAll("\\.", "");
        numberString = numberString.replaceAll("\\*", "");
        numberString = numberString.replaceAll("\\(", "");
        numberString = numberString.replaceAll("\\)", "");
        numberString = numberString.replaceAll("\\[", "");
        numberString = numberString.replaceAll("\\]", "");
        numberString = numberString.replaceAll("\\{", "");
        numberString = numberString.replaceAll("\\}", "");
        numberString = numberString.replaceAll("O", "0");

        try {
            if (Long.parseLong(numberString) <= 0)
                throw new PADException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, responseSource + "1#");
        } catch (Exception e) {
            throw new PADException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, responseSource + "2#");
        }

        if (!numberString.startsWith(ServerConstants.DIAL_CODE_SENEGAL) && !numberString.startsWith(ServerConstants.DIAL_CODE_MALI)
            && !numberString.startsWith(ServerConstants.DIAL_CODE_IRELAND))
            throw new PADValidationException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, responseSource + "3#");

        return numberString;
    }

    public static String formatVehicleRegNumber(String vehicleRegNumber) throws PADException {

        if (StringUtils.isBlank(vehicleRegNumber))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "formatVehicleRegNumber#");

        vehicleRegNumber = vehicleRegNumber.toUpperCase().trim();
        vehicleRegNumber = vehicleRegNumber.replaceAll(" ", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("-", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll(",", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("#", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("_", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("=", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll(":", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("!", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\+", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\.", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\*", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\(", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\)", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\[", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\]", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\{", "");
        vehicleRegNumber = vehicleRegNumber.replaceAll("\\}", "");

        return vehicleRegNumber;
    }

    public static long getLanguageIdByCode(String languageCode) {
        switch (languageCode) {
            case "EN":
                return ServerConstants.LANGUAGE_EN_ID;

            case "FR":
                return ServerConstants.LANGUAGE_FR_ID;

            default:
                return ServerConstants.LANGUAGE_FR_ID;
        }
    }

    public static String getLanguageCodeById(long languageId) {
        switch ((int) languageId) {
            case (int) ServerConstants.LANGUAGE_EN_ID:
                return "EN";

            case (int) ServerConstants.LANGUAGE_FR_ID:
                return "FR";

            default:
                return "FR";
        }
    }

    public static String getTransactionTypeName(int transacionType, long languageId) {

        String transactionTypeName = "";

        if (transacionType == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EXPORT) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Drop off Export" : "Exportation";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_DAY_M) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Drop off Empty - Day M" : "Déposer vide - Jour M";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_DAY_S) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Drop off Empty - Day S" : "Déposer vide - Jour S";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Drop off Empty - Triangle" : "Déposer vide - Triangle";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Drop off Empty - Night" : "Déposer vide - Nuit";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_PICK_UP_IMPORT) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Pick up Import" : "Importation";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_PICK_UP_EMPTY) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Pick up Empty" : "Collecter vide";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_URGENT_DROP_OFF_EXPORT) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Urgent Drop off Export" : "Exportation Urgente";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_URGENT_PICK_UP_IMPORT) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Urgent Pick up Import" : "Importation Urgente";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_WHITELIST) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Whitelisted" : "Liste blanche";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_DROP_OFF_IMPORT_DRAY_IN) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Drop off Import Dray In" : "Déposer Importer Dray In";

        } else if (transacionType == ServerConstants.TRANSACTION_TYPE_PICK_UP_EXPORT_DRAY_OFF) {
            transactionTypeName = languageId == ServerConstants.LANGUAGE_EN_ID ? "Pick Up Export Dray Off" : "Exporter Dray Off";
        }

        return transactionTypeName;
    }

    public static long getMnoIdFromPaymentOption(int paymentOption) {

        switch (paymentOption) {
            case ServerConstants.PAYMENT_OPTION_ORANGE_MONEY:
                return ServerConstants.MNO_ID_ORANGE_MONEY;

            case ServerConstants.PAYMENT_OPTION_WARI:
                return ServerConstants.MNO_ID_WARI;

            case ServerConstants.PAYMENT_OPTION_FREE_MONEY:
                return ServerConstants.MNO_ID_FREE_MONEY;

            case ServerConstants.PAYMENT_OPTION_E_MONEY:
                return ServerConstants.MNO_ID_E_MONEY;

            case ServerConstants.PAYMENT_OPTION_ECOBANK:
                return ServerConstants.MNO_ID_ECO_BANK;

            default:
                return ServerConstants.DEFAULT_LONG;
        }
    }

    public static String getPaymentOptionDescriptionById(int paymentOptionId, long languageId) {

        String PaymentOptionDescription = "";

        if (paymentOptionId == ServerConstants.PAYMENT_OPTION_CASH) {
            PaymentOptionDescription = languageId == ServerConstants.LANGUAGE_EN_ID ? "Cash" : "En espèces";

        } else if (paymentOptionId == ServerConstants.PAYMENT_OPTION_ORANGE_MONEY) {
            PaymentOptionDescription = languageId == ServerConstants.LANGUAGE_EN_ID ? "Orange Money" : "Orange Money";

        } else if (paymentOptionId == ServerConstants.PAYMENT_OPTION_WARI) {
            PaymentOptionDescription = languageId == ServerConstants.LANGUAGE_EN_ID ? "Wari" : "Wari";

        } else if (paymentOptionId == ServerConstants.PAYMENT_OPTION_FREE_MONEY) {
            PaymentOptionDescription = languageId == ServerConstants.LANGUAGE_EN_ID ? "Free Money" : "Free Money";

        } else if (paymentOptionId == ServerConstants.PAYMENT_OPTION_E_MONEY) {
            PaymentOptionDescription = languageId == ServerConstants.LANGUAGE_EN_ID ? "E-Money" : "E-Money";

        } else if (paymentOptionId == ServerConstants.PAYMENT_OPTION_ECOBANK) {
            PaymentOptionDescription = languageId == ServerConstants.LANGUAGE_EN_ID ? "Ecobank" : "Ecobank";

        }

        return PaymentOptionDescription;
    }

    public static String getMnoNameByMnoId(long mnoId) {

        if (mnoId == ServerConstants.MNO_ID_ORANGE_MONEY)
            return "Orange Money";
        else if (mnoId == ServerConstants.MNO_ID_WARI)
            return "Wari";
        else if (mnoId == ServerConstants.MNO_ID_FREE_MONEY)
            return "Free Money";
        else if (mnoId == ServerConstants.MNO_ID_E_MONEY)
            return "E-Money";
        else if (mnoId == ServerConstants.MNO_ID_ECO_BANK)
            return "Ecobank";
        else
            return ServerConstants.DEFAULT_STRING;
    }

    public static String getParkingStatusNameByStatusId(int status) {

        switch (status) {
            case ServerConstants.PARKING_STATUS_ENTRY:
                return "ENTRY";

            case ServerConstants.PARKING_STATUS_EXIT:
                return "EXIT";

            case ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE:
                return "EXIT DUE";

            case ServerConstants.PARKING_STATUS_ENTERED_PORT:
                return "ENTERED PORT";

            case ServerConstants.PARKING_STATUS_IN_TRANSIT_EXPIRED:
                return "IN TRANSIT EXPIRED";

            default:
                return "";
        }
    }

    public static String getPortAccessStatusNameByStatusId(int status) {

        switch (status) {
            case ServerConstants.PORT_ACCESS_STATUS_ENTRY:
                return "ENTRY";

            case ServerConstants.PORT_ACCESS_STATUS_DENY:
                return "DENY";

            case ServerConstants.PORT_ACCESS_STATUS_EXIT:
                return "EXIT";

            case ServerConstants.PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM:
                return "EXIT - CLOSED BY SYSTEM";

            default:
                return "";
        }
    }

    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // + 1 to include end date

        return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(i -> startDate.plusDays(i)).collect(Collectors.toList());
    }

    public static String getSymbolByCurrencyCode(String currencyCode) {

        switch (currencyCode) {
            case "XOF":
                return "CFA";

            default:
                return "CFA";
        }

    }

    public static Date getDatePlusDays(String dateString, String format, int daysToAdd) throws ParseException {
        Date futureDate;
        Date originalDateTo = parseDate(format, dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDateTo);
        calendar.add(Calendar.DATE, daysToAdd);
        futureDate = calendar.getTime();
        return futureDate;
    }

    public static BigDecimal calculateAmountTax(BigDecimal amountWithTax, BigDecimal taxAmount) {
        if (amountWithTax.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal tax = new BigDecimal(100).add(taxAmount);
            BigDecimal amount = amountWithTax.multiply(new BigDecimal(100));
            BigDecimal amountWithoutTax = amount.divide(tax, 0, RoundingMode.HALF_UP);

            return amountWithTax.subtract(amountWithoutTax);
        } else
            return BigDecimal.ZERO;

    }

    public static String formatBigDecimal(BigDecimal number) {
        return NumberFormat.getInstance(Locale.FRENCH).format(number);
    }

    public static Date getDateFromStringWithShiftedDays(String dateString, int daysToShift, String responseSource) throws PADException {
        Date dateResult = null;

        if (!StringUtils.isBlank(dateString)) {
            try {
                dateResult = getDatePlusDays(dateString, ServerConstants.dateFormatddMMyyyy, daysToShift);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, responseSource);
            }
        }
        return dateResult;
    }

    public static String toJson(Object object) {
        ObjectMapper compact = new ObjectMapper();
        try {
            compact.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            compact.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return compact.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
