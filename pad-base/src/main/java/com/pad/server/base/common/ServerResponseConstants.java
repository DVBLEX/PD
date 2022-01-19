package com.pad.server.base.common;

public class ServerResponseConstants {

    // [0] Generic Success Code
    public static final int    SUCCESS_CODE                                          = 0;
    public static final String SUCCESS_TEXT                                          = "Success.";

    // [1] Generic Failure Code
    public static final int    FAILURE_CODE                                          = 1;
    public static final String FAILURE_TEXT                                          = "Failure.";

    // [1000] API Validation - Invalid Request Format
    public static final int    INVALID_REQUEST_FORMAT_CODE                           = 1000;
    public static final String INVALID_REQUEST_FORMAT_TEXT                           = "Invalid Request Format.";

    // [1001 - 1040] API Validation - Missing Fields
    public static final int    MISSING_VEHICLE_REGISTRATION_NUMBER_CODE              = 1001;
    public static final String MISSING_VEHICLE_REGISTRATION_NUMBER_TEXT              = "Missing Vehicle Registration Number.";

    public static final int    MISSING_VEHICLE_REGISTRATION_COUNTRY_CODE             = 1002;
    public static final String MISSING_VEHICLE_REGISTRATION_COUNTRY_TEXT             = "Missing Vehicle Registration Country.";

    public static final int    MISSING_MSISDN_CODE                                   = 1003;
    public static final String MISSING_MSISDN_TEXT                                   = "Missing Mobile Number.";

    public static final int    MISSING_FIRST_NAME_CODE                               = 1004;
    public static final String MISSING_FIRST_NAME_TEXT                               = "Missing First Name.";

    public static final int    MISSING_LAST_NAME_CODE                                = 1005;
    public static final String MISSING_LAST_NAME_TEXT                                = "Missing Last Name.";

    public static final int    MISSING_ISSUING_COUNTRY_CODE                          = 1006;
    public static final String MISSING_ISSUING_COUNTRY_TEXT                          = "Missing Issuing Country.";

    public static final int    MISSING_LICENCE_NUMBER_CODE                           = 1007;
    public static final String MISSING_LICENCE_NUMBER_TEXT                           = "Missing Licence Number.";

    public static final int    MISSING_PORT_OPERATOR_TYPE_CODE                       = 1008;
    public static final String MISSING_PORT_OPERATOR_TYPE_TEXT                       = "Missing Port Operator Type.";

    public static final int    MISSING_TRANSACTION_TYPE_CODE                         = 1009;
    public static final String MISSING_TRANSACTION_TYPE_TEXT                         = "Missing Transaction Type.";

    public static final int    MISSING_NUMBER_OF_TRIPS_CODE                          = 1010;
    public static final String MISSING_NUMBER_OF_TRIPS_TEXT                          = "Missing Number of Trips.";

    public static final int    MISSING_DATE_MISSION_START_CODE                       = 1011;
    public static final String MISSING_DATE_MISSION_START_TEXT                       = "Missing Date Mission Start.";

    public static final int    MISSING_DATE_MISSION_END_CODE                         = 1012;
    public static final String MISSING_DATE_MISSION_END_TEXT                         = "Missing Date Mission End.";

    public static final int    MISSING_MISSION_CODE_CODE                             = 1015;
    public static final String MISSING_MISSION_CODE_TEXT                             = "Missing Mission Code.";

    public static final int    MISSING_VEHICLE_CODE_CODE                             = 1016;
    public static final String MISSING_VEHICLE_CODE_TEXT                             = "Missing Vehicle Code.";

    public static final int    MISSING_DRIVER_CODE_CODE                              = 1017;
    public static final String MISSING_DRIVER_CODE_TEXT                              = "Missing Driver Code.";

    public static final int    MISSING_DATE_SLOT_CODE                                = 1018;
    public static final String MISSING_DATE_SLOT_TEXT                                = "Missing Date Slot.";

    public static final int    MISSING_TIME_SLOT_CODE                                = 1019;
    public static final String MISSING_TIME_SLOT_TEXT                                = "Missing Time Slot.";

    public static final int    MISSING_TRIP_CODE_CODE                                = 1020;
    public static final String MISSING_TRIP_CODE_TEXT                                = "Missing Trip Code.";

    public static final int    MISSING_REASON_REJECTED_CODE                          = 1021;
    public static final String MISSING_REASON_REJECTED_TEXT                          = "Missing Reason Rejected.";

