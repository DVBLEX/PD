padApp.controller('BookingLimitController', [ '$scope', '$rootScope', '$filter', 'systemService', function($scope, $rootScope, $filter, systemService) {

    $rootScope.activeNavbar('#navbarBookingLimits');

    $scope.periodList = [];
    $scope.timeFromList = [];
    $scope.timeToList = [];
    $scope.formSearch = {};
    $scope.formData = {};
    $scope.bookingSlotLimitMap = undefined;
    $scope.portOperatorTransactionTypes = [];
    $scope.daysWeek = [];
    $scope.isLoaded = false;

    $scope.getDayTranslation = function(dayId) {
        switch (dayId) {
            case 2:
                return $rootScope.translation.KEY_SCREEN_MONDAY_LABEL;
                break;
            case 3:
                return $rootScope.translation.KEY_SCREEN_TUESDAY_LABEL;
                break;
            case 4:
                return $rootScope.translation.KEY_SCREEN_WEDNESDAY_LABEL;
                break;
            case 5:
                return $rootScope.translation.KEY_SCREEN_THURSDAY_LABEL;
                break;
            case 6:
                return $rootScope.translation.KEY_SCREEN_FRIDAY_LABEL;
                break;
            case 7:
                return $rootScope.translation.KEY_SCREEN_SATURDAY_LABEL;
                break;
            case 1:
                return $rootScope.translation.KEY_SCREEN_SUNDAY_LABEL;
                break;
            default:
                return "";
        }
    }

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.showModalBulk = function(dayOfWeek) {
        $scope.modalTitle = $scope.getDayTranslation(dayOfWeek.id) + " - " + dayOfWeek.dateString;
        $scope.formData = {};
        $scope.formData.dayOfWeek = dayOfWeek;
        $scope.timeToList = [];
        $scope.formData.isBulk = true;
        $('#bookingLimitModal').modal('show');
    };
    
    $scope.showModal = function(cell, index) {
        var dayOfWeek = $scope.daysWeek[index];
        $scope.modalTitle = $scope.getDayTranslation(cell.dayOfWeekId) + " - " + dayOfWeek.dateString;
        $scope.formData = {};
        $scope.formData.dayOfWeek = dayOfWeek;
        $scope.formData.hourSlotFrom = cell.hourSlotFrom;
        $scope.formData.hourSlotFromString = cell.hourSlotFromString;
        $scope.formData.hourSlotTo = cell.hourSlotTo;
        $scope.formData.hourSlotToString = cell.hourSlotToString;
        $scope.formData.isBulk = false;
        $('#bookingLimitModal').modal('show');
    };

    $scope.closeModal = function() {
        $('#bookingLimitModal').modal('hide');
    };
    
    $scope.getColumnTitle = function(dayOfWeek) {
        
        if(!dayOfWeek)
            return;
        
        var dateLabel = $scope.formSearch.period == "DEFAULT" ? $rootScope.translation.KEY_SCREEN_DEFAULT_LABEL : dayOfWeek.dateString;
        
        return $scope.getDayTranslation(dayOfWeek.id) + " - " + dateLabel;
        
    };

    $scope.getBookingLimitPeriods = function() {
        
        systemService.getBookingSlotLimitPeriods({}, function(response) {

            $scope.periodList = response.dataList;
            
            $scope.formSearch.period = $scope.periodList[0].value;

        }, function(error) {
            $scope.errorHandler(error);
        });
    };
    
    $scope.getBookingLimitPeriods();

    $scope.populateTimeFrom = function() {

        for (var i = 0; i <= 23; i++) {

            var hourString = "";
            if (i < 10) {
                hourString = "0" + i + ":00";

            } else {
                hourString = i + ":00";
            }

            $scope.timeFromList.push({
                value : i,
                label : hourString
            });
        }
    }

    $scope.populateTimeFrom();

    $scope.populateTimeTo = function() {

        $scope.timeToList = [];
        $scope.formData.hourSlotTo = undefined;

        var i = parseInt($scope.formData.hourSlotFrom) + 1;

        for (i; i <= 24; i++) {

            var hourString = "";
            if (i < 10) {
                hourString = "0" + i + ":00";

            } else {
                hourString = (i == 24 ? "00" : i) + ":00";
            }

            $scope.timeToList.push({
                value : i,
                label : hourString
            });
        }
    }

    $scope.search = function() {

        if (!$scope.formSearch.portOperator || !$scope.formSearch.transactionType || !$scope.formSearch.period)
            return;
        
        $scope.isLoaded = false;

        $rootScope.startSpinner();

        urlParams = {
            portOperatorId : $scope.formSearch.portOperator,
            transactionType : $scope.formSearch.transactionType,
            period : $scope.formSearch.period
        };

        systemService.getBookingSlotLimitMap(urlParams, function(response) {

            $scope.bookingSlotLimitMap = response.dataMap;
            $scope.daysWeek = response.dataList;
            $scope.searchForm.$setPristine();
            $scope.isLoaded = true;

            $rootScope.stopSpinner();

        }, function(error) {
            $scope.errorHandler(error);
        });
    }

    $scope.cancelCell = function(cell) {
        cell.newBookingLimit = cell.bookingLimit;
        cell.edit = false;
    };

    $scope.saveCell = function(cell) {

        $rootScope.startSpinner();

        var dayWeek = $filter('filter')($scope.daysWeek, {
            id : cell.dayOfWeekId
        });

        urlParams = {
            portOperatorId : cell.portOperatorId,
            transactionType : cell.transactionType,
            dayOfWeekId : cell.dayOfWeekId,
            dayOfWeekName : cell.dayOfWeekName,
            hourSlotFrom : cell.hourSlotFrom,
            hourSlotTo : cell.hourSlotTo,
            bookingLimit : cell.newBookingLimit,
            period : $scope.formSearch.period == "DEFAULT" ? $scope.formSearch.period : dayWeek[0].dateString
        };

        systemService.saveBookingSlotLimit(urlParams, function(response) {

            cell.edit = false;
            cell.bookingLimit = cell.newBookingLimit;

            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_BOOKING_LIMIT_UPDATED_MESSAGE + ".");

            $rootScope.stopSpinner();

        }, function(error) {
            cell.edit = false;
            cell.newBookingLimit = cell.bookingLimit;
            $scope.errorHandler(error);
        });
    };

    $scope.saveBulk = function() {

        $rootScope.startSpinner();
        
        urlParams = {
            portOperatorId : $scope.formSearch.portOperator,
            transactionType : $scope.formSearch.transactionType,
            dayOfWeekId : $scope.formData.dayOfWeek.id,
            dayOfWeekName : $scope.formData.dayOfWeek.label,
            hourSlotFrom : $scope.formData.hourSlotFrom,
            hourSlotTo : $scope.formData.hourSlotTo,
            bookingLimit : $scope.formData.newBookingLimit,
            period : $scope.formSearch.period == "DEFAULT" ? $scope.formSearch.period : $scope.formData.dayOfWeek.dateString
        };

        systemService.saveBookingSlotLimit(urlParams, function(response) {

            $scope.closeModal();
            $scope.search();

            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_BOOKING_LIMIT_UPDATED_MESSAGE + ".");

            $rootScope.stopSpinner();

        }, function(error) {
            $scope.closeModal();
            $scope.search();
            $scope.errorHandler(error);
        });
    };

    $scope.getTransactionTypes = function() {

        var portOperatorId = parseInt($scope.formSearch.portOperator);
        $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];
    };


    $scope.isBookingLimitInvalid = function(cell) {
        return cell.newBookingLimit == cell.bookingLimit || cell.newBookingLimit == undefined || cell.newBookingLimit == null || cell.newBookingLimit < 0;
    }
    
    $scope.getNegativeClass = function(cell) {

        if (cell === undefined || cell === "" || cell === null) {
            return '';

        } else {
            if (cell.bookingLimit - cell.bookingCount < 0) {
                return 'div-bl-remaining-negative';
            } else {
                return '';
            }
        }
    }

    $scope.$watch('formSearch.portOperator', function() {
        if ($scope.formSearch.portOperator !== undefined && $scope.formSearch.portOperator !== null && $scope.formSearch.portOperator !== "") {
            $scope.getTransactionTypes();
        }
    });

} ]);
