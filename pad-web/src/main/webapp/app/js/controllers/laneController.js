padApp.controller('LaneController', [ '$scope', '$rootScope', '$timeout', 'laneService', function($scope, $rootScope, $timeout, laneService) {

    $rootScope.activeNavbar('#navbarLane');

    $scope.formData = {};
    $scope.laneErrorResponse = "";
    $scope.buttonsDisabled = false;
    $scope.isFilterActive = false;
    $scope.regexMacAddress = /^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$/;
    $scope.regexIpAddress =/^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;
    $scope.isAdding = false;
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

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        laneService.count(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        //get Data
        $scope.sortType = null;
        $scope.sortReverse = false;

        var urlParams = {
        	sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        laneService.list(urlParams, $scope.getCallBack, $scope.errorHandler);
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
        $scope.filtersForm.$setPristine();

        $scope.refreshTableData();
    };

    $scope.addLaneModal = function() {
        $scope.modalTitle = $rootScope.translation.KEY_SCREEN_ADD_LANE_LABEL;
        $scope.modalButtonLabel = $rootScope.translation.KEY_SCREEN_ADD_LABEL;
        $scope.isAdding = true;
        $scope.showLaneModal();
    }

    $scope.updateLaneModal = function(row) {
        $scope.formData = angular.copy(row);
        $scope.modalTitle = $rootScope.translation.KEY_SCREEN_UPDATE_LANE_LABEL;
        $scope.modalButtonLabel = $rootScope.translation.KEY_BUTTON_UPDATE;
        $scope.isAdding = false;
        $scope.showLaneModal();
    }

    $scope.showLaneModal = function() {
        $('#laneModal').modal('show');
    }

    $scope.closeLaneModal = function() {

        $scope.formData = {};
        $scope.laneErrorResponse = "";
        $scope.buttonsDisabled = false;
        $scope.laneForm.$setPristine();
        $scope.isAdding = false;
        $('#laneModal').modal('hide');
    }
    
    $scope.saveOrUpdate = function() {
        var urlParams = {
            id : $scope.formData.id,
            laneId : $scope.formData.laneId,
            laneNumber : $scope.formData.laneNumber,
            zoneId : $scope.formData.zoneId,
            deviceId : $scope.formData.deviceId,
            deviceName : $scope.formData.deviceName,
            allowedHosts : $scope.formData.allowedHosts,
            isActive : $scope.formData.isActive,
            printerIp : $scope.formData.printerIp
        };
        
        if($scope.isAdding){
            $scope.saveLane(urlParams);
        }else{
            $scope.updateLane(urlParams);
        }
    }
    
    $scope.activateLane = function(row) {
        
        var urlParams = {
            laneId : row.laneId,
            isActive: row.isActive
        };
        
        $rootScope.startSpinner();
        
        laneService.activate(urlParams, function(response) {
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_LANE_UPDATED_MESSAGE);
            $scope.refreshTableData();
            
            $rootScope.stopSpinner();
        }, function(error) {
            $rootScope.showResultMessage(true, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            $scope.refreshTableData();
            
            $rootScope.stopSpinner();
        });
    }

    $scope.saveLane = function(urlParams) {
        
        $rootScope.startSpinner();

        laneService.save(urlParams, function(response) {
            $scope.closeLaneModal();
            $scope.refreshTableData();
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_LANE_SAVED_MESSAGE);
            
            $rootScope.stopSpinner();
        }, function(error) {
            $scope.laneErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            $scope.laneForm.$setPristine();
            $scope.buttonsDisabled = false;
            $scope.refreshTableData();
            
            $rootScope.stopSpinner();
        });
    }
    
    $scope.updateLane = function(urlParams) {
        
        $rootScope.startSpinner();

        laneService.update(urlParams, function(response) {
            $scope.closeLaneModal();
            $scope.refreshTableData();
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_LANE_UPDATED_MESSAGE);
            
            $rootScope.stopSpinner();
        }, function(error) {
            $scope.laneErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            $scope.laneForm.$setPristine();
            $scope.buttonsDisabled = false;
            $scope.refreshTableData();
            
            $rootScope.stopSpinner();
        });
    }

    $scope.refreshTableData();

} ]);
