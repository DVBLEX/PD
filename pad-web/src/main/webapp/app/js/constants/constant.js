padApp.constant("validationConstants", {
	REFERENCE_VALIDATION_LENGTH : 16,
	CONTAINER_VALIDATION_LENGTH : 16,
	REGNUMBER_VALIDATION_LENGTH_MIN : 3,
	REGNUMBER_VALIDATION_LENGTH_MAX : 16
});

padApp.constant("tripConstants", {
	TRIP_STATUS_ALL : -1,
	TRIP_STATUS_APPROVED : 1,
	TRIP_STATUS_COMPLETED : 2,
	TRIP_STATUS_ENTERED_PARKING : 3,
	TRIP_STATUS_EXITED_PARKING_PREMATURELY : 4,
	TRIP_STATUS_IN_TRANSIT : 5,
	TRIP_STATUS_ENTERED_PORT : 6,
	TRIP_STATUS_DENIED_PORT_ENTRY : 7,
	TRIP_STATUS_IN_TRANSIT_EXPIRED : 8,
	TRIP_STATUS_PENDING_APPROVAL : 10,
	TRIP_STATUS_CANCELLED : 11,
	TRIP_STATUS_ABORTED : 12,
	TRIP_STATUS_DENIED_BY_OFFICE_OPERATOR : 13,
	TRIP_STATUS_PORT_EXIT_EXPIRED : 14,
	TRIP_STATUS_COMPLETED_SYSTEM : 15,
    TRIP_STATUS_PENDING : 16,
	TRIP_STATUS_CANCELLED_SYSTEM : 17,
    TRIP_STATUS_EXITED_PARKING_PREMATURELY_EXPIRED : 18,
    TRIP_STATUS_CANCELLED_BY_KIOSK_OPERATOR : 19,
    TRIP_STATUS_IN_FLIGHT : 20
});

padApp.constant("accountStatusConstants", {
	ACCOUNT_STATUS_PENDING_FOR_ACTIVATION : 1,
	ACCOUNT_STATUS_ACTIVE : 2,
    ACCOUNT_STATUS_DENIED : 3,
	ACCOUNT_STATUS_INACTIVE : 4
});

padApp.constant("accountTypeConstants", {
	ACCOUNT_TYPE_COMPANY : 1,
	ACCOUNT_TYPE_INDIVIDUAL : 2,
	ACCOUNT_TYPE_OPERATOR : 100
});

padApp.constant("accountPaymentTermsTypeConstants", {
	ACCOUNT_PAYMENT_NO_TERMS_TYPE : -1,
	ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY : 1,
	ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY : 2
});

padApp.constant("missionConstants", {
	MISSION_STATUS_TRIPS_PENDING : 1,
	MISSION_STATUS_TRIPS_BOOKED : 2,
	MISSION_STATUS_EXPIRED : 3,
	MISSION_STATUS_CANCELLED : 4
});

padApp.constant("operatorRoleConstants", {
	OPERATOR_ROLE_TRANSPORTER : 1,
	OPERATOR_ROLE_PORT_OPERATOR : 2,
	OPERATOR_ROLE_PARKING_OPERATOR : 3,
	OPERATOR_ROLE_PARKING_KIOSK_OPERATOR : 4,
	OPERATOR_ROLE_PARKING_OFFICE_OPERATOR : 5,
	OPERATOR_ROLE_PORT_AUTHORITY_OPERATOR : 6,
	OPERATOR_ROLE_PORT_ENTRY_OPERATOR : 7,
	OPERATOR_ROLE_PORT_EXIT_OPERATOR : 8,
	OPERATOR_ROLE_PARKING_SUPERVISOR_OPERATOR : 9,
	OPERATOR_ROLE_FINANCE_OPERATOR : 50,
	OPERATOR_ROLE_ADMIN : 100
});

padApp.constant("portOperatorConstants", {
	PORT_OPERATOR_DPWORLD : 1,
	PORT_OPERATOR_TVS : 2,
	PORT_OPERATOR_DAKAR_TERMINAL : 3,
	PORT_OPERATOR_VIVO_ENERGY : 4,
	PORT_OPERATOR_SENSTOCK : 5,
	PORT_OPERATOR_ORYX : 6,
	PORT_OPERATOR_ERES : 7,
	PORT_OPERATOR_TM_NORTH : 8,
	PORT_OPERATOR_TM_SOUTH : 9,
	PORT_OPERATOR_ISTAMCO : 10,
	PORT_OPERATOR_MOLE_10 : 11,
	PORT_OPERATOR_TM : 99
});

padApp.constant("kioskSessionConstants", {
	KIOSK_SESSION_STATUS_ASSIGNED : 1,
	KIOSK_SESSION_STATUS_START : 2,
	KIOSK_SESSION_STATUS_END : 3,
	KIOSK_SESSION_STATUS_VALIDATED : 4
});

padApp.constant("kioskSessionTypeConstants", {
	KIOSK_SESSION_TYPE_PARKING : 1,
	KIOSK_SESSION_TYPE_VIRTUAL : 2
});

padApp.constant("kioskSessionLaneNumberConstants", {
	KIOSK_SESSION_LANE_NUMBER_1 : 1,
	KIOSK_SESSION_LANE_NUMBER_2 : 2,
	KIOSK_SESSION_LANE_NUMBER_3 : 3,
	KIOSK_SESSION_LANE_NUMBER_4 : 4,
	KIOSK_SESSION_LANE_NUMBER_5 : 5,
	KIOSK_SESSION_LANE_NUMBER_6 : 6,
	KIOSK_SESSION_LANE_NUMBER_7 : 7,
});