    public static final int    MISSING_FIND_TRIP_RESPONSE_CODE_CODE                  = 1022;
    public static final String MISSING_FIND_TRIP_RESPONSE_CODE_TEXT                  = "Missing Find Trip Response Code.";

    public static final int    MISSING_ACCOUNT_CODE_CODE                             = 1023;
    public static final String MISSING_ACCOUNT_CODE_TEXT                             = "Missing Account Code.";

    public static final int    MISSING_DATE_FROM_CODE                                = 1024;
    public static final String MISSING_DATE_FROM_TEXT                                = "Missing Date From.";

    public static final int    MISSING_DATE_TO_CODE                                  = 1025;
    public static final String MISSING_DATE_TO_TEXT                                  = "Missing Date To.";

    public static final int    MISSING_TIME_FROM_CODE                                = 1026;
    public static final String MISSING_TIME_FROM_TEXT                                = "Missing Date From.";

    public static final int    MISSING_TIME_TO_CODE                                  = 1027;
    public static final String MISSING_TIME_TO_TEXT                                  = "Missing Date To.";

    public static final int    MISSING_VEHICLE_REGISTRATION_ARRAY_CODE               = 1028;
    public static final String MISSING_VEHICLE_REGISTRATION_ARRAY_TEXT               = "Missing Vehicle Registration Array.";

    public static final int    MISSING_GATE_ID_CODE                                  = 1029;
    public static final String MISSING_GATE_ID_TEXT                                  = "Missing Gate ID.";

    public static final int    MISSING_LANGUAGE_ID_CODE                              = 1030;
    public static final String MISSING_LANGUAGE_ID_TEXT                              = "Missing Language ID.";

    public static final int    MISSING_USERNAME_CODE                                 = 1031;
    public static final String MISSING_USERNAME_TEXT                                 = "Missing USERNAME.";

    public static final int    MISSING_PASSWORD_CODE                                 = 1032;
    public static final String MISSING_PASSWORD_TEXT                                 = "Missing PASSWORD.";

    public static final int    MISSING_TRANSPORTER_SHORT_NAME_CODE                   = 1033;
    public static final String MISSING_TRANSPORTER_SHORT_NAME_TEXT                   = "Missing Transporter Short Name.";

    public static final int    MISSING_CONTAINER_TYPE_CODE                           = 1034;
    public static final String MISSING_CONTAINER_TYPE_TEXT                           = "Missing Container Type.";

    public static final int    MISSING_DATE_SLOT_FROM_CODE                           = 1035;
    public static final String MISSING_DATE_SLOT_FROM_TEXT                           = "Missing Date Slot From.";

    public static final int    MISSING_DATE_SLOT_TO_CODE                             = 1036;
    public static final String MISSING_DATE_SLOT_TO_TEXT                             = "Missing Date Slot To.";

    public static final int    MISSING_COMPANY_NAME_CODE                             = 1037;
    public static final String MISSING_COMPANY_NAME_TEXT                             = "Missing Company Name.";

    public static final int    MISSING_COMMENTS_FIELD_CODE                           = 1038;
    public static final String MISSING_COMMENTS_FIELD_TEXT                           = "Missing Comments Field.";

    // [1041 - 1100] API Validation - Invalid Data Type / Range
    public static final int    INVALID_TRANSPORTER_SHORT_NAME_CODE                   = 1040;
    public static final String INVALID_TRANSPORTER_SHORT_NAME_TEXT                   = "Invalid Transporter Short Name.";

    public static final int    INVALID_MSISDN_CODE                                   = 1041;
    public static final String INVALID_MSISDN_TEXT                                   = "Invalid mobile.";

    public static final int    INVALID_EMAIL_CODE                                    = 1042;
    public static final String INVALID_EMAIL_TEXT                                    = "Invalid EMAIL.";

    public static final int    INVALID_COUNTRY_CODE                                  = 1043;
    public static final String INVALID_COUNTRY_TEXT                                  = "Invalid Country.";

    public static final int    INVALID_VEHICLE_REGISTRATION_NUMBER_CODE              = 1044;
    public static final String INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT              = "Invalid Vehicle Registration Number.";

    public static final int    INVALID_VEHICLE_MAKE_CODE                             = 1045;
    public static final String INVALID_VEHICLE_MAKE_TEXT                             = "Invalid Vehicle Make.";

    public static final int    INVALID_FIRST_NAME_CODE                               = 1046;
    public static final String INVALID_FIRST_NAME_TEXT                               = "Invalid First Name.";

