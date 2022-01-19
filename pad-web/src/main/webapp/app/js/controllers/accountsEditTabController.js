padApp.controller('AccountsEditTabController', [ '$scope', '$rootScope', '$filter', 'accountService', function($scope, $rootScope, $filter, accountService) {

    $scope.disableSubmitButton = false;
    $scope.accountValue = null;

    $scope.updateEditForm = function() {

        const minAmount = parseInt($rootScope.maximumOverdraftLimitMinAmount, 10);
        const maxAmount = parseInt($rootScope.maximumOverdraftLimitMaxAmount, 10);

        if ($scope.amountOverdraftLimit < minAmount) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_MIN_AMOUNT_MESSAGE + $filter('customCurrency')(minAmount, 0) + ".";

        } else if ($scope.amountOverdraftLimit > maxAmount) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_MAX_AMOUNT_MESSAGE + $filter('customCurrency')(maxAmount, 0) + ".";

        } else if ($scope.amountOverdraftLimit % 500 !== 0) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_AMOUNT_MULTIPLE_500_MESSAGE + ".";

        } else {

            $scope.accountActivateErrorResponse = "";
            $scope.disableSubmitButton = true;

            const editData = {
                code : $scope.account.code,
                amountOverdraftLimit : $scope.amountOverdraftLimit,
				isActive : $scope.isActive
            };

            accountService.update(editData, function(response) {
                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");

                $scope.accountValue = $scope.amountOverdraftLimit;
				$scope.$parent.$parent.account.amountOverdraftLimit = $scope.amountOverdraftLimit;
				
				$scope.accountIsActiveValue = $scope.isActive;
				$scope.$parent.$parent.account.isActive = $scope.isActive;
				
                $scope.disableSubmitButton = false;

            }, function(error) {
                $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ERROR_LABEL + ".");
                $scope.disableSubmitButton = false;
            });
        }

    }

    $scope.restoreValue = function () {
        $scope.accountActivateErrorResponse = '';
        $scope.amountOverdraftLimit = $scope.accountValue;
		$scope.isActive = $scope.accountIsActiveValue;
		$scope.editForm.$setPristine();
    }

    if ($scope.$parent.$parent.account != null) {
        $scope.account = $scope.$parent.$parent.account;
        $scope.amountOverdraftLimit = $scope.$parent.$parent.account.amountOverdraftLimit;
        $scope.accountValue = $scope.$parent.$parent.account.amountOverdraftLimit;
		$scope.isActive = $scope.$parent.$parent.account.isActive + '';
        $scope.accountIsActiveValue = $scope.$parent.$parent.account.isActive + '';
    }

} ]);