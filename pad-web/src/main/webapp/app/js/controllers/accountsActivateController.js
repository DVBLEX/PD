padApp.controller('AccountsActivateController', [ '$scope', '$rootScope', '$filter', '$interval', 'accountService', function($scope, $rootScope, $filter, $interval, accountService) {

    $rootScope.activeNavbar('#navbarAccountsActivate');

    $rootScope.startSpinner();

    $scope.filterAccountNumber = "";
    $scope.filterCompanyName = "";
    $scope.filterIndividualMobileNumber = "";
    $scope.filterStatus = null;
    $scope.searchAccountSubmitButtonDisabled = false;
    $scope.sortColumn = "";
    $scope.sortAsc = true;

    $scope.errorHandler = function(error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {

        $rootScope.startSpinner();

        var urlParams = {
            number : $scope.filterAccountNumber,
            status : $scope.filterStatus,
            companyName : $scope.filterCompanyName,
            msisdn : $scope.filterIndividualMobileNumber // msisdn is only available if account is of type individual
        };

        accountService.countFilteredAccounts(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        var urlParams = {
            number : $scope.filterAccountNumber,
            status : $scope.filterStatus,
            companyName : $scope.filterCompanyName,
            msisdn : $scope.filterIndividualMobileNumber, // msisdn is only available if account is of type individual
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        accountService.listFilteredAccounts(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.getCallBack = function(response) {
        $scope.padTable.setData(response.dataList);
        $scope.searchAccountSubmitButtonDisabled = false;
        $rootScope.stopSpinner();
    }

    $scope.getCountCallBack = function(response) {
        $scope.padTable.setCount(response.dataList[0]);
        $scope.searchAccountSubmitButtonDisabled = false;
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

    $scope.searchAccount = function() {

        $scope.searchAccountSubmitButtonDisabled = true;
        $scope.refreshTableData();
    };

    $scope.clearSearchAccount = function() {

        $scope.filterStatus = null;
        $scope.filterAccountNumber = "";
        $scope.filterCompanyName = "";
        $scope.filterIndividualMobileNumber = "";
        $scope.accountFiltersForm.$setPristine();
        $scope.refreshTableData();
    };

    $scope.refreshTableData();

    $rootScope.stopSpinner();

} ]);
