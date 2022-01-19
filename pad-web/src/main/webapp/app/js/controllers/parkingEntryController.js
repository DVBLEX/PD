padApp
    .controller(
        'ParkingEntryController',
        [
            '$scope',
            '$rootScope',
            '$interval',
            'tripService',
            'systemService',
            'validationConstants',
            'transactionTypeConstants',
            function($scope, $rootScope, $interval, tripService, systemService, validationConstants, transactionTypeConstants) {

                $rootScope.startSpinner();

                $scope.formData = {};
                $scope.formData.vehicleRegNumberInput = "";
                $scope.formData.vehicleRegistrationCountryISO = "";
                $scope.formData.operatorType = "";
                $scope.independentOperators = [];
                $scope.formData.independentOperatorName = "";
                $scope.formData.independentOperatorCode = "";
                $scope.searchTripErrorResponse = "";
                $scope.addHocTripSuccessResponse = "";
                $scope.missionFoundSuccessResponse = "";
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
                $scope.proceedToPaymentOrParkingSuccessResponse = "";
                $scope.referenceNumbersList = [];
                $scope.isProcessFinished = false;
                $scope.updateMobileNumberErrorResponse = "";
                $scope.updateMobileNumberSuccessResponse = "";
                $scope.updateVehicleRegCountryISOSuccessResponse = "";
                $scope.updateVehicleRegCountryISOErrorResponse = "";
                $scope.isMobileNumberEditRequested = false;
                $scope.isVehicleRegCountryISOEditRequested = false;
                $scope.portOperatorTransactionTypes = [];
                $scope.tripBookingTimeJsonList = [];

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

                $scope.getVehicleRegistrationListFromTrips();

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
                                $scope.checkBalanceForTrip(response.dataList[0].code);

                            }, function(error) {

                                $scope.searchTripResponseCode = error.data.responseCode;

                                switch ($scope.searchTripResponseCode) {
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

                                        $scope.setResponseParamsAndShowOverviewScreen(error.data.dataList[0]);

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
                                $scope.checkBalanceForTrip(response.dataList[0].code);

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
                        // search by port operator, transaction type and mission reference number number
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
                                $scope.checkBalanceForTrip(response.dataList[0].code);

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

                                        break;
                                    // Mission found and trip record needs to be created
                                    case 1148:
                                        $scope.showAdhocTripScreen = true;
                                        $scope.tripSearchByOperatorAndTransactionType = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                                        $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;
                                        $scope.showOverviewScreen = false;
                                        $scope.missionFoundSuccessResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";

                                        $scope.formData.dateMissionStartString = error.data.dataList[0].dateMissionStartString;
                                        $scope.formData.dateMissionEndString = error.data.dataList[0].dateMissionEndString;
                                        $scope.formData.accountName = error.data.dataList[0].accountName;
                                        $scope.formData.companyName = error.data.dataList[0].companyName;
                                        $scope.formData.currency = error.data.dataList[0].currency;
                                        $scope.formData.accountBalance = error.data.dataList[0].accountBalance;

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

                    } else if ($scope.saveAdhocTripForm.transactionType.$invalid && $scope.searchTripResponseCode == 1147) {
                        $scope.saveAdhocTripErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TRANSACTION_TYPE_MESSAGE + ".";

                    } else {
                        $scope.saveAdhocTripErrorResponse = "";
                        $scope.addHocTripSuccessResponse = "";
                        $scope.mobileNumberDivClass = "";
                        $scope.mobileNumberSpanClass = "";
                        $scope.driverLanguageIdDivClass = "";
                        $scope.driverLanguageIdSpanClass = "";
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
                            getTripResponseCode : $scope.searchTripResponseCode
                        };

                        tripService.saveAdhocTrip(urlParams, function(response) {

                            $scope.formData.tripCode = response.dataList[0].code;
                            $scope.formData.dateSlotString = response.dataList[0].dateSlotString;
                            $scope.formData.accountName = response.dataList[0].accountName;
                            $scope.formData.companyName = response.dataList[0].companyName;
                            $scope.formData.currency = response.dataList[0].currency;
                            $scope.formData.accountBalance = response.dataList[0].accountBalance;
                            $scope.formData.isAdHoc = response.dataList[0].adHoc;

                            $scope.formData.mobileNumberCopy = $scope.formData.mobileNumber;
                            $scope.formData.driverLanguageIdCopy = $scope.formData.driverLanguageId;
                            $scope.formData.vehicleRegistrationCountryISOCopy = $scope.formData.vehicleRegistrationCountryISO;

                            $scope.showOverviewScreen = true;
                            $scope.tripSearchByOperatorAndTransactionType = false;
                            $scope.showAdhocTripScreen = false;

                            $scope.addHocTripSuccessResponse = $rootScope.translation.KEY_SCREEN_ADD_HOC_TRIP_CREATED_LABEL + ".";

                            $scope.checkBalanceForTrip(response.dataList[0].code);

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

                $scope.checkBalanceForTrip = function(tripCode) {

                    urlParams = {
                        code : tripCode
                    };

                    tripService.checkBalanceForTrip(urlParams, function(response) {

                        $scope.proceedToParking = true;
                        $scope.proceedToPayment = false;

                    }, function(error) {

                        $scope.saveAdhocTripSubmitButtonDisabled = false;
                        $scope.saveAdhocTripErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";

                        switch (error.data.responseCode) {
                            // account balance is low. proceed to payment
                            // OR
                            // No account associated with trip. Proceed to payment
                            case 1149:
                            case 1150:
                                $scope.proceedToPayment = true;
                                $scope.proceedToParking = false;
                                break;
                            default:
                                break;
                        }

                        $scope.errorHandler(error);
                    });
                };

                $scope.proceedVehicleEntry = function(type) {

                    if (type == 1) {
                        $scope.proceedToPaymentOrParkingSuccessResponse = $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ". "
                            + $rootScope.translation.KEY_SCREEN_PROCEED_TO_PARKING_LABEL + ".";
                    } else if (type == 2) {
                        $scope.proceedToPaymentOrParkingSuccessResponse = $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ". "
                            + $rootScope.translation.KEY_SCREEN_PROCEED_TO_PAYMENT_LABEL + ".";
                    }

                    $scope.updateMobileNumberErrorResponse = "";
                    $scope.updateMobileNumberSuccessResponse = "";
                    $scope.updateVehicleRegCountryISOErrorResponse = "";
                    $scope.updateVehicleRegCountryISOSuccessResponse = "";
                    $scope.isProcessFinished = true;

                };

                $scope.clearTripSearchForm = function() {

                    if ($scope.tripSearchForm !== undefined) {
                        $scope.tripSearchForm.$setPristine();
                    }
                    if ($scope.saveAdhocTripForm !== undefined) {
                        $scope.saveAdhocTripForm.$setPristine();
                    }
                    if ($scope.tripBalanceCheckForm !== undefined) {
                        $scope.tripBalanceCheckForm.$setPristine();
                    }

                    $scope.formData.vehicleRegNumberInput = "";
                    $scope.formData.vehicleRegistrationCountryISO = "";
                    $scope.formData.vehicleRegistrationCountryISOCopy = "";
                    $scope.dateSlot = "";
                    $scope.formData.operatorType = "";
                    $scope.formData.independentOperatorName = "";
                    $scope.formData.independentOperatorCode = "";
                    $scope.formData.timeSlot = "";
                    $scope.formData.transactionType = "";
                    $scope.formData.referenceNumber = "";
                    $scope.formData.mobileNumber = "";
                    $scope.formData.mobileNumberCopy = "";
                    $scope.formData.driverLanguageId = -1;
                    $scope.formData.driverLanguageIdCopy = -1;
                    $scope.formData.accountName = "";
                    $scope.formData.companyName = "";
                    $scope.formData.currency = "";
                    $scope.formData.accountBalance = 0;
                    $scope.formData.isAdHoc = false;
                    $scope.formData.dateMissionStartString = "";
                    $scope.formData.dateMissionEndString = "";
                    $scope.searchTripErrorResponse = "";
                    $scope.addHocTripSuccessResponse = "";
                    $scope.missionFoundSuccessResponse = "";
                    $scope.saveAdhocTripSubmitButtonDisabled = false;
                    $scope.tripBalanceCheckSubmitButtonDisabled = false;
                    $scope.saveAdhocTripErrorResponse = "";
                    $scope.tripBalanceCheckErrorResponse = "";
                    $scope.searchTripSubmitButtonDisabled = false;
                    $scope.proceedToPaymentOrParkingSuccessResponse = "";
                    $scope.updateMobileNumberErrorResponse = "";
                    $scope.updateMobileNumberSuccessResponse = "";
                    $scope.updateVehicleRegCountryISOErrorResponse = "";
                    $scope.updateVehicleRegCountryISOSuccessResponse = "";
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
                    $scope.operatorTypeDivClass = "";
                    $scope.operatorTypeSpanClass = "";
                    $scope.accountNameDivClass = "";
                    $scope.accountNameSpanClass = "";
                    $scope.companyNameDivClass = "";
                    $scope.companyNameSpanClass = "";
                    $scope.referenceNumbersList = [];
                    $scope.formData.tripCode = "";
                    $scope.isProcessFinished = false;
                    $scope.tripBookingTimeJsonList = [];

                    if ($scope.formData.dateParkingEntryString !== undefined && $scope.formData.dateParkingEntryString !== null) {
                        $scope.formData.dateParkingEntryString = "";
                    }
                    if ($scope.formData.parkingStatus !== undefined && $scope.formData.parkingStatus !== null) {
                        $scope.formData.parkingStatus = "";
                    }

                    $scope.cancelMobileNumberEdit();
                };

                $scope.backToSearchByReferenceNumber = function() {

                    if ($scope.saveAdhocTripForm !== undefined) {
                        $scope.saveAdhocTripForm.$setPristine();
                    }

                    $scope.formData.vehicleRegistrationCountryISOCopy = "";
                    $scope.dateSlot = "";
                    $scope.formData.timeSlot = "";
                    $scope.formData.operatorType = "";
                    $scope.formData.independentOperatorName = "";
                    $scope.formData.independentOperatorCode = "";
                    $scope.formData.transactionType = "";
                    $scope.formData.referenceNumber = "";
                    $scope.formData.mobileNumber = "";
                    $scope.formData.mobileNumberCopy = "";
                    $scope.formData.driverLanguageId = -1;
                    $scope.formData.driverLanguageIdCopy = -1;
                    $scope.formData.dateMissionStartString = "";
                    $scope.formData.dateMissionEndString = "";
                    $scope.referenceNumberDivClass = "";
                    $scope.referenceNumberSpanClass = "";
                    $scope.showAdhocTripScreen = false;
                    $scope.tripSearchByOperatorAndTransactionType = true;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReference = false;
                    $scope.tripSearchByOperatorAndTransactionTypeAndReferenceOrContainer = false;

                    $scope.tripBookingTimeJsonList = [];
                };

                $scope.requestMobileNumberEdit = function() {

                    $scope.isMobileNumberEditRequested = true;
                };

                $scope.requestVehicleRegCountryISOEdit = function() {

                    $scope.isVehicleRegCountryISOEditRequested = true;
                };

                $scope.cancelMobileNumberEdit = function() {

                    $scope.isMobileNumberEditRequested = false;
                    $scope.updateMobileNumberErrorResponse = "";

                    if ($scope.tripBalanceCheckForm !== undefined) {
                        $scope.tripBalanceCheckForm.$setPristine();
                    }

                    $scope.formData.mobileNumber = $scope.formData.mobileNumberCopy;
                    $scope.formData.driverLanguageId = $scope.formData.driverLanguageIdCopy;
                };

                $scope.cancelVehicleRegCountryISOEdit = function() {

                    $scope.isVehicleRegCountryISOEditRequested = false;
                    $scope.updateVehicleRegCountryISOErrorResponse = "";

                    if ($scope.tripBalanceCheckForm !== undefined) {
                        $scope.tripBalanceCheckForm.$setPristine();
                    }

                    $scope.formData.vehicleRegistrationCountryISO = $scope.formData.vehicleRegistrationCountryISOCopy;
                };

                $scope.updateDriverMobileNumber = function() {

                    $scope.updateMobileNumberSuccessResponse = "";
                    $scope.tripBalanceCheckForm.$setPristine();

                    if ($scope.tripBalanceCheckForm.mobileNumber.$invalid) {
                        $scope.updateMobileNumberErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";
                    } else {
                        $scope.updateMobileNumberErrorResponse = "";

                        urlParams = {
                            code : $scope.formData.tripCode,
                            driverMobile : $scope.formData.mobileNumber,
                            driverLanguageId : $scope.formData.driverLanguageId
                        };

                        tripService.updateDriverMobileNumber(urlParams, function(response) {

                            $scope.updateMobileNumberSuccessResponse = $rootScope.translation.KEY_SCREEN_DRIVER_DETAILS_UPDATED_MESSAGE + ".";
                            $scope.isMobileNumberEditRequested = false;
                            $scope.formData.mobileNumberCopy = $scope.formData.mobileNumber;
                            $scope.formData.driverLanguageIdCopy = $scope.formData.driverLanguageId;

                        }, function(error) {

                            $scope.updateMobileNumberErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                            $scope.errorHandler(error);
                        });
                    }
                };

                $scope.updateVehicleRegCountryISO = function() {

                    $scope.updateVehicleRegCountryISOSuccessResponse = "";
                    $scope.tripBalanceCheckForm.$setPristine();

                    urlParams = {
                        code : $scope.formData.tripCode,
                        vehicleRegistrationCountryISO : $scope.formData.vehicleRegistrationCountryISO
                    };

                    tripService.updateVehicleRegCountryISO(urlParams, function(response) {

                        $scope.updateVehicleRegCountryISOSuccessResponse = $rootScope.translation.KEY_SCREEN_DRIVER_DETAILS_UPDATED_MESSAGE + ".";
                        $scope.isVehicleRegCountryISOEditRequested = false;
                        $scope.formData.vehicleRegistrationCountryISOCopy = $scope.formData.vehicleRegistrationCountryISO;

                    }, function(error) {

                        $scope.updateVehicleRegCountryISOErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.errorHandler(error);
                    });
                };

                $scope.getTransactionTypes = function() {

                    var portOperatorId = parseInt($scope.formData.operatorType);
                    $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];
                };

                $scope.setResponseParamsAndShowOverviewScreen = function(responseData) {

                    $scope.formData.vehicleRegistrationCountryISO = responseData.vehicleRegistrationCountryISO;
                    $scope.formData.vehicleRegistrationCountryISOCopy = responseData.vehicleRegistrationCountryISO;
                    $scope.formData.tripCode = responseData.code;
                    $scope.formData.operatorType = responseData.portOperatorId;
                    $scope.formData.independentOperatorName = responseData.independentPortOperatorName;
                    $scope.formData.transactionType = responseData.transactionType;
                    $scope.formData.referenceNumber = responseData.referenceNumber;
                    $scope.formData.dateSlotString = responseData.dateSlotString;

                    if (responseData.dateParkingEntryString !== undefined && responseData.dateParkingEntryString !== null) {
                        $scope.formData.dateParkingEntryString = responseData.dateParkingEntryString;
                    }
                    if (responseData.parkingStatus !== undefined && responseData.parkingStatus !== null) {
                        $scope.formData.parkingStatus = responseData.parkingStatus;
                    }
                    $scope.formData.mobileNumber = "+" + responseData.driverMobile;
                    $scope.formData.mobileNumberCopy = "+" + responseData.driverMobile;
                    $scope.formData.driverLanguageId = responseData.driverLanguageId + '';
                    $scope.formData.driverLanguageIdCopy = responseData.driverLanguageId + '';

                    $scope.formData.accountName = responseData.accountName;
                    $scope.formData.companyName = responseData.companyName;
                    $scope.formData.currency = responseData.currency;
                    $scope.formData.accountBalance = responseData.accountBalance;
                    $scope.formData.isAdHoc = responseData.adHoc;

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

                $rootScope.stopSpinner();

                $scope.$watch('formData.operatorType', function() {

                    if ($scope.formData.operatorType !== undefined && $scope.formData.operatorType !== null && $scope.formData.operatorType !== "") {
                        $scope.getTransactionTypes();
                        $scope.independentOperators = $rootScope.getIndependentOperators($scope.formData.operatorType);

                        if ($scope.showAdhocTripScreen) {
                            $scope.formData.independentOperatorName = "";
                            $scope.formData.independentOperatorCode = "";
                        }

                        $("#independentOperatorName").autocomplete({
                            source: $scope.independentOperators,
                            focus: function (event, ui) {
                                // prevent autocomplete from updating the textbox
                                event.preventDefault();
                                $(this).val(ui.item.label);
                            },
                            select: function (event, ui) {
                                // prevent autocomplete from updating the textbox
                                event.preventDefault();
                                $(this).val(ui.item.label);
                                $scope.formData.independentOperatorName = ui.item.label;
                                $scope.formData.independentOperatorCode = ui.item.value;
                            }
                        });

                    }
                });

                $scope.$watch('dateSlot', function(newVal, oldVal) {

                    if (newVal != oldVal) {

                        // get list of available slot hours
                        if (newVal !== undefined && newVal !== "" && $scope.formData.operatorType !== undefined && $scope.formData.operatorType !== ""
                            && $scope.formData.transactionType !== undefined && $scope.formData.transactionType !== "") {

                            $rootScope.startSpinner();

                            var urlParams = {
                                portOperatorId : parseInt($scope.formData.operatorType),
                                transactionType : parseInt($scope.formData.transactionType),
                                dateSlotString : newVal
                            };

                            tripService.getAvailableBookingHours(urlParams, function(response) {

                                $scope.tripBookingTimeJsonList = response.dataList;

                                $rootScope.stopSpinner();

                            }, function(error) {

                                $scope.tripBookingTimeJsonList = [];
                                $scope.errorHandler(error);
                            });
                        }
                    }
                }, true);

            } ]);
