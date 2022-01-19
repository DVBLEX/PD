padApp.controller('AccountsAccountTabController', ['$scope', '$rootScope', '$filter', '$interval', 'accountService', function ($scope, $rootScope, $filter, $interval, accountService) {

    $rootScope.startSpinner();

    $scope.filterAccountNumber = "";
    $scope.filterAccountName = "";
    $scope.searchAccountSubmitButtonDisabled = false;
    $scope.sortColumn = "";
    $scope.sortAsc = true;
    $scope.filterStatus = $scope.$parent.$parent.valueStatus ;

    $scope.errorHandler = function (error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function () {

        $rootScope.startSpinner();

        var urlParams = {
            number: $scope.filterAccountNumber,
            accountName: $scope.filterAccountName,
            status: $scope.filterStatus,
        };

        accountService.countFilteredAccounts(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function (currentPage, pageCount) {

        var urlParams = {
            number: $scope.filterAccountNumber,
            accountName: $scope.filterAccountName,
            status: $scope.filterStatus,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage: currentPage,
            pageCount: pageCount
        };

        accountService.listFilteredAccounts(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.getCallBack = function (response) {

        if (response.dataList.length === 1) {
            $scope.account = response.dataList[0];
            $scope.$parent.$parent.account = response.dataList[0];
        } else {
			$scope.account = null;
            $scope.$parent.$parent.account = null;
		}

        $scope.padTable.setData(response.dataList);
        $scope.searchAccountSubmitButtonDisabled = false;
        $rootScope.stopSpinner();
    }

    $scope.getCountCallBack = function (response) {
        $scope.padTable.setCount(response.dataList[0]);
        $scope.searchAccountSubmitButtonDisabled = false;
        $rootScope.stopSpinner();
    }

    $scope.refreshTableData = function () {
        $scope.padTable.reloadTable();
    }

    $scope.sort = function (sortColumn) {

        if ($scope.sortColumn === sortColumn) {
            $scope.sortAsc = !$scope.sortAsc;
        } else {
            $scope.sortAsc = true;
        }

        $scope.sortColumn = sortColumn;

        $scope.refreshTableData();
    }

    $scope.searchAccount = function () {
        $scope.searchAccountSubmitButtonDisabled = true;
        $scope.$parent.$parent.valueStatus = $scope.filterStatus;
        $scope.refreshTableData();
    };

    $scope.clearSearchAccount = function () {
        $scope.account = null;
        $scope.$parent.$parent.account = null;
        $scope.filterAccountNumber = "";
        $scope.filterAccountName = "";
        $scope.filterStatus = $rootScope.accountStatusConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION.toString();
        $scope.padTable.setCount(0);
        $scope.accountFiltersForm.$setPristine();
        $scope.refreshTableData();
    };

    $scope.selectAccount = function (row) {
        if (!$scope.account || $scope.account.number !== row.number) {
            $scope.account = row;
            $scope.$parent.$parent.account = row;
            $scope.filterAccountNumber = row.number;
            var accountList = [];
            accountList.push(row);
            $scope.padTable.setCount(1);
            $scope.padTable.setData(accountList);
            $scope.$parent.$parent.valueStatus = $scope.filterStatus ;
        }
    };

    if ($scope.$parent.$parent.account != null) {
        $scope.account = null;
        //$scope.refreshTableData();
        $scope.selectAccount($scope.$parent.$parent.account);
    }

    $scope.$on('refreshAccountTable', function () {
            $scope.refreshTableData();
    })

    $scope.refreshTableData();

    $rootScope.stopSpinner();

}]);
