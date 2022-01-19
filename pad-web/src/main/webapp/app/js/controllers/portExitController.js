padApp.controller('PortExitController', [ '$scope', '$rootScope', '$interval', 'portExitService', function($scope, $rootScope, $interval, portExitService) {

    $rootScope.startSpinner();

    $scope.formData = {};
    $scope.searchVehicleRegErrorResponse = "";
    $scope.searchVehicleRegSuccessResponse = "";
    $scope.isVehicleRegFound = false;
    $scope.proceedExit = false;
    $scope.isProcessFinished = false;

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.getVehicleRegistrationList = function() {
        portExitService.getEnteredVehicleRegistrationList({}, function(response) {
            $("#vehicleRegNumber").autocomplete({
                source : response.dataList,
                select : function(event, ui) {
                    $scope.formData.vehicleRegNumber = ui.item.value;
                    $scope.searchEnteredVehicleReg();
                }
            });
        }, $scope.errorHandler);
    };

    $scope.getVehicleRegistrationList();

    $interval(function() {
        $scope.getVehicleRegistrationList();
    }, 60000);

    $scope.searchEnteredVehicleReg = function() {

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
            vehicleRegistration : $scope.formData.vehicleRegNumber
        };

        portExitService.searchEnteredVehicleReg(urlParams, function(response) {

            $scope.portAccess = response.dataList[0];

            $scope.searchVehicleRegErrorResponse = "";
            $scope.isVehicleRegFound = true;
            $scope.proceedExit = true;
            $scope.divRegNumExitClass = "has-success has-feedback";
            $scope.spanRegNumExitClass = "glyphicon glyphicon-ok form-control-feedback";

        }, function(error) {

            $scope.isVehicleRegFound = false;
            $scope.proceedExit = true;
            $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode);
            $scope.searchVehicleRegSuccessResponse = "";
            $scope.divRegNumExitClass = "has-error has-feedback";
            $scope.spanRegNumExitClass = "glyphicon glyphicon-remove form-control-feedback";

            $scope.errorHandler(error);
        });
    }

    $scope.vehiclePortExit = function() {

        if ($scope.isVehicleRegFound) {
            urlParams = {
                code : $scope.portAccess.code
            }
        } else {
            urlParams = {
                vehicleRegistration : $scope.formData.vehicleRegNumber
            }
        }

        portExitService.vehiclePortExit(urlParams, function(response) {

            $scope.searchVehicleRegSuccessResponse = $rootScope.translation.KEY_SCREEN_PORT_EXIT_COMPLETE_MESSAGE + ".";
            $scope.getVehicleRegistrationList();
            $scope.isProcessFinished = true;

        }, function(error) {

            $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            $scope.isProcessFinished = true;
            $scope.errorHandler(error);
        });

    }

    $scope.resetRegSearch = function() {
        $scope.isVehicleRegFound = false;
        $scope.proceedExit = false;
        $scope.searchVehicleRegErrorResponse = "";
        $scope.formData.vehicleRegNumber = "";
        $scope.divRegNumExitClass = "";
        $scope.spanRegNumExitClass = "";
        $scope.portAccess = null;
        $scope.searchVehicleRegSuccessResponse = "";
        $scope.isProcessFinished = false;
    }

    $rootScope.stopSpinner();

} ]);
