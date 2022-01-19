padApp.controller('ParkingExitController', [
    '$scope',
    '$rootScope',
    '$interval',
    'parkingService',
    'validationConstants',
    'transactionTypeConstants',
    function($scope, $rootScope, $interval, parkingService, validationConstants, transactionTypeConstants) {

        $rootScope.startSpinner();

        $scope.formData = {};
        $scope.searchVehicleRegErrorResponse = "";
        $scope.searchVehicleRegSuccessResponse = "";
        $scope.searchVehicleRegSuccessWarningResponse = "";
        $scope.isVehicleRegFound = false;
        $scope.proceedExit = false;
        $scope.showOverviewScreen = false;
        $scope.proceedExitSubmitButtonDisabled = false;
        $scope.enteredVehicleRegistrationList = [];
        $scope.isProcessFinished = false;

        $scope.errorHandler = function(error) {
            $rootScope.stopSpinner();
            console.log(error);
        };

        $scope.getEnteredVehicleRegistrationList = function() {
            parkingService.getEnteredVehicleRegistrationList({}, function(response) {
                $scope.enteredVehicleRegistrationList = response.dataList;
                $scope.setEnteredVehicleRegistrationAutocomplete();

            }, $scope.errorHandler);
        };

        $scope.setEnteredVehicleRegistrationAutocomplete = function() {
            $("#vehicleRegNumber").autocomplete({
                source : $scope.enteredVehicleRegistrationList,
                select : function(event, ui) {
                    $scope.formData.vehicleRegNumber = ui.item.value;
                    $scope.searchEnteredVehicleReg();
                }
            });
        }

        $scope.getEnteredVehicleRegistrationList();

        $interval(function() {
            $scope.getEnteredVehicleRegistrationList();
        }, 60000);

        $scope.resetRegSearch = function() {
            $scope.formData = {};
            $scope.isVehicleRegFound = false;
            $scope.proceedExit = false;
            $scope.searchVehicleRegErrorResponse = "";
            $scope.searchVehicleRegSuccessResponse = "";
            $scope.searchVehicleRegSuccessWarningResponse = "";
            $scope.proceedExitSubmitButtonDisabled = false;
            $scope.formData.vehicleRegNumber = "";
            $scope.divRegNumExitClass = "";
            $scope.spanRegNumExitClass = "";
            $scope.referenceNumberDivClass = "";
            $scope.referenceNumberSpanClass = "";
            $scope.transactionTypeDivClass = "";
            $scope.transactionTypeSpanClass = "";
            $scope.slotDateDivClass = "";
            $scope.slotDateSpanClass = "";
            $scope.mobileNumberDivClass = "";
            $scope.mobileNumberSpanClass = "";
            $scope.operatorTypeDivClass = "";
            $scope.operatorTypeSpanClass = "";
            $scope.accountNameDivClass = "";
            $scope.accountNameSpanClass = "";
            $scope.companyNameDivClass = "";
            $scope.companyNameSpanClass = "";
            $scope.showOverviewScreen = false;
            $scope.isProcessFinished = false;
        }

        $scope.searchEnteredVehicleReg = function() {

            if ($scope.formData.vehicleRegNumber === undefined || $scope.formData.vehicleRegNumber === null || $scope.formData.vehicleRegNumber === ""
                || $scope.formData.vehicleRegNumber.length < validationConstants.REGNUMBER_VALIDATION_LENGTH_MIN
                || $scope.formData.vehicleRegNumber.length > validationConstants.REGNUMBER_VALIDATION_LENGTH_MAX) {

                $scope.searchVehicleRegErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";
                $scope.divRegNumExitClass = "has-error has-feedback";
                $scope.spanRegNumExitClass = "glyphicon glyphicon-remove form-control-feedback";
                return;
            }

            $scope.isVehicleRegFound = false;

            $scope.searchVehicleRegErrorResponse = "";
            $scope.searchVehicleRegSubmitButtonDisabled = true;

            urlParams = {
                vehicleRegistration : $scope.formData.vehicleRegNumber
            };

            parkingService.searchEnteredVehicleReg(urlParams, function(response) {

                $scope.parking = response.dataList[0];

                if (!$scope.parking) {
                    $scope.proceedExit = true;
                    $scope.searchVehicleRegErrorResponse = $rootScope.translation.KEY_SCREEN_VEHICLE_NOT_FOUND_TRY_AGAIN_MESSAGE + ".";
                    $scope.searchVehicleRegSuccessResponse = "";
                    $scope.searchVehicleRegSuccessWarningResponse = "";
                    $scope.divRegNumExitClass = "has-error has-feedback";
                    $scope.spanRegNumExitClass = "glyphicon glyphicon-remove form-control-feedback";
                } else {
                    $scope.searchVehicleRegErrorResponse = "";
                    $scope.searchVehicleRegSuccessResponse = $rootScope.translation.KEY_SCREEN_VEHICLE_FOUND_MESSAGE + ".";

                    if ($scope.parking.parkingExitMessageId != null && $scope.parking.parkingExitMessageId != undefined) {

                        switch($scope.parking.parkingExitMessageId) {
                            case $rootScope.parkingExitMsgIdConstants.PARKING_EXIT_MESSAGE_ID_AUTH_EXIT:
                                $scope.searchVehicleRegSuccessResponse += " " + $rootScope.translation.KEY_SCREEN_PARKING_EXIT_MSG_AUTH_EXIT + ".";
                                break;

                            case $rootScope.parkingExitMsgIdConstants.PARKING_EXIT_MESSAGE_ID_PREMATURE_EXIT:
                                $scope.searchVehicleRegSuccessResponse = "";
                                $scope.searchVehicleRegSuccessWarningResponse = $rootScope.translation.KEY_SCREEN_PARKING_EXIT_MSG_PREMATURE_EXIT + ".";
                                break;

                            case $rootScope.parkingExitMsgIdConstants.PARKING_EXIT_MESSAGE_ID_EXIT_ONLY:
                                $scope.searchVehicleRegSuccessResponse = "";
                                $scope.searchVehicleRegSuccessWarningResponse = $rootScope.translation.KEY_SCREEN_PARKING_EXIT_MSG_EXIT_ONLY + ".";
                                break;

                            default:
                                break;
                        }
                    }

                    $scope.isVehicleRegFound = true;
                    $scope.proceedExit = true;

                    $scope.formData.parkingType = $scope.parking.type;
                    $scope.formData.operatorType = $scope.parking.portOperator;
                    $scope.formData.independentPortOperatorName = $scope.parking.independentPortOperatorName;
                    $scope.formData.transactionType = $scope.parking.transactionType;
                    $scope.formData.referenceNumber = $scope.parking.referenceNumber;
                    $scope.formData.dateSlotString = $scope.parking.dateSlotString;
                    $scope.formData.mobileNumber = $scope.parking.driverMobile;

                    $scope.formData.accountName = $scope.parking.accountName;
                    $scope.formData.companyName = $scope.parking.companyName;
                    $scope.formData.currency = $scope.parking.currency;
                    $scope.formData.accountBalance = $scope.parking.accountBalance;
                    $scope.formData.isAdHoc = $scope.parking.isAdHoc;

                    $scope.divRegNumExitClass = "has-success has-feedback";
                    $scope.spanRegNumExitClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.referenceNumberDivClass = "has-success has-feedback";
                    $scope.referenceNumberSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.transactionTypeDivClass = "has-success has-feedback";
                    $scope.transactionTypeSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.slotDateDivClass = "has-success has-feedback";
                    $scope.slotDateSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.mobileNumberDivClass = "has-success has-feedback";
                    $scope.mobileNumberSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.operatorTypeDivClass = "has-success has-feedback";
                    $scope.operatorTypeSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.accountNameDivClass = "has-success has-feedback";
                    $scope.accountNameSpanClass = "glyphicon glyphicon-ok form-control-feedback";
                    $scope.companyNameDivClass = "has-success has-feedback";
                    $scope.companyNameSpanClass = "glyphicon glyphicon-ok form-control-feedback";

                    // single trip found for vehicle reg
                    $scope.showOverviewScreen = true;
                }

            }, $scope.errorHandler);
        }

        $scope.exitVehicle = function() {

            $scope.proceedExitSubmitButtonDisabled = true;
            $scope.searchVehicleRegErrorResponse = "";

            if ($scope.parking) {
                urlParams = {
                    code : $scope.parking.code,
                    vehicleRegistration : $scope.formData.vehicleRegNumber
                }
            } else {
                urlParams = {
                    code : null,
                    vehicleRegistration : $scope.formData.vehicleRegNumber
                }

            }

            parkingService.vehicleExit(urlParams, function(response) {

                $scope.searchVehicleRegSuccessResponse = $rootScope.translation.KEY_SCREEN_EXIT_COMPLETED_MESSAGE + ".";

                var index = $scope.enteredVehicleRegistrationList.indexOf($scope.formData.vehicleRegNumber);
                $scope.enteredVehicleRegistrationList.splice(index, 1);
                $scope.setEnteredVehicleRegistrationAutocomplete();

                $scope.isProcessFinished = true;

            }, function(error) {

                $scope.errorHandler(error);
                $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });
        }

        $rootScope.stopSpinner();

} ]);
