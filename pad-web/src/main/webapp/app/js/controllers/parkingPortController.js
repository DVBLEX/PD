padApp.controller('ParkingPortController', [ '$scope', '$rootScope', '$interval', '$timeout', '$filter', 'portEntryService', function($scope, $rootScope, $interval, $timeout, $filter, portEntryService) {

    $rootScope.activeNavbar('#navbarParkingPort');

    $rootScope.startSpinner();

    var selectedFromDate = new Date();
    selectedFromDate.setMonth(selectedFromDate.getMonth() - 1);

    $scope.formData = {};
    $scope.filterPortOperator = "";
    $scope.filterVehicleRegistration = "";
    $scope.filterReferenceNumber = "";
    $scope.filterTransactionType = "-1";
    $scope.filterStatus = $rootScope.portAccessStatusConstants.PORT_ACCESS_STATUS_ENTRY + "";
    $scope.filterTimeEntryFromString = "0000";
    $scope.filterTimeEntryToString = "0000";
    $scope.sendExitParkingSmsDisabled = false;
    $scope.sortColumn = "";
    $scope.sortAsc = true;
    $scope.isExportRequested = false;

    $scope.errorHandler = function(error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.padTable = new PADTable(function() {

        $rootScope.startSpinner();

        //get Data count

        var urlParams = {
            portOperator : $scope.filterPortOperator,
            vehicleRegistration : $scope.filterVehicleRegistration,
            referenceNumber: $scope.filterReferenceNumber,
            transactionType: $scope.filterTransactionType,
            status : $scope.filterStatus,
            dateEntryFromString : $scope.filterDateEntryFromString,
            dateEntryToString : $scope.filterDateEntryToString,
            timeEntryFromString : [ $scope.filterTimeEntryFromString.slice(0, 2), ":", $scope.filterTimeEntryFromString.slice(2) ].join(''),
            timeEntryToString : [ $scope.filterTimeEntryToString.slice(0, 2), ":", $scope.filterTimeEntryToString.slice(2) ].join('')
        };

        if (angular.isDefined($scope.filtersForm))
            $scope.isFilterActive = !$scope.filtersForm.$pristine;

        portEntryService.countEntry(urlParams, $scope.getCountCallBack, $scope.errorHandler);

    }, function(currentPage, pageCount) {

        //get Data

        var urlParams = {
            portOperator : $scope.filterPortOperator,
            vehicleRegistration : $scope.filterVehicleRegistration,
            referenceNumber: $scope.filterReferenceNumber,
            transactionType: $scope.filterTransactionType,
            status : $scope.filterStatus,
            dateEntryFromString : $scope.filterDateEntryFromString,
            dateEntryToString : $scope.filterDateEntryToString,
            timeEntryFromString : [ $scope.filterTimeEntryFromString.slice(0, 2), ":", $scope.filterTimeEntryFromString.slice(2) ].join(''),
            timeEntryToString : [ $scope.filterTimeEntryToString.slice(0, 2), ":", $scope.filterTimeEntryToString.slice(2) ].join(''),
            sortColumn: $scope.sortColumn,
            sortAsc: $scope.sortAsc,
            currentPage : currentPage,
            pageCount : pageCount
        };

        portEntryService.listEntry(urlParams, $scope.getCallBack, $scope.errorHandler);
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
        $scope.getCountPortEntryByPortOperator();
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

        $('.port_entry_from.date').datepicker('destroy');
        $('.port_entry_to.date').datepicker('destroy');

        var dateLessThreeMonths = new Date();
        dateLessThreeMonths.setDate(dateLessThreeMonths.getDate() - 92);

        var dateLessOneMonth = new Date();
        dateLessOneMonth.setDate(dateLessOneMonth.getDate() - 30);

        var dateTomorrow = new Date();
        dateTomorrow.setDate(dateTomorrow.getDate() + 1);

        $('.port_entry_from.date').datepicker({
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

        $('.port_entry_to.date').datepicker({
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

        $(".port_entry_from.date").datepicker("update", dateLessOneMonth);
        $(".port_entry_to.date").datepicker("update", dateTomorrow);

        $scope.filterDateEntryFromString = $filter('date')(dateLessOneMonth, "dd/MM/yyyy");
        $scope.filterDateEntryToString = $filter('date')(dateTomorrow, "dd/MM/yyyy");

        $("#filterDateEntryFromString").val($filter('date')(dateLessOneMonth, "dd/MM/yyyy"));
        $("#filterDateEntryToString").val($filter('date')(dateTomorrow, "dd/MM/yyyy"));
    }

    $scope.initiateDatepickers();

    $scope.getCountPortEntryByPortOperator = function() {
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

        portEntryService.countPortEntryByPortOperator(urlParams, function(response) {

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

        }, $scope.errorHandler);
    }

    $scope.clearFilter = function() {
        $scope.filterPortOperator = "";
        $scope.filterVehicleRegistration = "";
        $scope.filterReferenceNumber = "";
        $scope.filterTransactionType = "-1";
        $scope.filterStatus = $rootScope.portAccessStatusConstants.PORT_ACCESS_STATUS_ENTRY + "";
        $scope.filterDateEntryFromString = "";
        $scope.filterDateEntryToString = "";
        $scope.filterTimeEntryFromString = "0000";
        $scope.filterTimeEntryToString = "0000";
        $scope.sortColumn = "";
        $scope.sortAsc = true;
        $scope.filtersForm.$setPristine();

        $scope.initiateDatepickers();

        $scope.refreshTableData();
    };

    $scope.formatTimeEntryFromString = function(filterTimeEntryFromString) {

        return [ $scope.filterTimeEntryFromString.slice(0, 2), ":", $scope.filterTimeEntryFromString.slice(2) ].join('');
    }

    $scope.formatTimeEntryToString = function(filterTimeEntryToString) {

        return [ $scope.filterTimeEntryToString.slice(0, 2), ":", $scope.filterTimeEntryToString.slice(2) ].join('');
    }

    $scope.exportData = function() {

        $scope.isExportRequested = true;
        $timeout(function(){
            $scope.isExportRequested = false;
        }, 5000);
    }

    $scope.clearTransactionType = function () {
        if ($scope.filterPortOperator === "") {
            $scope.filterTransactionType = -1;
        }
    }

    $scope.filterByOperator = function(operatorId) {
        $scope.clearFilter();
        $scope.filterPortOperator = operatorId.toString();
        $scope.filtersForm.$setDirty();
        $('.collapse').collapse("show");
        $scope.refreshTableData();
    }

    $scope.$watch('filterDateEntryFromString', function(newVal, oldVal) {
        if (newVal != oldVal) {
        	var dateFromParts = newVal.split("/");
        	var dateFrom = new Date(+dateFromParts[2], dateFromParts[1] - 1, +dateFromParts[0]);
        	dateFrom.setDate(dateFrom.getDate() + 92);

        	var dateTomorrow = new Date();
            dateTomorrow.setDate(dateTomorrow.getDate() + 1);

        	var endDate = (dateFrom.getTime() > dateTomorrow.getTime()) ? dateTomorrow : dateFrom;

            $('.port_entry_to.date').datepicker("setStartDate", newVal);
            $('.port_entry_to.date').datepicker("setEndDate", endDate);
        }
    }, true);

    $scope.$watch('filterDateEntryToString', function(newVal, oldVal) {
        if (newVal != oldVal) {
        	var dateToParts = newVal.split("/");
        	var dateTo = new Date(+dateToParts[2], dateToParts[1] - 1, +dateToParts[0]);
        	dateTo.setDate(dateTo.getDate() - 92);

        	$('.port_entry_from.date').datepicker("setStartDate", dateTo);
            $('.port_entry_from.date').datepicker("setEndDate", newVal);
        }
    }, true);

    $scope.refreshTableData();

    $rootScope.stopSpinner();

} ]);
