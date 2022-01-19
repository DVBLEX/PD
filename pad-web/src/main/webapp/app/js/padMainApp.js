padApp = angular.module('padApp', [ 'angularSpinner', 'ngResource', 'ngRoute', 'tcCom', 'ngIntlTelInput', 'chart.js' ]);

padApp.config([ '$httpProvider', '$routeProvider', function($httpProvider, $routeProvider) {

    $routeProvider.when('/', {
        templateUrl : 'app/views/home.html',
        controller : 'HomeController'
    }).when('/changePassword', {
        templateUrl : 'app/views/changePassword.html',
        controller : 'ChangePasswordController'

    }).when('/missionTrip', {
        templateUrl : 'app/views/transporter/missionTrip.html',
        controller : 'MissionTripController'
    }).when('/transporterMissions', {
        templateUrl : 'app/views/transporter/mission.html',
        controller : 'CreateMissionController'
    }).when('/vehicle', {
        templateUrl : 'app/views/transporter/vehicle.html',
        controller : 'VehicleController'
    }).when('/driver', {
        templateUrl : 'app/views/transporter/driver.html',
        controller : 'DriverController'
    }).when('/addVehicle', {
        templateUrl : 'app/views/transporter/vehicleAdd.html',
        controller : 'AddVehicleController'
    }).when('/addDriver', {
        templateUrl : 'app/views/transporter/driverAdd.html',
        controller : 'AddDriverController'
    }).when('/account', {
        templateUrl : 'app/views/transporter/account.html',
        controller : 'AccountTransporterController'
    }).when('/invoice', {
        templateUrl : 'app/views/transporter/invoice.html',
        controller : 'InvoiceController'
    }).when('/paymentTransporter', {
        templateUrl : 'app/views/transporter/payment.html',
        controller : 'PaymentTransporterController'
    }).when('/operatorTransporter', {
        templateUrl : 'app/views/transporter/operator.html',
        controller : 'OperatorTransporterController'
    }).when('/operatorTransporterEdit', {
        templateUrl : 'app/views/transporter/operatorEdit.html',
        controller : 'OperatorTransporterEditController'
    }).when('/topupTransporter', {
        templateUrl : 'app/views/transporter/topup.html',
        controller : 'PaymentFinanceController'

    }).when('/showTrip', {
        templateUrl : 'app/views/portoperator/trip.html',
        controller : 'ShowTripByPortOperatorController'
    }).when('/missions', {
        templateUrl : 'app/views/portoperator/mission.html',
        controller : 'CreateMissionController'
    }).when('/portWhitelist', {
        templateUrl : 'app/views/portoperator/portWhitelist.html',
        controller : 'PortWhitelistController'
    }).when('/reportIssue', {
        templateUrl : 'app/views/portoperator/reportIssue.html',
        controller : 'ReportIssueController'

    }).when('/parkingEntry', {
        templateUrl : 'app/views/parkingoperator/parkingEntry.html',
        controller : 'ParkingEntryController'
    }).when('/parkingExit', {
        templateUrl : 'app/views/parkingoperator/parkingExit.html',
        controller : 'ParkingExitController'
    }).when('/parkingAuthorizedExit', {
        templateUrl : 'app/views/parkingoperator/parkingAuthorizedExit.html',
        controller : 'ParkingAuthorizedExitController'

    }).when('/payment', {
        templateUrl : 'app/views/parkingkioskoperator/payment.html',
        controller : 'PaymentController'
    }).when('/exitOnly', {
        templateUrl : 'app/views/parkingkioskoperator/exitOnly.html',
        controller : 'ParkingExitOnlyController'

    }).when('/portEntry', {
        templateUrl : 'app/views/portentryoperator/portEntry.html',
        controller : 'PortEntryController'

    }).when('/portExit', {
        templateUrl : 'app/views/portexitoperator/portExit.html',
        controller : 'PortExitController'

    }).when('/parkingReleaseView', {
        templateUrl : 'app/views/parkingReleaseView.html',
        controller : 'ParkingReleaseController'

    }).when('/bookingLimits', {
        templateUrl : 'app/views/bookingLimit.html',
        controller : 'BookingLimitController'

    }).when('/parking', {
        templateUrl : 'app/views/parkingofficeoperator/parking.html',
        controller : 'ParkingController'
    }).when('/parkingPort', {
        templateUrl : 'app/views/parkingofficeoperator/port.html',
        controller : 'ParkingPortController'
    }).when('/accountsActivate', {
        templateUrl : 'app/views/parkingofficeoperator/account.html',
        controller : 'AccountsActivateController'
    }).when('/trip', {
        templateUrl : 'app/views/parkingofficeoperator/trip.html',
        controller : 'ApproveTripByOfficeOperatorController'
    }).when('/createMissions', {
        templateUrl : 'app/views/parkingofficeoperator/mission.html',
        controller : 'CreateMissionController'

    }).when('/operator', {
        templateUrl : 'app/views/adm/operator.html',
        controller : 'OperatorController'
    }).when('/operatorEdit', {
        templateUrl : 'app/views/adm/operatorEdit.html',
        controller : 'OperatorEditController'
    }).when('/trips', {
        templateUrl : 'app/views/trip.html',
        controller : 'TripController'
    }).when('/anpr', {
        templateUrl : 'app/views/adm/anprParameter.html',
        controller : 'AnprController'
    }).when('/vehicleCounter', {
        templateUrl : 'app/views/adm/vehicleCounter.html',
        controller : 'VehicleCounterController'
    }).when('/lane', {
        templateUrl : 'app/views/adm/lane.html',
        controller : 'LaneController'

    }).when('/accounts', {
        templateUrl : 'app/views/financeoperator/account.html',
        controller : 'AccountsController'
    }).when('/invoiceFinance', {
        templateUrl : 'app/views/financeoperator/invoice.html',
        controller : 'InvoiceController'
    }).when('/paymentFinance', {
        templateUrl : 'app/views/financeoperator/payment.html',
        controller : 'PaymentFinanceController'
    }).when('/kioskSession', {
        templateUrl : 'app/views/financeoperator/kioskSession.html',
        controller : 'KioskSessionController'
    });

    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

    $httpProvider.interceptors.push([ '$q', '$window', function($q, $window) {
        return {
            'responseError' : function(rejection) {
                var status = rejection.status;

                if (status == 401 || status == 403 || status == 405) {
                    $window.location.href = "/pad/login.htm?tp&denied";
                }

                return $q.reject(rejection);
            }
        };
    } ]);
} ]);

