padApp.controller('AccountsInvoicesTabController', ['$scope', '$rootScope', '$timeout', '$filter', 'accountService', function ($scope, $rootScope, $timeout, $filter, accountService) {

    $rootScope.startSpinner();

    $scope.sortColumn = "";
    $scope.sortAsc = true;

    var currentDate = new Date();
    var dateFirstOfYear = new Date(currentDate.getFullYear() - 1 , 0, 1);
    $scope.filterDateFromString = $filter('date')(dateFirstOfYear, "dd/MM/yyyy");

    $scope.errorHandler = function (error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function () {

        $rootScope.startSpinner();

        var urlParams = {
            accountCode: $scope.account.code,
            dateFromString: $scope.filterDateFromString
        };

        accountService.countAccountInvoices(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function (currentPage, pageCount) {

        var urlParams = {
            accountCode: $scope.account.code,
            dateFromString: $scope.filterDateFromString,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage: currentPage,
            pageCount: pageCount
        };

        accountService.listAccountInvoices(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.getCallBack = function (response) {

        $scope.padTable.setData(response.dataList);
        $rootScope.stopSpinner();
    };

    $scope.getCountCallBack = function (response) {
        $scope.padTable.setCount(response.dataList[0]);
        $rootScope.stopSpinner();
    };

    $scope.refreshTableData = function () {
        $scope.padTable.reloadTable();
    };

    $scope.sort = function (sortColumn) {

        if ($scope.sortColumn === sortColumn) {
            $scope.sortAsc = !$scope.sortAsc;
        } else {
            $scope.sortAsc = true;
        }

        $scope.sortColumn = sortColumn;

        $scope.refreshTableData();
    };

    $scope.downloadInvoice = function(row) {

        row.downloaded = true;

        $timeout(function(){
            row.downloaded = false;
        }, 60000);
    }


    if ($scope.$parent.$parent.account != null) {
        $scope.account = $scope.$parent.$parent.account;
        $scope.refreshTableData();
    }


    $rootScope.stopSpinner();

}]);
