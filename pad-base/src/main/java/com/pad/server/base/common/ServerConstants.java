package com.pad.server.base.common;

import java.math.BigDecimal;

public class ServerConstants {

    public static final String     SYSTEM_ENVIRONMENT_LOCAL                                                        = "LOCAL";
    public static final String     SYSTEM_ENVIRONMENT_DEV                                                          = "DEV";
    public static final String     SYSTEM_ENVIRONMENT_PROD                                                         = "PROD";

    public static final int        DEFAULT_INT                                                                     = -1;
    public static final long       DEFAULT_LONG                                                                    = -1l;
    public static final String     DEFAULT_STRING                                                                  = "";
    public static final int        ZERO_INT                                                                        = 0;

    public static final String     EXPORT_yyyyMMddHHmmss                                                           = "yyyyMMddHHmmss";
    public static final String     dateFormatddMMyyyy                                                              = "dd/MM/yyyy";
    public static final String     dateFormatDisplayddMMyyyyHHmm                                                   = "dd/MM/yyyy HH:mm";
    public static final String     dateFormatDisplayddMMyyyyHHmmss                                                 = "dd/MM/yyyy HH:mm:ss";
    public static final String     dateFormatddMMyyyyHHmm                                                          = "dd/MM/yyyy HHmm";
    public static final String     dateFormatyyyyMMddHHmm                                                          = "yyyy-MM-dd HH:mm";
    public static final String     dateFormatyyyyMMddHHmmss                                                        = "yyyy-MM-dd HH:mm:ss";
    public static final String     dateFormatddMMMyyyyHHmm                                                         = "dd MMM yyyy HH:mm";
    public static final String     dateFormatyyyyMMdd                                                              = "yyyyMMdd";
    public static final String     dateTimeFormatyyyyMMddHHmmssSSS                                                 = "yyyyMMdd_HHmmss_SSS";
    public static final String     dateFormatyyyyMMdd_ISO                                                          = "yyyy-MM-dd";
    public static final String     timeFormatHHmss                                                                 = "HH:mm:ss";
    public static final String     timeFormatHHmm                                                                  = "HHmm";

    public static final long       DAY_MILLIS                                                                      = 1000l * 60l * 60l * 24l;
    public static final long       NINE_MINUTES_MILLIS                                                             = 1000l * 60l * 9l;

    public static final int        ACCOUNT_TYPE_COMPANY                                                            = 1;
    public static final int        ACCOUNT_TYPE_INDIVIDUAL                                                         = 2;
    public static final int        ACCOUNT_TYPE_OPERATOR                                                           = 100;

    public static final String     ACCOUNT_TYPE_OPERATOR_URL_STRING                                                = "op";
    public static final String     ACCOUNT_TYPE_TRANSPORTER_URL_STRING                                             = "tp";

    public static final String     ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING                                     = "tpc";
    public static final String     ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING                                   = "tpi";

    public static final String     EMAILS_SEPARATOR                                                                = ",";

    public static final int        ACCOUNT_STATUS_PENDING_FOR_ACTIVATION                                           = 1;
    public static final int        ACCOUNT_STATUS_ACTIVE                                                           = 2;
    public static final int        ACCOUNT_STATUS_DENIED                                                           = 3;
    public static final int        ACCOUNT_STATUS_INACTIVE                                                         = 4;

    public static final int        OPERATOR_ROLE_TRANSPORTER                                                       = 1;
    public static final int        OPERATOR_ROLE_PORT_OPERATOR                                                     = 2;
    public static final int        OPERATOR_ROLE_PARKING_OPERATOR                                                  = 3;
    public static final int        OPERATOR_ROLE_PARKING_KIOSK_OPERATOR                                            = 4;
    public static final int        OPERATOR_ROLE_PARKING_OFFICE_OPERATOR                                           = 5;
    public static final int        OPERATOR_ROLE_PORT_AUTHORITY_OPERATOR                                           = 6;
    public static final int        OPERATOR_ROLE_PORT_ENTRY_OPERATOR                                               = 7;
    public static final int        OPERATOR_ROLE_PORT_EXIT_OPERATOR                                                = 8;
    public static final int        OPERATOR_ROLE_PARKING_SUPERVISOR_OPERATOR                                       = 9;
    public static final int        OPERATOR_ROLE_FINANCE_OPERATOR                                                  = 50;
    public static final int        OPERATOR_ROLE_ADMIN                                                             = 100;
    public static final int        OPERATOR_ROLE_API                                                               = 500;

