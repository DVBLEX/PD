padApp.controller('PortEntryController', [ '$scope', '$rootScope', '$interval', 'parkingService', 'portEntryService',
    function($scope, $rootScope, $interval, parkingService, portEntryService) {

        $rootScope.startSpinner();

        $scope.formData = {};
        $scope.searchVehicleRegErrorResponse = "";
        $scope.searchVehicleRegSuccessResponse = "";
        $scope.isVehicleRegFound = false;
        $scope.proceedEntry = false;
        $scope.isDeny = false;
        $scope.isProcessFinished = false;
        $scope.isSendReasonDeny = false;

        $scope.errorHandler = function(error) {
            $rootScope.stopSpinner();
            console.log(error);
        };
/*
        $scope.getVehicleRegistrationList = function() {
            parkingService.getExitedVehicleRegistrationList({}, function(response) {
                $("#vehicleRegNumber").autocomplete({
                    source : response.dataList,
                    select : function(event, ui) {
                        $scope.formData.vehicleRegNumber = ui.item.value;
                        $scope.searchExitedVehicleReg();
                    }
                });
            }, $scope.errorHandler);
        };

        $scope.getVehicleRegistrationList();

        $interval(function() {
            $scope.getVehicleRegistrationList();
        }, 60000);
*/

        $scope.searchExitedVehicleReg = function() {

            if (!$scope.formData.vehicleRegNumber) {
                $scope.searchVehicleRegErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";
                $scope.divRegNumExitClass = "has-error has-feedback";
                $scope.spanRegNumExitClass = "glyphicon glyphicon-remove form-control-feedback";
                return;
            }

            $scope.isVehicleRegFound = false;

            $scope.searchVehicleRegErrorResponse = "";
            $scope.searchVehicleRegSubmitButtonDisabled = true;

            urlParams = {
                vehicleRegistration : $scope.formData.vehicleRegNumber,
                isPortEntryReadOnly : !$rootScope.portEntryStatus
            };

            parkingService.searchExitedVehicleReg(urlParams, function(response) {

                $scope.parking = response.dataList[0];

                $scope.searchVehicleRegErrorResponse = "";
                $scope.isVehicleRegFound = true;
                $scope.proceedEntry = true;
                $scope.divRegNumExitClass = "has-success has-feedback";
                $scope.spanRegNumExitClass = "glyphicon glyphicon-ok form-control-feedback";

            }, function(error) {

                $scope.proceedEntry = false;
                $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.searchVehicleRegSuccessResponse = "";
                $scope.divRegNumExitClass = "has-error has-feedback";
                $scope.spanRegNumExitClass = "glyphicon glyphicon-remove form-control-feedback";

                $scope.isProcessFinished = true;

                $scope.errorHandler(error);
            });
        }

        $scope.vehiclePortEntry = function () {

            if ($scope.parking === undefined) {
                $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(1000) + ".";
                return;
            }

            let message = $rootScope.translation.KEY_SCREEN_CONFIRM_ACTION_MESSAGE + "?";

            urlParams = {
                vehicleRegistration : $scope.parking.vehicleRegistration,
            }

            portEntryService.checkLastEntry(urlParams, function(response) {
                $rootScope.startSpinner();
                $rootScope.isUpdating = true;

                let isWarning = true;
                let minutesFromLastPortEntry = response.dataList[0].minutesFromLastPortEntry;
                if (minutesFromLastPortEntry < 60) {
                    message = $rootScope.translation.KEY_SCREEN_THIS_TRUCK_MESSAGE + " " + $scope.parking.vehicleRegistration
                        + " " + $rootScope.translation.KEY_SCREEN_ENTERED_PORT_MESSAGE + " " + minutesFromLastPortEntry + " " + $rootScope.translation.KEY_SCREEN_MINUTES_AGO_CONFIRM_MESSAGE;

                    $rootScope.showGenericConfirmationModal(message, $scope.vehiclePortEntryConfirmationCallback, isWarning);
                } else {
                    $rootScope.showGenericConfirmationModal(message, $scope.vehiclePortEntryConfirmationCallback);
                }

                $rootScope.isUpdating = false;
                $rootScope.stopSpinner();

            }, function(error) {
                $rootScope.isUpdating = false;
                $scope.errorHandler(error);
            });

        };

        $scope.vehiclePortEntryConfirmationCallback = function() {

            $rootScope.startSpinner();
            $rootScope.isUpdating = true;

            var urlParams = {};
            if ($scope.parking.isWhitelisted) {

                urlParams = {
                    portOperatorId : $scope.parking.portOperator,
                    gateId : $scope.parking.gateId,
                    vehicleRegNumber : $scope.formData.vehicleRegNumber,
                    selectedZone : encodeURI($rootScope.selectedZone)
                }

                portEntryService.vehicleWhitelistPortEntry(urlParams, function(response) {

                    $rootScope.isUpdating = false;

                    $scope.searchVehicleRegSuccessResponse = $rootScope.translation.KEY_SCREEN_PORT_ENTRY_COMPLETE_MESSAGE + ".";
                    $scope.isProcessFinished = true;
                    //$scope.getVehicleRegistrationList();

                    $rootScope.stopSpinner();

                }, function(error) {

                    $rootScope.isUpdating = false;
                    $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    $scope.isProcessFinished = true;
                    $scope.errorHandler(error);
                });

            } else {

                urlParams = {
                    tripCode : $scope.parking.tripCode,
                    parkingCode : $scope.parking.code,
                    transactionType : $scope.parking.transactionType,
                    selectedZone : encodeURI($rootScope.selectedZone)
                }

                portEntryService.vehiclePortEntry(urlParams, function(response) {

                    $rootScope.isUpdating = false;
                    $scope.searchVehicleRegSuccessResponse = $rootScope.translation.KEY_SCREEN_PORT_ENTRY_COMPLETE_MESSAGE + ".";
                    $scope.isProcessFinished = true;
                    //$scope.getVehicleRegistrationList();

                    $rootScope.stopSpinner();

                }, function(error) {

                    $rootScope.isUpdating = false;
                    $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    $scope.isProcessFinished = true;
                    $scope.errorHandler(error);
                });
            }

        }

        $scope.vehiclePortDeny = function() {

            if ($scope.parking === undefined) {
                $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(1000) + ".";
                return;
            }

            if (!$scope.formData.reasonDeny) {
                $scope.searchVehicleRegErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_DENIAL_REASON_MESSAGE + ".";
                return;
            }

            $rootScope.showGenericConfirmationModal($rootScope.translation.KEY_SCREEN_CONFIRM_ACTION_MESSAGE + '?', function() {

                $rootScope.startSpinner();
                $rootScope.isUpdating = true;

                var urlParams = {};
                urlParams = {
                    tripCode : $scope.parking.tripCode,
                    parkingCode : $scope.parking.code,
                    reasonDeny : $scope.formData.reasonDeny
                }

                portEntryService.vehiclePortDeny(urlParams, function(response) {

                    $rootScope.isUpdating = false;
                    $scope.searchVehicleRegSuccessResponse = $rootScope.translation.KEY_SCREEN_DENIAL_COMPLETE_MESSAGE + ".";
                    $scope.isProcessFinished = true;
                    $scope.isSendReasonDeny = true;
                    $rootScope.stopSpinner();
                    //$scope.getVehicleRegistrationList();

                }, function(error) {

                    $rootScope.isUpdating = false;
                    $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    $scope.isProcessFinished = true;
                    $scope.errorHandler(error);
                });
            });
        };

        $scope.resetRegSearch = function() {
            $scope.isVehicleRegFound = false;
            $scope.proceedEntry = false;
            $scope.searchVehicleRegErrorResponse = "";
            $scope.formData.vehicleRegNumber = "";
            $scope.divRegNumExitClass = "";
            $scope.spanRegNumExitClass = "";
            $scope.parking = null;
            $scope.isDeny = false;
            $scope.isProcessFinished = false;
            $scope.searchVehicleRegSuccessResponse = "";
        }

        $scope.back = function() {
            if ($scope.isDeny) {
                $scope.isDeny = false;
                $scope.searchVehicleRegErrorResponse = "";
            } else {
                $scope.resetRegSearch();
            }
        }

        $rootScope.stopSpinner();

    } ]);
