padApp.controller('VehicleCounterController', [ '$scope', '$rootScope', '$timeout', 'vehicleCounterService', 'laneService', function($scope, $rootScope, $timeout, vehicleCounterService, laneService) {

    $rootScope.activeNavbar('#navbarVehicleCounter');
    
    $scope.filterDateCountStart = "";
    $scope.filterDateCountEnd = "";
    $scope.filterDevice = "";
    $scope.filterLane = "";
    $scope.filterSession = "";
    $scope.filterType = $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_AUTOMATIC_MANUAL;
    $scope.filterIsShowDefaultDates = false;
    $scope.isFilterActive = false;
    $scope.lanesList = [];
    $scope.sessionList = [];
    $scope.isExportRequested = false;
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
            dateCountStartString : $scope.filterDateCountStart,
            dateCountEndString : $scope.filterDateCountEnd,
            deviceId : $scope.filterDevice,
            laneNumber : $scope.filterLane,
            type : $scope.filterType,
            sessionIdString : $scope.filterSession,
            isShowDefaultDates : $scope.filterIsShowDefaultDates
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        vehicleCounterService.count(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        //get Data
        $scope.sortType = null;
        $scope.sortReverse = false;

        var urlParams = {
            dateCountStartString : $scope.filterDateCountStart,
            dateCountEndString : $scope.filterDateCountEnd,
            deviceId : $scope.filterDevice,
            laneNumber : $scope.filterLane,
            type : $scope.filterType,
            sessionIdString : $scope.filterSession,
            isShowDefaultDates : $scope.filterIsShowDefaultDates,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        vehicleCounterService.list(urlParams, $scope.getCallBack, $scope.errorHandler);
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
    
    $scope.clearSearch = function() {
        $scope.filterDateCountStart = "";
        $scope.filterDateCountEnd = "";
        $scope.filterDevice = "";
        $scope.filterLane = "";
        $scope.filterSession = "";
        $scope.filterType = $rootScope.vehicleCounterTypeConstants.VEHICLE_COUNTER_TYPE_AUTOMATIC_MANUAL;
        $scope.filterIsShowDefaultDates = false;
        $scope.filtersForm.$setPristine();

        $scope.refreshTableData();
    };
    
    $scope.getLanes = function() {
        
        var urlParams = {
            currentPage : 0,
            pageCount : -1
        };
        
        laneService.list(urlParams, function(response) {

            $scope.lanesList = response.dataList;
            
        }, function(error) {
            $scope.errorHandler(error);
        });
    };
    
    $scope.getLanes();
    
    $scope.getSessionList = function() {
        
        vehicleCounterService.getSessionList({}, function(response) {

            $scope.sessionList = response.dataList;
            
        }, function(error) {
            $scope.errorHandler(error);
        });
    };
    
    $scope.getSessionList();
    
    $scope.exportData = function() {

        $scope.isExportRequested = true;
        $timeout(function(){
            $scope.isExportRequested = false;
        }, 5000);
    }    
    
    $scope.$watch('filterDateCountStart', function(newVal, oldVal) {
        if (newVal != oldVal) {
            $('.count_to_date.date').datepicker("setStartDate", newVal);
        }
    }, true);

    $scope.$watch('filterDateCountEnd', function(newVal, oldVal) {
        if (newVal != oldVal) {
            $('.count_from_date.date').datepicker("setEndDate", newVal);
        }
    }, true);

    $scope.refreshTableData();

} ]);