    public static final int        PORT_OPERATOR_DPWORLD                                                           = 1;
    public static final int        PORT_OPERATOR_TVS                                                               = 2;
    public static final int        PORT_OPERATOR_DAKAR_TERMINAL                                                    = 3;
    public static final int        PORT_OPERATOR_VIVO_ENERGY                                                       = 4;
    public static final int        PORT_OPERATOR_SENSTOCK                                                          = 5;
    public static final int        PORT_OPERATOR_ORYX                                                              = 6;
    public static final int        PORT_OPERATOR_ERES                                                              = 7;
    public static final int        PORT_OPERATOR_TM_NORTH                                                          = 8;
    public static final int        PORT_OPERATOR_TM_SOUTH                                                          = 9;
    public static final int        PORT_OPERATOR_ISTAMCO                                                           = 10;
    public static final int        PORT_OPERATOR_MOLE_10                                                           = 11;
    public static final int        PORT_OPERATOR_TM                                                                = 99;

    public static final int        INDEPENDENT_PORT_OPERATOR_ISTAMCO                                               = 6;

    public static final int        MISSION_STATUS_TRIPS_PENDING                                                    = 1;
    public static final int        MISSION_STATUS_TRIPS_BOOKED                                                     = 2;
    public static final int        MISSION_STATUS_EXPIRED                                                          = 3;
    public static final int        MISSION_STATUS_CANCELLED                                                        = 4;

    public static final int        TRIP_TYPE_BOOKED                                                                = 100;
    public static final int        TRIP_TYPE_ADHOC                                                                 = 200;

    public static final int        TRIP_STATUS_APPROVED                                                            = 1;
    public static final int        TRIP_STATUS_COMPLETED                                                           = 2;
    public static final int        TRIP_STATUS_ENTERED_PARKING                                                     = 3;
    public static final int        TRIP_STATUS_EXITED_PARKING_PREMATURELY                                          = 4;
    public static final int        TRIP_STATUS_IN_TRANSIT                                                          = 5;
    public static final int        TRIP_STATUS_ENTERED_PORT                                                        = 6;
    public static final int        TRIP_STATUS_DENIED_PORT_ENTRY                                                   = 7;
    public static final int        TRIP_STATUS_IN_TRANSIT_EXPIRED                                                  = 8;
    public static final int        TRIP_STATUS_PENDING_APPROVAL                                                    = 10;
    public static final int        TRIP_STATUS_CANCELLED                                                           = 11;
    public static final int        TRIP_STATUS_ABORTED                                                             = 12;
    public static final int        TRIP_STATUS_DENIED_BY_OFFICE_OPERATOR                                           = 13;
    public static final int        TRIP_STATUS_PORT_EXIT_EXPIRED                                                   = 14;
    public static final int        TRIP_STATUS_COMPLETED_SYSTEM                                                    = 15;
    public static final int        TRIP_STATUS_PENDING                                                             = 16;
    public static final int        TRIP_STATUS_CANCELLED_SYSTEM                                                    = 17;
    public static final int        TRIP_STATUS_EXITED_PARKING_PREMATURELY_EXPIRED                                  = 18;
    public static final int        TRIP_STATUS_CANCELLED_BY_KIOSK_OPERATOR                                         = 19;
    public static final int        TRIP_STATUS_IN_FLIGHT                                                           = 20;
    public static final int        TRIP_STATUS_TRIP_TEST_CANCELLED                                                 = -100;
    // TRIP_STATUS_TRIP_TEST_CANCELLED is used when manually updating database to disable any test trips

    public static final int        TRIP_PORT_ENTERED_EXPIRY_MINUTES                                                = 60;
    public static final int        TRIP_APPROVED_VALIDITY_DAYS                                                     = 7;

    public static final int        CONTAINER_STATE_LOADED                                                          = 1;
    public static final int        CONTAINER_STATE_EMPTY                                                           = 2;

    public static final int        PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE                                          = 1;
    public static final int        PAYMENT_TYPE_ACCOUNT_TOPUP                                                      = 2;
    public static final int        PAYMENT_TYPE_TRIP_FEE                                                           = 3;
    public static final int        PAYMENT_TYPE_ACCOUNT_DEBIT                                                      = 4;
    public static final int        PAYMENT_TYPE_ACCOUNT_CREDIT                                                     = 5;

    public static final int        PAYMENT_OPTION_CASH                                                             = 1;
    public static final int        PAYMENT_OPTION_ORANGE_MONEY                                                     = 2;
    public static final int        PAYMENT_OPTION_WARI                                                             = 3;
    public static final int        PAYMENT_OPTION_FREE_MONEY                                                       = 4;
    public static final int        PAYMENT_OPTION_E_MONEY                                                          = 5;
    public static final int        PAYMENT_OPTION_ECOBANK                                                          = 6;
    public static final int        PAYMENT_OPTION_BANK_TRANSFER                                                    = 7;
    public static final int        PAYMENT_OPTION_CHEQUE                                                           = 8;
    public static final int        PAYMENT_OPTION_ACCOUNT_CREDIT                                                   = 9;
    public static final int        PAYMENT_OPTION_CASH_REFUND                                                      = 10;
    public static final int        PAYMENT_OPTION_ACCOUNT_DEBIT                                                    = 11;