    public static final int    INVALID_LAST_NAME_CODE                                = 1047;
    public static final String INVALID_LAST_NAME_TEXT                                = "Invalid Last Name.";

    public static final int    INVALID_ISSUING_COUNTRY_CODE                          = 1048;
    public static final String INVALID_ISSUING_COUNTRY_TEXT                          = "Invalid Issuing Country.";

    public static final int    INVALID_LICENCE_NUMBER_CODE                           = 1049;
    public static final String INVALID_LICENCE_NUMBER_TEXT                           = "Invalid Licence Number.";

    public static final int    INVALID_PORT_OPERATOR_TYPE_CODE                       = 1050;
    public static final String INVALID_PORT_OPERATOR_TYPE_TEXT                       = "Invalid Port Operator Type.";

    public static final int    INVALID_NUMBER_OF_TRIPS_CODE                          = 1051;
    public static final String INVALID_NUMBER_OF_TRIPS_TEXT                          = "Invalid Number of Trips.";

    public static final int    INVALID_REFERENCE_NUMBER_CODE                         = 1054;
    public static final String INVALID_REFERENCE_NUMBER_TEXT                         = "Invalid Reference number.";

    public static final int    INVALID_TRANSACTION_TYPE_CODE                         = 1055;
    public static final String INVALID_TRANSACTION_TYPE_TEXT                         = "Invalid Transaction Type.";

    public static final int    INVALID_CONTAINER_ID_CODE                             = 1056;
    public static final String INVALID_CONTAINER_ID_TEXT                             = "Invalid Container ID.";

    public static final int    INVALID_AMOUNT_CODE                                   = 1057;
    public static final String INVALID_AMOUNT_TEXT                                   = "Invalid amount.";

    public static final int    INVALID_FILE_UPLOAD_FORMAT_CODE                       = 1058;
    public static final String INVALID_FILE_UPLOAD_FORMAT_TEXT                       = "Invalid File Format.";

    public static final int    INVALID_FILE_UPLOAD_NAME_LENGTH_CODE                  = 1059;
    public static final String INVALID_FILE_UPLOAD_NAME_LENGTH_TEXT                  = "Invalid File Length - Max size: %d";

    public static final int    INVALID_FILE_CODE                                     = 1060;
    public static final String INVALID_FILE_TEXT                                     = "Invalid File.";

    public static final int    INVALID_USERNAME_CODE                                 = 1061;
    public static final String INVALID_USERNAME_TEXT                                 = "Invalid Username.";

    public static final int    INVALID_URL_CODE                                      = 1062;
    public static final String INVALID_URL_TEXT                                      = "Invalid Link - Page not found.";

    public static final int    INVALID_DATE_SLOT_FROM_CODE                           = 1063;
    public static final String INVALID_DATE_SLOT_FROM_TEXT                           = "Invalid Date Slot From.";

    public static final int    INVALID_DATE_SLOT_TO_CODE                             = 1064;
    public static final String INVALID_DATE_SLOT_TO_TEXT                             = "Invalid Date Slot To.";

    public static final int    INVALID_CONTAINER_TYPE_CODE                           = 1065;
    public static final String INVALID_CONTAINER_TYPE_TEXT                           = "Invalid Container Type.";

    /////////////////////////////

    public static final int    INVALID_FIRST_LAST_STAFF_USER_NAME_CODE               = 1093;
    public static final String INVALID_FIRST_LAST_STAFF_USER_NAME_TEXT               = "Invalid First/Last Staff User name. Special characters are not allowed.";

    public static final int    KIOSK_SESSION_DATA_MISMATCH_CODE                      = 1094;
    public static final String KIOSK_SESSION_DATA_MISMATCH_TEXT                      = "A data field recorded for this kiosk session has failed to meet a validation check.";

    public static final int    ADHOC_TRIP_DATE_SLOT_MAX_LIMIT_EXCEEDED_CODE          = 1095;
    public static final String ADHOC_TRIP_DATE_SLOT_MAX_LIMIT_EXCEEDED_TEXT          = "The Ad-hoc trip can be booked up to 24 hours in advance. Please select a slot time within the next 24 hours.";

    public static final int    ACCOUNT_REFUND_AMOUNT_EXCEED_AVAILABLE_CREDIT_CODE    = 1096;
    public static final String ACCOUNT_REFUND_AMOUNT_EXCEED_AVAILABLE_CREDIT_TEXT    = "The requested refund amount exceeds the available account credit.";

