padApp.service('selectorOptionsService', ['$rootScope', function ($rootScope){
    $rootScope.getSelectorOptions = function() {
        $rootScope.operatorRoleOptions = [
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PARKING_SUPERVISOR_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PARKING_SUPERVISOR_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PARKING_KIOSK_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PARKING_OFFICE_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PARKING_OFFICE_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PORT_OPERATOR_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_AUTHORITY_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PORT_AUTHORITY_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_ENTRY_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PORT_ENTRY_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_EXIT_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PORT_EXIT_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_ADMIN,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_ADMIN_LABEL
            },
            {
                value: $rootScope.operatorRoleConstants.OPERATOR_ROLE_FINANCE_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_FINANCE_LABEL
            }
        ];

        $rootScope.typeOptions = [
            {
                value: $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_AUTOMATIC_MANUAL,
                name: $rootScope.translation.KEY_SCREEN_AUTOMATICT_LABEL + "/" + $rootScope.translation.KEY_SCREEN_MANUAL_LABEL
            },
            {
                value: $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_AUTOMATIC,
                name: $rootScope.translation.KEY_SCREEN_AUTOMATICT_LABEL
            },
            {
                value: $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_MANUAL,
                name: $rootScope.translation.KEY_SCREEN_MANUAL_LABEL
            },
            {
                value: $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_UNKNOWN,
                name: $rootScope.translation.KEY_SCREEN_UNKNOWN_LABEL
            },
            {
                value: $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_HEARTBEAT,
                name: $rootScope.translation.KEY_SCREEN_HEARTBEAT_LABEL
            }
        ];

        $rootScope.typeInvoiceStatementOptions = [
            {
                value: $rootScope.invoicesTypeConstants.INVOICES_TYPE_STATEMENT,
                name: $rootScope.translation.KEY_SCREEN_STATEMENT_LABEL
            },
            {
                value: $rootScope.invoicesTypeConstants.INVOICES_TYPE_INVOICE,
                name: $rootScope.translation.KEY_SCREEN_INVOICE_LABEL
            }
        ];

        $rootScope.typeParkingOptions = [
            {
                value: $rootScope.parkingTypeConstants.PARKING_TYPE_PARKING,
                name: $rootScope.translation.KEY_NAVBAR_PARKING
            },
            {
                value: $rootScope.parkingTypeConstants.PARKING_TYPE_EXIT_ONLY,
                name: $rootScope.translation.KEY_NAVBAR_PARKING_EXIT_ONLY
            }
        ];

        $rootScope.typeKioskSessionOptions = [
            {
                value: $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_PARKING_KIOSK_LABEL
            },
            {
                value: $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_VIRTUAL,
                name: $rootScope.translation.KEY_SCREEN_OPERATOR_TYPE_VIRTUAL_KIOSK_LABEL
            }
        ];

        $rootScope.typeAccountOptions = [
            {
                value: $rootScope.paymentTypeConstants.PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE,
                name: $rootScope.getPaymentTypeText($rootScope.paymentTypeConstants.PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE)
            },
            {
                value: $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_TOPUP,
                name: $rootScope.getPaymentTypeText($rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_TOPUP)
            },
            {
                value: $rootScope.paymentTypeConstants.PAYMENT_TYPE_TRIP_FEE,
                name: $rootScope.getPaymentTypeText($rootScope.paymentTypeConstants.PAYMENT_TYPE_TRIP_FEE)
            },
            {
                value: $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_DEBIT,
                name: $rootScope.getPaymentTypeText($rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_DEBIT)
            },
            {
                value: $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_CREDIT,
                name: $rootScope.getPaymentTypeText($rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_CREDIT)
            }
        ];

        $rootScope.reportTypeOptions = [
            {
                value: $rootScope.reportTypeConstants.REPORT_TYPE_PARKING_ENTRY,
                name: $rootScope.translation.KEY_SCREEN_PARKING_ENTRY_COUNTS_LABEL
            },
            {
                value: $rootScope.reportTypeConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_PORT_ENTRY_OPERATOR_COUNTS_LABEL
            }
        ];

        $rootScope.statusOptions = [
            {
                value: $rootScope.tripConstants.TRIP_STATUS_PENDING_APPROVAL,
                name: $rootScope.translation.KEY_SCREEN_PENDING_APPROVAL_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_APPROVED,
                name: $rootScope.translation.KEY_SCREEN_APPROVED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_DENIED_BY_OFFICE_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_DENIED_BY_OFFICE_OPERATOR_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_COMPLETED,
                name: $rootScope.translation.KEY_SCREEN_COMPLETED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_ENTERED_PARKING,
                name: $rootScope.translation.KEY_SCREEN_ENTERED_PARKING_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY,
                name: $rootScope.translation.KEY_SCREEN_EXITED_PARKING_PREMATURELY_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY_EXPIRED,
                name: $rootScope.translation.KEY_SCREEN_EXITED_PARKING_PREMATURELY_EXPIRED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_IN_TRANSIT,
                name: $rootScope.translation.KEY_SCREEN_IN_TRASIT_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_IN_TRANSIT_EXPIRED,
                name: $rootScope.translation.KEY_SCREEN_IN_TRANSIT_EXPIRED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_ENTERED_PORT,
                name: $rootScope.translation.KEY_SCREEN_ENTERED_PORT_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_DENIED_PORT_ENTRY,
                name: $rootScope.translation.KEY_SCREEN_DENIED_PORT_ENTRY_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_CANCELLED,
                name: $rootScope.translation.KEY_SCREEN_CANCELLED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_CANCELLED_SYSTEM,
                name: $rootScope.translation.KEY_SCREEN_CANCELLED_BY_SYSTEM_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_ABORTED,
                name: $rootScope.translation.KEY_SCREEN_ABORTED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_PORT_EXIT_EXPIRED,
                name: $rootScope.translation.KEY_SCREEN_PORT_EXIT_EXPIRED_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_COMPLETED_SYSTEM,
                name: $rootScope.translation.KEY_SCREEN_COMPLETED_BY_SYSTEM_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_PENDING,
                name: $rootScope.translation.KEY_SCREEN_PENDING_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_CANCELLED_BY_KIOSK_OPERATOR,
                name: $rootScope.translation.KEY_SCREEN_CANCELLED_BY_KIOSK_OPERATOR_LABEL
            },
            {
                value: $rootScope.tripConstants.TRIP_STATUS_IN_FLIGHT,
                name: $rootScope.translation.KEY_SCREEN_IN_FLIGHT_LABEL
            }
        ];

        $rootScope.statusParkingOptions = [
            {
                value: "",
                name: $rootScope.translation.KEY_SCREEN_ENTRY_LABEL + "/" + $rootScope.translation.KEY_SCREEN_EXIT_DUE_LABEL
            },
            {
                value: $rootScope.parkingStatusConstants.PARKING_STATUS_ENTRY,
                name: $rootScope.translation.KEY_SCREEN_ENTRY_LABEL
            },
            {
                value: $rootScope.parkingStatusConstants.PARKING_STATUS_EXIT,
                name: $rootScope.translation.KEY_SCREEN_EXIT_LABEL
            },
            {
                value: $rootScope.parkingStatusConstants.PARKING_STATUS_REMINDER_EXIT_DUE,
                name: $rootScope.translation.KEY_SCREEN_EXIT_DUE_LABEL
            },
            {
                value: $rootScope.parkingStatusConstants.PARKING_STATUS_ENTERED_PORT,
                name: $rootScope.translation.KEY_SCREEN_ENTERED_PORT_LABEL
            },
            {
                value: $rootScope.parkingStatusConstants.PARKING_STATUS_IN_TRANSIT_EXPIRED,
                name: $rootScope.translation.KEY_SCREEN_IN_TRANSIT_EXPIRED_LABEL
            },
            {
                value: $rootScope.parkingStatusConstants.PARKING_STATUS_EXITED_PREMATURELY_EXPIRED,
                name: $rootScope.translation.KEY_SCREEN_EXITED_PREMATURELY_EXPIRED_LABEL
            }
        ];

        $rootScope.statusKioskSessionOptions = [
            {
                value: $rootScope.kioskSessionConstants.KIOSK_SESSION_STATUS_ASSIGNED,
                name: $rootScope.translation.KEY_SCREEN_STATUS_ASSIGNED_LABEL
            },
            {
                value: $rootScope.kioskSessionConstants.KIOSK_SESSION_STATUS_START,
                name: $rootScope.translation.KEY_SCREEN_START_LABEL
            },
            {
                value: $rootScope.kioskSessionConstants.KIOSK_SESSION_STATUS_END,
                name: $rootScope.translation.KEY_SCREEN_END_LABEL
            },
            {
                value: $rootScope.kioskSessionConstants.KIOSK_SESSION_STATUS_VALIDATED,
                name: $rootScope.translation.KEY_SCREEN_STATUS_VALIDATED_LABEL
            }
        ];

        $rootScope.statusPortEntryOptions = [
            {
                value: $rootScope.portAccessStatusConstants.PORT_ACCESS_STATUS_ENTRY,
                name: $rootScope.translation.KEY_SCREEN_ENTRY_LABEL
            },
            {
                value: $rootScope.portAccessStatusConstants.PORT_ACCESS_STATUS_DENY,
                name: $rootScope.translation.KEY_SCREEN_DENY_LABEL
            },
            {
                value: $rootScope.portAccessStatusConstants.PORT_ACCESS_STATUS_EXIT,
                name: $rootScope.translation.KEY_SCREEN_EXIT_LABEL
            },
            {
                value: $rootScope.portAccessStatusConstants.PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM,
                name: $rootScope.translation.KEY_SCREEN_EXIT_CLOSED_BY_SYSTEM_LABEL
            }
        ];

        $rootScope.statusMissionOptions = [
            {
                value: $rootScope.missionConstants.MISSION_STATUS_TRIPS_PENDING,
                name: $rootScope.translation.KEY_SCREEN_TRIPS_PENDING_MESSAGE
            },
            {
                value: $rootScope.missionConstants.MISSION_STATUS_TRIPS_BOOKED,
                name: $rootScope.translation.KEY_SCREEN_TRIPS_BOOKED_LABEL
            },
            {
                value: $rootScope.missionConstants.MISSION_STATUS_EXPIRED,
                name: $rootScope.translation.KEY_SCREEN_EXPIRED_LABEL
            },
            {
                value: $rootScope.missionConstants.MISSION_STATUS_CANCELLED,
                name: $rootScope.translation.KEY_SCREEN_CANCELLED_LABEL
            }
        ];

        $rootScope.statusAccountOptions = [
            {
                value: $rootScope.accountStatusConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION,
                name: $rootScope.translation.KEY_SCREEN_ACCOUNT_PENDING_ACTIVATION_LABEL
            },
            {
                value: $rootScope.accountStatusConstants.ACCOUNT_STATUS_ACTIVE,
                name: $rootScope.translation.KEY_SCREEN_ACTIVE_LABEL
            },
            {
                value: $rootScope.accountStatusConstants.ACCOUNT_STATUS_DENIED,
                name: $rootScope.translation.KEY_SCREEN_DENIED_LABEL
            },
            {
                value: $rootScope.accountStatusConstants.ACCOUNT_STATUS_INACTIVE,
                name: $rootScope.translation.KEY_SCREEN_INACTIVE_LABEL
            }
        ];

        $rootScope.paymentOptions = [
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_WARI,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_WARI)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_E_MONEY,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_E_MONEY)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_ECOBANK,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_ECOBANK)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT)
            },
            {
                value: $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND,
                name: $rootScope.getPaymentOption($rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND)
            }
        ];

        $rootScope.regCountryOptions = [
            {
                value: "SN",
                name: $rootScope.translation.KEY_SCREEN_SENEGAL_LABEL
            },
            {
                value: "ML",
                name: "Mali"
            },
            {
                value: "GM",
                name: $rootScope.translation.KEY_SCREEN_GAMBIA_LABEL
            },
            {
                value: "GN",
                name: $rootScope.translation.KEY_SCREEN_GUINEA_LABEL
            },
            {
                value: "GW",
                name: $rootScope.translation.KEY_SCREEN_GUINEA_BISSAU_LABEL
            },
            {
                value: "MR",
                name: $rootScope.translation.KEY_SCREEN_MAURITANIA_LABEL
            }
        ];

        $rootScope.languageIdOptions = [
            {
                value: $rootScope.languageConstants.LANGUAGE_ENGLISH,
                name: $rootScope.translation.KEY_SCREEN_ENGLISH_LABEL
            },
            {
                value: $rootScope.languageConstants.LANGUAGE_FRENCH,
                name: $rootScope.translation.KEY_SCREEN_FRENCH_LABEL
            },
            {
                value: $rootScope.languageConstants.LANGUAGE_WOLOF,
                name: $rootScope.translation.KEY_SCREEN_WOLOF_LABEL
            },
            {
                value: $rootScope.languageConstants.LANGUAGE_BAMBARA,
                name: $rootScope.translation.KEY_SCREEN_BAMBARA_LABEL
            }
        ];

        $rootScope.zoneOptions = [
            {
                value: "Môle 3"
            },
            {
                value: "Môle 10"
            },
            {
                value: "North Zone"
            }
        ];
    }
}])