    public static final int        MSISDN_LENGTH_MIN                                                               = 10;
    public static final int        MSISDN_LENGTH                                                                   = 12;

    public static final String     CURRENCY_CFA_FRANC                                                              = "XOF";

    public static final String     REGEX_EMAIL                                                                     = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String     REGEX_MESSAGE_FORMAT                                                            = "\\$\\{(?:\\s|\\&nbsp\\;)*([\\w\\_\\-]+)(?:\\s|\\&nbsp\\;)*\\}";
    public static final String     REGEX_SHA256                                                                    = "[A-Fa-f0-9]{64}";
    public static final String     REGEX_SHA256_SHORTENED                                                          = "[A-Fa-f0-9]{25}";
    public static final String     REGEX_PASSWORD                                                                  = "(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\\d.*){1,})(:?^[\\w\\&\\?\\!\\$\\#\\*\\+\\=\\%\\^\\@\\-\\.\\,\\_]{8,32}$)";
    public static final String     REGEX_ACTIONS                                                                   = "^(?:(?:\\d)+(?:\\s)*(?:\\,){0,1}(?:\\s)*)+$";
    public static final String     REGEX_REGISTRATION_CODE                                                         = "[\\d]{5}";
    public static final String     REGEX_UNIVERSAL_COUNTRY_CODE                                                    = "[A-Z]{2}";
    public static final String     REGEX_ALPHA_NUMERIC                                                             = "^[a-zA-Z0-9]*$";
    public static final String     REGEX_MSISDN                                                                    = "^\\+?[0-9]\\d{10,12}$";
    public static final String     REGEX_NAME                                                                      = "^[a-zA-Z .'-]*$";

    public static final int        PROCESS_EMAIL                                                                   = 1;
    public static final int        PROCESS_SMS                                                                     = 1;

    public static final int        PROCESS_REQUEST_AUTH                                                            = -2;
    public static final int        PROCESS_REQUEST                                                                 = -1;
    public static final int        PROCESS_NOTPROCESSED                                                            = 0;
    public static final int        PROCESS_PROGRESS                                                                = 1;
    public static final int        PROCESS_PROCESSED                                                               = 2;
    public static final int        PROCESS_CANCELLED                                                               = 3;
    public static final int        PROCESS_FAILED                                                                  = 4;

    public static final long       SYSTEM_TIMER_TASK_SMS_ID                                                        = 101l;
    public static final long       SYSTEM_TIMER_TASK_EMAIL_ID                                                      = 102l;
    public static final long       SYSTEM_TIMER_TASK_DAILY_STATISTICS_ID                                           = 104l;
    public static final long       SYSTEM_TIMER_TASK_SYSTEM_STATUS_CHECK_ID                                        = 110l;
    public static final long       SYSTEM_TIMER_TASK_TRIP_ANPR_EXPIRY_ID                                           = 111l;

    public static final int        EMAIL_DEFAULT_PRIORITY                                                          = 15;

    public static final int        CAPTCHA_MAX_ATTEMPT                                                             = 4;

    public static final String     SYSTEM_TOKEN_PREFIX                                                             = "pad";
    public static final String     SYSTEM_TOKEN_SUFFIX1                                                            = "8p2HO";
    public static final String     SYSTEM_TOKEN_SUFFIX2                                                            = "c6I0z";

