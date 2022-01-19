padApp.controller('ParkingReleaseController', [ '$scope', '$rootScope', '$interval', 'parkingService', 'systemService', function($scope, $rootScope, $interval, parkingService, systemService) {

    $rootScope.activeNavbar('#navbarParkingRelease');

    $rootScope.startSpinner();

    $scope.portOperatorList = [];
    $scope.parkingReleaseStats = [];
    $scope.parkingReleaseStatsMap = null;
    $scope.isParkingSupervisorReadOnlyEnabled = null;
    $scope.disableControls = false;
    $scope.selectedPortOperator = null;
    $scope.selectedParkingReleaseStatsObject = null;
    $scope.confirmActionErrorResponse = "";

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.getParkingReleaseStats = function() {

        $rootScope.startSpinner();

        systemService.getParkingReleaseStats({}, function(response) {

            $scope.portOperatorList = response.dataList;
            $scope.parkingReleaseStatsMap = response.dataMap;
            $scope.isParkingSupervisorReadOnlyEnabled = response.data;

            $scope.disableControls = false;

            if ($scope.isParkingSupervisorReadOnlyEnabled && $rootScope.isParkingSupervisorOperator == 'true') {
                $scope.disableControls = true;
            }

            $rootScope.stopSpinner();

        }, function(error) {

            $rootScope.stopSpinner();
            $scope.disableControls = false;
        });
    };

    $scope.getParkingReleaseStatsByPortOperator = function(portOperatorId) {

        if ($scope.parkingReleaseStatsMap !== null && $scope.parkingReleaseStatsMap !== undefined) {
            $scope.parkingReleaseStats = $scope.parkingReleaseStatsMap[portOperatorId];
        }
        return $scope.parkingReleaseStats;
    };

    $scope.toggleAutoRelease = function (selectedPortOperator, selectedParkingReleaseStatsObject) {

        $rootScope.startSpinner();
        $scope.disableControls = true;

        var requestData = {};
        if (selectedParkingReleaseStatsObject.isAutoReleaseOn) {
            // turn off auto release for this port operator & transaction type combination
            requestData = {
                portOperator : selectedPortOperator.id,
                transactionType : selectedParkingReleaseStatsObject.transactionType,
                isAutoReleaseOn : false
            };
        } else if (!selectedParkingReleaseStatsObject.isAutoReleaseOn) {
            // turn on auto release for this port operator & transaction type combination
            requestData = {
                portOperator : selectedPortOperator.id,
                transactionType : selectedParkingReleaseStatsObject.transactionType,
                isAutoReleaseOn : true
            };
        }

        parkingService.toggleAutoRelease(requestData, function(response) {

            $scope.getParkingReleaseStats();
            $rootScope.stopSpinner();

        }, function(error) {

            $scope.errorHandler(error);
            $scope.disableControls = false;
        });
    };

    $scope.updateParkingSupervisorReadOnlyFlag = function() {

        $rootScope.startSpinner();
        $scope.disableControls = true;

        var requestData = {};
        if ($scope.isParkingSupervisorReadOnlyEnabled) {
            // disable read-only for parking supervisor
            requestData = {
                isParkingSupervisorReadOnlyEnabled : false
            };
        } else if (!$scope.isParkingSupervisorReadOnlyEnabled) {
            // enable read-only for parking supervisor
            requestData = {
                isParkingSupervisorReadOnlyEnabled : true
            };
        }

        parkingService.updateParkingSupervisorReadOnlyFlag(requestData, function(response) {

            $scope.isParkingSupervisorReadOnlyEnabled = response.data;
            $scope.disableControls = false;

            $rootScope.stopSpinner();

        }, function(error) {

            $scope.errorHandler(error);
            $scope.disableControls = false;
        });
    };

    $scope.showConfirmationModal = function(selectedPortOperator, selectedParkingReleaseStatsObject) {

        $('#confirmationModal').modal('show');

        $scope.selectedPortOperator = selectedPortOperator;
        $scope.selectedParkingReleaseStatsObject = selectedParkingReleaseStatsObject;
    };

    $scope.closeConfirmationDialog = function() {

        $scope.confirmActionErrorResponse = "";
        $scope.selectedPortOperator = null;
        $scope.selectedParkingReleaseStatsObject = null;

        $('#confirmationModal').modal('hide');
    };

    $scope.triggerManualRelease = function() {

        $rootScope.startSpinner();
        $scope.confirmActionErrorResponse = "";
        $scope.disableControls = true;

        if ($scope.selectedPortOperator == null || $scope.selectedParkingReleaseStatsObject == null) {
            $scope.disableControls = false;
            $rootScope.stopSpinner();
            return;

        } else {
            var requestData = {
                portOperator : $scope.selectedPortOperator.id,
                transactionType : $scope.selectedParkingReleaseStatsObject.transactionType,
                vehiclesAlreadyReleasedCount : $scope.selectedParkingReleaseStatsObject.releasedVehicleRegistrationList.length,
                bookingLimitCount : $scope.selectedParkingReleaseStatsObject.bookingLimitCount,
                releaseCount : parseInt($scope.selectedParkingReleaseStatsObject.releaseCount)
            };

            parkingService.triggerManualRelease(requestData, function(response) {

                $scope.disableControls = false;
                $rootScope.stopSpinner();

                if (response.data == 0) {
                    $scope.confirmActionErrorResponse = $rootScope.translation.KEY_SCREEN_NO_MORE_VEHICLES_TO_RELEASE_MESSAGE + ".";

                } else if (response.data > 0) {
                    $rootScope.showResultMessage(true, response.data + " " + $rootScope.translation.KEY_SCREEN_VEHICLES_WERE_MANUALLY_RELEASED_MESSAGE + " " + $scope.selectedPortOperator.name + " - "
                                                        + $rootScope.$eval('translation.' + $scope.selectedParkingReleaseStatsObject.translateKey) + ".");

                    $scope.closeConfirmationDialog();
                    $scope.getParkingReleaseStats();
                }

            }, function(error) {

                $rootScope.stopSpinner();

                $scope.confirmActionErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.disableControls = false;
            });
        }
    };

    $scope.$on('$destroy', function() {
        $interval.cancel(refreshDataTask);
    });

    $scope.getParkingReleaseStats();

    var refreshDataTask = $interval(function() {
        $scope.getParkingReleaseStats();
    }, 60000);

    $rootScope.stopSpinner();

} ]);
