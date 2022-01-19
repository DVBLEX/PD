padApp.controller('ApproveTripByOfficeOperatorController', [ '$scope', '$rootScope', '$interval', '$filter', 'tripService',
    function($scope, $rootScope, $interval, $filter, tripService) {

    $rootScope.activeNavbar('#navbarOfficeOperatorApproveTrip');

    $scope.formData = {};
    $scope.filterPortOperator = "";
    $scope.independentOperators = [];
    $scope.filterIndependentOperatorName = "";
    $scope.filterTransactionType = -1;
    $scope.filterAccountNumber = "";
    $scope.filterStatus = $rootScope.tripConstants.TRIP_STATUS_PENDING_APPROVAL + "";
    $scope.filterTimeSlotRequestedFromString = "0000";
    $scope.filterTimeSlotRequestedToString = "0000";
    $scope.filterTimeSlotApprovedFromString = "0000";
    $scope.filterTimeSlotApprovedToString = "0000";
    $scope.confirmActionErrorResponse = "";
    $scope.searchTripSubmitButtonDisabled = false;
    $scope.approveButtonDisabled = false;
    $scope.denyButtonDisabled = false;
    $scope.updateButtonDisabled = false;
    $scope.selectedTripCodes = new Array();
    $scope.selectedTrips = new Array();
    $scope.selectedTrip = {};
    var tripCountTask;
    $scope.isNewDataAvailable = false;
    $scope.selectAllTrigger = false;
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
        	transactionType : parseInt($scope.filterTransactionType),
            status : $scope.filterStatus,
            referenceNumber : $scope.filterReferenceNumber,
            accountNumber : $scope.filterAccountNumber,
            vehicleRegistration: $scope.filterVehicleRegistration,
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
        	portOperatorId : parseInt($scope.filterPortOperator),
            independentPortOperatorName : $scope.filterIndependentOperatorName,
        	transactionType : parseInt($scope.filterTransactionType),
            status : $scope.filterStatus,
            referenceNumber : $scope.filterReferenceNumber,
            accountNumber : $scope.filterAccountNumber,
            vehicleRegistration: $scope.filterVehicleRegistration,
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

        $scope.refreshTripsPendingApprovalCount();

        $rootScope.stopSpinner();

        $scope.scheduleTripCountsTask();
    }

    $scope.countTripsCallBack = function(response) {

        $rootScope.stopSpinner();
        
        $scope.refreshTripsPendingApprovalCount();

        if (response.dataList[0] != $scope.padTable.count) {
            // new data is available so update refresh button css class
            $scope.isNewDataAvailable = true;

            if (angular.isDefined(tripCountTask)) {
                $interval.cancel(tripCountTask);
                tripCountTask = undefined;
            }

        } else {
            $scope.isNewDataAvailable = false;
        }
    }
    
    $scope.refreshTableData = function() {

        $scope.padTable.reloadTable();
        $scope.isNewDataAvailable = false;
        $scope.selectAllTrigger = false;
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
    	$scope.filterPortOperator = "";
        $scope.filterIndependentOperatorName = "";
    	$scope.filterTransactionType = -1;
        $scope.filterStatus = $rootScope.tripConstants.TRIP_STATUS_PENDING_APPROVAL + "";
        $scope.filterReferenceNumber = "";
        $scope.filterAccountNumber = "";
        $scope.filterVehicleRegistration = "";
        $scope.filterDateSlotApprovedFromString = "";
        $scope.filterDateSlotApprovedToString = "";
        $scope.filterTimeSlotRequestedFromString = "0000";
        $scope.filterTimeSlotRequestedToString = "0000";
        $scope.filterTimeSlotApprovedFromString = "0000";
        $scope.filterTimeSlotApprovedToString = "0000";
        
        $scope.initiateDatepickers();

        $scope.refreshTableData();
    };

        $scope.clearTransactionType = function () {
            if ($scope.filterPortOperator === "") {
                $scope.filterTransactionType = -1;
            }
        }

    $scope.getSelectedTripCodes = function () {

        $scope.selectedTripCodes = new Array();
        $scope.selectedTrips = new Array();

        var dataList = $scope.padTable.data;

        for (var i = 0; i < dataList.length; i++) {
            if (dataList[i].isSelected == true) {
                $scope.selectedTripCodes.push(dataList[i].code);
                $scope.selectedTrips.push(dataList[i]);
            }
        }
    };

    $scope.bulkSelect = function() {
        
        $scope.selectAllTrigger = !$scope.selectAllTrigger;

        var dataList = $scope.padTable.data;
        
        $scope.selectedTripCodes = new Array();
        $scope.selectedTrips = new Array();
        
        for (var i = 0; i < dataList.length; i++) {
            dataList[i].isSelected = $scope.selectAllTrigger;
            if($scope.selectAllTrigger){
	            $scope.selectedTripCodes.push(dataList[i].code);
	            $scope.selectedTrips.push(dataList[i]);
            }
        }
    };

    $scope.showConfirmationModal = function(actionType) {
        // actionType 1 = approve
        // actionType 2 = deny

        $scope.actionType = actionType;
        $scope.getSelectedTripCodes();

        if ($scope.selectedTripCodes.length == 0) {
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_SELECT_AT_LEAST_ONE_TRIP_MESSAGE + ".");
            return;
        }

        if (actionType == 1) {
            // approve
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_APPROVE_LABEL;
            $('#confirmationModal').modal('show');

        } else if (actionType == 2) {
            // deny
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_DENY_LABEL;
            $('#confirmationModal').modal('show');

        } else {
            return;
        }
    };

    $scope.closeConfirmationDialog = function() {

        $scope.confirmActionErrorResponse = "";
        $scope.selectedTrip = {};
        $scope.dateSlotApprovedString = "";
        $scope.timeSlotApprovedString = "";

        $('#confirmationModal').modal('hide');
    };

    $scope.approveTrip = function() {

        if ($scope.selectedTripCodes.length == 0) {
            $scope.confirmActionErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_AT_LEAST_ONE_TRIP_MESSAGE + ".";

        } else {
            $rootScope.startSpinner();

            $scope.approveButtonDisabled = true;
            $scope.confirmActionErrorResponse = "";

            var tripData = {
                tripCodes: $scope.selectedTripCodes,
                actionType: 1
            };

            tripService.updateTrip(tripData, function(response) {

                $scope.approveButtonDisabled = false;
                $scope.closeConfirmationDialog();
                $scope.refreshTableData();

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SELECTED_TRIPS_APPROVED_MESSAGE + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $scope.confirmActionErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.approveButtonDisabled = false;
                $scope.errorHandler(error);
            });
        }
    };

    $scope.denyTrip = function() {

        if ($scope.selectedTripCodes.length == 0) {
            $scope.confirmActionErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_AT_LEAST_ONE_TRIP_MESSAGE + ".";

        } else if ($scope.confirmationForm.reasonDeny.$invalid) {
            $scope.confirmActionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_DENIAL_REASON_MESSAGE + ".";

        } else {
            $rootScope.startSpinner();

            $scope.denyButtonDisabled = true;
            $scope.confirmActionErrorResponse = "";

            var tripData = {
                tripCodes: $scope.selectedTripCodes,
                actionType: 2,
                reasonDeny: $scope.formData.reasonDeny
            };

            tripService.updateTrip(tripData, function(response) {

                $scope.denyButtonDisabled = false;
                $scope.closeConfirmationDialog();
                $scope.refreshTableData();

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SELECTED_TRIPS_DENIED_MESSAGE + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $scope.confirmActionErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.denyButtonDisabled = false;
                $scope.errorHandler(error);
            });
        }
    };

    $scope.editApprovedSlotDate = function(selectedTrip) {

        $scope.selectedTrip = selectedTrip;

        $scope.dateSlotApprovedString = $scope.selectedTrip.dateSlotApprovedString == ""
        ? $scope.selectedTrip.dateSlotString.substring(0,10) : $scope.selectedTrip.dateSlotApprovedString.substring(0,10);

        $scope.timeSlotApprovedString = $scope.selectedTrip.dateSlotApprovedString == ""
        ? $scope.selectedTrip.dateSlotString.substring(11,13) + $scope.selectedTrip.dateSlotString.substring(14)
        : $scope.selectedTrip.dateSlotApprovedString.substring(11,13) + $scope.selectedTrip.dateSlotApprovedString.substring(14);
    };

    $scope.updateApprovedSlotDateForTrip = function() {

        $scope.confirmActionErrorResponse = "";

        if ($scope.selectedTrip.code === null || $scope.selectedTrip.code === undefined) {

        } else {

            for (var i=0; $scope.selectedTrips.length; i++) {
                if ($scope.selectedTrips[i].code === $scope.selectedTrip.code) {
                    $scope.selectedTrips[i].dateSlotApprovedString = $scope.dateSlotApprovedString + " "
                    + [ $scope.timeSlotApprovedString.slice(0, 2), ":", $scope.timeSlotApprovedString.slice(2) ].join('');
                    break;
                }
            }

            var tripData = {
                code: $scope.selectedTrip.code,
                actionType: 3,
                dateSlotString : $scope.dateSlotApprovedString,
                timeSlotString : $scope.timeSlotApprovedString
            };

            tripService.updateTrip(tripData, function(response) {

                $scope.updateButtonDisabled = true;

            }, function(error) {

                $scope.confirmActionErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.updateButtonDisabled = false;
                $scope.errorHandler(error);
            });
        }
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

    $scope.$watch('dateSlotApprovedString', function() {
        if ($scope.dateSlotApprovedString === "" || $scope.dateSlotApprovedString === null || $scope.dateSlotApprovedString === undefined
            || $scope.selectedTrip.code === null || $scope.selectedTrip.code === undefined) {
            $scope.updateButtonDisabled = true;

        } else {
            $scope.updateButtonDisabled = false;
        }
    });

    $scope.$watch('timeSlotApprovedString', function() {
        if ($scope.timeSlotApprovedString === "" || $scope.timeSlotApprovedString === null || $scope.timeSlotApprovedString === undefined
            || $scope.selectedTrip.code === null || $scope.selectedTrip.code === undefined) {
            $scope.updateButtonDisabled = true;

        } else {
            $scope.updateButtonDisabled = false;
        }
    });

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

    $scope.$on('$destroy', function() {

        if (angular.isDefined(tripCountTask)) {
            $interval.cancel(tripCountTask);
            tripCountTask = undefined;
        }
    });
    
    $scope.refreshTripsPendingApprovalCount = function() {
    	var urlParams = {
            status : $rootScope.tripConstants.TRIP_STATUS_PENDING_APPROVAL + ""
        };

        tripService.countTrips(urlParams, function(response){
        	
        	 $rootScope.tripsPendingApprovalCount = response.dataList[0];
        	
        }, $scope.errorHandler);
    }


    $scope.scheduleTripCountsTask = function() {

        if (angular.isDefined(tripCountTask)) {
            $interval.cancel(tripCountTask);
            tripCountTask = undefined;
        }

        tripCountTask = $interval(function() {

            var urlParams = {
            	portOperatorId : parseInt($scope.filterPortOperator),
                transactionType : parseInt($scope.filterTransactionType),
                status : $scope.filterStatus,
                referenceNumber : $scope.filterReferenceNumber,
                vehicleRegistration: $scope.filterVehicleRegistration,
                dateSlotRequestedFromString : $scope.filterDateSlotRequestedFromString,
                dateSlotRequestedToString : $scope.filterDateSlotRequestedToString,
                timeSlotRequestedFromString : [ $scope.filterTimeSlotRequestedFromString.slice(0, 2), ":", $scope.filterTimeSlotRequestedFromString.slice(2) ].join(''),
                timeSlotRequestedToString : [ $scope.filterTimeSlotRequestedToString.slice(0, 2), ":", $scope.filterTimeSlotRequestedToString.slice(2) ].join(''),
                dateSlotApprovedFromString : $scope.filterDateSlotApprovedFromString,
                dateSlotApprovedToString : $scope.filterDateSlotApprovedToString,
                timeSlotApprovedFromString : [ $scope.filterTimeSlotApprovedFromString.slice(0, 2), ":", $scope.filterTimeSlotApprovedFromString.slice(2) ].join(''),
                timeSlotApprovedToString : [ $scope.filterTimeSlotApprovedToString.slice(0, 2), ":", $scope.filterTimeSlotApprovedToString.slice(2) ].join('')
            };

            tripService.countTrips(urlParams, $scope.countTripsCallBack, $scope.errorHandler);
        }, 60000);
    };

    $scope.refreshTableData();

    $rootScope.stopSpinner();

} ]);
