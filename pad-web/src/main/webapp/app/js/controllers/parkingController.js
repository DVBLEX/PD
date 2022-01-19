padApp.controller('ParkingController', [ '$scope', '$rootScope', '$timeout', '$interval', '$filter', 'parkingService', function($scope, $rootScope, $timeout, $interval, $filter, parkingService) {

    $rootScope.activeNavbar('#navbarParking');

    $rootScope.startSpinner();

    $scope.sortType = null;
    $scope.sortReverse = false;
    $scope.formData = {};
    $scope.filterPortOperator = "";
    $scope.filterTransactionType = -1;
    $scope.filterVehicleRegistration = "";
    $scope.filterStatus = "";
    $scope.filterType = $rootScope.parkingTypeConstants.PARKING_TYPE_ALL + "";
    $scope.filterInTransit = false;
    $scope.filterTimeEntryFromString = "0000";
    $scope.filterTimeEntryToString = "0000";
    $scope.sendExitParkingSmsDisabled = false;
    $scope.selectedVehicles = [];
    $scope.isExportRequested = false;
    $scope.confirmActionErrorResponse = "";
    $scope.disableControls = false;
    $scope.sortColumn = "";
    $scope.sortAsc = true;

    $scope.errorHandler = function(error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {

        $rootScope.startSpinner();

        var urlParams = {
            portOperator : $scope.filterPortOperator,
            transactionType : parseInt($scope.filterTransactionType),
            vehicleRegistration : $scope.filterVehicleRegistration,
            type : $scope.filterType,
            status : $scope.filterStatus,
            isInTransit : $scope.filterInTransit,
            dateEntryFromString : $scope.filterDateEntryFromString,
            dateEntryToString : $scope.filterDateEntryToString,
            timeEntryFromString : [ $scope.filterTimeEntryFromString.slice(0, 2), ":", $scope.filterTimeEntryFromString.slice(2) ].join(''),
            timeEntryToString : [ $scope.filterTimeEntryToString.slice(0, 2), ":", $scope.filterTimeEntryToString.slice(2) ].join('')
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        parkingService.countParking(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        var urlParams = {
            portOperator : $scope.filterPortOperator,
            transactionType : parseInt($scope.filterTransactionType),
            vehicleRegistration : $scope.filterVehicleRegistration,
            type : $scope.filterType,
            status : $scope.filterStatus,
            isInTransit : $scope.filterInTransit,
            dateEntryFromString : $scope.filterDateEntryFromString,
            dateEntryToString : $scope.filterDateEntryToString,
            timeEntryFromString : [ $scope.filterTimeEntryFromString.slice(0, 2), ":", $scope.filterTimeEntryFromString.slice(2) ].join(''),
            timeEntryToString : [ $scope.filterTimeEntryToString.slice(0, 2), ":", $scope.filterTimeEntryToString.slice(2) ].join(''),
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        parkingService.listParking(urlParams, $scope.getCallBack, $scope.errorHandler);
    });

    $scope.padTable.setPageBookingCount(500);

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
        $scope.selectedVehicles = [];
        $scope.getCountParkingByPortOperator();
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

    $scope.exportData = function() {

        $scope.isExportRequested = true;
        $timeout(function(){
            $scope.isExportRequested = false;
        }, 5000);
    }

    $scope.initiateDatepickers = function() {

        $('.parking_entry_from.date').datepicker('destroy');
        $('.parking_entry_to.date').datepicker('destroy');

        var dateLessThreeMonths = new Date();
        dateLessThreeMonths.setDate(dateLessThreeMonths.getDate() - 92);

        var dateLessOneMonth = new Date();
        dateLessOneMonth.setDate(dateLessOneMonth.getDate() - 30);

        var dateTomorrow = new Date();
        dateTomorrow.setDate(dateTomorrow.getDate() + 1);

        $('.parking_entry_from.date').datepicker({
        	format : "dd/mm/yyyy",
            defaultViewDate: dateLessOneMonth,
            startDate : dateLessThreeMonths,
            endDate : dateTomorrow,
            setDate: dateLessOneMonth,
            todayBtn : "linked",
            autoclose : true,
            todayHighlight : true,
            orientation: 'bottom',
            language: $filter('lowercase')($rootScope.language)
        })

        $('.parking_entry_to.date').datepicker({
            format : "dd/mm/yyyy",
            defaultViewDate: dateTomorrow,
            startDate : dateLessOneMonth,
            endDate : dateTomorrow,
            setDate: dateTomorrow,
            todayBtn : "linked",
            autoclose : true,
            todayHighlight : true,
            orientation: 'bottom',
            language: $filter('lowercase')($rootScope.language)
        }).on('hide', function(ev) {
            if(!ev.date){
                $("#filterDateEntryToString").val($scope.filterDateEntryToString);
            }
        });

        $(".parking_entry_from.date").datepicker("update", dateLessOneMonth);
        $(".parking_entry_to.date").datepicker("update", dateTomorrow);

        $scope.filterDateEntryFromString = $filter('date')(dateLessOneMonth, "dd/MM/yyyy");
        $scope.filterDateEntryToString = $filter('date')(dateTomorrow, "dd/MM/yyyy");

        $("#filterDateEntryFromString").val($filter('date')(dateLessOneMonth, "dd/MM/yyyy"));
        $("#filterDateEntryToString").val($filter('date')(dateTomorrow, "dd/MM/yyyy"));
    }

    $scope.initiateDatepickers();

    $scope.formatTimeEntryFromString = function(filterTimeEntryFromString) {

        return [ $scope.filterTimeEntryFromString.slice(0, 2), ":", $scope.filterTimeEntryFromString.slice(2) ].join('');
    }

    $scope.formatTimeEntryToString = function(filterTimeEntryToString) {

        return [ $scope.filterTimeEntryToString.slice(0, 2), ":", $scope.filterTimeEntryToString.slice(2) ].join('');
    }

    $scope.getCountParkingByPortOperator = function() {
        var urlParams = {};

        $scope.countDPWorldVehicles = 0;
        $scope.countTVSVehicles = 0;
        $scope.countDakarTerminalVehicles = 0;
        $scope.countVivoEnergyVehicles = 0;
        $scope.countSenstockVehicles = 0;
        $scope.countOryxVehicles = 0;
        $scope.countEresVehicles = 0;
        $scope.countTMNorthVehicles = 0;
        $scope.countTMSouthVehicles = 0;
        $scope.countMole10Vehicles = 0;

        $scope.countVehicleExitOnly = 0;
        $scope.countVehicleInTransit = 0;

        parkingService.countParkingByPortOperator(urlParams, function(response) {

            $scope.countDPWorldVehicles = response.dataList[0];
            $scope.countTVSVehicles = response.dataList[1];
            $scope.countDakarTerminalVehicles = response.dataList[2];
            $scope.countVivoEnergyVehicles = response.dataList[3];
            $scope.countSenstockVehicles = response.dataList[4];
            $scope.countOryxVehicles = response.dataList[5];
            $scope.countEresVehicles = response.dataList[6];
            $scope.countTMNorthVehicles = response.dataList[7];
            $scope.countTMSouthVehicles = response.dataList[8];
            $scope.countMole10Vehicles = response.dataList[9];

            $scope.countVehicleExitOnly = response.dataList[10];
            $scope.countVehicleInTransit = response.dataList[11];

        }, $scope.errorHandler);
    }

    $scope.clearTransactionType = function () {
        if ($scope.filterPortOperator === "") {
            $scope.filterTransactionType = -1;
        }
    }

    $scope.clearFilter = function() {
        $scope.filterPortOperator = "";
        $scope.filterTransactionType = -1;
        $scope.filterVehicleRegistration = "";
        $scope.filterType = $rootScope.parkingTypeConstants.PARKING_TYPE_ALL + "";
        $scope.filterStatus = "";
        $scope.selectedVehicles = [];
        $scope.filterInTransit = false;
        $scope.filterTimeEntryFromString = "0000";
        $scope.filterTimeEntryToString = "0000";
        $scope.filtersForm.$setPristine();

        $scope.initiateDatepickers();

        $scope.refreshTableData();
    };

    $scope.filterByOperator = function(operatorId) {
        $scope.clearFilter();
        $scope.filterPortOperator = operatorId.toString();
        $scope.filtersForm.$setDirty();
        $('.collapse').collapse("show");
        $scope.refreshTableData();
    }

    $scope.filterByType = function(type) {
        $scope.clearFilter();
        $scope.filterType = type + '';
        $scope.filtersForm.$setDirty();
        $('.collapse').collapse("show");
        $scope.refreshTableData();
    }

    $scope.filterByInTransit = function() {
        $scope.clearFilter();
        $scope.filterType = $rootScope.parkingTypeConstants.PARKING_TYPE_PARKING + "";
        $scope.filterStatus = $rootScope.parkingStatusConstants.PARKING_STATUS_EXIT + "";
        $scope.filterInTransit = true;
        $scope.filtersForm.$setDirty();
        $('.collapse').collapse("show");
        $scope.refreshTableData();
    }

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

    $scope.sendExitParkingSms = function(parkingCode) {

        var codes = [];
        for (var i = 0; i < $scope.selectedVehicles.length; i++) {
            codes.push($scope.selectedVehicles[i].code);
        }

        $scope.sendExitParkingSmsDisabled = true;

        var urlParams = {
            exitParkingCodes : codes,
            addSecondsSchedule : 0
        };

        parkingService.sendExitParkingSms(urlParams, function(response) {

            $scope.sendExitParkingSmsDisabled = false;
            $scope.closeExitParkingDialog();

            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_EXIT_PARKING_SMS_SENT_MESSAGE);

            $scope.refreshTableData();

        }, function(error) {

            $scope.sendExitParkingSmsDisabled = false;
            $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
        });
    };

    $scope.showExitParkingDialog = function(selectedRow) {

        $scope.selectedRow = selectedRow;

        $('#parkingExitModal').modal('show');
    };

    $scope.closeExitParkingDialog = function() {

        $('#parkingExitModal').modal('hide');

        $scope.refreshTableData();
    };

    $scope.showVehicleStateUpdateDialog = function(selectedRow) {

        $('#parkingStateUpdateModal').modal('show');

        $scope.selectedRow = selectedRow;
        $scope.formData.vehicleState = $scope.selectedRow.vehicleState;
    };

    $scope.closeVehicleStateUpdateDialog = function() {

        $scope.confirmActionErrorResponse = "";
        $scope.selectedRow = null;

        $('#parkingStateUpdateModal').modal('hide');

        $scope.refreshTableData();
    };

    $scope.updateVehicleState = function(selectedRow) {

        $scope.selectedRow = selectedRow;

        if ($scope.selectedRow.vehicleState == $scope.formData.vehicleState) {
            $scope.confirmActionErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_VEHICLE_STATE_MESSAGE + ".";
            return;
        }

        $rootScope.startSpinner();
        $scope.disableControls = true;
        $scope.confirmActionErrorResponse = "";

        var requestData = {
            code : $scope.selectedRow.code,
            vehicleState : parseInt($scope.formData.vehicleState)
        };

        parkingService.updateVehicleState(requestData, function(response) {

            $scope.disableControls = false;
            $scope.closeVehicleStateUpdateDialog();

            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL);
            $rootScope.stopSpinner();

        }, function(error) {

            $scope.errorHandler(error);
            $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            $scope.disableControls = false;
        });
    };

    $scope.toggleSelection = function (row) {
        var idx = $scope.selectedVehicles.indexOf(row);

        if (idx > -1) {
            $scope.selectedVehicles.splice(idx, 1);
        } else {
            $scope.selectedVehicles.push(row);
        }
    };

    $scope.deleteVehicle = function (row) {
        $scope.selectedVehicles.splice($scope.selectedVehicles.indexOf(row), 1);
    };

    $scope.$watch('filterDateEntryFromString', function(newVal, oldVal) {
        if (newVal != oldVal) {
        	var dateFromParts = newVal.split("/");
        	var dateFrom = new Date(+dateFromParts[2], dateFromParts[1] - 1, +dateFromParts[0]);
        	dateFrom.setDate(dateFrom.getDate() + 92);

        	var dateTomorrow = new Date();
            dateTomorrow.setDate(dateTomorrow.getDate() + 1);

        	var endDate = (dateFrom.getTime() > dateTomorrow.getTime()) ? dateTomorrow : dateFrom;

            $('.parking_entry_to.date').datepicker("setStartDate", newVal);
            $('.parking_entry_to.date').datepicker("setEndDate", endDate);
        }
    }, true);

    $scope.$watch('filterDateEntryToString', function(newVal, oldVal) {
        if (newVal != oldVal) {
        	var dateToParts = newVal.split("/");
        	var dateTo = new Date(+dateToParts[2], dateToParts[1] - 1, +dateToParts[0]);
        	dateTo.setDate(dateTo.getDate() - 92);

        	$('.parking_entry_from.date').datepicker("setStartDate", dateTo);
            $('.parking_entry_from.date').datepicker("setEndDate", newVal);
        }
    }, true);

    $scope.$on('$destroy', function() {
        $interval.cancel(refreshTableDataTask);
    });

    $scope.refreshTableData();

    var refreshTableDataTask = $interval(function() {
        if($scope.selectedVehicles.length === 0 && $scope.padTable.currentPage == 1){
            $scope.refreshTableData();
        }
    }, 30000);

    $(function() {
        $('[data-toggle="tooltip"]').tooltip()
    });

    $rootScope.stopSpinner();

} ]);
