padApp.controller('ParkingExitOnlyController', [ '$scope', '$rootScope', '$interval', 'parkingService', function($scope, $rootScope, $interval, parkingService) {

    $rootScope.activeNavbar('#navbarExitOnly');

    $rootScope.startSpinner();

    $scope.formData = {};
    $scope.addParkingExitOnlyErrorResponse = "";
    $scope.addParkingExitOnlySuccessResponse = "";
    $scope.addParkingExitOnlySubmitButtonDisabled = false;
    $scope.isProcessFinished = false;

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.addParkingExitOnly = function() {

        if ($scope.parkingExitOnlyForm.vehicleRegNumber.$invalid) {
            $scope.addParkingExitOnlyErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";
            $scope.vehicleRegNumberDivClass = "has-error has-feedback";
            $scope.vehicleRegNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";

        } else if ($scope.parkingExitOnlyForm.mobileNumber.$invalid) {
            $scope.addParkingExitOnlyErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";
            $scope.mobileNumberDivClass = "has-error has-feedback";
            $scope.mobileNumberSpanClass = "glyphicon glyphicon-remove form-control-feedback";

        } else {
            $rootScope.startSpinner();
            $scope.addParkingExitOnlyErrorResponse = "";
            $scope.addParkingExitOnlySubmitButtonDisabled = true;

            urlParams = {
                vehicleRegistration : $scope.formData.vehicleRegNumber,
                vehicleColor : $scope.formData.color,
                driverMobile : $scope.formData.mobileNumber,
                entryLaneId : $rootScope.kioskSessionLaneId
            };

            parkingService.saveParkingExitOnly(urlParams, function(response) {

                $rootScope.stopSpinner();
                $scope.addParkingExitOnlyErrorResponse = "";
                $scope.addParkingExitOnlySuccessResponse = $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL;

                $scope.isProcessFinished = true;

            }, function(error) {

                $scope.addParkingExitOnlySubmitButtonDisabled = false;
                $scope.addParkingExitOnlyErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";

                $scope.isProcessFinished = true;

                $scope.errorHandler(error);
            });
        }
    };

    $scope.resetRegSearch = function() {
        $scope.formData = {};
        $scope.addParkingExitOnlySuccessResponse = "";
        $scope.addParkingExitOnlyErrorResponse = "";
        $scope.addParkingExitOnlySubmitButtonDisabled = false;
        $scope.isProcessFinished = false;
    };

    $rootScope.stopSpinner();

} ]);