    public static final int    TRIP_FEE_NOT_DEFINED_CODE                             = 1097;
    public static final String TRIP_FEE_NOT_DEFINED_TEXT                             = "Trip fee is not defined for this port operator and transaction type.";

    public static final int    TRIP_CANNOT_BE_CANCELLED_CODE                         = 1098;
    public static final String TRIP_CANNOT_BE_CANCELLED_TEXT                         = "Trip cannot be cancelled.";

    public static final int    TRIP_ALREADY_CANCELLED_CODE                           = 1099;
    public static final String TRIP_ALREADY_CANCELLED_TEXT                           = "Trip was already cancelled.";

    public static final int    ADHOC_TRIP_CREATE_NOT_ALLOWED_CODE                    = 1100;
    public static final String ADHOC_TRIP_CREATE_NOT_ALLOWED_TEXT                    = "Creating an ad-hoc trip is currently not allowed for this port operator and transaction type.";

    public static final int    MAX_AMOUNT_VEHICLE_RELEASE_REACHED_CODE               = 1126;
    public static final String MAX_AMOUNT_VEHICLE_RELEASE_REACHED_TEXT               = "More than 50% of released vehicles did not exit the parking area. Further manual release is currently not allowed.";

    public static final int    LIMIT_EXCEEDED_REQUEST_FORGOT_PASSWORD_CODE           = 1127;
    public static final String LIMIT_EXCEEDED_REQUEST_FORGOT_PASSWORD_TEXT           = "You have exceeded the number of attempts allowed.";

    public static final int    INVALID_TOO_WEAK_PASSWORD_CODE                        = 1128;
    public static final String INVALID_TOO_WEAK_PASSWORD_TEXT                        = "The given new password is too weak. Use at least 1 capital letter, 1 number and 1 special character. The password length has to be between 8 and 32 characters.";

    public static final int    MISMATCH_PASSWORD_CODE                                = 1129;
    public static final String MISMATCH_PASSWORD_TEXT                                = "Confirm Password does not match Password.";

    public static final int    INVALID_CURRENT_PASSWORD_CODE                         = 1130;
    public static final String INVALID_CURRENT_PASSWORD_TEXT                         = "The Current Password does not match";

    public static final int    LIMIT_EXCEEDED_VERIFICATION_CODE_SENT_CODE            = 1131;
    public static final String LIMIT_EXCEEDED_VERIFICATION_CODE_SENT_TEXT            = "You have exceeded the number of attempts allowed.";

    public static final int    CUSTOMER_EMAIL_ALREADY_REGISTERED_CODE                = 1132;
    public static final String CUSTOMER_EMAIL_ALREADY_REGISTERED_TEXT                = "Email is already registered.";

    public static final int    INVALID_VERIFICATION_CODE_CODE                        = 1133;
    public static final String INVALID_VERIFICATION_CODE_TEXT                        = "Invalid Verification Code.";

    public static final int    LIMIT_VERIFICATION_EXCEEDED_CODE                      = 1134;
    public static final String LIMIT_VERIFICATION_EXCEEDED_TEXT                      = "You have exceeded the number of attempts allowed.";

    public static final int    EXPIRED_VERIFICATION_CODE_CODE                        = 1135;
    public static final String EXPIRED_VERIFICATION_CODE_TEXT                        = "The Verification Code is expired.";

    public static final int    VERIFICATION_EXPIRED_CODE                             = 1136;
    public static final String VERIFICATION_EXPIRED_TEXT                             = "The time for completing the registration is up. Please start the registration process again.";

    public static final int    RESOLUTION_DATE_BEFORE_ISSUE_DATE_CODE                = 1137;
    public static final String RESOLUTION_DATE_BEFORE_ISSUE_DATE_TEXT                = "Resolution date cannot be before issue date.";

    public static final int    INVALID_OLD_PASSWORD_CODE                             = 1138;
    public static final String INVALID_OLD_PASSWORD_TEXT                             = "Invalid old password.";

    public static final int    ESTIMATED_RESOLUTION_DATE_IN_PAST_CODE                = 1139;
    public static final String ESTIMATED_RESOLUTION_DATE_IN_PAST_TEXT                = "Estimated resolution date cannot be in the past.";

    public static final int    INVALID_NEW_PASSWORD_CODE                             = 1140;
    public static final String INVALID_NEW_PASSWORD_TEXT                             = "Invalid New Password. New Password can't match Current Password.";

