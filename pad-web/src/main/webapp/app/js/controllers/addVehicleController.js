padApp.controller('AddVehicleController', [ '$scope', '$rootScope', '$filter', 'usSpinnerService', 'vehicleService',
    function($scope, $rootScope, $filter, usSpinnerService, vehicleService) {
        
        $rootScope.activeNavbar('#navbarVehicle');
    
        $scope.formData = {};
        $rootScope.stopSpinner();
        $scope.addVehicleErrorResponse = "";
        $scope.addVehicleSubmitButtonDisabled = false;

        $scope.addVehicle = function() {

            if ($scope.vehicleAddForm.regCountry.$invalid) {
                $scope.addVehicleErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_MESSAGE + ".";

            } else if ($scope.vehicleAddForm.regNumber.$invalid) {
                $scope.addVehicleErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";

            } else if ($scope.formData.make === undefined || $scope.formData.make === null || $scope.formData.make === "") {
                $scope.addVehicleErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VEHICLE_MAKE_MESSAGE + ".";

            } else {
                $rootScope.startSpinner();
                $scope.addVehicleErrorResponse = "";
                $scope.addVehicleSubmitButtonDisabled = true;

                var vehicleData = {
                    registrationCountryISO : $scope.formData.regCountry,
                    vehicleRegistration : $scope.formData.regNumber,
                    make : $scope.formData.make,
                    color : $scope.formData.color
                };

                vehicleService.saveVehicle(vehicleData, function(response) {

                    $rootScope.stopSpinner();
                    $scope.addVehicleErrorResponse = "";
                    $rootScope.go('/vehicle');

                }, function(error) {

                    $rootScope.stopSpinner();
                    $scope.addVehicleSubmitButtonDisabled = false;
                    $scope.addVehicleErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

} ]);
