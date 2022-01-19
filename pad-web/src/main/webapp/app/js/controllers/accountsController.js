padApp.controller('AccountsController', ['$scope', '$rootScope', '$filter', '$interval', 'accountService', function ($scope, $rootScope, $filter, $interval, accountService) {

    $rootScope.activeNavbar('#navbarAccounts');

    $rootScope.startSpinner();

    $scope.formData = {};
    $scope.account = null;
    $scope.selectedRow = {};
    $scope.accountActivateDisabled = false;
    $scope.accountActivateErrorResponse = "";
    $scope.activeTab = "account";
    $scope.tabContent = "app/views/financeoperator/accountTab.html";

    $scope.valueStatus  = $rootScope.accountStatusConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION.toString();

    $scope.errorHandler = function (error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.closeAccountActivateConfirmationDialog = function () {

        $scope.accountActivateDisabled = false;
        $scope.accountActivateErrorResponse = "";
        $scope.selectedRow = {};
        $scope.formData = {};

        $('#accountActivateConfirmationModal').modal('hide');
    };

    $scope.showAccountActivateConfirmationDialog = function (row) {

        $scope.isActivateAccount = true;
        $scope.confirmationDialogTitle = $rootScope.translation.KEY_SCREEN_ACCOUNT_ACTIVATION_LABEL;
        $scope.selectedRow = row;

        $('#accountActivateConfirmationModal').modal('show');
    };

    $scope.showAccountDenialConfirmationDialog = function (row) {

        $scope.isActivateAccount = false;
        $scope.confirmationDialogTitle = $rootScope.translation.KEY_SCREEN_ACCOUNT_DENIAL_LABEL;
        $scope.selectedRow = row;

        $('#accountActivateConfirmationModal').modal('show');
    };

    $scope.refreshAccountTable = function () {
        $rootScope.$broadcast('refreshAccountTable');
    }

    $scope.activateAccount = function (accountCode) {
        var minAmount = parseInt($rootScope.maximumOverdraftLimitMinAmount, 10);
        var maxAmount = parseInt($rootScope.maximumOverdraftLimitMaxAmount, 10);

        if ($scope.accountActivateConfirmationForm.paymentTermsType.$invalid) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PAYMENT_TERMS_TYPE_MESSAGE + ".";

        } else if ($scope.accountActivateConfirmationForm.amountOverdraftLimit.$invalid) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_MAX_AMOUNT_OVERDRAFT_LIMIT_MESSAGE + ".";

        } else if ($scope.formData.amountOverdraftLimit < minAmount) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_MIN_AMOUNT_MESSAGE + $filter('customCurrency')(minAmount, 0) + ".";

        } else if ($scope.formData.amountOverdraftLimit > maxAmount) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_MAX_AMOUNT_MESSAGE + $filter('customCurrency')(maxAmount, 0) + ".";

        } else if ($scope.formData.amountOverdraftLimit % 500 !== 0) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_AMOUNT_MULTIPLE_500_MESSAGE + ".";

        } else {

            $scope.accountActivateErrorResponse = "";
            $scope.accountActivateDisabled = true;

            urlParams = {
                code: accountCode,
                paymentTermsType: $scope.formData.paymentTermsType,
                amountOverdraftLimit: $scope.formData.amountOverdraftLimit
            };

            accountService.activateAccount(urlParams, function (response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_ACTIVATED_MESSAGE + ".");

                $scope.closeAccountActivateConfirmationDialog();
                $scope.refreshAccountTable() ;

            }, function (error) {

                $scope.accountActivateDisabled = false;
                $scope.errorHandler(error);
                $scope.refreshAccountTable() ;

            });
        }
    };

    $scope.denyAccount = function (accountCode) {
        if ($scope.accountActivateConfirmationForm.denialReason.$invalid) {
            $scope.accountActivateErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_DENIAL_REASON_MESSAGE + ".";

        } else {
            $scope.accountActivateErrorResponse = "";
            $scope.accountActivateDisabled = true;

            urlParams = {
                code: accountCode,
                denialReason: $scope.formData.denialReason
            };

            accountService.denyAccount(urlParams, function (response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DENIED_MESSAGE + ".");

                $scope.closeAccountActivateConfirmationDialog();
                $scope.refreshAccountTable();

            }, function (error) {

                $scope.accountActivateDisabled = false;
                $scope.errorHandler(error);
                $scope.refreshAccountTable();
            });
        }
    };

    $scope.tabsAllowed = function () {
        return !!($scope.account && ($scope.account.status === $rootScope.accountStatusConstants.ACCOUNT_STATUS_ACTIVE || $scope.account.status === $rootScope.accountStatusConstants.ACCOUNT_STATUS_INACTIVE));
    };

    $scope.switchTab = function (tabName) {
        $scope.activeTab = tabName;
        $scope.tabContent = "app/views/financeoperator/" + tabName + "Tab.html";
        if (tabName !== "account") {

        }
    };

    $rootScope.stopSpinner();

}]);