    public static final long       EMAIL_PASSWORD_FORGOT_TEMPLATE_TYPE                                             = 1;
    public static final long       EMAIL_VERIFICATION_CODE_TEMPLATE_TYPE                                           = 2;
    public static final long       EMAIL_COMPANY_REGISTRATION_COMPLETED_TEMPLATE_TYPE                              = 3;
    public static final long       EMAIL_ACCOUNT_ACTIVATED_TEMPLATE_TYPE                                           = 4;
    public static final long       EMAIL_PASSWORD_SETUP_TEMPLATE_TYPE                                              = 5;
    public static final long       EMAIL_TRIP_APPROVED_NOTIFICATION_TEMPLATE_TYPE                                  = 6;
    public static final long       EMAIL_TRIP_DENIED_NOTIFICATION_TEMPLATE_TYPE                                    = 7;
    public static final long       EMAIL_MISSION_ADDED_NOTIFICATION_TEMPLATE_TYPE                                  = 8;
    public static final long       EMAIL_INVOICE_NOTIFICATION_TYPE                                                 = 9;
    public static final long       EMAIL_RECEIPT_NOTIFICATION_TYPE                                                 = 10;
    public static final long       EMAIL_NEW_REGISTRATION_AGS_NOTIFICATION_TYPE                                    = 11;
    public static final long       EMAIL_MISSION_CANCEL_NOTIFICATION_TEMPLATE_TYPE                                 = 12;
    public static final long       EMAIL_ACCOUNT_DENIED_TEMPLATE_TYPE                                              = 13;
    public static final long       EMAIL_MISSION_TRIP_ADDED_API_NOTIFICATION_TEMPLATE_TYPE                         = 14;
    public static final long       EMAIL_TRANSPORTER_SHORT_NAME_MAPPING_ERROR_TEMPLATE_TYPE                        = 15;
    public static final long       EMAIL_TRANSPORTER_SHORT_NAME_MAPPING_ERROR_AGS_TEAM_TEMPLATE_TYPE               = 16;
    public static final long       EMAIL_TRIP_REJECTED_NOTIFICATION_TEMPLATE_TYPE                                  = 17;
    public static final long       EMAIL_MISSION_TRIP_UPDATED_API_NOTIFICATION_TEMPLATE_TYPE                       = 18;
    public static final long       EMAIL_PORT_OPERATOR_ISSUE_ALERT_CREATED_TEMPLATE_TYPE                           = 19;
    public static final long       EMAIL_PORT_OPERATOR_ISSUE_ALERT_UPDATED_TEMPLATE_TYPE                           = 20;
    public static final long       EMAIL_PORT_OPERATOR_ISSUE_ALERT_RESOLVED_TEMPLATE_TYPE                          = 21;
    public static final long       EMAIL_LOW_ACCOUNT_BALANCE_WARNING_TYPE                                          = 23;
    public static final long       EMAIL_FINANCE_USER_TRANSACTIONS_NOTIFICATION_TYPE                               = 24;

    public static final long       SMS_PARKING_EXIT_TEMPLATE_TYPE                                                  = 1;
    public static final long       SMS_VERIFICATION_CODE_TEMPLATE_TYPE                                             = 2;
    public static final long       SMS_INDIVIDUAL_REGISTRATION_COMPLETED_TEMPLATE_TYPE                             = 3;
    public static final long       SMS_PASSWORD_FORGOT_TEMPLATE_TYPE                                               = 4;
    public static final long       SMS_PASSWORD_SETUP_TEMPLATE_TYPE                                                = 5;
    public static final long       SMS_TRIP_APPROVED_DRIVER_NOTIFICATION_TEMPLATE_TYPE                             = 6;
    public static final long       SMS_TRIP_APPROVED_TRANSPORTER_NOTIFICATION_TEMPLATE_TYPE                        = 7;
    public static final long       SMS_TRIP_DENIED_TRANSPORTER_NOTIFICATION_TEMPLATE_TYPE                          = 8;
    public static final long       SMS_MISSION_ADDED_NOTIFICATION_TEMPLATE_TYPE                                    = 9;
    public static final long       SMS_ACCOUNT_ACTIVATED_TEMPLATE_TYPE                                             = 10;
    public static final long       SMS_INVOICE_NOTIFICATION_TYPE                                                   = 11;
    public static final long       SMS_RECEIPT_NOTIFICATION_TYPE                                                   = 12;
    public static final long       SMS_MISSION_CANCEL_NOTIFICATION_TEMPLATE_TYPE                                   = 13;
    public static final long       SMS_ACCOUNT_DENIED_TEMPLATE_TYPE                                                = 14;
    public static final long       SMS_MISSION_TRIP_ADDED_API_NOTIFICATION_TEMPLATE_TYPE                           = 15;
    public static final long       SMS_MISSION_TRIP_UPDATED_API_NOTIFICATION_TEMPLATE_TYPE                         = 16;
    public static final long       SMS_LOW_ACCOUNT_BALANCE_WARNING_TYPE                                            = 17;

    public static final long       LANGUAGE_EN_ID                                                                  = 1;
    public static final long       LANGUAGE_FR_ID                                                                  = 2;
    public static final long       LANGUAGE_WO_ID                                                                  = 3;
    public static final long       LANGUAGE_BM_ID                                                                  = 4;

    public static final String     LANGUAGE_EN_ISO_CODE                                                            = "EN";
    public static final String     LANGUAGE_FR_ISO_CODE                                                            = "FR";

    public static final long       SET_UP_PASSWORD_LINK_VALID_HOURS                                                = 3;
    public static final long       FORGOT_PASSWORD_LINK_VALID_MINUTES                                              = 30;

