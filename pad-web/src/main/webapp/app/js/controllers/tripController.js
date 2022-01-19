padApp.controller('TripController', [ '$scope', '$rootScope', 'tripService', 'accountService',
    function($scope, $rootScope, tripService, accountService) {
    
    $rootScope.activeNavbar('#navbarTrips');

    $scope.filterPortOperator = "";
    $scope.independentOperators = [];
    $scope.filterIndependentOperatorName = "";
    $scope.filterStatus = "-1";
    $scope.searchTripSubmitButtonDisabled = false;
    $scope.accountNamesList = [];
    $scope.sortColumn = "";
    $scope.sortAsc = true;

    $scope.errorHandler = function(error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {
        
        $rootScope.startSpinner();

        var urlParams = {
            portOperatorId : parseInt($scope.filterPortOperator),
            independentPortOperatorName : $scope.filterIndependentOperatorName,
            status : $scope.filterStatus,
            referenceNumber : $scope.filterReferenceNumber,
            accountCode : $scope.companyAccountCode
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        tripService.countTrips(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        $scope.sortType = null;
        $scope.sortReverse = false;

        var urlParams = {
            portOperatorId : parseInt($scope.filterPortOperator),
            independentPortOperatorName : $scope.filterIndependentOperatorName,
            status : $scope.filterStatus,
            referenceNumber : $scope.filterReferenceNumber,
            accountCode : $scope.companyAccountCode,
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        tripService.listTrips(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.getCallBack = function(response) {

        $scope.padTable.setData(response.dataList);
        $scope.searchTripSubmitButtonDisabled = false;
        $rootScope.stopSpinner();
    }

    $scope.getCountCallBack = function(response) {

        $scope.padTable.setCount(response.dataList[0]);
        $scope.searchTripSubmitButtonDisabled = false;
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

    $scope.searchTrip = function() {

        $scope.searchTripSubmitButtonDisabled = true;
        $scope.refreshTableData();
    };

    $scope.clearSearchTrip = function() {

        $scope.filterPortOperator = "";
        $scope.filterIndependentOperatorName = "";
        $scope.filterStatus = -1;
        $scope.filterReferenceNumber = "";
        $scope.companyAccountCode = "";

        $scope.refreshTableData();
    };

    $scope.getActiveAccountNames = function() {

        accountService.getActiveAccountNames({}, function(response) {

            $scope.accountNamesMap = response.dataMap;

            var codes = Object.keys($scope.accountNamesMap);

            angular.forEach(codes, function(value, key) {
                var obj = {};
                obj.code = value;
                obj.name = $scope.accountNamesMap[value];

                $scope.accountNamesList.push(obj);
            });

        }, function(error) {

            $scope.getCallBackError(error);
        });
    };

        $scope.$watch('filterPortOperator', function () {

            if (+$scope.filterPortOperator !== undefined && +$scope.filterPortOperator !== null) {
                $scope.independentOperators = $scope.filterPortOperator === "" ? [] : $rootScope.getIndependentOperators(+$scope.filterPortOperator)

                $scope.filterIndependentOperatorName = "";

                $("#filterIndependentOperatorName").autocomplete({
                    source: $scope.independentOperators,
                    focus: function (event, ui) {
                        // prevent autocomplete from updating the textbox
                        event.preventDefault();
                        $(this).val(ui.item.label);
                        $scope.filterIndependentOperatorName = ui.item.label;
                    },
                    select: function (event, ui) {
                        // prevent autocomplete from updating the textbox
                        event.preventDefault();
                        $(this).val(ui.item.label);
                        $scope.filterIndependentOperatorName = ui.item.label;
                    }
                });

            }
        });

    $scope.refreshTableData();
    $scope.getActiveAccountNames();

    $rootScope.stopSpinner();
    
} ]);