padApp.constant("transactionTypeConstants", {
	TRANSACTION_TYPE_DROP_OFF_EXPORT : 1,
	TRANSACTION_TYPE_DROP_OFF_EMPTY_DAY_M : 2,
	TRANSACTION_TYPE_PICK_UP_IMPORT : 3,
	TRANSACTION_TYPE_PICK_UP_EMPTY : 4,
	TRANSACTION_TYPE_URGENT_DROP_OFF_EXPORT : 5,
	TRANSACTION_TYPE_URGENT_PICK_UP_IMPORT : 6,
	TRANSACTION_TYPE_NIGHT_RUN : 7,
	TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT : 8,
	TRANSACTION_TYPE_DROP_OFF_IMPORT_DRAY_IN : 9,
	TRANSACTION_TYPE_PICK_UP_EXPORT_DRAY_OFF : 10,
	TRANSACTION_TYPE_DROP_OFF_EMPTY_DAY_S : 11,
	TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE : 12
});

padApp.constant("driverAssociationStatusConstants", {
	DRIVER_ASSOCIATION_STATUS_PENDING : 1,
	DRIVER_ASSOCIATION_STATUS_REJECTED : 2,
	DRIVER_ASSOCIATION_STATUS_APPROVED : 3,
	DRIVER_ASSOCIATION_STATUS_DELETED : 4
});

padApp.constant("portAccessStatusConstants", {
	PORT_ACCESS_STATUS_ENTRY : 1,
	PORT_ACCESS_STATUS_DENY : 2,
	PORT_ACCESS_STATUS_EXIT : 3,
	PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM : 4,
	PORT_ACCESS_STATUS_ALL : -1
});

padApp.constant("reportTypeConstants", {
	REPORT_TYPE_PARKING_ENTRY : 1,
	REPORT_TYPE_PORT_ENTRY_OPERATOR : 2
});

padApp.constant("parkingExitMsgIdConstants", {
	PARKING_EXIT_MESSAGE_ID_AUTH_EXIT : 1,
	PARKING_EXIT_MESSAGE_ID_PREMATURE_EXIT : 2,
	PARKING_EXIT_MESSAGE_ID_EXIT_ONLY : 3
});

padApp.constant("paymentTypeConstants", {
	PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE: 1,
	PAYMENT_TYPE_ACCOUNT_TOPUP : 2,
	PAYMENT_TYPE_TRIP_FEE : 3,
	PAYMENT_TYPE_ACCOUNT_DEBIT : 4,
	PAYMENT_TYPE_ACCOUNT_CREDIT : 5,
})

padApp.constant("paymentOptionConstants", {
	PAYMENT_OPTION_CASH : 1,
	PAYMENT_OPTION_ORANGE_MONEY : 2,
	PAYMENT_OPTION_WARI : 3,
	PAYMENT_OPTION_FREE_MONEY : 4,
	PAYMENT_OPTION_E_MONEY : 5,
	PAYMENT_OPTION_ECOBANK : 6,
    PAYMENT_OPTION_BANK_TRANSFER : 7,
    PAYMENT_OPTION_CHEQUE : 8,
    PAYMENT_OPTION_ACCOUNT_CREDIT : 9,
    PAYMENT_OPTION_CASH_REFUND : 10,
    PAYMENT_OPTION_ACCOUNT_DEBIT : 11
});

padApp.constant("parkingStatusConstants", {
	PARKING_STATUS_ENTRY : 1,
	PARKING_STATUS_EXIT : 2,
	PARKING_STATUS_REMINDER_EXIT_DUE : 3,
	PARKING_STATUS_ENTERED_PORT : 4,
	PARKING_STATUS_IN_TRANSIT_EXPIRED : 5,
    PARKING_STATUS_EXITED_PREMATURELY_EXPIRED : 6,
	PARKING_STATUS_ALL : 10
});

padApp.constant("parkingTypeConstants", {
	PARKING_TYPE_PARKING : 1,
	PARKING_TYPE_EXIT_ONLY : 2,
	PARKING_TYPE_ALL : 10
});

padApp.constant("vehicleParkingStateConstants", {
	VEHICLE_PARKING_STATE_NORMAL : 1,
	VEHICLE_PARKING_STATE_BROKEN_DOWN : 2,
	VEHICLE_PARKING_STATE_CLAMPED : 3,
	VEHICLE_PARKING_STATE_UNRESPONSIVE : 4
});

padApp.constant("vehicleCounterTypeConstants", {
	VEHICLE_COUNTER_TYPE_AUTOMATIC_MANUAL : 'AM',
	VEHICLE_COUNTER_TYPE_AUTOMATIC : 'A',
	VEHICLE_COUNTER_TYPE_MANUAL : 'M',
	VEHICLE_COUNTER_TYPE_UNKNOWN : 'U',
	VEHICLE_COUNTER_TYPE_HEARTBEAT : 'H'
});

padApp.constant("invoicesTypeConstants", {
	INVOICES_TYPE_STATEMENT: 1,
	INVOICES_TYPE_INVOICE: 2,
});

padApp.constant("languageConstants", {
	LANGUAGE_ENGLISH: 1,
	LANGUAGE_FRENCH: 2,
	LANGUAGE_WOLOF: 3,
	LANGUAGE_BAMBARA: 4
});
