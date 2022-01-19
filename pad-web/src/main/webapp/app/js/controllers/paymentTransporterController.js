padApp.controller('PaymentTransporterController', [ '$scope', '$rootScope', '$timeout', 'receiptService', 'accountService', function($scope, $rootScope, $timeout, receiptService, accountService) {

    $rootScope.activeNavbar('#navbarPaymentTransporter');

    $rootScope.startSpinner();

    $scope.searchSubmitButtonDisabled = false;
    $scope.sortColumn = "";
    $scope.sortAsc = true;    

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {

        $rootScope.startSpinner();

        //get Data count

        var urlParams = {};

        receiptService.count(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        $rootScope.startSpinner();

        var urlParams = {
        	sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        receiptService.list(urlParams, $scope.getCallBack, $scope.errorHandler);
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

    $scope.downloadReceipt = function(row) {

        row.downloaded = true;
        
        $timeout(function(){
            row.downloaded = false;
        }, 60000);
    }

    $scope.search = function() {

        $scope.searchSubmitButtonDisabled = true;
        $scope.refreshTableData();
    };

    $scope.refreshTableData();
    
    if($rootScope.isAdmin == "true"){
        $scope.getActiveAccountNames();
    }

    $rootScope.stopSpinner();

} ]);
