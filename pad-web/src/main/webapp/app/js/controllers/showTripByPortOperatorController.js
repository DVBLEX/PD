padApp.controller('ShowTripByPortOperatorController', [ '$scope', '$rootScope', '$filter', 'tripService',
    function($scope, $rootScope, $filter, tripService) {

    $rootScope.activeNavbar('#navbarPortOperatorShowTrip');

    $scope.formData = {};
    $scope.filterStatus = "-1";
    $scope.filterTimeSlotRequestedFromString = "0000";
    $scope.filterTimeSlotRequestedToString = "0000";
    $scope.filterTimeSlotApprovedFromString = "0000";
    $scope.filterTimeSlotApprovedToString = "0000";
    $scope.confirmActionErrorResponse = "";
    $scope.searchTripSubmitButtonDisabled = false;
    $scope.sortColumn = "";
    $scope.sortAsc = true;

    $scope.errorHandler = function(error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {

        $rootScope.startSpinner();

        var urlParams = {
            status : $scope.filterStatus,
            portOperatorId : $rootScope.portOperatorId,
            independentPortOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.independentPortOperatorId : null,
            referenceNumber : $scope.filterReferenceNumber,
            dateSlotRequestedFromString : $scope.filterDateSlotRequestedFromString,
            dateSlotRequestedToString : $scope.filterDateSlotRequestedToString,
            timeSlotRequestedFromString : [ $scope.filterTimeSlotRequestedFromString.slice(0, 2), ":", $scope.filterTimeSlotRequestedFromString.slice(2) ].join(''),
            timeSlotRequestedToString : [ $scope.filterTimeSlotRequestedToString.slice(0, 2), ":", $scope.filterTimeSlotRequestedToString.slice(2) ].join(''),
            dateSlotApprovedFromString : $scope.filterDateSlotApprovedFromString,
            dateSlotApprovedToString : $scope.filterDateSlotApprovedToString,
            timeSlotApprovedFromString : [ $scope.filterTimeSlotApprovedFromString.slice(0, 2), ":", $scope.filterTimeSlotApprovedFromString.slice(2) ].join(''),
            timeSlotApprovedToString : [ $scope.filterTimeSlotApprovedToString.slice(0, 2), ":", $scope.filterTimeSlotApprovedToString.slice(2) ].join('')
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        tripService.countTrips(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        $scope.sortType = null;
        $scope.sortReverse = false;

        var urlParams = {
            status : $scope.filterStatus,
            portOperatorId : $rootScope.portOperatorId,
            independentPortOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.independentPortOperatorId : null,
            referenceNumber : $scope.filterReferenceNumber,
            dateSlotRequestedFromString : $scope.filterDateSlotRequestedFromString,
            dateSlotRequestedToString : $scope.filterDateSlotRequestedToString,
            timeSlotRequestedFromString : [ $scope.filterTimeSlotRequestedFromString.slice(0, 2), ":", $scope.filterTimeSlotRequestedFromString.slice(2) ].join(''),
            timeSlotRequestedToString : [ $scope.filterTimeSlotRequestedToString.slice(0, 2), ":", $scope.filterTimeSlotRequestedToString.slice(2) ].join(''),
            dateSlotApprovedFromString : $scope.filterDateSlotApprovedFromString,
            dateSlotApprovedToString : $scope.filterDateSlotApprovedToString,
            timeSlotApprovedFromString : [ $scope.filterTimeSlotApprovedFromString.slice(0, 2), ":", $scope.filterTimeSlotApprovedFromString.slice(2) ].join(''),
            timeSlotApprovedToString : [ $scope.filterTimeSlotApprovedToString.slice(0, 2), ":", $scope.filterTimeSlotApprovedToString.slice(2) ].join(''),
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
    
    $scope.initiateDatepickers = function() {

    	$('.trip_slot_requested_filter_from.date').datepicker('destroy');
        $('.trip_slot_requested_filter_to.date').datepicker('destroy');

        var dateLessOneWeek = new Date();
        dateLessOneWeek.setDate(dateLessOneWeek.getDate() - 7);
        
        var datePlusThreeMonths = angular.copy(dateLessOneWeek);
        datePlusThreeMonths.setDate(datePlusThreeMonths.getDate() + 92);
        
        var datePlusOneMonth = angular.copy(dateLessOneWeek);
        datePlusOneMonth.setDate(datePlusOneMonth.getDate() + 30);

        $('.trip_slot_requested_filter_from.date').datepicker({
        	format : "dd/mm/yyyy",
            defaultViewDate: dateLessOneWeek,
            startDate : dateLessOneWeek,
            endDate : datePlusThreeMonths,
            setDate: dateLessOneWeek,
            todayBtn : "linked",
            autoclose : true,
            todayHighlight : true,
            orientation: 'bottom',
            language: $filter('lowercase')($rootScope.language)
        })
        
        $('.trip_slot_requested_filter_to.date').datepicker({
            format : "dd/mm/yyyy",
            defaultViewDate: datePlusOneMonth,
            startDate : dateLessOneWeek,
            endDate : datePlusThreeMonths,
            setDate: datePlusOneMonth,
            todayBtn : "linked",
            autoclose : true,
            todayHighlight : true,
            orientation: 'bottom',
            language: $filter('lowercase')($rootScope.language)
        }).on('hide', function(ev) { 
            if(!ev.date){
                $("#filterDateSlotRequestedToString").val($scope.filterDateEntryToString);
            }
        });

        $(".trip_slot_requested_filter_from.date").datepicker("update", dateLessOneWeek);
        $(".trip_slot_requested_filter_to.date").datepicker("update", datePlusOneMonth);

        $scope.filterDateSlotRequestedFromString = $filter('date')(dateLessOneWeek, "dd/MM/yyyy");
        $scope.filterDateSlotRequestedToString = $filter('date')(datePlusOneMonth, "dd/MM/yyyy");
       
        $("#filterDateSlotRequestedFromString").val($filter('date')(dateLessOneWeek, "dd/MM/yyyy"));
        $("#filterDateSlotRequestedToString").val($filter('date')(datePlusOneMonth, "dd/MM/yyyy"));
    }
    
    $scope.initiateDatepickers();

    $scope.searchTrip = function() {

        $scope.searchTripSubmitButtonDisabled = true;
        $scope.refreshTableData();
    };

    $scope.clearSearchTrip = function() {

        $scope.filterStatus = "-1";
        $scope.filterReferenceNumber = "";
        $scope.filterDateSlotApprovedFromString = "";
        $scope.filterDateSlotApprovedToString = "";
        $scope.filterTimeSlotRequestedFromString = "0000";
        $scope.filterTimeSlotRequestedToString = "0000";
        $scope.filterTimeSlotApprovedFromString = "0000";
        $scope.filterTimeSlotApprovedToString = "0000";
        
        $scope.initiateDatepickers();

        $scope.refreshTableData();
    };

    $scope.$watch('filterDateSlotRequestedFromString', function(newVal, oldVal) {
        if (newVal != oldVal) {
        	var dateFromParts = newVal.split("/");
        	var dateFrom = new Date(+dateFromParts[2], dateFromParts[1] - 1, +dateFromParts[0]);
        	dateFrom.setDate(dateFrom.getDate() + 92);
        	
            $('.trip_slot_requested_filter_to.date').datepicker("setStartDate", newVal);
            $('.trip_slot_requested_filter_to.date').datepicker("setEndDate", dateFrom);
        }
    }, true);

    $scope.$watch('filterDateSlotRequestedToString', function(newVal, oldVal) {
        if (newVal != oldVal) {
        	var dateToParts = newVal.split("/");
        	var dateTo = new Date(+dateToParts[2], dateToParts[1] - 1, +dateToParts[0]);
        	dateTo.setDate(dateTo.getDate() - 92);
            
        	$('.trip_slot_requested_filter_from.date').datepicker("setStartDate", dateTo);
            $('.trip_slot_requested_filter_from.date').datepicker("setEndDate", newVal);
        }
    }, true);

    $scope.$watch('filterDateSlotApprovedFromString', function(newVal, oldVal) {
        if (newVal != oldVal) {
            $('.trip_slot_approved_filter_to.date').datepicker("setStartDate", newVal);
        }
    }, true);

    $scope.$watch('filterDateSlotApprovedToString', function(newVal, oldVal) {
        if (newVal != oldVal) {
            $('.trip_slot_approved_filter_from.date').datepicker("setEndDate", newVal);
        }
    }, true);

    $scope.refreshTableData();

    $rootScope.stopSpinner();

} ]);