    public static final String     SPRING_SECURITY_ROLE_PREFIX                                                     = "ROLE_";

    public static final int        CHANNEL_SYSTEM                                                                  = 1024;

    public static final long       SCHEDULER_ID                                                                    = -100l;

    public static final int        SIZE_VERIFICATION_CODE                                                          = 5;

    public static final int        DEFAULT_VALIDATION_LENGTH_16                                                    = 16;
    public static final int        DEFAULT_VALIDATION_LENGTH_32                                                    = 32;
    public static final int        DEFAULT_VALIDATION_LENGTH_64                                                    = 64;
    public static final int        DEFAULT_VALIDATION_LENGTH_128                                                   = 128;
    public static final int        DEFAULT_VALIDATION_LENGTH_256                                                   = 256;

    public static final int        REGNUMBER_VALIDATION_LENGTH_MIN                                                 = 3;
    public static final int        REGNUMBER_VALIDATION_LENGTH_MAX                                                 = 16;
    public static final int        REFERENCE_NUMBER_VALIDATION_LENGTH_MIN                                          = 3;
    public static final int        REFERENCE_NUMBER_VALIDATION_LENGTH_MAX                                          = 16;
    public static final int        TRANSACTION_TYPE_API_VALIDATION_LENGTH_MIN                                      = 2;
    public static final int        TRANSACTION_TYPE_API_VALIDATION_LENGTH_MAX                                      = 4;
    public static final int        TRANSPORTER_SHORT_NAME_VALIDATION_LENGTH_MIN                                    = 3;
    public static final int        TRANSPORTER_SHORT_NAME_VALIDATION_LENGTH_MAX                                    = 12;
    public static final int        CONTAINER_ID_VALIDATION_LENGTH_MIN                                              = 3;
    public static final int        CONTAINER_ID_VALIDATION_LENGTH_MAX                                              = 16;
    public static final int        CONTAINER_TYPE_VALIDATION_LENGTH_MIN                                            = 2;
    public static final int        CONTAINER_TYPE_VALIDATION_LENGTH_MAX                                            = 4;

    public static final int        SYSTEM_CHECK_TYPE_QUERY                                                         = 1;
    public static final int        SYSTEM_CHECK_TYPE_QUERY_REPLICATION                                             = 2;
    public static final int        SYSTEM_CHECK_TYPE_HTTP_GET                                                      = 3;

    public static final int        COMP_OP_EQUAL_TO                                                                = 0;
    public static final int        COMP_OP_LESS_THAN                                                               = 1;
    public static final int        COMP_OP_GREATER_THAN                                                            = 2;
    public static final int        COMP_OP_LESS_THAN_EQUAL_TO                                                      = 11;
    public static final int        COMP_OP_GREATER_THAN_EQUAL_TO                                                   = 12;
    public static final int        COMP_OP_NOT_EQUAL_TO                                                            = 100;

    public static final int        PARKING_TYPE_PARKING                                                            = 1;
    public static final int        PARKING_TYPE_EXIT_ONLY                                                          = 2;
    public static final int        PARKING_TYPE_ALL                                                                = 10;

    public static final int        PARKING_STATUS_ENTRY                                                            = 1;
    public static final int        PARKING_STATUS_EXIT                                                             = 2;
    public static final int        PARKING_STATUS_REMINDER_EXIT_DUE                                                = 3;
    public static final int        PARKING_STATUS_ENTERED_PORT                                                     = 4;
    public static final int        PARKING_STATUS_IN_TRANSIT_EXPIRED                                               = 5;
    public static final int        PARKING_STATUS_EXITED_PREMATURELY_EXPIRED                                       = 6;
    public static final int        PARKING_STATUS_ALL                                                              = 10;

    public static final int        VEHICLE_PARKING_STATE_NORMAL                                                    = 1;
    public static final int        VEHICLE_PARKING_STATE_BROKEN_DOWN                                               = 2;
    public static final int        VEHICLE_PARKING_STATE_CLAMPED                                                   = 3;
    public static final int        VEHICLE_PARKING_STATE_UNRESPONSIVE                                              = 4;

    public static final int        PORT_ACCESS_STATUS_ENTRY                                                        = 1;
    public static final int        PORT_ACCESS_STATUS_DENY                                                         = 2;
    public static final int        PORT_ACCESS_STATUS_EXIT                                                         = 3;
    public static final int        PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM                                        = 4;

    public static final int        PORT_ACCESS_WHITELIST_STATUS_ACTIVE                                             = 1;
    public static final int        PORT_ACCESS_WHITELIST_STATUS_DELETED                                            = 2;