    public static final int    VEHICLE_ON_ACTIVE_TRIP_CODE                           = 1141;
    public static final String VEHICLE_ON_ACTIVE_TRIP_TEXT                           = "Vehicle has an active trip.";

    public static final int    SLOT_DATE_IN_PAST_CODE                                = 1142;
    public static final String SLOT_DATE_IN_PAST_TEXT                                = "Slot date cannot be in the past.";

    public static final int    INVALID_SLOT_DATE_RANGE_CODE                          = 1143;
    public static final String INVALID_SLOT_DATE_RANGE_TEXT                          = "Slot date should be between mission start and end date.";

    public static final int    NO_TRIPS_ASSOCIATED_WITH_VEHICLE_CODE                 = 1144;
    public static final String NO_TRIPS_ASSOCIATED_WITH_VEHICLE_TEXT                 = "Vehicle was not found. Please search by Operator and Transaction Type.";

    public static final int    MISSING_REFERENCE_NUMBER_CODE                         = 1145;
    public static final String MISSING_REFERENCE_NUMBER_TEXT                         = "Missing Reference number.";

    public static final int    NO_MISSION_ASSOCIATED_WITH_TRIP_CODE                  = 1146;
    public static final String NO_MISSION_ASSOCIATED_WITH_TRIP_TEXT                  = "No mission associated with trip.";

    public static final int    CREATE_ADHOC_TRIP_AND_MISSION_CODE                    = 1147;
    public static final String CREATE_ADHOC_TRIP_AND_MISSION_TEXT                    = "Create ad-hoc trip and mission.";

    public static final int    CREATE_ADHOC_TRIP_CODE                                = 1148;
    public static final String CREATE_ADHOC_TRIP_TEXT                                = "Create ad-hoc trip.";

    public static final int    ACCOUNT_BALANCE_LOW_CODE                              = 1149;
    public static final String ACCOUNT_BALANCE_LOW_TEXT                              = "Account balance is low. Proceed to payment.";

    public static final int    NO_ACCOUNT_ASSOCIATED_WITH_TRIP_CODE                  = 1150;
    public static final String NO_ACCOUNT_ASSOCIATED_WITH_TRIP_TEXT                  = "No account associated with trip. Proceed to payment.";

    public static final int    MISSION_DATE_IN_PAST_CODE                             = 1151;
    public static final String MISSION_DATE_IN_PAST_TEXT                             = "Mission start date cannot be in the past. Please select a valid mission start date.";

    public static final int    VEHICLE_ALREADY_ENTERED_PARKING_AREA_CODE             = 1152;
    public static final String VEHICLE_ALREADY_ENTERED_PARKING_AREA_TEXT             = "Vehicle had already entered the parking area.";

    public static final int    VEHICLE_ALREADY_ASSOCIATED_WITH_TRIP_CODE             = 1153;
    public static final String VEHICLE_ALREADY_ASSOCIATED_WITH_TRIP_TEXT             = "Vehicle already associated with trip. Proceed to payment.";

    public static final int    VEHICLE_ALREADY_ENTERED_PORT_AREA_CODE                = 1154;
    public static final String VEHICLE_ALREADY_ENTERED_PORT_AREA_TEXT                = "Vehicle has already entered the port.";

    public static final int    UNEXPECTED_MISSION_STATUS_CODE                        = 1155;
    public static final String UNEXPECTED_MISSION_STATUS_TEXT                        = "Unexpected mission status.";

    public static final int    MISSION_TRIPS_LIMIT_REACHED_CODE                      = 1156;
    public static final String MISSION_TRIPS_LIMIT_REACHED_TEXT                      = "Mission trips limit reached.";

    public static final int    UNEXPECTED_TRIP_STATUS_CODE                           = 1157;
    public static final String UNEXPECTED_TRIP_STATUS_TEXT                           = "Unexpected trip status.";

    public static final int    VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE                = 1158;
    public static final String VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT                = "Vehicle is not authorized port entry.";

    public static final int    CUSTOMER_EMAIL_ALREADY_REPORTED_CODE                  = 1159;
    public static final String CUSTOMER_EMAIL_ALREADY_REPORTED_TEXT                  = "Email is already reported.";

    public static final int    MISSION_TRIPS_CANNOT_BE_LESS_THAN_TRIPS_BOOKED_CODE   = 1161;
    public static final String MISSION_TRIPS_CANNOT_BE_LESS_THAN_TRIPS_BOOKED_TEXT   = "Mission total trips cannot be less than trips booked.";

