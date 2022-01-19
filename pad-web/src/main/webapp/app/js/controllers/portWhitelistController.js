padApp.controller('PortWhitelistController', [ '$scope', '$rootScope', '$filter', 'portEntryService', 'systemService', 'transactionTypeConstants',
    function($scope, $rootScope, $filter, portEntryService, systemService, transactionTypeConstants) {

        $rootScope.activeNavbar('#navbarPortWhitelist');

        $scope.formData = {};
        $scope.createButtonDisabled = false;
        $scope.createErrorResponse = "";
        $scope.selectedVehicles = [];
        $scope.portOperatorGates = [];
        $scope.portOperatorIdentifier = $rootScope.portOperatorId;
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
                portOperatorId : $scope.portOperatorIdentifier,
                dateFromString : $scope.filterDateFromString,
                dateToString : $scope.filterDateToString,
                vehicleRegistration : $scope.filterVehicleRegistration
            };

            if (angular.isDefined($scope.filtersForm))
                $scope.isFilterActive = !$scope.filtersForm.$pristine;

            portEntryService.countWhitelist(urlParams, $scope.getCountCallBack, $scope.errorHandler);

        }, function(currentPage, pageCount) {

            //get Data

            $scope.sortType = null;
            $scope.sortReverse = false;

            var urlParams = {
                portOperatorId : $scope.portOperatorIdentifier,
                dateFromString : $scope.filterDateFromString,
                dateToString : $scope.filterDateToString,
                vehicleRegistration : $scope.filterVehicleRegistration,
                sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage : currentPage,
                pageCount : pageCount
            };

            portEntryService.listWhitelist(urlParams, $scope.getCallBack, $scope.errorHandler);

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

        $scope.search = function() {
            $scope.refreshTableData();
        };

        $scope.clearSearch = function() {

            $scope.filterDateFromString = null;
            $scope.filterDateToString = null;
            $scope.filterVehicleRegistration = null;
            $scope.filtersForm.$setPristine();
            $scope.refreshTableData();
        };

        $scope.showCreateModal = function() {

            var tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);

            $scope.dateFrom = $filter('date')(tomorrow, "dd/MM/yyyy");
            $scope.dateTo = $filter('date')(tomorrow, "dd/MM/yyyy");
            $('.whitelist_date_from.date').datepicker("setDate", tomorrow);
            $('.whitelist_date_to.date').datepicker("setDate", tomorrow);

            $scope.formData.timeFrom = "0000";
            $scope.formData.timeTo = "0600";

            $scope.formData.vehicleRegistration = "";

            $('#createModal').modal('show');
        }

        $scope.closeCreateDialog = function() {

            $scope.portOperatorGates = [];
            $scope.formData = {};
            $scope.dateFrom = "";
            $scope.dateTo = "";
            $scope.formData.timeFrom = "";
            $scope.formData.timeTo = "";
            $scope.createErrorResponse = "";
            $scope.selectedVehicles = [];

            $('#createModal').modal('hide');
        }

        $scope.addVehicle = function() {
            if ($scope.selectedVehicles.indexOf($scope.formData.vehicleRegistration) === -1) {
                $scope.selectedVehicles.push($scope.formData.vehicleRegistration);
                $scope.formData.vehicleRegistration = "";
                $( "#vehicleRegistration" ).focus();
                $scope.createErrorResponse = "";
            }
        }

        $scope.deleteVehicle = function(regNumber) {
            for (var i = 0; i < $scope.selectedVehicles.length; i++) {
                if ($scope.selectedVehicles[i] === regNumber) {
                    $scope.selectedVehicles.splice(i, 1);
                }
            }
        }

        $scope.create = function() {

            $scope.createForm.$setPristine();

            if ($scope.createForm.dateFrom.$invalid) {
                $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_DATE_FROM_MESSAGE + ".";

            } else if ($scope.createForm.timeFrom.$invalid) {
                $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_TIME_FROM_MESSAGE + ".";

            } else if ($scope.createForm.dateTo.$invalid) {
                $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_DATE_TO_MESSAGE + ".";

            } else if ($scope.createForm.timeTo.$invalid) {
                $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_TIME_TO_MESSAGE + ".";

            } else if ($scope.portOperatorGates.length > 1 && $scope.createForm.gateId.$invalid) {
                $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PORT_OPERATOR_GATE_MESSAGE + ".";

            } else if ($scope.selectedVehicles.length == 0) {
                $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_VEHICLE_REGISTRATION_MESSAGE + ".";

            } else {
                $rootScope.startSpinner();
                $scope.createErrorResponse = "";
                $scope.createButtonDisabled = true;

                var params = {
                    portOperatorId : $scope.portOperatorIdentifier,
                    gateId : $rootScope.getGateIdFromSelectedPortOperatorAndTransactionType($scope.portOperatorIdentifier, undefined),
                    dateFromString : $scope.dateFrom,
                    timeFromString : $scope.formData.timeFrom,
                    dateToString : $scope.dateTo,
                    timeToString : $scope.formData.timeTo,
                    vehicleRegistrationArray : $scope.selectedVehicles
                };

                portEntryService.createWhitelist(params, function(response) {

                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_WHITELIST_CREATED_SUCCESS_MESSAGE + ".");
                    $rootScope.stopSpinner();

                    $scope.createButtonDisabled = false;
                    $scope.closeCreateDialog();
                    $scope.refreshTableData();

                }, function(error) {

                    $rootScope.stopSpinner();

                    if (error.data.responseCode == 1194) {
                        $scope.createErrorResponse = $rootScope.translation.KEY_SCREEN_VEHICLE_ALREADY_WHITELISTED_MESSAGE + ". " + error.data.data;

                    } else {
                        $scope.createErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    }

                    $scope.createButtonDisabled = false;
                });
            }
        }

        $scope.deleteWhitelist = function(code) {

            var params = {
                code : code
            };

            portEntryService.deleteWhitelist(params, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_WHITELIST_DELETED_SUCCESS_MESSAGE + ".");
                $rootScope.stopSpinner();

                $scope.refreshTableData();

            }, function(error) {
                $rootScope.stopSpinner();
                $scope.createErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });
        }

        $scope.$watch('dateFrom', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.whitelist_date_to.date').datepicker("setStartDate", newVal);
            }
        }, true);

        $scope.$watch('dateTo', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.whitelist_date_from.date').datepicker("setEndDate", newVal);
            }
        }, true);

        $scope.$watch('filterDateFromString', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.filter_whitelist_date_to.date').datepicker("setStartDate", newVal);
            }
        }, true);

        $scope.$watch('filterDateToString', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.filter_whitelist_date_from.date').datepicker("setEndDate", newVal);
            }
        }, true);

        $scope.refreshTableData();

} ]);
