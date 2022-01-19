padApp.controller('OperatorTransporterEditController', [ '$scope', '$rootScope', 'usSpinnerService', 'operatorService', 'operatorFactory',
    function($scope, $rootScope, usSpinnerService, operatorService, operatorFactory) {

        $rootScope.activeNavbar('#navbarOperator');
        $rootScope.startSpinner();

        $scope.emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;
        $scope.operator = operatorFactory.getOperator();
        $scope.action = angular.isDefined($scope.operator.code) ? 'Edit' : 'Add';
        $scope.operatorFormErrorResponse = "";
        $scope.disableSubmitButton = false;

        $scope.save = function() {

            if ($scope.operatorForm.firstName.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

            } else if ($scope.operatorForm.lastName.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

            } else if ($scope.operator.email !== undefined && $scope.operator.email !== "" && $scope.operatorForm.email.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_EMAIL_MESSAGE + ".";

            } else if ($scope.action === 'Add' && parseInt($scope.operator.role) == $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_OPERATOR 
                && $scope.operatorForm.portOperator.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_MESSAGE + ".";

            } else if ($scope.action === 'Add' && $scope.operatorForm.language.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_LANGUAGE_MESSAGE + ".";

            } else {
                $rootScope.startSpinner();
                $scope.operatorFormErrorResponse = "";
                $scope.disableSubmitButton = true;

                if ($scope.action === 'Add') {
                 
                    var operatorData = {
                        firstName : $scope.operator.firstName,
                        lastName : $scope.operator.lastName,
                        email : ($scope.operator.email === undefined ? "" : $scope.operator.email),
                        msisdn : ($scope.operator.msisdn === undefined ? "" : $scope.operator.msisdn),
                        roleId : parseInt($scope.operator.role),
                        portOperatorId : $scope.operator.portOperator,
                        language : $scope.operator.language
                    };

                    operatorService.addOperator(operatorData, function(response) {

                        $scope.operatorFormErrorResponse = "";
                        $scope.disableSubmitButton = false;
                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_USER_ACCOUNT_ADDED_SUCCESS_MESSAGE + ".");
                        $rootScope.stopSpinner();
                        $rootScope.go('/operatorTransporter');

                    }, function(error) {

                        $rootScope.stopSpinner();
                        $scope.operatorFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.disableSubmitButton = false;
                    });

                } else {

                    var operatorData = {
                        code : $scope.operator.code,
                        firstName : $scope.operator.firstName,
                        lastName : $scope.operator.lastName,
                        email : ($scope.operator.email === undefined ? "" : $scope.operator.email),
                        msisdn : ($scope.operator.msisdn === undefined ? "" : $scope.operator.msisdn),
                        active : ($scope.operator.isActive === "true" ? true : false)
                    };

                    operatorService.updateOperator(operatorData, function(response) {

                        $scope.operatorFormErrorResponse = "";
                        $scope.disableSubmitButton = false;
                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_USER_ACCOUNT_UPDATED_SUCCESS_MESSAGE + ".");
                        $rootScope.stopSpinner();

                    }, function(error) {

                        $rootScope.stopSpinner();
                        $scope.operatorFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.disableSubmitButton = false;
                    });
                }
            }
        }

        $rootScope.stopSpinner();

} ]);