    public static final int    MISSION_CANNOT_BE_EDITED_CODE                         = 1162;
    public static final String MISSION_CANNOT_BE_EDITED_TEXT                         = "Mission cannot be edited at this time.";

    public static final int    CUSTOMER_MSISDN_ALREADY_REGISTERED_CODE               = 1163;
    public static final String CUSTOMER_MSISDN_ALREADY_REGISTERED_TEXT               = "Msisdn is already registered.";

    public static final int    PARKING_EXIT_SMS_WAS_ALREADY_SENT_CODE                = 1164;
    public static final String PARKING_EXIT_SMS_WAS_ALREADY_SENT_TEXT                = "Parking exit SMS was already sent in the last few minutes.";

    public static final int    MISSION_CANNOT_BE_CANCEL_CODE                         = 1165;
    public static final String MISSION_CANNOT_BE_CANCEL_TEXT                         = "The mission cannot be cancelled.";

    public static final int    MISSION_WITH_BAD_ALREADY_EXISTS_CODE                  = 1167;
    public static final String MISSION_WITH_BAD_ALREADY_EXISTS_TEXT                  = "A mission with this BAD / Booking Export already exists.";

    public static final int    MISSING_CONTAINER_ID_CODE                             = 1168;
    public static final String MISSING_CONTAINER_ID_TEXT                             = "Missing Container ID.";

    public static final int    MISSION_WITH_CONTAINER_ALREADY_EXISTS_CODE            = 1169;
    public static final String MISSION_WITH_CONTAINER_ALREADY_EXISTS_TEXT            = "A mission with this Container ID already exists.";

    public static final int    VEHICLE_ALREADY_EXITED_PORT_AREA_CODE                 = 1170;
    public static final String VEHICLE_ALREADY_EXITED_PORT_AREA_TEXT                 = "Vehicle has already exited the port.";

    public static final int    VEHICLE_NOT_FOUND_PROCEED_EXIT_CODE                   = 1171;
    public static final String VEHICLE_NOT_FOUND_PROCEED_EXIT_TEXT                   = "Vehicle not found. Proceed Exit?";

    public static final int    VEHICLE_ALREADY_ADDED_TO_ACCOUNT_CODE                 = 1172;
    public static final String VEHICLE_ALREADY_ADDED_TO_ACCOUNT_TEXT                 = "Vehicle has already been added to this account.";

    public static final int    VEHICLE_ALREADY_PARKED_CODE                           = 1173;
    public static final String VEHICLE_ALREADY_PARKED_TEXT                           = "Vehicle is already parked. Please search again.";

    public static final int    OPERATOR_WITH_USERNAME_ALREADY_EXISTS_CODE            = 1174;
    public static final String OPERATOR_WITH_USERNAME_ALREADY_EXISTS_TEXT            = "Operator with this username already exists.";

    public static final int    OPERATOR_ALREADY_DEACTIVATED_CODE                     = 1175;
    public static final String OPERATOR_ALREADY_DEACTIVATED_TEXT                     = "Operator account was already deactivated.";

    public static final int    KIOSK_OPERATOR_ACTIVE_SESSION_EXISTS_CODE             = 1176;
    public static final String KIOSK_OPERATOR_ACTIVE_SESSION_EXISTS_TEXT             = "An active kiosk session for this operator already exists.";

    public static final int    AMOUNT_END_LESS_THAN_AMOUNT_START_CODE                = 1177;
    public static final String AMOUNT_END_LESS_THAN_AMOUNT_START_TEXT                = "The end amount collected cannot be less than the start amount.";

    public static final int    AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_CODE     = 1178;
    public static final String AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_TEXT     = "The end amount collected is different than the expected amount.";

    public static final int    FAILURE_SAVING_FILE_CODE                              = 1179;
    public static final String FAILURE_SAVING_FILE_TEXT                              = "Saving File Failure.";

    public static final int    MAPPING_DEVICE_ALREADY_MAPPED_CODE                    = 1180;
    public static final String MAPPING_DEVICE_ALREADY_MAPPED_TEXT                    = "Device already mapped.";

    public static final int    TRIP_CREATE_DATE_START_VIOLATION_CODE                 = 1181;
    public static final String TRIP_CREATE_DATE_START_VIOLATION_TEXT                 = "Trip cannot be created. Slot start date and time is after todays date and time.";

