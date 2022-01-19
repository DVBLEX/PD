padApp.controller('VehicleController', [ '$scope', '$rootScope', 'vehicleService', function($scope, $rootScope, vehicleService) {

    $rootScope.activeNavbar('#navbarVehicle');

    $rootScope.startSpinner();

    $scope.filterRegCountry = "";
    $scope.filterRegNumber = "";
    $scope.filterMake = "";
    $scope.filterColor = "";
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
            registrationCountryISO : $scope.filterRegCountry,
            vehicleRegistration : $scope.filterRegNumber,
            make : $scope.filterMake,
            color : $scope.filterColor
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        vehicleService.countVehicles(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        //get Data

        $rootScope.startSpinner();
        $scope.sortType = null;
        $scope.sortReverse = false;

        var urlParams = {
            registrationCountryISO : $scope.filterRegCountry,
            vehicleRegistration : $scope.filterRegNumber,
            make : $scope.filterMake,
            color : $scope.filterColor,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        vehicleService.listVehicles(urlParams, $scope.getCallBack, $scope.errorHandler);
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

    $scope.activateVehicle = function(vehicleCode, isActive) {

        var urlParams = {
            code : vehicleCode,
            isActive : isActive
        };

        vehicleService.activateVehicle(urlParams, function(response) {

            $rootScope.stopSpinner();
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_VEHICLE_ACTIVATED_SUCCESSFULLY_MESSAGE + ".");

        }, function(error) {

            $rootScope.stopSpinner();
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ERROR_LABEL + ".");
        });

    }

    $scope.refreshTableData();

} ]);
