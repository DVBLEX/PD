padApp.controller('AddDriverController', [ '$scope', '$rootScope', 'driverService', function($scope, $rootScope, driverService) {

    $rootScope.activeNavbar('#navbarDriver');
    $rootScope.startSpinner();

    $scope.formData = {};
    $scope.addDriverErrorResponse = "";
    $scope.addDriverSubmitButtonDisabled = false;
    $scope.emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;

    $scope.addDriver = function() {

        if ($scope.driverAddForm.firstName.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

        } else if ($scope.driverAddForm.lastName.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

        } else if ($scope.driverAddForm.email.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_EMAIL_MESSAGE + ".";

        } else if ($scope.driverAddForm.mobileNumber.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";

        } else if ($scope.driverAddForm.issuingCountry.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_ISSUE_MESSAGE + ".";

        } else if ($scope.driverAddForm.licenceNumber.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LICENCE_NUMBER_MESSAGE + ".";

        } else if ($scope.driverAddForm.languageId.$invalid) {
            $scope.addDriverErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_LANGUAGE_MESSAGE + ".";

        } else {
            $rootScope.startSpinner();
            $scope.addDriverErrorResponse = "";
            $scope.addDriverSubmitButtonDisabled = true;

            var driverData = {
                firstName : $scope.formData.firstName,
                lastName : $scope.formData.lastName,
                email : $scope.formData.email,
                msisdn : $scope.formData.mobileNumber,
                issuingCountryISO : $scope.formData.issuingCountry,
                licenceNumber : $scope.formData.licenceNumber,
                languageId : $scope.formData.languageId
            };

            driverService.saveDriver(driverData, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_DRIVER_ADDED_SUCCESS_MESSAGE + ".");
                $rootScope.stopSpinner();

                $scope.addDriverErrorResponse = "";
                $scope.addDriverSubmitButtonDisabled = false;

                $rootScope.go('/driver');

            }, function(error) {

                $rootScope.stopSpinner();

                $scope.addDriverSubmitButtonDisabled = false;
                $scope.addDriverErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });
        }
    };

    $rootScope.stopSpinner();

} ]);