    public static final int    TRIP_CREATE_DATE_END_VIOLATION_CODE                   = 1182;
    public static final String TRIP_CREATE_DATE_END_VIOLATION_TEXT                   = "Trip cannot be created. Slot end date and time is in the past.";

    public static final int    TRIP_ALREADY_BOOKED_CODE                              = 1183;
    public static final String TRIP_ALREADY_BOOKED_TEXT                              = "Trip reference number is already assigned to a trip.";

    public static final int    WHITELIST_EXISTS_CODE                                 = 1184;
    public static final String WHITELIST_EXISTS_TEXT                                 = "A whitelist with this datetime already exists.";

    public static final int    MISSION_WITH_REFERENCE_NOT_FOUND_CODE                 = 1185;
    public static final String MISSION_WITH_REFERENCE_NOT_FOUND_TEXT                 = "Mission not found with this reference number.";

    public static final int    DRIVER_ALREADY_ASSOCIATED_TO_ACCOUNT_CODE             = 1186;
    public static final String DRIVER_ALREADY_ASSOCIATED_TO_ACCOUNT_TEXT             = "Driver has already been associated to this transporter account.";

    public static final int    DRIVER_NOT_ASSOCIATED_TO_ACCOUNT_CODE                 = 1187;
    public static final String DRIVER_NOT_ASSOCIATED_TO_ACCOUNT_TEXT                 = "Driver is not associated to this transporter account.";

    public static final int    BOOKING_TRIP_LIMIT_REACHED_FOR_PORT_OPERATOR_CODE     = 1188;
    public static final String BOOKING_TRIP_LIMIT_REACHED_FOR_PORT_OPERATOR_TEXT     = "Trip booking limit reached for this port operator.";

    public static final int    BOOKING_TRIP_SLOT_LIMIT_REACHED_CODE                  = 1189;
    public static final String BOOKING_TRIP_SLOT_LIMIT_REACHED_TEXT                  = "Trip booking limit reached.";

    public static final int    VEHICLE_ALREADY_EXITED_PARKING_AREA_CODE              = 1190;
    public static final String VEHICLE_ALREADY_EXITED_PARKING_AREA_TEXT              = "Vehicle has already exited the parking area.";

    public static final int    COTIZEL_PAYMENT_FAILED_CODE                           = 1191;
    public static final String COTIZEL_PAYMENT_FAILED_TEXT                           = "Payment failed.";

    public static final int    MISSION_DATE_NOT_STARTED_CODE                         = 1192;
    public static final String MISSION_DATE_NOT_STARTED_TEXT                         = "The trip can be created only after the mission start date. Check the Mission Created email/SMS for reference.";

    public static final int    MISSION_DATE_EXPIRED_CODE                             = 1193;
    public static final String MISSION_DATE_EXPIRED_TEXT                             = "The trip can't be created as the mission date is expired. Check the Mission Created Email/SMS for reference.";

    public static final int    VEHICLE_REGISTRATION_ALREADY_WHITELISTED_CODE         = 1194;
    public static final String VEHICLE_REGISTRATION_ALREADY_WHITELISTED_TEXT         = "Vehicle already whitelisted.";

    public static final int    RECEIPT_LINK_EXPIRED_CODE                             = 1195;
    public static final String RECEIPT_LINK_EXPIRED_TEXT                             = "Receipt Link is Expired.";

    public static final int    ACCOUNT_NOT_FOUND_CODE                                = 1196;
    public static final String ACCOUNT_NOT_FOUND_TEXT                                = "Account not found.";

    public static final int    RECEIPT_DOWNLOAD_LOCK_CODE                            = 1197;
    public static final String RECEIPT_DOWNLOAD_LOCK_TEXT                            = "Receipt download is locked due too many wrong attempts.";

    public static final int    TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE                  = 1198;
    public static final String TRIP_FOR_VEHICLE_ALREADY_BOOKED_TEXT                  = "This vehicle has already been booked on another trip.";

    public static final int    LANE_ALREADY_EXIST_CODE                               = 1199;
    public static final String LANE_ALREADY_EXIST_TEXT                               = "This vehicle has already been booked on another trip.";

    // [1200] API Failure - Internal Generic
    public static final int    API_FAILURE_CODE                                      = 1200;
    public static final String API_FAILURE_TEXT                                      = "Failure.";

    // [1201 - 1299] API Failure - Internal
    public static final int    API_AUTHENTICATION_FAILURE_CODE                       = 1201;
    public static final String API_AUTHENTICATION_FAILURE_TEXT                       = "Authentication Failure.";

