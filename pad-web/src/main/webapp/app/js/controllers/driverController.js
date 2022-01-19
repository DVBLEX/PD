padApp.controller('DriverController', [ '$scope', '$rootScope', 'driverService', function($scope, $rootScope, driverService) {

    $rootScope.activeNavbar('#navbarDriver');
    
    $scope.filterFirstName = "";
    $scope.filterLastName = "";
    $scope.filterEmail = "";
    $scope.filterMsisdn = "";
    $scope.filterLicenceNumber = "";
    $scope.confirmActionErrorResponse = "";
    $scope.selectedRow = null;
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
            firstName : $scope.filterFirstName,
            lastName : $scope.filterLastName,
            email : $scope.filterEmail,
            msisdn : $scope.filterMsisdn,
            licenceNumber : $scope.filterLicenceNumber
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        driverService.countDrivers(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        //get Data
        $scope.sortType = null;
        $scope.sortReverse = false;

        var urlParams = {
            firstName : $scope.filterFirstName,
            lastName : $scope.filterLastName,
            email : $scope.filterEmail,
            msisdn : $scope.filterMsisdn,
            licenceNumber : $scope.filterLicenceNumber,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        driverService.listDrivers(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.getCallBack = function(response) {
        $scope.padTable.setData(response.dataList);
        $rootScope.stopSpinner();
    }

    $scope.getCountCallBack = function(response) {
        $scope.padTable.setCount(response.dataList[0]);
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

    $scope.showConfirmationModal = function(row) {

        $('#confirmationModal').modal('show');

        $scope.selectedRow = row;
    };

    $scope.closeConfirmationDialog = function() {

        $scope.confirmActionErrorResponse = "";
        $scope.selectedRow = null;

        $('#confirmationModal').modal('hide');
    };

    $scope.removeDriverAssociation = function() {

        if ($scope.selectedRow !== null && $scope.selectedRow.status === $rootScope.driverAssociationStatusConstants.DRIVER_ASSOCIATION_STATUS_APPROVED) {

            var urlParams = {
                    code : $scope.selectedRow.code
            };

            driverService.removeDriverAssociation(urlParams, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");
                
                $scope.closeConfirmationDialog();
                $scope.refreshTableData();

            }, function(error) {

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");

                $scope.errorHandler(error);
            });
            
        } else {
            $scope.closeConfirmationDialog();
        }
    }

    $scope.refreshTableData();

} ]);