padApp.config([ 'ngIntlTelInputProvider', function(ngIntlTelInputProvider) {
    ngIntlTelInputProvider.set({
        utilsScript : "lib/intl-tel-input-12.0.3/js/utils.js",
        initialCountry : 'sn',
        onlyCountries : [ 'sn', 'ml' ],
        preferredCountries : [],
        autoPlaceholder : "off",
        // separateDialCode : true,
        nationalMode : false
    });
} ]);

padApp.config([ 'ChartJsProvider', function(ChartJsProvider) {
    ChartJsProvider.setOptions({
        colors : [ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360' ]
    });
} ]);

padApp.run([ '$rootScope', '$location', 'sessionService', function($rootScope, $location, sessionService) {

    $rootScope.go = function(path) {
        $location.path(path);

        $('#navbar').collapse('hide');
    };

    $rootScope.$on('$routeChangeStart', function(event, next, current) {
        if (!current) {
            $rootScope.go('/');
        }
    });

    $rootScope.showKioskSessionEndConfirmationDialog = function() {

        $('#kioskSessionEndConfirmationModal').modal({
            backdrop : 'static',
            keyboard : false,
            show : true
        });
    };

    $rootScope.closeKioskSessionEndConfirmationDialog = function() {

        $('#kioskSessionEndConfirmationModal').modal('hide');
    };

    $rootScope.endKioskSession = function() {

        var sessionData = {
            code : $rootScope.kioskSessionCode
        };

        sessionService.endSession(sessionData, function(response) {

            $rootScope.isKioskSessionEndRequested = true;
            $rootScope.hideNavbarMenuOptions('#navbarKioskPayment');
            $rootScope.hideNavbarMenuOptions('#navbarExitOnly');
            $rootScope.closeKioskSessionEndConfirmationDialog();
            $rootScope.go('/');

        }, function(error) {

            console.log(error);
        });
    };

} ]);

padApp.controller('PADController', [
    '$scope',
    '$rootScope',
    '$window',
    '$timeout',
    '$location',
    '$filter',
    'translationService',
    'usSpinnerService',
    'systemService',
    'selectorOptionsService',
    'validationConstants',
    'tripConstants',
    'missionConstants',
    'accountStatusConstants',
    'accountTypeConstants',
    'accountPaymentTermsTypeConstants',
    'operatorRoleConstants',
    'portOperatorConstants',
    'kioskSessionConstants',
    'kioskSessionTypeConstants',
    'kioskSessionLaneNumberConstants',
    'transactionTypeConstants',
    'driverAssociationStatusConstants',
    'portAccessStatusConstants',
    'reportTypeConstants',
    'parkingExitMsgIdConstants',
    'paymentOptionConstants',
    'paymentTypeConstants',
    'parkingStatusConstants',
    'parkingTypeConstants',
    'vehicleParkingStateConstants',
    'vehicleCounterTypeConstants',
    'invoicesTypeConstants',
    'languageConstants',
    function($scope, $rootScope, $window, $timeout, $location, $filter, translationService, usSpinnerService, systemService, selectorOptionsService, validationConstants, tripConstants, missionConstants,
        accountStatusConstants, accountTypeConstants, accountPaymentTermsTypeConstants, operatorRoleConstants, portOperatorConstants, kioskSessionConstants, kioskSessionTypeConstants,
        kioskSessionLaneNumberConstants, transactionTypeConstants, driverAssociationStatusConstants, portAccessStatusConstants, reportTypeConstants, parkingExitMsgIdConstants, paymentOptionConstants, paymentTypeConstants,
        parkingStatusConstants, parkingTypeConstants, vehicleParkingStateConstants, vehicleCounterTypeConstants, invoicesTypeConstants, languageConstants) {

        $rootScope.isLoading = false;
        $rootScope.nameUser = "";
        $rootScope.validationConstants = validationConstants;
        $rootScope.tripConstants = tripConstants;
        $rootScope.missionConstants = missionConstants;
        $rootScope.accountStatusConstants = accountStatusConstants;
        $rootScope.accountTypeConstants = accountTypeConstants;
        $rootScope.accountPaymentTermsTypeConstants = accountPaymentTermsTypeConstants;
        $rootScope.operatorRoleConstants = operatorRoleConstants;
        $rootScope.portOperatorConstants = portOperatorConstants;
        $rootScope.kioskSessionConstants = kioskSessionConstants;
        $rootScope.kioskSessionTypeConstants = kioskSessionTypeConstants;
        $rootScope.kioskSessionLaneNumberConstants = kioskSessionLaneNumberConstants;
        $rootScope.transactionTypeConstants = transactionTypeConstants;
        $rootScope.driverAssociationStatusConstants = driverAssociationStatusConstants;
        $rootScope.portAccessStatusConstants = portAccessStatusConstants;
        $rootScope.reportTypeConstants = reportTypeConstants;
        $rootScope.parkingExitMsgIdConstants = parkingExitMsgIdConstants;
        $rootScope.paymentOptionConstants = paymentOptionConstants;
        $rootScope.paymentTypeConstants = paymentTypeConstants;
        $rootScope.parkingStatusConstants = parkingStatusConstants;
        $rootScope.parkingTypeConstants = parkingTypeConstants;
        $rootScope.vehicleParkingStateConstants = vehicleParkingStateConstants;
        $rootScope.vehicleCounterTypeConstants = vehicleCounterTypeConstants;
        $rootScope.invoicesTypeConstants = invoicesTypeConstants;
        $rootScope.languageConstants = languageConstants;
        $rootScope.portOperatorList = [];
        $rootScope.portOperatorGatesList = [];
        $rootScope.portOperatorsForAddingStaff = [];
        $rootScope.portOperatorTransactionTypesMap = {};

        $scope.init = function(visitorFirstName, operatorCode, isTransporterOperator, isPortOperator, portOperatorId,
            independentPortOperatorId, independentPortOperatorCode, isKioskSessionAllowed, kioskSessionType, kioskSessionCode,
            kioskSessionLaneNumber, kioskSessionLaneId, kioskSessionAmountCollected, isParkingOperator, isParkingKioskOperator, isParkingOfficeOperator, isPortAuthorityOperator, isPortEntryOperator,
            isPortExitOperator, isParkingSupervisorOperator, isFinanceOperator, isAdmin, isTestEnvironment, taxPercentage, kioskFeeMinAmount, kioskFeeMaxAmount,
            financeFeeMinAmount, financeFeeMaxAmount, financeInitialFloatMinAmount, financeInitialFloatMaxAmount, maximumOverdraftLimitMinAmount, maximumOverdraftLimitMaxAmount,
            transporterAccountNumber, transporterAccountStatus, transporterAccountType, transporterAccountPaymentTermsType, isBookingLimitCheckEnabled, isPortEntryFiltering, language, tripsPendingApprovalCount,
            entryLaneVideoFeedUrl, autoReleaseExitCapacityPercentage, dropOffEmptyNightMissionStartTime, dropOffEmptyNightMissionEndTime, dropOffEmptyTriangleMissionStartTime, dropOffEmptyTriangleMissionEndTime,
            pendingTripsTransporterCount) {

            $rootScope.visitorFirstName = visitorFirstName;
            $rootScope.operatorCode = operatorCode;
            $rootScope.isTransporterOperator = isTransporterOperator;
            $rootScope.isPortOperator = isPortOperator;
            $rootScope.portOperatorId = parseInt(portOperatorId);
            $rootScope.independentPortOperatorId = parseInt(independentPortOperatorId);
            $rootScope.independentPortOperatorCode = independentPortOperatorCode;
            $rootScope.isKioskSessionAllowed = isKioskSessionAllowed;
            $rootScope.kioskSessionType = parseInt(kioskSessionType);
            $rootScope.kioskSessionCode = kioskSessionCode;
            $rootScope.kioskSessionLaneNumber = kioskSessionLaneNumber;
            $rootScope.kioskSessionLaneId  = kioskSessionLaneId;
            $rootScope.kioskSessionAmountCollected = kioskSessionAmountCollected;
            $rootScope.isParkingOperator = isParkingOperator;
            $rootScope.isParkingKioskOperator = isParkingKioskOperator;
            $rootScope.isParkingOfficeOperator = isParkingOfficeOperator;
            $rootScope.isPortAuthorityOperator = isPortAuthorityOperator;
            $rootScope.isPortEntryOperator = isPortEntryOperator;
            $rootScope.isParkingSupervisorOperator = isParkingSupervisorOperator;
            $rootScope.isPortExitOperator = isPortExitOperator;
            $rootScope.isFinanceOperator = isFinanceOperator;
            $rootScope.isAdmin = isAdmin;
            $rootScope.isTestEnvironment = isTestEnvironment;
            $rootScope.taxPercentage = taxPercentage;
            $rootScope.kioskFeeMinAmount = kioskFeeMinAmount;
            $rootScope.kioskFeeMaxAmount = kioskFeeMaxAmount;
            $rootScope.financeFeeMinAmount = financeFeeMinAmount;
            $rootScope.financeFeeMaxAmount = financeFeeMaxAmount;
            $rootScope.financeInitialFloatMinAmount = financeInitialFloatMinAmount;
            $rootScope.financeInitialFloatMaxAmount = financeInitialFloatMaxAmount;
            $rootScope.maximumOverdraftLimitMinAmount = maximumOverdraftLimitMinAmount;
            $rootScope.maximumOverdraftLimitMaxAmount = maximumOverdraftLimitMaxAmount;
            $rootScope.transporterAccountNumber = transporterAccountNumber;
            $rootScope.transporterAccountStatus = transporterAccountStatus;
            $rootScope.transporterAccountType = transporterAccountType;
            $rootScope.transporterAccountPaymentTermsType = transporterAccountPaymentTermsType;
            $rootScope.isBookingLimitCheckEnabled = isBookingLimitCheckEnabled;
            $rootScope.isPortEntryFiltering = isPortEntryFiltering;
            $rootScope.language = language;
            $rootScope.tripsPendingApprovalCount = tripsPendingApprovalCount;
            $rootScope.entryLaneVideoFeedUrl = entryLaneVideoFeedUrl;
            $rootScope.autoReleaseExitCapacityPercentage = autoReleaseExitCapacityPercentage;
            $rootScope.dropOffEmptyNightMissionStartTime = dropOffEmptyNightMissionStartTime;
            $rootScope.dropOffEmptyNightMissionEndTime = dropOffEmptyNightMissionEndTime;
            $rootScope.dropOffEmptyTriangleMissionStartTime = dropOffEmptyTriangleMissionStartTime;
            $rootScope.dropOffEmptyTriangleMissionEndTime = dropOffEmptyTriangleMissionEndTime;
            $rootScope.pendingTripsTransporterCount = pendingTripsTransporterCount;

            translationService.getLanguages($rootScope.language);
        }

        $rootScope.startSpinner = function() {
            $rootScope.isLoading = true;
            usSpinnerService.spin("pad-spinner");
        }

        $rootScope.stopSpinner = function() {
            $rootScope.isLoading = false;
            usSpinnerService.stop("pad-spinner");
        }

        $rootScope.getSymbolByCurrencyCode = function(currencyCode) {
            if (currencyCode === "XOF") {
                return 'CFA';
            } else {
                return 'CFA'; // for now return this currency as default
            }
        };

        $rootScope.getPaymentOption = function(option) {

            if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + '') {
                return $rootScope.translation.KEY_SCREEN_CASH_LABEL;
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY + '') {
                return 'Orange Money';
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_WARI || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_WARI + '') {
                return 'Wari';
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY + '') {
                return 'Free Money';
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_E_MONEY || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_E_MONEY + '') {
                return 'E-Money';
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ECOBANK || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ECOBANK + '') {
                return 'Ecobank';
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER + '') {
                return $rootScope.translation.KEY_SCREEN_BANK_TRANSFER_LABEL;
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE + '') {
                return $rootScope.translation.KEY_SCREEN_CHEQUE_LABEL;
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT + '') {
                return $rootScope.translation.KEY_SCREEN_ACCOUNT_CREDIT_LABEL;
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND + '') {
                return $rootScope.translation.KEY_SCREEN_CASH_REFUND_LABEL;
            } else if (option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT || option === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT + '') {
                return $rootScope.translation.KEY_SCREEN_ACCOUNT_DEBIT_LABEL;
            } else {
                return '';
            }
        };

        $rootScope.getCountryNameByISOCode = function(isoCode) {
            if (isoCode === "SN") {
                return $rootScope.translation.KEY_SCREEN_SENEGAL_LABEL;
            } else if (isoCode === "ML") {
                return 'Mali';
            } else if (isoCode === "GM") {
                return $rootScope.translation.KEY_SCREEN_GAMBIA_LABEL;
            } else if (isoCode === "GN") {
                return $rootScope.translation.KEY_SCREEN_GUINEA_LABEL;
            } else if (isoCode === "GW") {
                return $rootScope.translation.KEY_SCREEN_GUINEA_BISSAU_LABEL;
            } else if (isoCode === "MR") {
                return $rootScope.translation.KEY_SCREEN_MAURITANIA_LABEL;
            }
        };

        $rootScope.getLanguageName = function(languageId) {
            if (languageId === "1" || languageId === 1) {
                return $rootScope.translation.KEY_SCREEN_ENGLISH_LABEL;
            } else if (languageId === "2" || languageId === 2) {
                return $rootScope.translation.KEY_SCREEN_FRENCH_LABEL;
            } else if (languageId === "3" || languageId === 3) {
                return $rootScope.translation.KEY_SCREEN_WOLOF_LABEL;
            } else if (languageId === "4" || languageId === 4) {
                return $rootScope.translation.KEY_SCREEN_BAMBARA_LABEL;
            }
        };

        $rootScope.getOperatorName = function(operatorId) {
            if ($rootScope.portOperatorTransactionTypesMap[operatorId] !== undefined) {
                return $rootScope.portOperatorTransactionTypesMap[operatorId][0].portOperatorName;
            } else {
                return "";
            }
        };

        $rootScope.getOperatorNameShort = function(operatorId) {
            if ($rootScope.portOperatorTransactionTypesMap[operatorId] !== undefined) {
                return $rootScope.portOperatorTransactionTypesMap[operatorId][0].portOperatorNameShort;
            } else {
                return "";
            }
        };

        $rootScope.getIndependentOperators = function(portOperatorId) {
                if ($rootScope.portOperatorList !== null && $rootScope.portOperatorList !== undefined) {
                    for (var i=0; i<$rootScope.portOperatorList.length; i++) {
                        if (portOperatorId === $rootScope.portOperatorList[i].id) {
                            return $rootScope.portOperatorList[i].independentPortOperatorJsonList !== undefined ? $rootScope.portOperatorList[i].independentPortOperatorJsonList : [];
                        }
                    }
                }
        };

        $rootScope.getIndependentOperatorsTM = function(portOperatorId) {
                if ($rootScope.portOperatorsForAddingStaff !== null && $rootScope.portOperatorsForAddingStaff !== undefined) {
                    for (var i=0; i<$rootScope.portOperatorsForAddingStaff.length; i++) {
                        if (portOperatorId === $rootScope.portOperatorsForAddingStaff[i].id) {
                            return $rootScope.portOperatorsForAddingStaff[i].independentPortOperatorJsonList !== undefined ? $rootScope.portOperatorsForAddingStaff[i].independentPortOperatorJsonList : [];
                        }
                    }
                }
        };

        $rootScope.getIndependentOperator = function(portOperatorId, independentPortOperatorCode) {
                if ($rootScope.portOperatorsForAddingStaff !== null && $rootScope.portOperatorsForAddingStaff !== undefined) {
                    for (var i=0; i<$rootScope.portOperatorsForAddingStaff.length; i++) {
                        if (portOperatorId === $rootScope.portOperatorsForAddingStaff[i].id) {
                            return $rootScope.portOperatorsForAddingStaff[i].independentPortOperatorJsonList.find(function (item) {
                                return item.value === independentPortOperatorCode;
                            })
                        }
                    }
                }
            return [];
        };

        $rootScope.getIndependentOperatorName = function(portOperatorId, independentOperatorCode, nameType) {
            // nameType 1 = short name
            // nameType 2 = long name
            $scope.independentOperator = null;

            if (portOperatorId === undefined) {
                return '';
            }

            if (independentOperatorCode.length > 0) {
                if ($rootScope.portOperatorList !== null && $rootScope.portOperatorList !== undefined) {
                    for (var i=0; i<$rootScope.portOperatorList.length; i++) {
                        if (portOperatorId === $rootScope.portOperatorList[i].id && $rootScope.portOperatorList[i].independentPortOperatorJsonList !== undefined) {
                            $scope.independentOperator = $filter('filter')($rootScope.portOperatorList[i].independentPortOperatorJsonList, {value: independentOperatorCode})[0];
                            return ($scope.independentOperator === undefined || $scope.independentOperator === null) ? "" : (nameType === 1 ? $scope.independentOperator.nameShort : $scope.independentOperator.label);
                        }
                    }
                }
            }
            return "";
        };


        $rootScope.getAccountTypeText = function(accountType) {
            if (accountType === "1" || accountType === 1) {
                return $rootScope.translation.KEY_SCREEN_COMPANY_LABEL;
            } else if (accountType === "2" || accountType === 2) {
                return $rootScope.translation.KEY_SCREEN_INDIVIDUAL_LABEL;
            }
        };

        $rootScope.getParkingStatusText = function(status) {
            if (status === "1" || status === 1) {
                return $rootScope.translation.KEY_SCREEN_ENTRY_LABEL;
            } else if (status === "2" || status === 2) {
                return $rootScope.translation.KEY_SCREEN_EXIT_LABEL;
            } else if (status === "3" || status === 3) {
                return $rootScope.translation.KEY_SCREEN_EXIT_DUE_LABEL;
            } else if (status === "4" || status === 4) {
                return $rootScope.translation.KEY_SCREEN_ENTERED_PORT_LABEL;
            } else if (status === "5" || status === 5) {
                return $rootScope.translation.KEY_SCREEN_IN_TRANSIT_EXPIRED_LABEL;
            } else if (status === "6" || status === 6) {
                return $rootScope.translation.KEY_SCREEN_EXITED_PREMATURELY_EXPIRED_LABEL;
            }
        };

        $rootScope.getPortAccessStatusText = function(status) {
            if (status === "1" || status === 1) {
                return $rootScope.translation.KEY_SCREEN_ENTRY_LABEL;
            } else if (status === "2" || status === 2) {
                return $rootScope.translation.KEY_SCREEN_DENY_LABEL;
            } else if (status === "3" || status === 3) {
                return $rootScope.translation.KEY_SCREEN_EXIT_LABEL;
            }
        };

        $rootScope.getTransactionTypeName = function(portOperatorId, transactionType) {

            if ($rootScope.portOperatorTransactionTypesMap !== undefined && portOperatorId !== undefined && transactionType !== undefined) {

                var portOperatorId = parseInt(portOperatorId);
                $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];

                if ($scope.portOperatorTransactionTypes !== undefined) {
                    for (var i=0; i<$scope.portOperatorTransactionTypes.length; i++) {
                        if ($scope.portOperatorTransactionTypes[i].transactionType === parseInt(transactionType)) {
                            return $rootScope.$eval('translation.' + $scope.portOperatorTransactionTypes[i].translateKey);
                        }
                    }
                }
                return "";
            } else {
                return "";
            }
        };

        $rootScope.getMonthName = function(monthId) {
            switch (monthId) {
                case "1":
                    return $rootScope.translation.KEY_SCREEN_JANUARY_LABEL;
                case "2":
                    return $rootScope.translation.KEY_SCREEN_FEBRUARY_LABEL;
                case "3":
                    return $rootScope.translation.KEY_SCREEN_MARCH_LABEL;
                case "4":
                    return $rootScope.translation.KEY_SCREEN_APRIL_LABEL;
                case "5":
                    return $rootScope.translation.KEY_SCREEN_MAY_LABEL;
                case "6":
                    return $rootScope.translation.KEY_SCREEN_JUNE_LABEL;
                case "7":
                    return $rootScope.translation.KEY_SCREEN_JULY_LABEL;
                case "8":
                    return $rootScope.translation.KEY_SCREEN_AUGUST_LABEL;
                case "9":
                    return $rootScope.translation.KEY_SCREEN_SEPTEMBER_LABEL;
                case "10":
                    return $rootScope.translation.KEY_SCREEN_OCTOBER_LABEL;
                case "11":
                    return $rootScope.translation.KEY_SCREEN_NOVEMBER_LABEL;
                case "12":
                    return $rootScope.translation.KEY_SCREEN_DECEMBER_LABEL;
            }
        };

        $rootScope.getTypePaymentText = function(typePayment) {
            if (typePayment === "1" || typePayment === 1) {
                return $rootScope.translation.KEY_SCREEN_BANK_TRANSFER_LABEL;
            } else if (typePayment === "2" || typePayment === 2) {
                return $rootScope.translation.KEY_SCREEN_CHEQUE_LABEL;
            } else if (typePayment === "3" || typePayment === 3) {
                return $rootScope.translation.KEY_SCREEN_CASH_LABEL;
            } else {
                return "";
            }
        };

        $rootScope.getVehicleStateText = function(state) {
            if (state === "1" || state === 1) {
                return $rootScope.translation.KEY_SCREEN_VEHICLE_STATE_NORMAL_LABEL;
            } else if (state === "2" || state === 2) {
                return $rootScope.translation.KEY_SCREEN_VEHICLE_STATE_BROKEN_DOWN_LABEL;
            } else if (state === "3" || state === 3) {
                return $rootScope.translation.KEY_SCREEN_VEHICLE_STATE_CLAMPED_LABEL;
            } else if (state === "4" || state === 4) {
                return $rootScope.translation.KEY_SCREEN_VEHICLE_STATE_UNRESPONSIVE_LABEL;
            } else {
                return "";
            }
        };

        $rootScope.getPaymentTypeText = function(paymentType) {

            if (paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE || paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE + '') {
                return $rootScope.translation.KEY_SCREEN_NO_ACCOUNT_ADHOC_TRIP_FEE_LABEL;
            } else if (paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_TOPUP || paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_TOPUP + '') {
                return $rootScope.translation.KEY_SCREEN_TOPUP_LABEL;
            } else if (paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_TRIP_FEE || paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_TRIP_FEE + '') {
                return $rootScope.translation.KEY_SCREEN_TRIP_FEE_LABEL;
            } else if (paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_DEBIT || paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_DEBIT + '') {
                return $rootScope.translation.KEY_SCREEN_ACCOUNT_DEBIT_LABEL;
            } else if (paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_CREDIT || paymentType === $rootScope.paymentTypeConstants.PAYMENT_TYPE_ACCOUNT_CREDIT + '') {
                return $rootScope.translation.KEY_SCREEN_ACCOUNT_CREDIT_LABEL;
            } else {
                return '';
            }
        };

        $rootScope.getChannelText = function (laneSessionId, roleId) {
            if (laneSessionId === -1) {
                if (roleId === 1) {
                    return 'TRANSPORTER';
                } else {
                    return 'OFFICE';
                }
            } else {
                return 'KIOSK';
            }
        }

        $scope.previewCompanyAddress = function(formData) {

            if (formData !== undefined && formData !== "" && formData != null) {
                var companyAddrPreview = formData.address1;
                companyAddrPreview += "<br>" + formData.address2;

                if (formData.address3 != undefined && formData.address3 != "") {
                    companyAddrPreview += "<br>" + formData.address3;
                }
                if (formData.address4 != undefined && formData.address4 != "") {
                    companyAddrPreview += "<br>" + formData.address4;
                }

                companyAddrPreview += "<br>" + formData.postCode;

                if (formData.registrationCountryISO == "SN") {

                    if ($rootScope.language === 'FR') {
                        companyAddrPreview += "<br>Sénégal";
                    } else {
                        companyAddrPreview += "<br>Senegal";
                    }

                } else if (formData.registrationCountryISO == "ML") {
                    companyAddrPreview += "<br>Mali";
                }

                return companyAddrPreview;
            } else {
                return "";
            }
        };

        var timer;
        $rootScope.showResultMessage = function(isSuccess, message) {
            $timeout.cancel(timer);

            var showTime = 4500;
            if (isSuccess) {
                $rootScope.showSuccess = message;
                timer = $timeout(function() {
                    $rootScope.showSuccess = "";
                }, showTime);
            } else {
                $rootScope.showError = message;
                timer = $timeout(function() {
                    $rootScope.showError = "";
                }, showTime);
            }
        };

        $rootScope.showResultCancel = function() {
            $timeout.cancel(timer);
            $rootScope.showSuccess = "";
            $rootScope.showError = "";
        };

        $rootScope.toDate = function(dateStr) {
            var parts = dateStr.split("/");
            return new Date(parts[2], parts[1] - 1, parts[0]);
        };

        $rootScope.validateAPIResponse = function(response) {
            if (response === null || response == undefined || response.dataList === null || response.dataList == undefined) {
                if ($rootScope.isTransporterOperator) {
                    $window.location.href = "/pad/login.htm?tp&denied";
                } else {
                    $window.location.href = "/pad/login.htm?op&denied";
                }
            }
        };

        $rootScope.activeNavbar = function(navbarId) {
            $('#navbar .navbar-nav').find('li.active').removeClass('active');
            $(navbarId).addClass('active');
        };

        $rootScope.hideNavbarMenuOptions = function(navbarId) {
            $(navbarId).removeClass('active');
            $(navbarId).addClass('hidden');
        };

        $rootScope.getTranslationStringByResponseCode = function(responseCode) {
            return $rootScope.$eval('translation.' + 'KEY_RESPONSE_' + responseCode);
        };

        $rootScope.getTranslationStringByKeyString = function(key) {
            return $rootScope.$eval('translation.' + key);
        };

        $rootScope.showAddMissionTripModal = false;

        $rootScope.changeLanguage = function(language) {
            $rootScope.language = language;
            $('#navbar').collapse('hide');
            switch (language) {
                case 'EN':
                    $rootScope.translation = $rootScope.languageList.en.translationKeys;
                    break;
                case 'FR':
                    $rootScope.translation = $rootScope.languageList.fr.translationKeys;
                    break;
                default:
                    $rootScope.translation = $rootScope.languageList.fr.translationKeys;
            }

            $rootScope.getSelectorOptions() ;
        };

        $rootScope.showAddMissionTripDialog = function() {

            if ($location.path() !== '/missionTrip') {

                // navigating from outside of mission trip page, show add mission trip popup automatically
                $rootScope.showAddMissionTripModal = true;
                $rootScope.go('/missionTrip');

            } else {
                // if already on mission trip tab, don't show add mission trip popup automatically
                $rootScope.showAddMissionTripModal = false;
            }
        };

        $scope.getPortOperators = function() {

            $rootScope.startSpinner();

            systemService.getPortOperators({}, function(response) {

                $rootScope.portOperatorList = response.dataList
                    .filter(function (portOperator) {
                        return portOperator.id !== $rootScope.portOperatorConstants.PORT_OPERATOR_TM 
                            && portOperator.isActive;
                    });
                $rootScope.portOperatorsForAddingStaff = response.dataList
                    .filter(function (portOperator) {
                        return portOperator.id !== $rootScope.portOperatorConstants.PORT_OPERATOR_TM_NORTH
                            && portOperator.id !== $rootScope.portOperatorConstants.PORT_OPERATOR_TM_SOUTH 
                            && portOperator.isActive;
                    });
                $rootScope.portOperatorTransactionTypesMap = response.dataMap;

                $rootScope.stopSpinner();

            }, function(error) {

                $rootScope.stopSpinner();
            });
        }

        $scope.getPortOperators();

        $scope.getPortOperatorGates = function () {

            $rootScope.startSpinner();

            systemService.getPortOperatorGates({}, function (response) {

                $rootScope.portOperatorGatesList = response.dataList;
                $rootScope.stopSpinner();

            }, function (error) {

                $rootScope.stopSpinner();

            })

        }

        $scope.getPortOperatorGates();

        $rootScope.showGenericConfirmationModal = function(text, callbackFunction, isWarn) {

            var timer;
            $scope.confirmationModalText = text;
            $scope.isWarning = isWarn;
            $scope.callbackFunction = function() {
                callbackFunction();
                $('#genericConfirmationModal').modal('hide');

                $timeout.cancel(timer);
                timer = $timeout(function() {
                    $scope.modalButtonsForm.$invalid = false;
                    $scope.modalButtonsForm.$valid = true;
                }, 5000);
            }
            $('#genericConfirmationModal').modal('show');
        };

        $rootScope.closeGenericConfirmationModal = function() {
            $('#genericConfirmationModal').modal('hide');

            $timeout.cancel(timer);
            timer = $timeout(function() {
                $scope.modalButtonsForm.$invalid = false;
                $scope.modalButtonsForm.$valid = true;
            }, 5000);
        };

        $rootScope.getGateIdFromSelectedPortOperatorAndTransactionType = function(portOperatorId, transactionType) {

            if ($rootScope.portOperatorTransactionTypesMap !== undefined && portOperatorId !== undefined && transactionType !== undefined) {

                var portOperatorId = parseInt(portOperatorId);
                $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];

                if ($scope.portOperatorTransactionTypes !== undefined) {
                    for (var i=0; i<$scope.portOperatorTransactionTypes.length; i++) {
                        if ($scope.portOperatorTransactionTypes[i].transactionType === parseInt(transactionType)) {
                            return $scope.portOperatorTransactionTypes[i].portOperatorGateId;
                        }
                    }
                }
                return -1;
            } else {
                return -1;
            }
        };

        $rootScope.getGateNumberFromSelectedPortOperatorAndTransactionType = function(portOperatorId, transactionType) {

            if ($rootScope.portOperatorTransactionTypesMap !== undefined && portOperatorId !== undefined && transactionType !== undefined) {

                var portOperatorId = parseInt(portOperatorId);
                $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];

                if ($scope.portOperatorTransactionTypes !== undefined) {
                    for (var i=0; i<$scope.portOperatorTransactionTypes.length; i++) {
                        if ($scope.portOperatorTransactionTypes[i].transactionType === parseInt(transactionType)) {
                            return $scope.portOperatorTransactionTypes[i].portOperatorGateName;
                        }
                    }
                }
                return "";
            } else {
                return "";
            }
        };

        $rootScope.getTransactionTypesForPortOperator = function(portOperatorId) {

            if ($rootScope.portOperatorTransactionTypesMap !== undefined && portOperatorId !== undefined) {

                var portOperatorId = parseInt(portOperatorId);
                $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];

                if ($scope.portOperatorTransactionTypes !== undefined) {
                    return $scope.portOperatorTransactionTypes;
                } else {
                    return [];
                }
            } else {
                return [];
            }
        };

    } ]);
