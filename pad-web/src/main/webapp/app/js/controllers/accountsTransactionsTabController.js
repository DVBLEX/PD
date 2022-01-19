padApp.controller('AccountsTransactionsTabController', ['$scope', '$rootScope', '$filter', '$interval', 'accountService', function ($scope, $rootScope, $filter, $interval, accountService) {

    $rootScope.startSpinner();

    $scope.filterDateFromString = "";
    $scope.filterDateToString = "";
    $scope.sortColumn = "";
    $scope.sortAsc = true;

    $scope.errorHandler = function (error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.initiateDatepickers = function () {

        $('.filter_date_from.date').datepicker('destroy');
        $('.filter_date_to.date').datepicker('destroy');

        var currentDate = new Date();
        var dateFirstOfLastMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);

        var dateFirstOfYear = new Date(currentDate.getFullYear() - 1 , 0, 1);

        $('.filter_date_from.date').datepicker({
            format: "dd/mm/yyyy",
            defaultViewDate: dateFirstOfLastMonth,
            startDate: dateFirstOfYear,
            endDate : currentDate,
            setDate: dateFirstOfLastMonth,
            todayBtn: "linked",
            autoclose: true,
            todayHighlight: true,
            orientation: 'bottom',
            language: $filter('lowercase')($rootScope.language)
        })

        $('.filter_date_to.date').datepicker({
            format: "dd/mm/yyyy",
            defaultViewDate: currentDate,
            startDate: dateFirstOfLastMonth,
            setDate: currentDate,
            todayBtn: "linked",
            autoclose: true,
            todayHighlight: true,
            orientation: 'bottom',
            language: $filter('lowercase')($rootScope.language)
        }).on('hide', function (ev) {
            if (!ev.date) {
                $("#filterDateToString").val($scope.filterDateToString);
            }
        });

        $(".filter_date_from.date").datepicker("update", dateFirstOfLastMonth);
        $(".filter_date_to.date").datepicker("update", currentDate);

        $scope.filterDateFromString = $filter('date')(dateFirstOfLastMonth, "dd/MM/yyyy");
        $scope.filterDateToString = $filter('date')(currentDate, "dd/MM/yyyy");

        $("#filter_date_from").val($filter('date')(dateFirstOfLastMonth, "dd/MM/yyyy"));
        $("#filter_date_to").val($filter('date')(currentDate, "dd/MM/yyyy"));
    };

    $scope.initiateDatepickers();

    $scope.$watch('filterDateFromString', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            $('.filter_date_to.date').datepicker("setStartDate", newVal);
        }
    }, true);

    $scope.$watch('filterDateToString', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            $('.filter_date_from.date').datepicker("setEndDate", newVal);
        }
    }, true);

    $scope.padTable = new PADTable(function () {

        $rootScope.startSpinner();

        var urlParams = {
            accountCode: $scope.account.code,
            dateFromString: $scope.filterDateFromString,
            dateToString: $scope.filterDateToString,
        };

        accountService.countAccountStatements(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function (currentPage, pageCount) {

        var urlParams = {
            accountCode: $scope.account.code,
            dateFromString: $scope.filterDateFromString,
            dateToString: $scope.filterDateToString,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage: currentPage,
            pageCount: pageCount
        };

        accountService.listAccountStatements(urlParams, $scope.getCallBack, $scope.errorHandler);
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

    $scope.searchStatements = function () {
        $scope.refreshTableData();
    };

    $scope.clearSearchStatements = function () {
        $scope.initiateDatepickers();
        $scope.padTable.setCount(0);
        $scope.refreshTableData();
    };


    if ($scope.$parent.$parent.account != null) {
        $scope.account = $scope.$parent.$parent.account;
        $scope.refreshTableData();
    }


    $rootScope.stopSpinner();

}]);
