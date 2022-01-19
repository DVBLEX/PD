padApp.controller('InvoiceController', [ '$scope', '$rootScope', '$timeout', 'invoiceService', 'accountService', function($scope, $rootScope, $timeout, invoiceService, accountService) {

    $rootScope.activeNavbar('#navbarInvoice');

    $rootScope.startSpinner();

    $scope.filterAccountNumber = "";
    $scope.filterType = "";
    $scope.searchSubmitButtonDisabled = false;
    $scope.accountNamesList = [];
    $scope.confirmActionErrorResponse = "";
    $scope.sortColumn = "";
    $scope.sortAsc = true;

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {

        $rootScope.startSpinner();

        //get Data count

        var urlParams = {
            accountNumber : $scope.filterAccountNumber,
            type : $scope.filterType
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        invoiceService.countInvoices(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        $rootScope.startSpinner();

        var urlParams = {
            accountNumber : $scope.filterAccountNumber,
            type  : $scope.filterType,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        invoiceService.listInvoices(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.getCallBack = function(response) {
        $scope.padTable.setData(response.dataList);
        $scope.searchSubmitButtonDisabled = false;
        $rootScope.stopSpinner();
    }

    $scope.getCountCallBack = function(response) {
        $scope.padTable.setCount(response.dataList[0]);
        $scope.searchSubmitButtonDisabled = false;
        $rootScope.stopSpinner();
    }

    $scope.refreshTableData = function() {
        $scope.padTable.reloadTable();
    }
    
    $scope.sort = function(sortColumn){
    	
    	if($scope.sortColumn === sortColumn){
    		$scope.sortAsc = !$scope.sortAsc;
    	}else{
    		$scope.sortAsc = true;
    	}
    	
    	$scope.sortColumn = sortColumn;
        
        $scope.refreshTableData();
    }

    $scope.downloadInvoice = function(row) {

        row.downloaded = true;
        
        $timeout(function(){
            row.downloaded = false;
        }, 60000);
    }

    $scope.search = function() {

        $scope.searchSubmitButtonDisabled = true;
        $scope.refreshTableData();
    };

    $scope.clearSearch = function() {

        $scope.filterAccountNumber = "";
        $scope.filterType = "";
        $scope.filtersForm.$setPristine();

        $scope.refreshTableData();
    };

    $scope.getActiveAccountNames = function() {

        accountService.getActiveAccountNames({}, function(response) {

            $scope.accountNamesList = response.dataMap;

        }, function(error) {

            $scope.getCallBackError(error);
        });
    };

    $scope.refreshTableData();
    
    if($rootScope.isFinanceOperator == "true"){
        $scope.getActiveAccountNames();
    }

    $rootScope.stopSpinner();

} ]);
