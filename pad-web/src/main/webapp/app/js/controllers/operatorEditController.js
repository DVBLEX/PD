padApp.controller('OperatorEditController', [ '$scope', '$rootScope', 'usSpinnerService', 'operatorService', 'operatorFactory',
    function($scope, $rootScope, usSpinnerService, operatorService, operatorFactory) {

        $rootScope.activeNavbar('#navbarOperator');
        $rootScope.startSpinner();

        $scope.emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;
        $scope.nameRegexp = /^[a-zA-Z .'-]*$/;
        $scope.operator = operatorFactory.getOperator();
        $scope.action = angular.isDefined($scope.operator.code) ? 'Edit' : 'Add';
        $scope.operatorFormErrorResponse = "";
        $scope.disableSubmitButton = false;
        $scope.independentOperators = [];
        $scope.independentOperatorName = "";
        $scope.independentOperatorCode = "";

        if ($scope.action === "Edit" && $scope.operator.independentPortOperatorCode){

            const independentPortOperatorObj = $rootScope.getIndependentOperator($scope.operator.portOperatorId, $scope.operator.independentPortOperatorCode);

            if (independentPortOperatorObj) {
                $scope.independentOperatorName = independentPortOperatorObj.label;
                $scope.independentOperatorCode = independentPortOperatorObj.value;
            } else {
                return null;
            }

        }

        $scope.save = function() {

            if ($scope.operatorForm.firstName.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

            } else if (!$scope.nameRegexp.test($scope.operator.firstName)) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_FIRST_NAME_VALIDATION_MESSAGE + ".";

            } else if ($scope.operatorForm.lastName.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

            } else if (!$scope.nameRegexp.test($scope.operator.lastName)) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_LAST_NAME_VALIDATION_MESSAGE + ".";

            } else if ($scope.operatorForm.email.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_EMAIL_MESSAGE + ".";

            } else if ($scope.operatorForm.msisdn.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";

            } else if ($scope.operatorForm.role.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_OPERATOR_ROLE_MESSAGE + ".";

            } else if ($scope.action === 'Add' && parseInt($scope.operator.role) == $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_OPERATOR
                && $scope.operatorForm.portOperator.$invalid) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_MESSAGE + ".";

            } else if (parseInt($scope.operator.role) == $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_OPERATOR && $scope.independentOperators.length !== 0 &&
                ($scope.independentOperatorName === undefined || $scope.independentOperatorName === null || $scope.independentOperatorName === '')) {
                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

            } else if (parseInt($scope.operator.role) == $rootScope.operatorRoleConstants.OPERATOR_ROLE_PORT_OPERATOR && $scope.independentOperators.length !== 0 &&
                ($scope.independentOperatorCode === undefined || $scope.independentOperatorCode === null || $scope.independentOperatorCode === '')) {

                $scope.operatorFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

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
                        msisdn : $scope.operator.msisdn,
                        roleId : parseInt($scope.operator.role),
                        independentPortOperatorCode : $scope.independentOperatorCode,
                        portOperatorId : $scope.operator.portOperator,
                        language : $scope.operator.language
                    };

                    operatorService.addOperator(operatorData, function(response) {

                        $scope.operatorFormErrorResponse = "";
                        $scope.disableSubmitButton = false;
                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_OPERATOR_ADDED_SUCCESS_MESSAGE + ".");
                        $rootScope.stopSpinner();
                        $rootScope.go('/operator');

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
                        msisdn : $scope.operator.msisdn,
                        active : ($scope.operator.isActive === "true" ? true : false)
                    };

                    operatorService.updateOperator(operatorData, function(response) {

                        $scope.operatorFormErrorResponse = "";
                        $scope.disableSubmitButton = false;
                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_OPERATOR_UPDATED_SUCCESS_MESSAGE + ".");
                        $rootScope.stopSpinner();
                        // $rootScope.go('/operator');

                    }, function(error) {

                        $rootScope.stopSpinner();
                        $scope.operatorFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.disableSubmitButton = false;
                    });
                }
            }
        };

        $scope.unlockOperator = function() {

            if ($scope.operator !== undefined && $scope.operator !== null && $scope.operator.code !== undefined &&  $scope.operator.code !== null && $scope.operator.code !== "") {

                $scope.disableSubmitButton = true;

                var operatorData = {
                    code : $scope.operator.code
                };

                operatorService.unlockOperator(operatorData, function(response) {

                    $scope.operatorFormErrorResponse = "";
                    $scope.disableSubmitButton = false;
                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_OPERATOR_UNLOCKED_MESSAGE + ".");
                    $rootScope.stopSpinner();
                    $rootScope.go('/operator');

                }, function(error) {

                    $rootScope.stopSpinner();
                    $scope.disableSubmitButton = false;
                });
            }
        };

        $scope.$watch('operator.portOperator', function() {

            if ($scope.operator.portOperator !== undefined && $scope.operator.portOperator !== null && $scope.operator.portOperator !== "") {
                 $scope.independentOperators = $rootScope.getIndependentOperatorsTM($scope.operator.portOperator);

                if ($scope.action === "Add") {
                    $scope.independentOperatorName = "";
                    $scope.independentOperatorCode = "";
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
                        $scope.independentOperatorName = ui.item.label;
                        $scope.independentOperatorCode = ui.item.value;
                    }
                });

            }
        });

        $rootScope.stopSpinner();

} ]);