    public static final int    ACCESS_DENIED_CODE                                    = 1202;
    public static final String ACCESS_DENIED_TEXT                                    = "Access Denied.";

    // [1300] API Failure - Internal
    public static final int    DATABASE_FAILURE_CODE                                 = 1300;
    public static final String DATABASE_FAILURE_TEXT                                 = "Database Error";

    // [1400 - 1499] API Failure - External
    public static final int    EXTERNAL_API_SOCKET_TIMEOUT_CODE                      = 1400;
    public static final String EXTERNAL_API_SOCKET_TIMEOUT_TEXT                      = "Connection Timeout.";

    public static final int    EXTERNAL_API_CONNECTION_FAILURE_CODE                  = 1401;
    public static final String EXTERNAL_API_CONNECTION_FAILURE_TEXT                  = "Connection Failure.";

    public static final int    EXTERNAL_API_SYSTEM_OFFLINE_CODE                      = 1402;
    public static final String EXTERNAL_API_SYSTEM_OFFLINE_TEXT                      = "System Offline.";

    public static final int    EXTERNAL_API_ERROR_CODE                               = 1403;
    public static final String EXTERNAL_API_ERROR_TEXT                               = "API error.";

    public static final int    EXTERNAL_API_300_ERROR_CODE                           = 1404;
    public static final String EXTERNAL_API_300_ERROR_TEXT                           = "API error. Invalid Token.";

    public static final int    EXTERNAL_API_301_ERROR_CODE                           = 1405;
    public static final String EXTERNAL_API_301_ERROR_TEXT                           = "API error. Access not allowed.";

    public static final int    EXTERNAL_API_302_ERROR_CODE                           = 1406;
    public static final String EXTERNAL_API_302_ERROR_TEXT                           = "API error. You no longer have SMS.";

    public static final int    EXTERNAL_API_303_ERROR_CODE                           = 1407;
    public static final String EXTERNAL_API_303_ERROR_TEXT                           = "API error. Group not found.";

    public static final int    EXTERNAL_API_304_ERROR_CODE                           = 1408;
    public static final String EXTERNAL_API_304_ERROR_TEXT                           = "API error. Incorrect login or password.";

    public static final int    EXTERNAL_API_401_ERROR_CODE                           = 1409;
    public static final String EXTERNAL_API_401_ERROR_TEXT                           = "API error. Invalid invoice reference.";

    public static final int    EXTERNAL_API_402_ERROR_CODE                           = 1410;
    public static final String EXTERNAL_API_402_ERROR_TEXT                           = "API error. Invoice reference already paid.";

    public static final int    EXTERNAL_API_403_ERROR_CODE                           = 1411;
    public static final String EXTERNAL_API_403_ERROR_TEXT                           = "API error. Invalid phone number or non-existent wallet account.";

    public static final int    EXTERNAL_API_405_ERROR_CODE                           = 1412;
    public static final String EXTERNAL_API_405_ERROR_TEXT                           = "API error. Similar transaction has been made recently.";

    public static final int    EXTERNAL_API_400_ERROR_CODE                           = 1413;
    public static final String EXTERNAL_API_400_ERROR_TEXT                           = "API error. Insufficient balance.";

    public static final int    PARKING_PERMISSION_ID_NOT_FOUND_CODE                  = 1450;
    public static final int    PARKING_PERMISSION_EXPIRED_CODE                       = 1451;
    public static final int    PARKING_PERMISSION_MANUAL_PARKING_EXIT_CODE           = 1452;
    public static final int    PARKING_PERMISSION_MANUAL_PORT_ENTRY_CODE             = 1453;
    public static final int    PARKING_PERMISSION_DATETIME_FORMAT_PARSE_ERROR        = 1454;
    public static final int    PARKING_PERMISSION_ID_CANNOT_DELETE_STILL_IN_USE_CODE = 1455;

    public static final int    API_SUCCESS_CODE                                      = 1600;
    public static final String API_SUCCESS_TEXT                                      = "Success.";

    public static final int    SUCCESS_TEST_CODE                                     = 3000;

    public static final int    MNO_RESPONSE_CODE_FAILURE                             = 1500;
    public static final int    MNO_RESPONSE_CODE_INITIATED                           = 1555;
    public static final int    MNO_RESPONSE_CODE_CALLBACK_FAILURE                    = 1560;
    public static final int    MNO_RESPONSE_CODE_SUCCESS                             = 1600;

}