    public static final int        TRANSACTION_TYPE_DROP_OFF_EXPORT                                                = 1;
    public static final int        TRANSACTION_TYPE_DROP_OFF_EMPTY_DAY_M                                           = 2;
    public static final int        TRANSACTION_TYPE_PICK_UP_IMPORT                                                 = 3;
    public static final int        TRANSACTION_TYPE_PICK_UP_EMPTY                                                  = 4;
    public static final int        TRANSACTION_TYPE_URGENT_DROP_OFF_EXPORT                                         = 5;
    public static final int        TRANSACTION_TYPE_URGENT_PICK_UP_IMPORT                                          = 6;
    public static final int        TRANSACTION_TYPE_WHITELIST                                                      = 7;
    public static final int        TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT                                           = 8;
    public static final int        TRANSACTION_TYPE_DROP_OFF_IMPORT_DRAY_IN                                        = 9;
    public static final int        TRANSACTION_TYPE_PICK_UP_EXPORT_DRAY_OFF                                        = 10;
    public static final int        TRANSACTION_TYPE_DROP_OFF_EMPTY_DAY_S                                           = 11;
    public static final int        TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE                                        = 12;

    public static final int        SMS_EN_EXIT_PARKING_TEMPLATE_ID                                                 = 1;
    public static final int        SMS_FR_EXIT_PARKING_TEMPLATE_ID                                                 = 2;
    public static final int        SMS_WO_EXIT_PARKING_TEMPLATE_ID                                                 = 25;
    public static final int        SMS_BM_EXIT_PARKING_TEMPLATE_ID                                                 = 26;

    public static final int        SMS_FR_NEW_INVOICE_TEMPLATE_ID                                                  = 21;

    public static final int        EXIT_PARKING_SMS_TIME_LIMIT_SECONDS                                             = 300;

    public static final int        ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY                                               = 1;
    public static final int        ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY                                              = 2;

    public static final int        ACTION_TYPE_APPROVE_TRIP                                                        = 1;
    public static final int        ACTION_TYPE_DENY_TRIP                                                           = 2;
    public static final int        ACTION_TYPE_UPDATE_TRIP_SLOT_DATETIME                                           = 3;
    public static final int        ACTION_TYPE_UPDATE_TRIP_API                                                     = 4;

    public static final int        SESSION_TYPE_PARKING                                                            = 1;
    public static final int        SESSION_TYPE_VIRTUAL                                                            = 2;

    public static final int        SESSION_STATUS_ASSIGNED                                                         = 1;
    public static final int        SESSION_STATUS_START                                                            = 2;
    public static final int        SESSION_STATUS_END                                                              = 3;
    public static final int        SESSION_STATUS_VALIDATED                                                        = 4;

    public static final int        SESSION_VALIDATION_STEP_NOT_VALIDATED                                           = 0;
    public static final int        SESSION_VALIDATION_STEP_PARTIALLY_VALIDATED                                     = 1;
    public static final int        SESSION_VALIDATION_STEP_VALIDATED                                               = 2;

    public static final int        DRIVER_ASSOCIATION_STATUS_PENDING                                               = 1;
    public static final int        DRIVER_ASSOCIATION_STATUS_REJECTED                                              = 2;
    public static final int        DRIVER_ASSOCIATION_STATUS_APPROVED                                              = 3;
    public static final int        DRIVER_ASSOCIATION_STATUS_DELETED                                               = 4;

    public static final int        REPORT_TYPE_PARKING_ENTRY_COUNTS                                                = 1;
    public static final int        REPORT_TYPE_PORT_ENTRY_OPERATOR_COUNTS                                          = 2;

    public static final String     DIAL_CODE_SENEGAL                                                               = "221";
    public static final String     DIAL_CODE_MALI                                                                  = "223";
    public static final String     DIAL_CODE_IRELAND                                                               = "353";
    public static final String     PLUG_TEST_MSISDN                                                                = "22177111111";

    public static final String     MSISDN_PREFIX_MALI_SENEGAL                                                      = "7";

    public static final int        ANPR_PRIORITY_NORMAL                                                            = 15;
    public static final int        ANPR_PRIORITY_HIGH                                                              = 10;

    public static final int        ANPR_WHITELIST_HOURS                                                            = 24;

    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY                   = 1;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY                      = 2;
    public static final int        REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY                   = 3;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT                    = 4;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_WHITELISTED          = 5;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT               = 6;
    public static final int        REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_DISABLED   = 7;
    public static final int        REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED      = 8;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT                       = 9;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_REENTRY_AFTER_PREM_EXIT = 10;
    public static final int        REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT                    = 11;
    public static final int        REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY                      = 12;
    public static final int        REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT                       = 13;
    public static final int        REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED       = 14;
    public static final int        REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT               = 15;
    public static final int        REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED       = 16;
    public static final int        REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED    = 17;
    public static final int        REQUEST_TYPE_ANPR_API_GET_ENTRY_LOG                                             = 100;

