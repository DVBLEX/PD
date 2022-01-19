padApp.controller('AnprController', [ '$scope', '$rootScope', 'usSpinnerService', 'anprService', function($scope, $rootScope, usSpinnerService, anprService) {

    $rootScope.activeNavbar('#navbarAnpr');
    $rootScope.startSpinner();

    $scope.anprParameter = null;
    $scope.anprFormErrorResponse = "";
    $scope.disableSubmitButton = false;

    anprService.getAnprParameter({}, function(response) {
        $scope.anprParameter = response.data;
    }, function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    });

    $scope.update = function() {

        $rootScope.startSpinner();
        $scope.anprFormErrorResponse = "";
        $scope.disableSubmitButton = true;

        var anprParameterData = {
            entryLogPageSize : $scope.anprParameter.entryLogPageSize,
            entryLogConnectTimeout : $scope.anprParameter.entryLogConnectTimeout,
            entryLogSocketTimeout : $scope.anprParameter.entryLogSocketTimeout,
            entryLogConnRequestTimeout : $scope.anprParameter.entryLogConnRequestTimeout,
            defaultConnectTimeout : $scope.anprParameter.defaultConnectTimeout,
            defaultSocketTimeout : $scope.anprParameter.defaultSocketTimeout,
            defaultConnRequestTimeout : $scope.anprParameter.defaultConnRequestTimeout,
            parkingPermissionHoursInFuture : $scope.anprParameter.parkingPermissionHoursInFuture,
            parkingPermissionHoursPriorSlotDate : $scope.anprParameter.parkingPermissionHoursPriorSlotDate,
            parkingPermissionHoursAfterSlotDate : $scope.anprParameter.parkingPermissionHoursAfterSlotDate,
            parkingPermissionHoursAfterExitDate : $scope.anprParameter.parkingPermissionHoursAfterExitDate,
            btDowntimeSecondsLimit : $scope.anprParameter.btDowntimeSecondsLimit,
            btUptimeSecondsLimit : $scope.anprParameter.btUptimeSecondsLimit,
            agsparkingEntryLane1VideoFeedUrl: $scope.anprParameter.agsparkingEntryLane1VideoFeedUrl,
            agsparkingEntryLane2VideoFeedUrl: $scope.anprParameter.agsparkingEntryLane2VideoFeedUrl,
            agsparkingEntryLane3VideoFeedUrl: $scope.anprParameter.agsparkingEntryLane3VideoFeedUrl,
            agsparkingEntryLane4VideoFeedUrl: $scope.anprParameter.agsparkingEntryLane4VideoFeedUrl,
            agsparkingEntryLane5VideoFeedUrl: $scope.anprParameter.agsparkingEntryLane5VideoFeedUrl
        };

        anprService.updateAnprParameter(anprParameterData, function(response) {

            $scope.anprFormErrorResponse = "";
            $scope.disableSubmitButton = false;
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ANPR_PARAMETER_UPDATED_SUCCESS_MESSAGE + ".");
            $rootScope.stopSpinner();

        }, function(error) {

            $rootScope.stopSpinner();
            $scope.anprFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            $scope.disableSubmitButton = false;
        });
    }

    $rootScope.stopSpinner();

} ]);
