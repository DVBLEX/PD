package com.pad.server.anpr.common;

public class ServerConstants {

    public static final int    DEFAULT_INT                                                                     = -1;
    public static final long   DEFAULT_LONG                                                                    = -1l;
    public static final String DEFAULT_STRING                                                                  = "";
    public static final int    ZERO_INT                                                                        = 0;

    public static final String ANPR_API_LIST_ENTRY_LOG_ENDPOINT                                                = "EntryLog/List";
    public static final String ANPR_API_CREATE_CAR_ENDPOINT                                                    = "Cars/Create";
    public static final String ANPR_API_CREATE_PARKINGPERMISSIONS_ENDPOINT                                     = "ParkingPermissions/Create";
    public static final String ANPR_API_DELETE_PARKINGPERMISSIONS_ENDPOINT                                     = "ParkingPermissions/Delete";
    public static final String ANPR_API_UPDATE_PARKINGPERMISSIONS_STATUS_ENDPOINT                              = "ParkingPermissions/ChageStatus";

    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY                   = 1;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY                      = 2;
    public static final int    REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY                   = 3;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT                    = 4;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_WHITELISTED          = 5;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT               = 6;
    public static final int    REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_DISABLED   = 7;
    public static final int    REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED      = 8;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT                       = 9;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_REENTRY_AFTER_PREM_EXIT = 10;
    public static final int    REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT                    = 11;
    public static final int    REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY                      = 12;
    public static final int    REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT                       = 13;
    public static final int    REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED       = 14;
    public static final int    REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT               = 15;
    public static final int    REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED       = 16;
    public static final int    REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED    = 17;
    public static final int    REQUEST_TYPE_ANPR_API_GET_ENTRY_LOG                                             = 100;

    public static final String dateFormatMMddyyyyhhmma                                                         = "MM/dd/yyyy hh:mm a";

    public static final int    PROCESS_REQUEST                                                                 = -1;
    public static final int    PROCESS_NOTPROCESSED                                                            = 0;
    public static final int    PROCESS_PROGRESS                                                                = 1;
    public static final int    PROCESS_PROCESSED                                                               = 2;
    public static final int    PROCESS_CANCELLED                                                               = 3;
    public static final int    PROCESS_FAILED                                                                  = 4;

    public static final int    ANPR_PRIORITY                                                                   = 15;

    public static final long   NINE_MINUTES_MILLIS                                                             = 1000l * 60l * 9l;

    public static final long   SYSTEM_TIMER_TASK_ANPR_ID                                                       = 105l;
    public static final long   SYSTEM_TIMER_TASK_ANPR_ENTRY_LOG_ID                                             = 106l;
    public static final long   SYSTEM_TIMER_TASK_ANPR_ENTRY_LOG_PROCESS_ID                                     = 107l;
    public static final long   SYSTEM_TIMER_TASK_ANPR_PARKING_PERMISSION_ID                                    = 108l;
    public static final long   SYSTEM_TIMER_TASK_ANPR_PARKING_RELEASE_ID                                       = 109l;

    public static final String ANPR_API_FILTER_GREATER_THAN                                                    = "~gt~";
    public static final String ANPR_API_FILTER_GREATER_THAN_OR_EQUAL                                           = "~gte~";

    public static final int    JDBC_BATCH_SIZE                                                                 = 25;

    public static final long   ENTRY_EVENT_TYPE_ID_APPROVED                                                    = 401l;
    public static final long   ENTRY_EVENT_TYPE_ID_OPENED_MANUALLY                                             = 402l;
    public static final long   ENTRY_EVENT_TYPE_ID_PERMANENTLY_OPEN                                            = 403l;
    public static final long   ENTRY_EVENT_TYPE_ID_FREEFLOW                                                    = 404l;
    public static final long   ENTRY_EVENT_TYPE_ID_BACKTRACKED                                                 = 405l;
    public static final long   ENTRY_EVENT_TYPE_ID_NO_PERMISSION                                               = 406l;
    public static final long   ENTRY_EVENT_TYPE_ID_ANPR_FAILED                                                 = 407l;
    public static final long   ENTRY_EVENT_TYPE_ID_BLACKLISTED                                                 = 408l;
    public static final long   ENTRY_EVENT_TYPE_ID_LEAD_IN                                                     = 409l;
    public static final long   ENTRY_EVENT_TYPE_ID_LEAD_OUT                                                    = 410l;
    public static final long   ENTRY_EVENT_TYPE_ID_LEAD_THROUGH                                                = 411l;
    public static final long   ENTRY_EVENT_TYPE_ID_AUTO_LEAD_OUT                                               = 412l;
    public static final long   ENTRY_EVENT_TYPE_ID_DISABLE_KEEP_OPEN                                           = 413l;
    public static final long   ENTRY_EVENT_TYPE_ID_ILLEGAL_ENTRY                                               = 414l;
    public static final long   ENTRY_EVENT_TYPE_ID_ANTIPASSBACK                                                = 415l;
    public static final long   ENTRY_EVENT_TYPE_ID_SAME_CAR_AS_PREVIOUS                                        = 416l;
    public static final long   ENTRY_EVENT_TYPE_ID_PERMISSION_TIMEZONE_MISMATCH                                = 417l;
    public static final long   ENTRY_EVENT_TYPE_ID_NO_PERMISSION_FOR_ZONE                                      = 419l;
    public static final long   ENTRY_EVENT_TYPE_ID_NO_PERMISSION_DUE_PRIVATE_EVENT                             = 420l;
    public static final long   ENTRY_EVENT_TYPE_ID_FULL_FOR_TENANT                                             = 421l;
    public static final long   ENTRY_EVENT_TYPE_ID_COUNTRY_CODE_PERMISSION_MISMATCH                            = 422l;

    public static final int    PARKING_PERMISSION_ID_PICKED_FOR_PROCESSING                                     = -100;

    public static final int    PARKING_PERMISSION_TYPE_PARKING_ENTRY                                           = 1;
    public static final int    PARKING_PERMISSION_TYPE_PARKING_EXIT                                            = 2;
    public static final int    PARKING_PERMISSION_TYPE_PORT_ENTRY                                              = 3;
    public static final int    PARKING_PERMISSION_TYPE_PORT_EXIT                                               = 4;

    public static final String PROCESS_ANPR_EVENT_NO_PARKING_PERMISSION_RESPONSE_SOURCE                        = "processAnprEvent#noParkingPermission";
}
