padApp
    .controller(
        'PaymentController',
        [
            '$scope',
            '$rootScope',
            '$interval',
            '$timeout',
            '$filter',
            'tripService',
            'systemService',
            'paymentService',
            'parkingService',
            'receiptService',
            'validationConstants',
            'transactionTypeConstants',
            '$sce',
            function($scope, $rootScope, $interval, $timeout, $filter, tripService, systemService, paymentService, parkingService, receiptService, validationConstants, transactionTypeConstants,
                $sce) {

                $rootScope.activeNavbar('#navbarKioskPayment');

                $rootScope.startSpinner();

                $scope.formData = {};
                $scope.formData.vehicleRegNumberInput = "";
                $scope.formData.operatorType = "";
                $scope.independentOperators = [];
                $scope.formData.independentOperatorName = "";
                $scope.formData.independentOperatorCode = "";
                $scope.searchTripErrorResponse = "";
                $scope.addHocTripSuccessResponse = "";
                $scope.topupButtonDisabled = false;
                $scope.paymentConfirmationButtonDisabled = false;
                $scope.missionFoundSuccessResponse = "";
                $scope.isMissionFound = false;
                $scope.searchVehicleRegSubmitButtonDisabled = false;
                $scope.searchTripSubmitButtonDisabled = false;
                $scope.proceedToPayment = false;
                $scope.proceedToParking = false;
                $scope.tripSearchByOperatorAndTransactionType = false;
                $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                $scope.showAdhocTripScreen = false;
                $scope.showOverviewScreen = false;
                $scope.searchTripResponseCode = -1;
                $scope.saveAdhocTripSubmitButtonDisabled = false;
                $scope.tripBalanceCheckSubmitButtonDisabled = false;
                $scope.saveAdhocTripErrorResponse = "";
                $scope.tripBalanceCheckErrorResponse = "";
                $scope.cancelAdHocTripErrorResponse = "";
                $scope.cancelAdHocTripSuccessResponse = "";
                $scope.proceedToPaymentOrParkingSuccessResponse = "";
                $scope.referenceNumbersList = [];
                $scope.makePaymentErrorResponse = "";
                $scope.makePaymentInfoResponse = "";
                $scope.makePaymentSuccessResponse = "";
                $scope.formData.accountDetails = {};
                $scope.formData.isAccountAvailable = false;
                $scope.formData.accountBalance = 0;
                $scope.formData.feeAmountDue = 0;
                $scope.formData.vehicleRegistrationCountryISO = "";
                $scope.tripCode = "";
                $scope.isProceedToParkingGranted = false;
                $scope.saveParkingEntrySuccessResponse = "";
                $scope.saveParkingEntryErrorResponse = "";
                $scope.cancelDirectToPortSuccessResponse = "";
                $scope.cancelDirectToPortErrorResponse = "";
                $scope.saveParkingEntrySubmitButtonDisabled = false;
                $scope.isVehicleEligibleForParkingWithoutPayment = false;
                $scope.isTripInTransit = false;
                $scope.isVehicleWhitelisted = false;
                $scope.isProcessFinished = false;
                $scope.portOperatorTransactionTypes = [];
                $scope.tripBookingTimeJsonList = [];

                $scope.isPaymentOptionSelected = false;
                $scope.onlinePaymentCode = "";
                $scope.onlinePaymentStatusCheckIntervalSec = 5;
                $scope.onlinePaymentStatusCheckLimitMinutes = 10;
                $scope.onlinePaymentStatusCheckCount = 0;
                $scope.isPaymentSuccessful = false;
                $scope.onlinePaymentRequestSubmitted = false;
                $scope.paymentReference = "";
                $scope.mobilePaymentOptionsStatusMap = null;
                $scope.isOnlinePaymentStatusCheckLimitReached = false;
                $scope.driverMsisdn = "";
                $scope.isPrintReceipt = false;
                $scope.paymentCode = "";
                $scope.isPrintReceiptButtonDisabled = false;
                $scope.isVehicleProcessedParkingEntryThroughANPR = false;
                $scope.printReceiptTimeout = undefined;
                var timer;

                $scope.errorHandler = function(error) {
                    $rootScope.stopSpinner();
                    console.log(error);
                };

                $scope.getVehicleRegistrationListFromTrips = function() {
                    tripService.getVehicleRegistrationListFromTrips({}, function(response) {
                        $("#vehicleRegNumberInput").autocomplete({
                            source : response.dataList,
                            select : function(event, ui) {
                                $scope.formData.vehicleRegNumberInput = ui.item.value;
                                $scope.searchTrip(1);
                            }
                        });
                    }, $scope.errorHandler);
                };

                $interval(function() {
                    $scope.getVehicleRegistrationListFromTrips();
                }, 60000);

                $scope.searchTrip = function(step) {

                    $scope.searchTripResponseCode = -1;

                    if (step == 1) {
                        // search by vehicle reg only
                        if ($scope.tripSearchForm.vehicleRegNumberInput.$invalid) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";
                            $scope.vehicleRegNumberDivClass = "has-error has-feedback";
                            $scope.vehicleRegNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";

                        } else if ($scope.formData.vehicleRegNumberInput === undefined || $scope.formData.vehicleRegNumberInput === null
                            || $scope.formData.vehicleRegNumberInput === "" || $scope.formData.vehicleRegNumberInput.length < validationConstants.REGNUMBER_VALIDATION_LENGTH_MIN
                            || $scope.formData.vehicleRegNumberInput.length > validationConstants.REGNUMBER_VALIDATION_LENGTH_MAX) {

                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";
                            $scope.vehicleRegNumberDivClass = "has-error has-feedback";
                            $scope.vehicleRegNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";

                        } else {
                            $scope.searchTripErrorResponse = "";
                            $scope.searchTripSubmitButtonDisabled = true;

                            urlParams = {
                                vehicleRegistration : $scope.formData.vehicleRegNumberInput
                            };

                            tripService.searchTrip(urlParams, function(response) {

                                $scope.setResponseParamsAndShowOverviewScreen(response.dataList[0]);
                                $scope.calcAmountDueForTrip($scope.tripCode);

                            }, function(error) {

                                $scope.searchTripResponseCode = error.data.responseCode;

                                switch ($scope.searchTripResponseCode) {
                                     // Vehicle entered park as exit only
                                    case 1152:
                                        $scope.vehicleRegNumberDivClass = "has-error has-feedback";
                                        $scope.vehicleRegNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";
                                        $scope.searchTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                                        $scope.searchTripSubmitButtonDisabled = true;
                                        $scope.isProcessFinished = true;

                                        break;
                                    // No valid trips associated with vehicle
                                    case 1144:
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = true;
                                        $scope.tripSearchByOperatorAndTransactionType = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                                        $scope.showAdhocTripScreen = false;
                                        $scope.showOverviewScreen = false;
                                        $scope.searchTripSubmitButtonDisabled = false;
                                        $scope.searchTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                                        break;
                                    // more than 1 trip found, so ask for port operator and transaction type number
                                    case 1145:
                                        $scope.tripSearchByOperatorAndTransactionType = true;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                                        $scope.showAdhocTripScreen = false;
                                        $scope.showOverviewScreen = false;
                                        $scope.searchTripSubmitButtonDisabled = false;

                                        $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_MULTIPLE_TRIPS_FOUND_MESSAGE + ".";

                                        break;
                                    // No mission associated with trip
                                    case 1146:
                                        $scope.showAdhocTripScreen = true;
                                        $scope.tripSearchByOperatorAndTransactionType = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                                        $scope.searchTripSubmitButtonDisabled = false;
                                        $scope.showOverviewScreen = false;

                                        break;
                                    // Vehicle is already parked
                                    case 1173:
                                        $scope.vehicleRegNumberDivClass = "has-error has-feedback";
                                        $scope.vehicleRegNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";
                                        $scope.searchTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode);
                                        $scope.searchTripSubmitButtonDisabled = true;
                                        $scope.isProcessFinished = true;
                                        $scope.isVehicleProcessedParkingEntryThroughANPR = error.data.dataList[0].isVehicleProcessedParkingEntryThroughANPR;

                                        break;
                                    // there exists active port whitelist for vehicle. kiosk operator should create "exit only" session for vehicle
                                    case 1194:
                                        $scope.isVehicleWhitelisted = true;
                                        $scope.isProcessFinished = true;
                                        break;

                                    default:
                                        break;
                                }

                                $scope.errorHandler(error);
                            });
                        }

                    } else if (step == 2) {

                        // search by port operator and transaction type
                        if ($scope.tripSearchForm.operatorType.$invalid) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.formData.operatorType === undefined || $scope.formData.operatorType === null || $scope.formData.operatorType === '') {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.independentOperators.length !== 0 &&
                            ($scope.formData.independentOperatorName === undefined || $scope.formData.independentOperatorName === null || $scope.formData.independentOperatorName === '')) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.independentOperators.length !== 0 &&
                            ($scope.formData.independentOperatorCode === undefined || $scope.formData.independentOperatorCode === null || $scope.formData.independentOperatorCode === '')) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.tripSearchForm.transactionType.$invalid) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TRANSACTION_TYPE_MESSAGE + ".";
                            $scope.transactionTypeDivClass = "has-error has-feedback";
                            $scope.transactionTypeSpanClass = "glyphicon glyphicon-remove form-control-feedback";

                        } else {
                            $scope.searchTripErrorResponse = "";
                            $scope.saveAdhocTripErrorResponse = "";
                            $scope.missionFoundSuccessResponse = "";
                            $scope.isMissionFound = false;
                            $scope.referenceNumberDivClass = "";
                            $scope.referenceNumberSpanClass = "";
                            $scope.searchTripSubmitButtonDisabled = true;

                            $scope.formData.accountName = "";
                            $scope.formData.companyName = "";
                            $scope.formData.currency = "";
                            $scope.formData.accountBalance = 0;

                            urlParams = {
                                vehicleRegistration : $scope.formData.vehicleRegNumberInput,
                                portOperatorId : $scope.formData.operatorType,
                                transactionType : $scope.formData.transactionType,
                                referenceNumber : $scope.formData.referenceNumber
                            };

                            tripService.searchTrip(urlParams, function(response) {

                                $scope.setResponseParamsAndShowOverviewScreen(response.dataList[0]);
                                $scope.calcAmountDueForTrip($scope.tripCode);

                            }, function(error) {

                                $scope.searchTripResponseCode = error.data.responseCode;
                                $scope.referenceNumbersList = error.data.dataList;
                                $scope.tripSearchByOperatorAndTransactionTypeAndReference = true;
                                $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_SEARCH_BY_BAD_NUMBER_MESSAGE + ".";
                                $scope.tripSearchByOperatorAndTransactionType = true;
                                $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                                $scope.searchTripSubmitButtonDisabled = false;

                                $scope.errorHandler(error);
                            });
                        }

                    } else if (step == 3) {
                        // search by port operator, transaction type and mission reference number
                        if ($scope.tripSearchForm.operatorType.$invalid) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.formData.operatorType === undefined || $scope.formData.operatorType === null || $scope.formData.operatorType === '') {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.independentOperators.length !== 0 &&
                            ($scope.formData.independentOperatorName === undefined || $scope.formData.independentOperatorName === null || $scope.formData.independentOperatorName === '')) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.independentOperators.length !== 0 &&
                            ($scope.formData.independentOperatorCode === undefined || $scope.formData.independentOperatorCode === null || $scope.formData.independentOperatorCode === '')) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

                        } else if ($scope.tripSearchForm.transactionType.$invalid) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TRANSACTION_TYPE_MESSAGE + ".";
                            $scope.transactionTypeDivClass = "has-error has-feedback";
                            $scope.transactionTypeSpanClass = "glyphicon glyphicon-remove form-control-feedback";

                        } else if (($scope.tripSearchByOperatorAndTransactionTypeAndReference || $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer)
                                && $scope.tripSearchForm.referenceNumber.$invalid) {
                            $scope.searchTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_BAD_NUMBER_MESSAGE + ".";
                            $scope.referenceNumberDivClass = "has-error has-feedback";
                            $scope.referenceNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";

                        } else {
                            $scope.searchTripErrorResponse = "";
                            $scope.saveAdhocTripErrorResponse = "";
                            $scope.missionFoundSuccessResponse = "";
                            $scope.isMissionFound = false;
                            $scope.referenceNumberDivClass = "";
                            $scope.referenceNumberSpanClass = "";
                            $scope.searchTripSubmitButtonDisabled = true;

                            $scope.formData.accountName = "";
                            $scope.formData.companyName = "";
                            $scope.formData.currency = "";
                            $scope.formData.accountBalance = 0;

                            urlParams = {
                                vehicleRegistration : $scope.formData.vehicleRegNumberInput,
                                portOperatorId : $scope.formData.operatorType,
                                transactionType : $scope.formData.transactionType,
                                referenceNumber : $scope.formData.referenceNumber
                            };

                            tripService.searchTrip(urlParams, function(response) {

                                $scope.setResponseParamsAndShowOverviewScreen(response.dataList[0]);
                                $scope.calcAmountDueForTrip($scope.tripCode);

                            }, function(error) {

                                $scope.searchTripResponseCode = error.data.responseCode;

                                switch ($scope.searchTripResponseCode) {
                                    // No mission record for reference number number and port operator
                                    case 1147:
                                        $scope.showAdhocTripScreen = true;
                                        $scope.tripSearchByOperatorAndTransactionType = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                                        $scope.showOverviewScreen = false;
                                        $scope.saveAdhocTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                                        $scope.isShowReceiptMessage = true;

                                        break;
                                    // Mission found and trip record needs to be created
                                    case 1148:
                                        $scope.showAdhocTripScreen = true;
                                        $scope.tripSearchByOperatorAndTransactionType = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                                        $scope.showOverviewScreen = false;
                                        $scope.missionFoundSuccessResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                                        $scope.isMissionFound = true;

                                        $scope.formData.dateMissionStartString = error.data.dataList[0].dateMissionStartString;
                                        $scope.formData.dateMissionEndString = error.data.dataList[0].dateMissionEndString;
                                        $scope.formData.accountName = error.data.dataList[0].accountName;
                                        $scope.formData.companyName = error.data.dataList[0].companyName;
                                        $scope.formData.currency = error.data.dataList[0].currency;
                                        $scope.formData.accountBalance = error.data.dataList[0].accountBalance;
                                        $scope.isShowReceiptMessage = error.data.dataList[0].isShowReceiptMessage;

                                        break;
                                    default:
                                        break;
                                }

                                $scope.searchTripSubmitButtonDisabled = false;
                                $scope.errorHandler(error);
                            });
                        }
                    }
                };

                $scope.saveAdhocTrip = function() {

                    $scope.missionFoundSuccessResponse = "";

                    if ($scope.formData.vehicleRegNumberInput === undefined || $scope.formData.operatorType === undefined || $scope.searchTripResponseCode == -1) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_INVALID_REQUEST_MESSAGE + ".";

                    } else if ($scope.independentOperators.length !== 0 &&
                        ($scope.formData.independentOperatorName === undefined || $scope.formData.independentOperatorName === null || $scope.formData.independentOperatorName === '')) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

                    } else if ($scope.independentOperators.length !== 0 &&
                        ($scope.formData.independentOperatorCode === undefined || $scope.formData.independentOperatorCode === null || $scope.formData.independentOperatorCode === '')) {

                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

                    } else if ($scope.formData.vehicleRegistrationCountryISO === undefined || $scope.formData.vehicleRegistrationCountryISO === '') {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_REGISTRATION_MESSAGE + ".";

                    } else if ($scope.saveAdhocTripForm.mobileNumber.$invalid) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";

                    } else if ($scope.formData.driverLanguageId === undefined || $scope.formData.driverLanguageId === -1) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_LANGUAGE_MESSAGE + ".";

                    } else if ($scope.saveAdhocTripForm.dateSlot.$invalid) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_SLOT_DATE_MESSAGE + ".";

                    } else if ($scope.saveAdhocTripForm.timeSlot.$invalid) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_SLOT_TIME_MESSAGE + ".";

                    } else if ($scope.formData.timeSlot === undefined || $scope.formData.timeSlot === '') {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_SLOT_TIME_MESSAGE + ".";

                    }   else if ($scope.saveAdhocTripForm.companyName.$invalid) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_COMPANY_NAME_MESSAGE + ".";

                    } else if ($scope.saveAdhocTripForm.transactionType.$invalid && $scope.searchTripResponseCode == 1147) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TRANSACTION_TYPE_MESSAGE + ".";

                    } else {
                        $scope.saveAdhocTripErrorResponse = "";
                        $scope.addHocTripSuccessResponse = "";
                        $scope.mobileNumberDivClass = "";
                        $scope.mobileNumberSpanClass = "";
                        $scope.driverLanguageIdDivClass = "";
                        $scope.driverLanguageIdSpanClass = "";
                        $scope.companyNameDivClass = "";
                        $scope.companyNameSpanClass = "";
                        $scope.saveAdhocTripSubmitButtonDisabled = true;

                        urlParams = {
                            vehicleRegistration : $scope.formData.vehicleRegNumberInput,
                            vehicleRegistrationCountryISO : $scope.formData.vehicleRegistrationCountryISO,
                            portOperatorId : $scope.formData.operatorType,
                            independentPortOperatorCode : $scope.formData.independentOperatorCode,
                            gateId : $rootScope.getGateIdFromSelectedPortOperatorAndTransactionType($scope.formData.operatorType, $scope.formData.transactionType),
                            transactionType : $scope.formData.transactionType,
                            referenceNumber : $scope.formData.referenceNumber,
                            dateSlotString : $scope.dateSlot,
                            timeSlotString : $scope.formData.timeSlot,
                            driverMobile : $scope.formData.mobileNumber,
                            driverLanguageId : $scope.formData.driverLanguageId,
                            companyName : $scope.formData.companyName,
                            getTripResponseCode : $scope.searchTripResponseCode
                        };
                        
                        $rootScope.startSpinner();

                        tripService.saveAdhocTrip(urlParams, function(response) {

                            $scope.formData.dateSlotString = response.dataList[0].dateSlotString;
                            $scope.formData.accountName = response.dataList[0].accountName;
                            $scope.formData.companyName = response.dataList[0].companyName;
                            $scope.formData.currency = response.dataList[0].currency;
                            $scope.formData.accountBalance = response.dataList[0].accountBalance;
                            $scope.formData.isAdHoc = response.dataList[0].adHoc;
                            $scope.formData.tripFeeAmount = response.dataList[0].tripFeeAmount;
                            $scope.formData.isVehicleActive = response.dataList[0].isVehicleActive;

                            $scope.tripCode = response.dataList[0].code;
                            $scope.driverMsisdn = $scope.formData.mobileNumber;

                            if (response.dataList[0].account != null) {

                                $scope.formData.isAccountAvailable = true;
                                $scope.formData.accountDetails = response.dataList[0].account;
                            }

                            $scope.showOverviewScreen = true;
                            $scope.tripSearchByOperatorAndTransactionType = false;
                            $scope.showAdhocTripScreen = false;
                            $scope.addHocTripSuccessResponse = $rootScope.translation.KEY_SCREEN_ADD_HOC_TRIP_CREATED_LABEL + ".";

                            // calculate amount due
                            $scope.calcAmountDueForTrip($scope.tripCode);
                            
                            $rootScope.stopSpinner();

                        }, function(error) {

                            $scope.saveAdhocTripSubmitButtonDisabled = false;
                            $scope.saveAdhocTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";

                            // check if slot date is outside the mission start end timeframe. If it is show additional info on error message.
                            if (error.data.responseCode === 1143) {
                                $scope.saveAdhocTripErrorResponse += " [" + $scope.formData.dateMissionStartString.substring(0, 10) + " - "
                                    + $scope.formData.dateMissionEndString.substring(0, 10) + "]";
                            }

                            $scope.errorHandler(error);
                        });
                    }
                };

                $scope.$watch('formData.amountFee', function() {

                    if ($scope.formData.amountFee > 0 && ($scope.formData.amountCashGiven - $scope.formData.amountFee) >= 0) {
                        $scope.formData.amountChangeDue = $scope.formData.amountCashGiven - $scope.formData.amountFee;
                    } else {
                        $scope.formData.amountChangeDue = 0;
                    }
                });

                $scope.$watch('formData.amountCashGiven', function() {

                    if ($scope.formData.amountFee > 0 && ($scope.formData.amountCashGiven - $scope.formData.amountFee) >= 0) {
                        $scope.formData.amountChangeDue = $scope.formData.amountCashGiven - $scope.formData.amountFee;
                    } else {
                        $scope.formData.amountChangeDue = 0;
                    }
                });

                $scope.calcAmountDueForTrip = function(tripCode) {

                    $scope.tripCode = tripCode;

                    urlParams = {
                        code : tripCode
                    };

                    tripService.calcAmountDueForTrip(urlParams, function(response) {

                        $scope.formData.feeAmountDue = response.data.tripFeeAmount;
                        $scope.taxAmount = response.data.tripTaxAmount;
                        $scope.dateTimeString = $filter('date')(new Date(), "dd/MM/yyyy HH:mm");

                        if ($scope.formData.isAdHoc) {
                            $scope.formData.amountFee = $scope.formData.feeAmountDue;
                        }

                        if ($scope.formData.isAccountAvailable) {
                            $scope.formData.emailReceipt = '1';
                        } else {
                            $scope.formData.emailReceipt = '2';
                        }

                        $scope.mobilePaymentOptionsStatusMap = response.dataMap;

                    }, function(error) {

                        $scope.errorHandler(error);
                    });
                };

                $scope.closePaymentConfirmationDialog = function() {

                    $scope.makePaymentErrorResponse = "";
                    $scope.makePaymentInfoResponse = "";
                    $scope.onlinePaymentCode = "";
                    $scope.paymentReference = "";
                    $scope.isPaymentSuccessful = false;
                    $scope.onlinePaymentRequestSubmitted = false;
                    $scope.topupButtonDisabled = false;
                    $scope.paymentConfirmationButtonDisabled = false;
                    $scope.tripPaymentForm.$setPristine();

                    $scope.formData.paymentOption = null;
                    $scope.formData.firstName = "";
                    $scope.formData.lastName = "";
                    $scope.formData.driverMsisdn = "";
                    $scope.formData.amountTopup = "";
                    $scope.formData.amountCashGiven = "";
                    $scope.formData.amountFee = "";
                    $scope.onlinePaymentStatusCheckCount = 0;
                    $scope.isOnlinePaymentStatusCheckLimitReached = false;

                    $('#paymentConfirmationModal').modal('hide');

                    if (timer !== undefined && timer !== null) {

                        $timeout.cancel(timer);
                        timer = null;
                    }
                };

                $scope.showPaymentConfirmationDialog = function() {

                    if ($scope.tripPaymentForm.paymentOption.$invalid) {
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PAYMENT_OPTION_MESSAGE + ".";
                        return;

                    } else if ($scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + ''
                        && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY + ''
                        && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY + '') {

                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PAYMENT_OPTION_MESSAGE + ".";
                        return;
                    } else if ($scope.formData.firstName === undefined || $scope.formData.firstName === null || $scope.formData.firstName === '') {
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";
                        return;

                    } else if ($scope.formData.lastName === undefined || $scope.formData.lastName === null || $scope.formData.lastName === '') {
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";
                        return;

                    } else if ($scope.tripPaymentForm.driverMsisdn.$invalid) {
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";
                        return;

                    }

                    if ($scope.formData.amountFee > parseInt($rootScope.kioskFeeMaxAmount, 10)) {
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_MAX_PAYMENT_AMOUNT_MESSAGE + $filter('customCurrency')(parseInt($rootScope.kioskFeeMaxAmount, 10), 0) + ".";
                        return;
                    } else if ($scope.formData.amountFee < parseInt($rootScope.kioskFeeMinAmount, 10)) {
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_MIN_PAYMENT_AMOUNT_MESSAGE + $filter('customCurrency')(parseInt($rootScope.kioskFeeMinAmount, 10), 0) + ".";
                        return;
                    }

                    if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + '') {

                        if ($scope.tripPaymentForm.amountFee.$invalid) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FEE_AMOUNT_MESSAGE + ".";
                            return;

                        } else if ($scope.formData.amountFee < $scope.formData.feeAmountDue) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_INVALID_FEE_AMOUNT_MESSAGE + ".";
                            return;

                        } else if ($scope.formData.amountFee <= 0) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_VALID_AMOUNT_MESSAGE + ".";
                            return;

                        } else if ($scope.tripPaymentForm.amountCashGiven.$invalid) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_CASH_GIVEN_MESSAGE + ".";
                            return;

                        } else if ($scope.formData.amountCashGiven < $scope.formData.amountFee) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_INVALID_CASH_GIVEN_AMOUNT_FOR_PAYMENT_MESSAGE + ".";
                            return;
                        }

                    } else {
                        if ($scope.tripPaymentForm.amountFee.$invalid) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FEE_AMOUNT_MESSAGE + ".";
                            return;

                        } else if ($scope.formData.amountFee < $scope.formData.feeAmountDue) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_INVALID_FEE_AMOUNT_MESSAGE + ".";
                            return;

                        } else if ($scope.formData.amountFee <= 0) {
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_VALID_AMOUNT_MESSAGE + ".";
                            return;
                        }
                    }

                    $scope.makePaymentErrorResponse = "";
                    $scope.makePaymentInfoResponse = "";
                    $scope.topupButtonDisabled = true;

                    $('#paymentConfirmationModal').modal('show');
                };

                $scope.makeTopup = function() {

                    $scope.makePaymentErrorResponse = "";
                    $scope.makePaymentInfoResponse = "";
                    $scope.paymentConfirmationButtonDisabled = true;
                    $scope.isOnlinePaymentStatusCheckLimitReached = false;

                    var urlParams = {};

                    if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + '') {
                        urlParams = {
                            tripCode : $scope.tripCode,
                            paymentOption : parseInt($scope.formData.paymentOption),
                            firstName : $scope.formData.firstName,
                            lastName : $scope.formData.lastName,
                            msisdn : $scope.formData.driverMsisdn,
                            paymentAmount : $scope.formData.amountCashGiven,
                            // if account is available then its a topup so use the amount selected by the user, otherwise use the calculated amount due (trip fee)
                            feeDueAmount :  ($scope.formData.isAccountAvailable ? $scope.formData.amountFee : $scope.formData.feeAmountDue),
                            changeDueAmount : $scope.formData.amountChangeDue
                        };

                    } else {
                        urlParams = {
                            tripCode : $scope.tripCode,
                            paymentOption : parseInt($scope.formData.paymentOption),
                            firstName : $scope.formData.firstName,
                            lastName : $scope.formData.lastName,
                            msisdn : $scope.formData.driverMsisdn,
                            paymentAmount : $scope.formData.amountFee
                        };
                    }

                    $rootScope.startSpinner();

                    paymentService.makeTopup(urlParams, function(response) {

                        $rootScope.kioskSessionAmountCollected = parseInt($rootScope.kioskSessionAmountCollected);

                        if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + '') {
                            // cash payment

                            if ($scope.formData.isAccountAvailable) {
                                $scope.formData.accountBalance = response.data.accountBalanceAmount;
                            }

                            $scope.paymentCode = response.data.code;
                            $scope.isPrintReceipt = true;

                            var paymentAmount = $scope.formData.amountCashGiven - $scope.formData.amountChangeDue;
                            $rootScope.kioskSessionAmountCollected += paymentAmount;

                            $scope.isProceedToParkingGranted = true;

                            if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING) {
                                $scope.allowProceedToParking();

                            } else {
                                $scope.saveParkingEntrySuccessResponse = $rootScope.translation.KEY_SCREEN_PORT_ENTRY_MSG_AUTH_ENTRY + ".";
                            }

                            $scope.calcAmountDueForTrip($scope.tripCode);
                            $('#paymentConfirmationModal').modal('hide');

                            $scope.paymentConfirmationButtonDisabled = false;

                        } else {
                            // mobile payment

                            $scope.onlinePaymentCode = response.data.onlinePaymentCode;

                            if ($scope.onlinePaymentCode !== "" && $scope.onlinePaymentCode.length == 64) {

                                timer = $timeout(function() {
                                    $scope.checkOnlinePaymentStatus();

                                }, $scope.onlinePaymentStatusCheckIntervalSec * 1000);

                                $scope.onlinePaymentRequestSubmitted = true;
                            }
                        }

                        $rootScope.stopSpinner();

                        $scope.makePaymentErrorResponse = "";
                        $scope.makePaymentInfoResponse = "";
                        //$scope.makePaymentSuccessResponse = $rootScope.translation.KEY_SCREEN_FEE_AMOUNT_PAID_MESSAGE + ".";

                    }, function(error) {

                        $rootScope.stopSpinner();
                        
                        $scope.onlinePaymentRequestSubmitted = false;
                        
                        if(error.data.responseCode === 1400){
                            $scope.topupButtonDisabled = true;
                            $scope.paymentConfirmationButtonDisabled = true;
                            $scope.makePaymentInfoResponse = $rootScope.translation.KEY_SCREEN_ONLINE_PAYMENT_SUBMITTED_MESSAGE;
                        }else{
                            $scope.topupButtonDisabled = false;
                            $scope.paymentConfirmationButtonDisabled = false;
                            $scope.makePaymentErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        }
                        

                        $('#paymentConfirmationModal').modal('hide');
                    });
                };

                $scope.checkOnlinePaymentStatus = function() {

                    paymentService.checkPaymentStatus({

                        onlinePaymentCode : $scope.onlinePaymentCode

                    }, function(response) {

                        $scope.onlinePaymentStatusCheckCount = 0;
                        $scope.onlinePaymentRequestSubmitted = false;
                        $scope.paymentReference = response.data.referenceAggregator;
                        $scope.isPrintReceipt = response.data.isPrintReceipt;
                        $scope.paymentCode = response.data.code;
                        $scope.isPaymentSuccessful = true;

                        if ($scope.formData.isAccountAvailable) {
                            $scope.newBalanceAmount = $scope.formData.accountBalance + $scope.formData.amountFee;
                        }

                        $scope.isProceedToParkingGranted = true;
                        $scope.allowProceedToParking();
                        $scope.calcAmountDueForTrip($scope.tripCode);
                        $scope.paymentConfirmationButtonDisabled = false;

                    }, function(error) {

                        if (error.data.responseCode === 1191) {
                            // payment failed
                            $scope.onlinePaymentStatusCheckCount = 0;
                            $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_PAYMENT_FAILED_LABEL;

                        } else {
                            // continue checking for payment status
                            $scope.onlinePaymentStatusCheckCount++;

                            if ($scope.onlinePaymentStatusCheckCount * $scope.onlinePaymentStatusCheckIntervalSec <= $scope.onlinePaymentStatusCheckLimitMinutes * 60) {

                                timer = $timeout(function() {
                                    $scope.checkOnlinePaymentStatus();

                                }, $scope.onlinePaymentStatusCheckIntervalSec * 1000);

                            } else {
                                $scope.onlinePaymentStatusCheckCount = 0;
                                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_PAYMENT_CONFIRMATION_PENDING + " "
                                    + $rootScope.getPaymentOption($scope.formData.paymentOption) + ".";
                                $scope.makePaymentErrorResponse += " " + $rootScope.translation.KEY_SCREEN_PAYMENT_CONFIRMATION_UPDATE + ".";
                                $scope.isOnlinePaymentStatusCheckLimitReached = true;
                            }
                        }
                    });
                };

                $scope.proceedWithoutPayment = function() {

                    $scope.makePaymentErrorResponse = "";
                    $scope.makePaymentInfoResponse = "";
                    $scope.isProceedToParkingGranted = true;
                    $scope.isProceedWithoutPayment = true;

                    if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING) {
                        $scope.allowProceedToParking();

                    } else if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_VIRTUAL) {
                        $scope.chargeTripFeeToTransporter();
                    }
                };

                // below is used when theres a vehicle on a trip in transit and it shows up at parking entry.
                // kiosk op will search and click on the proceed button which will create exit only session
                $scope.proceedWithoutPaymentExitOnly = function() {

                    $scope.makePaymentErrorResponse = "";
                    $scope.makePaymentInfoResponse = "";
                    $scope.isProceedToParkingGranted = true;
                    $scope.allowProceedToParkingWhenExitOnly();
                };

                $scope.chargeTripFeeToTransporter = function() {

                    urlParams = {
                        code : $scope.tripCode
                    };
                    
                    $rootScope.startSpinner();

                    tripService.chargeTripFeeToTransporter(urlParams, function(response) {

                        $scope.saveParkingEntrySuccessResponse = $rootScope.translation.KEY_SCREEN_PORT_ENTRY_MSG_AUTH_ENTRY + ".";
                        $scope.formData.accountBalance = $scope.formData.accountBalance - $scope.formData.tripFeeAmount;

                        $scope.getVehicleRegistrationListFromTrips();
                        
                        $rootScope.stopSpinner();

                    }, function(error) {

                        $scope.saveParkingEntryErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.isProceedWithoutPayment = false;
                        $scope.errorHandler(error);
                    });
                };

                $scope.allowProceedToParking = function() {

                    if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING) {

                        $scope.saveParkingEntryErrorResponse = "";
                        $scope.saveParkingEntrySubmitButtonDisabled = true;
                        $scope.isPaymentOptionSelected = false;

                        urlParams = {
                            tripCode : $scope.tripCode,
                            entryLaneId : $rootScope.kioskSessionLaneId,
                            driverLanguageId : $scope.driverLanguageId
                        };
                        
                        $rootScope.startSpinner();

                        parkingService.saveParkingEntry(urlParams, function(response) {

                            $scope.saveParkingEntrySuccessResponse = $rootScope.translation.KEY_SCREEN_PROCEED_TO_PARKING_LABEL + ".";
                            $scope.formData.accountBalance = $scope.formData.accountBalance - $scope.formData.tripFeeAmount;

                            $scope.getVehicleRegistrationListFromTrips();
                            
                            $rootScope.stopSpinner();

                        }, function(error) {

                            $scope.saveParkingEntryErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";

                            $scope.saveParkingEntrySubmitButtonDisabled = false;
                            $scope.isProceedWithoutPayment = false;

                            // if vehicle already entered the parking area, go back to search screen
                            if (error.data.responseCode == 1152) {
                                $scope.saveParkingEntrySubmitButtonDisabled = true;
                            }

                            $scope.errorHandler(error);
                        });
                    }
                };

                $scope.allowProceedToParkingWhenExitOnly = function() {

                    $scope.saveParkingEntryErrorResponse = "";
                    $scope.saveParkingEntrySubmitButtonDisabled = true;
                    $scope.isPaymentOptionSelected = false;

                    urlParams = {
                        tripCode : $scope.tripCode,
                        vehicleRegistration : $scope.formData.vehicleRegNumberInput,
                        driverMobile : $scope.formData.mobileNumber,
                        entryLaneId : $rootScope.kioskSessionLaneId
                    };
                    
                    $rootScope.startSpinner();

                    parkingService.saveParkingExitOnly(urlParams, function(response) {

                        $scope.saveParkingEntrySuccessResponse = $rootScope.translation.KEY_SCREEN_PROCEED_TO_PARKING_LABEL + ".";
                        
                        $rootScope.stopSpinner();

                    }, function(error) {

                        $scope.saveParkingEntryErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.saveParkingEntrySubmitButtonDisabled = false;

                        $scope.errorHandler(error);
                    });
                };

                $scope.clearTripSearchForm = function() {

                    if ($scope.tripSearchForm !== undefined) {
                        $scope.tripSearchForm.$setPristine();
                    }
                    if ($scope.saveAdhocTripForm !== undefined) {
                        $scope.saveAdhocTripForm.$setPristine();
                    }
                    if ($scope.tripPaymentForm !== undefined) {
                        $scope.tripPaymentForm.$setPristine();
                    }

                    $scope.formData.vehicleRegNumberInput = "";
                    $scope.formData.vehicleRegistrationCountryISO = "";
                    $scope.tripCode = "";
                    $scope.dateSlot = "";
                    $scope.formData.operatorType = "";
                    $scope.formData.independentOperatorName = "";
                    $scope.formData.independentOperatorCode = "";
                    $scope.formData.timeSlot = "";
                    $scope.formData.transactionType = "";
                    $scope.formData.referenceNumber = "";
                    $scope.formData.mobileNumber = "";
                    $scope.formData.driverLanguageId = -1;
                    $scope.formData.driverMsisdn = "";
                    $scope.formData.accountName = "";
                    $scope.formData.companyName = "";
                    $scope.formData.currency = "";
                    $scope.formData.accountBalance = 0;
                    $scope.formData.feeAmountDue = 0;
                    $scope.formData.amountFee = "";
                    $scope.formData.amountCashGiven = "";
                    $scope.formData.accountDetails = {};
                    $scope.formData.isAccountAvailable = false;
                    $scope.formData.isAdHoc = false;
                    $scope.formData.isDirectToPort = false;
                    $scope.formData.dateSlotString = "";
                    $scope.formData.dateMissionStartString = "";
                    $scope.formData.dateMissionEndString = "";
                    $scope.formData.paymentOption = null;
                    $scope.searchTripErrorResponse = "";
                    $scope.addHocTripSuccessResponse = "";
                    $scope.missionFoundSuccessResponse = "";
                    $scope.isMissionFound = false;
                    $scope.saveAdhocTripSubmitButtonDisabled = false;
                    $scope.tripBalanceCheckSubmitButtonDisabled = false;
                    $scope.saveAdhocTripErrorResponse = "";
                    $scope.tripBalanceCheckErrorResponse = "";
                    $scope.cancelAdHocTripErrorResponse = "";
                    $scope.cancelAdHocTripSuccessResponse = "";
                    $scope.searchTripSubmitButtonDisabled = false;
                    $scope.proceedToPaymentOrParkingSuccessResponse = "";
                    $scope.proceedToPayment = false;
                    $scope.proceedToParking = false;
                    $scope.tripSearchByOperatorAndTransactionType = false;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                    $scope.showAdhocTripScreen = false;
                    $scope.showOverviewScreen = false;
                    $scope.vehicleRegNumberDivClass = "";
                    $scope.vehicleRegNumberSpanClass = "";
                    $scope.referenceNumberDivClass = "";
                    $scope.referenceNumberSpanClass = "";
                    $scope.transactionTypeDivClass = "";
                    $scope.transactionTypeSpanClass = "";
                    $scope.slotDateDivClass = "";
                    $scope.slotDateSpanClass = "";
                    $scope.mobileNumberDivClass = "";
                    $scope.mobileNumberSpanClass = "";
                    $scope.driverLanguageIdDivClass = "";
                    $scope.driverLanguageIdSpanClass = "";
                    $scope.companyNameDivClass = "";
                    $scope.companyNameSpanClass = "";
                    $scope.operatorTypeDivClass = "";
                    $scope.operatorTypeSpanClass = "";
                    $scope.accountNameDivClass = "";
                    $scope.accountNameSpanClass = "";
                    $scope.companyNameDivClass = "";
                    $scope.companyNameSpanClass = "";
                    $scope.referenceNumbersList = [];
                    $scope.topupButtonDisabled = false;
                    $scope.makePaymentSuccessResponse = "";
                    $scope.makePaymentErrorResponse = "";
                    $scope.makePaymentInfoResponse = "";
                    $scope.isProceedToParkingGranted = false;
                    $scope.isProceedWithoutPayment = false;
                    $scope.saveParkingEntrySuccessResponse = "";
                    $scope.saveParkingEntryErrorResponse = "";
                    $scope.cancelDirectToPortSuccessResponse = "";
                    $scope.cancelDirectToPortErrorResponse = "";
                    $scope.saveParkingEntrySubmitButtonDisabled = false;
                    $scope.isProcessFinished = false;
                    $scope.tripBookingTimeJsonList = [];
                    $scope.isVehicleEligibleForParkingWithoutPayment = false;
                    $scope.isTripInTransit = false;
                    $scope.isVehicleWhitelisted = false;
                    $scope.isShowReceiptMessage = false;
                    $scope.isVehicleProcessedParkingEntryThroughANPR = false;
                    $scope.isPrintReceipt = false;
                    $scope.cancelPrintReceiptTimeout();

                    $scope.onlinePaymentCode = "";
                    $scope.paymentReference = "";
                    $scope.isPaymentSuccessful = false;
                    $scope.onlinePaymentRequestSubmitted = false;
                    $scope.formData.firstName = "";
                    $scope.formData.lastName = "";
                    $scope.formData.driverMsisdn = "";
                    $scope.onlinePaymentStatusCheckCount = 0;

                    if ($scope.formData.isFeePaid !== undefined && $scope.formData.isFeePaid !== null) {
                        $scope.formData.isFeePaid = false;
                    }

                    if (timer !== undefined && timer !== null) {

                        $timeout.cancel(timer);
                        timer = null;
                    }
                };

                $scope.backToSearchByReferenceNumber = function() {

                    if ($scope.saveAdhocTripForm !== undefined) {
                        $scope.saveAdhocTripForm.$setPristine();
                    }

                    $scope.dateSlot = "";
                    $scope.formData.timeSlot = "";
                    $scope.formData.operatorType = "";
                    $scope.formData.independentOperatorName = "";
                    $scope.formData.independentOperatorCode = "";
                    $scope.formData.transactionType = "";
                    $scope.formData.referenceNumber = "";
                    $scope.formData.vehicleRegistrationCountryISO = "";
                    $scope.formData.mobileNumber = "";
                    $scope.formData.driverLanguageId = -1;
                    $scope.formData.driverMsisdn = "";
                    $scope.formData.dateMissionStartString = "";
                    $scope.formData.dateMissionEndString = "";
                    $scope.referenceNumberDivClass = "";
                    $scope.referenceNumberSpanClass = "";
                    $scope.mobileNumberDivClass = "";
                    $scope.mobileNumberSpanClass = "";
                    $scope.driverLanguageIdDivClass = "";
                    $scope.driverLanguageIdSpanClass = "";
                    $scope.companyNameDivClass = "";
                    $scope.companyNameSpanClass = "";
                    $scope.showAdhocTripScreen = false;
                    $scope.tripSearchByOperatorAndTransactionType = true;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                    $scope.tripBookingTimeJsonList = [];
                };

                $scope.isPaymentOptionEnabled = function(paymentOptionConstant) {

                    if ($scope.mobilePaymentOptionsStatusMap === null) {
                        return false;

                    } else {
                        var isActive = $scope.mobilePaymentOptionsStatusMap[paymentOptionConstant];

                        return (isActive === undefined) ? false : isActive;
                    }
                };

                $scope.getTransactionTypes = function() {

                    var portOperatorId = parseInt($scope.formData.operatorType);
                    $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];
                };

                $scope.isAllowedForKioskSessionType = function(transactionType) {
                    return function(transactionType) {
                        if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_VIRTUAL) {
                            // for virtual kiosk sessions display only the transaction types that are allowed (direct to port)
                            return transactionType.isAllowedForVirtualKioskOp;

                        } else {
                            return transactionType;
                        }
                    }
                };

                $scope.closeKioskLaneConfirmationDialog = function() {

                    $('#kioskLaneConfirmationModal').modal('hide');
                };

                $scope.showKioskLaneConfirmationDialog = function() {

                    $('#kioskLaneConfirmationModal').modal({
                        backdrop : 'static',
                        keyboard : false,
                        show : true
                    });

                    sessionStorage.setItem('isKioskLaneConfirmationDialogAlreadyDisplayed', 'true');
                };

                $scope.setResponseParamsAndShowOverviewScreen = function(responseData) {

                    $scope.formData.vehicleRegistrationCountryISO = responseData.vehicleRegistrationCountryISO;
                    $scope.formData.operatorType = responseData.portOperatorId;
                    $scope.formData.independentOperatorName = responseData.independentPortOperatorName;
                    $scope.formData.transactionType = responseData.transactionType;
                    $scope.formData.referenceNumber = responseData.referenceNumber;
                    $scope.formData.dateSlotString = responseData.dateSlotString;
                    $scope.formData.mobileNumber = responseData.driverMobile;
                    $scope.formData.driverLanguageId = responseData.driverLanguageId + '';
                    $scope.formData.accountName = responseData.accountName;
                    $scope.formData.companyName = responseData.companyName;
                    $scope.formData.currency = responseData.currency;
                    $scope.formData.accountBalance = responseData.accountBalance;
                    $scope.formData.isAdHoc = responseData.adHoc;
                    $scope.formData.tripFeeAmount = responseData.tripFeeAmount;
                    $scope.formData.isDirectToPort = responseData.isDirectToPort;
                    $scope.formData.isVehicleActive = responseData.isVehicleActive;
                    $scope.formData.status = responseData.status;
                    
                    $scope.isShowReceiptMessage = responseData.isShowReceiptMessage;
                    $scope.tripCode = responseData.code;
                    $scope.driverMsisdn = $scope.formData.mobileNumber;
                    

                    if (responseData.feePaid !== undefined && responseData.feePaid !== null) {
                        $scope.formData.isFeePaid = responseData.feePaid;
                    }
                    if (responseData.isEligibleForParkingWithoutPayment !== undefined && responseData.isEligibleForParkingWithoutPayment !== null) {
                        $scope.isVehicleEligibleForParkingWithoutPayment = responseData.isEligibleForParkingWithoutPayment;
                    }
                    if (responseData.isTripInTransit !== undefined && responseData.isTripInTransit !== null) {
                        $scope.isTripInTransit = responseData.isTripInTransit;
                    }
                    if (responseData.account != null) {
                        $scope.formData.isAccountAvailable = true;
                        $scope.formData.accountDetails = responseData.account;
                    }
                    if (responseData.isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit) {
                        $scope.makePaymentSuccessResponse = $rootScope.translation.KEY_SCREEN_FEE_ALREADY_PAID_MESSAGE + ".";
                        $scope.isProceedToParkingGranted = true;
                        $scope.allowProceedToParking();
                    }

                    $scope.vehicleRegNumberDivClass = "has-success has-feedback";
                    $scope.vehicleRegNumberSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.referenceNumberDivClass = "has-success has-feedback";
                    $scope.referenceNumberSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.transactionTypeDivClass = "has-success has-feedback";
                    $scope.transactionTypeSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.slotDateDivClass = "has-success has-feedback";
                    $scope.slotDateSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.mobileNumberDivClass = "has-success has-feedback";
                    $scope.mobileNumberSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.driverLanguageIdDivClass = "has-success has-feedback";
                    $scope.driverLanguageIdSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.companyNameDivClass = "has-success has-feedback";
                    $scope.companyNameSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.operatorTypeDivClass = "has-success has-feedback";
                    $scope.operatorTypeSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.accountNameDivClass = "has-success has-feedback";
                    $scope.accountNameSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.companyNameDivClass = "has-success has-feedback";
                    $scope.companyNameSpanClass = "glyphicon glyphicon-ok form-control-feedback";

                    $scope.showOverviewScreen = true;
                    $scope.tripSearchByOperatorAndTransactionType = false;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                    $scope.showAdhocTripScreen = false;
                };

                if (sessionStorage.getItem('isKioskLaneConfirmationDialogAlreadyDisplayed') === null) {

                    if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING) {
                        $scope.showKioskLaneConfirmationDialog();
                    } else {
                        sessionStorage.setItem('isKioskLaneConfirmationDialogAlreadyDisplayed', 'true');
                    }
                }

                $scope.printReceipt = function() {

                    $scope.startPrintReceiptTimeout();

                    var urlParams = {
                        paymentCode : $scope.paymentCode
                    };

                    $rootScope.startSpinner();

                    receiptService.printReceipt(urlParams, function(response) {

                        $scope.cancelPrintReceiptTimeout();
                        $rootScope.stopSpinner();


                    }, function(error) {

                        $scope.cancelPrintReceiptTimeout();
                        $rootScope.stopSpinner();
                    });
                };

                $scope.startPrintReceiptTimeout = function(){

                    $scope.cancelPrintReceiptTimeout();

                    $scope.isPrintReceiptButtonDisabled = true;
                    $scope.printReceiptButtonTitle = $rootScope.translation.KEY_SCREEN_PRINT_RECEIPT_DISABLE_MESSAGE;

                    $scope.printReceiptTimeout = $timeout(function(){
                       $scope.cancelPrintReceiptTimeout();
                    }, 90000);

                };

                $scope.cancelPrintReceiptTimeout = function(){

                    $scope.isPrintReceiptButtonDisabled = false;
                    $scope.printReceiptButtonTitle = "";
                    $rootScope.stopSpinner();

                    $timeout.cancel($scope.printReceiptTimeout);

                };

                $scope.cancelAdHocTrip = function(){

                     $rootScope.startSpinner();

                     var urlParams = {
                        code : $scope.tripCode
                    };

                    $scope.addHocTripSuccessResponse = "";
                    tripService.cancelAdHocTrip(urlParams, function(response) {

                        $scope.cancelAdHocTripSuccessResponse = $rootScope.translation.KEY_SCREEN_ADHOC_TRIP_CANCELLED_SUCCESSFULLY_MESSAGE;
                        $rootScope.stopSpinner();

                    }, function(error) {

                        $scope.cancelAdHocTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode);
                        $rootScope.stopSpinner();

                    });
                };
                
                $scope.cancelDirectToPortTrip = function() {

                    $rootScope.showGenericConfirmationModal($rootScope.translation.KEY_SCREEN_TRIP_CANCEL_ALERT_MESSAGE, function() {
        
                        var urlParams = {
                            code : $scope.tripCode
                        };
    
                        tripService.cancelTrip(urlParams, function(response) {
    
                            $scope.cancelDirectToPortSuccessResponse = $rootScope.translation.KEY_SCREEN_DIRECT_TO_PORT_TRIP_CANCELLED_SUCCESSFULLY_MESSAGE;
    
                        }, function(error) {
                            $scope.errorHandler(error);
                            
                            $scope.cancelDirectToPortErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode);
    
                        });
                    });
                };
                

                $scope.getVehicleRegistrationListFromTrips();
                $rootScope.stopSpinner();

                $scope.$watch('formData.operatorType', function() {

                    if ($scope.formData.operatorType !== undefined && $scope.formData.operatorType !== null && $scope.formData.operatorType !== "") {
                        $scope.getTransactionTypes();

                        if (!$rootScope.independentPortOperatorId) {
                            $scope.independentOperators = $rootScope.getIndependentOperators($scope.formData.operatorType);

                            if ($scope.showAdhocTripScreen) {
                                $scope.formData.independentOperatorName = "";
                                $scope.formData.independentOperatorCode = "";
                            }

                            $("#independentOperatorName").autocomplete({
                                source: $scope.independentOperators,
                                focus: function(event, ui) {
                                    // prevent autocomplete from updating the textbox
                                    event.preventDefault();
                                    $(this).val(ui.item.label);
                                },
                                select: function(event, ui) {
                                    // prevent autocomplete from updating the textbox
                                    event.preventDefault();
                                    $(this).val(ui.item.label);
                                    $scope.formData.independentOperatorName = ui.item.label;
                                    $scope.formData.independentOperatorCode = ui.item.value;
                                }
                            });
                        }
                    }
                });

                $scope.$watch('formData.paymentOption', function() {

                    if ($scope.formData.paymentOption !== undefined && $scope.formData.paymentOption !== null && $scope.formData.paymentOption !== "") {

                        $scope.isPaymentOptionSelected = true;

                        if ($scope.driverMsisdn != '') {
                            $scope.formData.driverMsisdn = $scope.driverMsisdn;

                        } else {
                            $scope.formData.driverMsisdn = "";
                        }

                    } else {
                        $scope.isPaymentOptionSelected = false;
                    }
                });

                $scope.$watch('dateSlot', function(newVal, oldVal) {

                    if (newVal != oldVal) {

                        // get list of available slot hours
                        if (newVal !== undefined && newVal !== "" && $scope.formData.operatorType !== undefined && $scope.formData.operatorType !== ""
                            && $scope.formData.transactionType !== undefined
                            && $scope.formData.transactionType !== "") {

                            $rootScope.startSpinner();

                            var urlParams = {
                                portOperatorId : parseInt($scope.formData.operatorType),
                                transactionType : parseInt($scope.formData.transactionType),
                                dateSlotString : newVal
                            };

                            tripService.getAvailableBookingHours(urlParams, function(response) {

                                $scope.tripBookingTimeJsonList = response.dataList;

                                // preset the slot time to the next available slot if the selected dateSlot is today. Get the calculated time value from backend
                                // if the selected dateSlot is tomorrow, dont preset the slot time by default
                                if (response.data !== undefined && response.data !== null && response.data !== '') {
                                    $scope.formData.timeSlot = response.data;
                                } else {
                                    $scope.formData.timeSlot = "";
                                }

                                $rootScope.stopSpinner();

                            }, function(error) {

                                $scope.tripBookingTimeJsonList = [];
                                $scope.errorHandler(error);
                            });
                        }
                    }
                }, true);

            } ]);
