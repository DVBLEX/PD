padApp.controller('ParkingAuthorizedExitController', [ '$scope', '$rootScope', 'parkingService', function($scope, $rootScope, parkingService) {

    $rootScope.activeNavbar('#navbarParkingPort');

    $rootScope.startSpinner();

    $scope.formData = {};
    $scope.filterPortOperator = "";
    $scope.filterVehicleRegistration = "";
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
            portOperator : $scope.filterPortOperator,
            transactionType : -1,
            vehicleRegistration : $scope.filterVehicleRegistration,
            status : $rootScope.parkingStatusConstants.PARKING_STATUS_REMINDER_EXIT_DUE + "",
            type : $rootScope.parkingTypeConstants.PARKING_TYPE_PARKING + ""
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        parkingService.countParking(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        var urlParams = {
            portOperator : $scope.filterPortOperator,
            transactionType : -1,
            vehicleRegistration : $scope.filterVehicleRegistration,
            status : $rootScope.parkingStatusConstants.PARKING_STATUS_REMINDER_EXIT_DUE + "",
            type : $rootScope.parkingTypeConstants.PARKING_TYPE_PARKING + "",
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        parkingService.listParking(urlParams, $scope.getCallBack, $scope.errorHandler);
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

    $scope.clearFilter = function() {
        $scope.filterPortOperator = "";
        $scope.filterVehicleRegistration = "";
        $scope.filtersForm.$setPristine();

        $scope.refreshTableData();
    };

    $scope.getDateSlotPastClass = function(dateSlotString) {

        if (dateSlotString === undefined || dateSlotString === "" || dateSlotString === null) {
            return '';

        } else {
            var day = dateSlotString.substring(0, 2);
            var month = dateSlotString.substring(3, 5);
            var year = dateSlotString.substring(6, 10);
            var hour = dateSlotString.substring(11, 13);
            var minute = dateSlotString.substring(14, 16);

            var dateSlot = new Date(year, month - 1, day, hour, minute, 0);
            var dateNow = new Date();

            if (dateSlot < dateNow) {
                return 'past-date-slot';
            } else {
                return '';
            }
        }
    }

    $scope.refreshTableData();

    $rootScope.stopSpinner();

} ]);