    public static final int        INVOICE_GENERATION_DAY_MONTH                                                    = 1;

    public static final int        PARKING_EXIT_MESSAGE_ID_AUTH_EXIT                                               = 1;
    public static final int        PARKING_EXIT_MESSAGE_ID_PREMATURE_EXIT                                          = 2;
    public static final int        PARKING_EXIT_MESSAGE_ID_EXIT_ONLY                                               = 3;

    public static final long       ACTIVITY_LOG_LOG_IN                                                             = 1;
    public static final long       ACTIVITY_LOG_LOG_OUT                                                            = 2;
    public static final long       ACTIVITY_LOG_REGISTRATION                                                       = 3;
    public static final long       ACTIVITY_LOG_ACTIVATE_ACCOUNT                                                   = 4;
    public static final long       ACTIVITY_LOG_DRIVER_ADD                                                         = 5;
    public static final long       ACTIVITY_LOG_VEHICLE_ADD                                                        = 6;
    public static final long       ACTIVITY_LOG_TRIP_ADD                                                           = 7;
    public static final long       ACTIVITY_LOG_INVOICE_DOWNLOAD                                                   = 8;
    public static final long       ACTIVITY_LOG_MISSION_ADD                                                        = 9;
    public static final long       ACTIVITY_LOG_TRIP_CANCEL                                                        = 10;
    public static final long       ACTIVITY_LOG_PARKING_ENTRY                                                      = 11;
    public static final long       ACTIVITY_LOG_PARKING_EXIT                                                       = 12;
    public static final long       ACTIVITY_LOG_PARKING_EXIT_SMS_SEND                                              = 13;
    public static final long       ACTIVITY_LOG_PARKING_EXIT_ONLY                                                  = 14;
    public static final long       ACTIVITY_LOG_TOP_UP                                                             = 15;
    public static final long       ACTIVITY_LOG_TOP_UP_FINANCE_OPERATOR                                            = 16;
    public static final long       ACTIVITY_LOG_PORT_ENTRY                                                         = 17;
    public static final long       ACTIVITY_LOG_PORT_WHITELIST_ENTRY                                               = 18;
    public static final long       ACTIVITY_LOG_PORT_DENY                                                          = 19;
    public static final long       ACTIVITY_LOG_PORT_EXIT                                                          = 20;
    public static final long       ACTIVITY_LOG_PORT_WHITELIST_ADD                                                 = 21;
    public static final long       ACTIVITY_LOG_PORT_WHITELIST_DELETE                                              = 22;
    public static final long       ACTIVITY_LOG_SESSION_ADD                                                        = 23;
    public static final long       ACTIVITY_LOG_SESSION_END                                                        = 24;
    public static final long       ACTIVITY_LOG_SESSION_VALIDATE                                                   = 25;
    public static final long       ACTIVITY_LOG_TRIP_ADHOC_ADD                                                     = 26;
    public static final long       ACTIVITY_LOG_TRIP_UPDATE                                                        = 27;
    public static final long       ACTIVITY_LOG_TRIP_DRIVER_MOBILE_UPDATE                                          = 28;
    public static final long       ACTIVITY_LOG_OPERATOR_CREATE                                                    = 29;
    public static final long       ACTIVITY_LOG_OPERATOR_UPDATE                                                    = 30;
    public static final long       ACTIVITY_LOG_OPERATOR_PASSWORD_CHANGE                                           = 31;
    public static final long       ACTIVITY_LOG_OPERATOR_RESET_PASSWORD_SEND                                       = 32;
    public static final long       ACTIVITY_LOG_TRIP_SEARCH                                                        = 33;
    public static final long       ACTIVITY_LOG_TRIP_VALIDATE_REFERENCE_NUMBER                                     = 34;
    public static final long       ACTIVITY_LOG_RECEIPT_DOWNLOAD                                                   = 35;
    public static final long       ACTIVITY_LOG_TRIP_ABORT                                                         = 36;
    public static final long       ACTIVITY_LOG_TOGGLE_PARKING_AUTO_RELEASE                                        = 37;
    public static final long       ACTIVITY_LOG_TRIGGER_PARKING_MANUAL_RELEASE                                     = 38;
    public static final long       ACTIVITY_LOG_MISSION_CANCEL                                                     = 39;
    public static final long       ACTIVITY_LOG_RECEIPT_PRINT                                                      = 40;
    public static final long       ACTIVITY_LOG_UPDATE_TRANSACTION_TYPE_FLAG                                       = 41;
    public static final long       ACTIVITY_LOG_DENY_ACCOUNT                                                       = 42;
    public static final long       ACTIVITY_LOG_TRIP_APPROVE                                                       = 43;
    public static final long       ACTIVITY_LOG_TRIP_REJECT                                                        = 44;
    public static final long       ACTIVITY_LOG_VEHICLE_UPDATE                                                     = 45;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_COMPANY_TELEPHONE                                   = 46;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_MSISDN                                              = 47;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_ADDRESS                                             = 48;
    public static final long       ACTIVITY_LOG_TRIP_ADHOC_CANCEL                                                  = 49;
    public static final long       ACTIVITY_LOG_ACCOUNT_DEBIT                                                      = 50;
    public static final long       ACTIVITY_LOG_ACCOUNT_CREDIT                                                     = 51;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_APPROVED_TRIP_EMAIL                                 = 52;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT                                                     = 53;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_COMPANY_EMAIL                                       = 54;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_LOW_BALANCE_WARN                                    = 55;
    public static final long       ACTIVITY_LOG_UPDATE_ACCOUNT_DEDUCT_CREDIT_REGISTERED_TRUCKS                     = 56;
    public static final long       ACTIVITY_LOG_PORT_ENTRY_ZONE_SELECTED                                           = 57;

    public static final String     COTIZEL_API_PROCESS_PAYMENT_ENDPOINT                                            = "orderreference";

    public static final long       CLIENT_ID_AGS                                                                   = 1001l;

    public static final long       AGGREGATOR_ID_COTIZEL                                                           = 1001l;

    public static final long       MNO_ID_ORANGE_MONEY                                                             = 101l;
    public static final long       MNO_ID_WARI                                                                     = 102l;
    public static final long       MNO_ID_FREE_MONEY                                                               = 103l;
    public static final long       MNO_ID_E_MONEY                                                                  = 104l;
    public static final long       MNO_ID_ECO_BANK                                                                 = 105l;

    public static final String     MNO_ORANGE_MONEY                                                                = "PAIEMENTMARCHANDOM";
    public static final String     MNO_WARI                                                                        = "WARI";
    public static final String     MNO_FREE_MONEY                                                                  = "PAIEMENTMARCHANDTIGO";
    public static final String     MNO_E_MONEY                                                                     = "PAIEMENTMARCHANDEM";
    public static final String     MNO_ECO_BANK                                                                    = "SNECOBANKPAIEMENTCASHOUT";

    public static final String     COTIZEL_CALLBACK_STATUS_SUCCESS                                                 = "SUCCESSFUL";
    public static final String     COTIZEL_CALLBACK_STATUS_FAILURE                                                 = "FAILED";

    public static final int        SIZE_RECEIPT_UNIQUE_URL                                                         = 10;

    public static final int        ACCOUNT_NUMBER_MIN                                                              = 100000;
    public static final int        ACCOUNT_NUMBER_MAX                                                              = 999999;

    public static final int        PARKING_PERMISSION_ID_PICKED_FOR_PROCESSING                                     = -100;

    public static final int        PARKING_PERMISSION_TYPE_PARKING_ENTRY                                           = 1;
    public static final int        PARKING_PERMISSION_TYPE_PARKING_EXIT                                            = 2;
    public static final int        PARKING_PERMISSION_TYPE_PORT_ENTRY                                              = 3;
    public static final int        PARKING_PERMISSION_TYPE_PORT_EXIT                                               = 4;

    public static final int        BOOKING_SLOT_LIMIT_PERIOD_WEEKS                                                 = 8;
    public static final String     STRING_SPLIT_DATE                                                               = "@";

    public static final int        ZONE_ID_PARKING                                                                 = 10001;
    public static final int        ZONE_ID_PORT                                                                    = 10002;

    public static final String     VEHICLE_COUNTER_DEFAULT_DATE                                                    = "01/01/2020";

    public static final long       ENTRY_EVENT_TYPE_ID_APPROVED                                                    = 401l;

    public static final int        TYPE_STATEMENT                                                                  = 1;
    public static final int        TYPE_INVOICE                                                                    = 2;

    public static final String     DEFAULT_SORTING_FIELD                                                           = "id";

    public static final BigDecimal DEFAULT_AMOUNT_LOW_ACCOUNT_BALANCE_WARN                                         = new BigDecimal(5000);

    public static final String     SESSION_ATTRIBUTE_SELECTED_ZONE                                                 = "selectedZone";
    
    public static final int        AMOUNT_HOLD_DATE_SLOT_PAST_HOURS                                                = 4;
    public static final int        AMOUNT_HOLD_DATE_SLOT_FUTURE_HOURS                                              = 4;
}
