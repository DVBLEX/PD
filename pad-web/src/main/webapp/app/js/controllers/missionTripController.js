padApp.controller('MissionTripController', [
    '$scope',
    '$rootScope',
    '$timeout',
    '$location',
    'accountDetailsFactory',
    'tripService',
    'systemService',
    'vehicleService',
    function($scope, $rootScope, $timeout, $location, accountDetailsFactory, tripService, systemService, vehicleService) {

        $rootScope.activeNavbar('#navbarMissionTrip');

        $scope.formData = {};
        $scope.result = {};
        $scope.formData.operatorType = $rootScope.portOperatorConstants.PORT_OPERATOR_DPWORLD;
        $scope.filterPortOperator = "";
        $scope.independentOperators = [];
        $scope.filterIndependentOperatorName = "";
        $scope.filterTransactionType = -1;
        $scope.filterStatus = "-1";
        $scope.missionTripSearchErrorResponse = "";
        $scope.missionTripSaveErrorResponse = "";
        $scope.createTripInfoMessage = "";
        $scope.createTripSuccessResponse = "";
        $scope.filterTimeSlotFromString = "0000";
        $scope.filterTimeSlotToString = "0000";
        $scope.validateButtonDisabled = false;
        $scope.addMissionTripSubmitButtonDisabled = false;
        $scope.searchTripSubmitButtonDisabled = false;
        $scope.isApprove = false;
        $scope.isResultFound = false;
        $scope.isVehicleFound = false;
        $scope.tripBookingTimeJsonList = [];
        $scope.sortColumn = "";
        $scope.sortAsc = true;

        $scope.accountvehicles = accountDetailsFactory.getVehicleDetails();
        $scope.accountdrivers = accountDetailsFactory.getDriverDetails();

        $scope.padTable = new PADTable(function() {

            $rootScope.startSpinner();

            //get Data count

            var urlParams = {
                portOperatorId : parseInt($scope.filterPortOperator),
                independentPortOperatorName : $scope.filterIndependentOperatorName,
                transactionType : parseInt($scope.filterTransactionType),
                referenceNumber : $scope.filterReferenceNumber,
                containerId : "",
                status : $scope.filterStatus,
                vehicleRegistration : $scope.filterVehicleRegistrationNumber,
                dateSlotApprovedFromString : $scope.filterDateSlotFromString,
                dateSlotApprovedToString : $scope.filterDateSlotToString,
                timeSlotApprovedFromString : [ $scope.filterTimeSlotFromString.slice(0, 2), ":", $scope.filterTimeSlotFromString.slice(2) ].join(''),
                timeSlotApprovedToString : [ $scope.filterTimeSlotToString.slice(0, 2), ":", $scope.filterTimeSlotToString.slice(2) ].join('')
            };

            if (angular.isDefined($scope.missionTripFiltersForm))
                $scope.isFilterActive = !$scope.missionTripFiltersForm.$pristine;

            tripService.countTrips(urlParams, $scope.getCountCallBack, $scope.errorHandler);

        }, function(currentPage, pageCount) {

            //get Data

            $scope.sortType = null;
            $scope.sortReverse = false;

            var urlParams = {
                portOperatorId : parseInt($scope.filterPortOperator),
                independentPortOperatorName : $scope.filterIndependentOperatorName,
                transactionType : parseInt($scope.filterTransactionType),
                referenceNumber : $scope.filterReferenceNumber,
                containerId : "",
                status : $scope.filterStatus,
                vehicleRegistration : $scope.filterVehicleRegistrationNumber,
                dateSlotApprovedFromString : $scope.filterDateSlotFromString,
                dateSlotApprovedToString : $scope.filterDateSlotToString,
                timeSlotApprovedFromString : [ $scope.filterTimeSlotFromString.slice(0, 2), ":", $scope.filterTimeSlotFromString.slice(2) ].join(''),
                timeSlotApprovedToString : [ $scope.filterTimeSlotToString.slice(0, 2), ":", $scope.filterTimeSlotToString.slice(2) ].join(''),
                sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage : currentPage,
                pageCount : pageCount
            };

            tripService.listTrips(urlParams, $scope.getCallBack, $scope.errorHandler);
        });

        $scope.errorHandler = function(error) {

            $rootScope.stopSpinner();
            console.log(error);
        };

        $scope.getCallBack = function(response) {

            $scope.padTable.setData(response.dataList);
            $scope.searchTripSubmitButtonDisabled = false;
            $rootScope.stopSpinner();
        }

        $scope.getCountCallBack = function(response) {

            $scope.padTable.setCount(response.dataList[0]);
            $scope.searchTripSubmitButtonDisabled = false;

            // below check is used to open up "add trip" dialog box automatically
            if ($rootScope.showAddMissionTripModal) {

                $scope.showMissionTripDialog(1);
                $rootScope.showAddMissionTripModal = false;
            }

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

        $scope.searchMissionTrip = function() {

            $scope.searchTripSubmitButtonDisabled = true;
            $scope.refreshTableData();
        };

        $scope.clearSearchMissionTrip = function() {

            $scope.filterPortOperator = "";
            $scope.filterIndependentOperatorName = "";
            $scope.filterTransactionType = -1;
            $scope.filterReferenceNumber = "";
            $scope.filterContainerId = "";
            $scope.filterStatus = "-1";
            $scope.filterVehicleRegistrationNumber = "";
            $scope.filterDateSlotFromString = "";
            $scope.filterDateSlotToString = "";
            $scope.filterTimeSlotFromString = "0000";
            $scope.filterTimeSlotToString = "0000";

            $scope.refreshTableData();
        };

        $scope.showMissionTripDialog = function(actionType) {

            // actionType 1 = add trip
            // actionType 2 = edit trip
            // actionType 3 = approve trip

            $scope.actionType = actionType;

            if (actionType == 1) {
                // add
                $scope.modalTitle = $rootScope.translation.KEY_SCREEN_ADD_TRIP_LABEL;
                $scope.isApprove = false;

                $('#missionTripModal').modal('show');

            } else if (actionType == 2) {
                // edit
                $scope.modalTitle = $rootScope.translation.KEY_SCREEN_EDIT_LABEL;
                $scope.isApprove = false;
                $('#missionTripModal').modal('show');

            }else if (actionType == 3) {
                // approve
                $scope.modalTitle = $rootScope.translation.KEY_SCREEN_APPROVE_LABEL;
                $scope.isApprove = true;
                $('#missionTripModal').modal('show');

            } else {
                return;
            }
        }

        $scope.closeMissionTripDialog = function() {

            $scope.resetMissionTripSearch();

            $('#missionTripModal').modal('hide');
        }

        $scope.resetMissionTripSearch = function() {

            $scope.missionTripSearchErrorResponse = "";
            $scope.missionTripSaveErrorResponse = "";
            $scope.createTripSuccessResponse = "";
            $scope.createTripInfoMessage = "";
            $scope.formData = {};
            $scope.formData.operatorType = $rootScope.portOperatorConstants.PORT_OPERATOR_DPWORLD;
            $scope.formData.referenceNumber = "";
            $scope.isResultFound = false;
            $scope.isVehicleFound = false;
            $scope.isApprove = false;
            $scope.result = {};
            $scope.dateSlot = "";
            $scope.tripBookingTimeJsonList = [];
        }

        $scope.validateReferenceNumber = function() {

            if ($scope.missionTripForm.operatorType.$invalid) {
                $scope.missionTripSearchErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_OPERATOR_MESSAGE + ".";

            } else if ($scope.missionTripForm.referenceNumber.$invalid) {

                $scope.missionTripSearchErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_BAD_NUMBER_MESSAGE + ".";

            } else {
                $rootScope.startSpinner();

                $scope.validateButtonDisabled = true;
                $scope.missionTripSearchErrorResponse = "";

                var tripData = {
                    portOperatorId : parseInt($scope.formData.operatorType),
                    referenceNumber : $scope.formData.referenceNumber
                };

                tripService.validateReferenceNumber(tripData, function(response) {

                    $scope.validateButtonDisabled = false;

                    $scope.result = response.dataList[0];
                    $scope.result.transactionType = $scope.result.transactionType + '';
                    $scope.isResultFound = true;

                    $scope.accountvehicles = accountDetailsFactory.getVehicleDetails();
                    $scope.accountdrivers = accountDetailsFactory.getDriverDetails();

                    var dateFromParts = $scope.result.dateMissionStartString.substring(0,10).split("/");
                    var dateToParts = $scope.result.dateMissionEndString.substring(0,10).split("/");

                    // month is 0-based, that's why we need dataParts[1] - 1
                    var dateFromLimit = new Date(+dateFromParts[2], dateFromParts[1] - 1, +dateFromParts[0]);
                    var dateToLimit = new Date(+dateToParts[2], dateToParts[1] - 1, +dateToParts[0]);

                    $('.slot_start_date.date').datepicker("setStartDate", dateFromLimit);
                    $('.slot_start_date.date').datepicker("setEndDate", dateToLimit);

                    $rootScope.stopSpinner();

                }, function(error) {

                    $scope.missionTripSearchErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    $scope.validateButtonDisabled = false;
                    $scope.errorHandler(error);
                });
            }
        };

        $scope.getAccountDriverAndVehicleList = function() {

            var urlParams = {};

            systemService.getAccountDriverAndVehicleList(urlParams, function(response) {

                accountDetailsFactory.setVehicleDetails(response.dataList[0]);
                accountDetailsFactory.setDriverDetails(response.dataList[1]);

            }, function(error) {

                $scope.errorHandler(error);
            });
        };

        $scope.getAccountDriverAndVehicleList();

        $scope.addMissionTrip = function() {

            $rootScope.startSpinner();
            $scope.addMissionTripSubmitButtonDisabled = true;

            var vehicleReg = "";
            var dateSlot = ""
            var timeSlot = ""

            for (var i = 0; i < $scope.accountvehicles.length; i++) {
                if ($scope.formData.vehicle === $scope.accountvehicles[i].code) {
                    vehicleReg = $scope.accountvehicles[i].vehicleRegistration;
                    break;
                }
            }

            dateSlot = $scope.dateSlot;
            timeSlot = $scope.formData.timeSlot;

            var tripData = {
                vehicleRegistration : vehicleReg,
                referenceNumber : $scope.result.referenceNumber,
                containerId : $scope.result.containerId,
                portOperatorId : $scope.result.portOperatorId,
                transactionType : $scope.result.transactionType,
                driverCode : $scope.formData.driver,
                vehicleCode : $scope.formData.vehicle,
                dateSlotString : dateSlot,
                timeSlotString : timeSlot,
                dateMissionEndString : $scope.result.dateMissionEndString,
                missionCode : $scope.result.missionCode
            };

            tripService.addTrip(tripData, function(response) {

                $scope.createTripInfoMessage = "";
                $scope.missionTripSaveErrorResponse = "";
                $scope.formData.vehicle = "";
                $scope.formData.driver = "";
                $scope.dateSlot = "";
                $scope.formData.timeSlot = "";
                $scope.createTripSuccessResponse = $rootScope.translation.KEY_SCREEN_TRIP_ADDED_SUCCESS_MESSAGE + ".";

                var timer;
                $timeout.cancel(timer);
                timer = $timeout(function() {
                    $scope.createTripSuccessResponse = "";
                }, 4500);

                $rootScope.stopSpinner();

                $scope.addMissionTripSubmitButtonDisabled = false;
                $scope.refreshTableData();

            }, function(error) {

                $scope.errorHandler(error);
                $scope.missionTripSaveErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.addMissionTripSubmitButtonDisabled = false;

                // check if slot date is outside the mission start end timeframe. If it is show additional info on error message.
                if (error.data.responseCode === 1143) {
                    $scope.missionTripSaveErrorResponse += " [" + $scope.result.dateMissionStartString + " - " + $scope.result.dateMissionEndString + "]";

                    // check if vehicle was already booked on another trip thats active
                } else if (error.data.responseCode === 1198) {
                    $scope.missionTripSaveErrorResponse = error.data.data;
                }
            });
        };

        $scope.cancelTrip = function(row) {

            $rootScope.showGenericConfirmationModal($rootScope.translation.KEY_SCREEN_TRIP_CANCEL_ALERT_MESSAGE, function() {

                if (row.status === $rootScope.tripConstants.TRIP_STATUS_PENDING_APPROVAL 
                    || row.status === $rootScope.tripConstants.TRIP_STATUS_APPROVED
                    || row.status == $rootScope.tripConstants.TRIP_STATUS_IN_FLIGHT
                    || (row.status == $rootScope.tripConstants.TRIP_STATUS_IN_TRANSIT && (row.isDirectToPort || row.isAllowMultipleEntries))
                    || (row.isAllowMultipleEntries && row.isDirectToPort && row.status == $rootScope.tripConstants.TRIP_STATUS_ENTERED_PORT)) {

                    var urlParams = {
                        code : row.code
                    };

                    tripService.cancelTrip(urlParams, function(response) {

                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");

                        $scope.refreshTableData();

                    }, function(error) {
                        $scope.errorHandler(error);
                        $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");

                    });
                }
            });
        };

        $scope.approveTrip = function(row) {

            $scope.validateButtonDisabled = false;

            $scope.result = row;
            $scope.formData.operatorType = $scope.result.portOperatorId;
            $scope.formData.referenceNumber = $scope.result.referenceNumber;
            $scope.formData.tripDateSlot = $scope.result.dateSlotString.substring(0,10);
            $scope.formData.tripTimeSlot = $scope.result.dateSlotString.substring(11);
            $scope.result.transactionType = $scope.result.transactionType + '';

            $scope.isResultFound = true;

            $scope.showMissionTripDialog(3);

            $scope.accountdrivers = accountDetailsFactory.getDriverDetails();

        };

         $scope.approveMissionTrip = function() {

            if ($scope.missionTripForm.driver.$invalid) {
                $scope.missionTripSearchErrorResponse = $rootScope.translation.KEY_SCREEN_VEHICLES_DRIVERS_LABEL + ".";

            } else {

                var urlParams = {
                    code : $scope.result.code,
                    driverCode : $scope.formData.driver
                };

                tripService.approveTrip(urlParams, function(response) {

                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_TRIP_APPROVED_SUCCESS_MESSAGE + ".");

                    $scope.refreshTableData();

                    $scope.closeMissionTripDialog();

                }, function(error) {
                    $scope.errorHandler(error);
                    $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
                });
            }
        };

        $scope.rejectMissionTrip = function() {

            $rootScope.showGenericConfirmationModal($rootScope.translation.KEY_SCREEN_TRIP_REJECTED_ALERT_MESSAGE, function() {

                var urlParams = {
                    code : $scope.result.code
                };

                tripService.rejectTrip(urlParams, function(response) {

                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_TRIP_REJECTED_SUCCESS_MESSAGE + ".");

                    $scope.refreshTableData();

                    $scope.closeMissionTripDialog();

                }, function(error) {
                    $scope.errorHandler(error);
                    $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
                });

            });
        };


        $scope.updateTripInfoMessage = function() {

            // check if allowed multiple port entries
            if ($scope.dateSlot !== undefined && $scope.dateSlot !== null && $scope.dateSlot !== ""
                && $scope.formData.timeSlot !== undefined && $scope.formData.timeSlot !== null && $scope.formData.timeSlot !== ""
                && $scope.result.dateMissionEndString !== "" && $scope.result.isDirectToPort && $scope.result.isAllowMultipleEntries) {

                var slotTimeStr = $scope.formData.timeSlot.substring(0,2) + ":" + $scope.formData.timeSlot.substring(2);

                $scope.createTripInfoMessage = $rootScope.translation.KEY_SCREEN_TRIP_MULTIPLE_PORT_ENTRIES_MESSAGE + " "
                + $scope.dateSlot + " " + slotTimeStr + " " + $rootScope.translation.KEY_SCREEN_DATE_TO_LABEL + " " + $scope.result.dateMissionEndString + ". "
                + $rootScope.translation.KEY_VEHICLE_NOT_REQUIRED_TO_ENTER_PARKING_MESSAGE + ".";

            // check if allowed to go direct to port and enter port once
            } else if ($scope.result.isDirectToPort && !$scope.result.isAllowMultipleEntries) {

                $scope.createTripInfoMessage = $rootScope.translation.KEY_SCREEN_TRIP_SINGLE_PORT_ENTRY_MESSAGE + ". "
                + $rootScope.translation.KEY_VEHICLE_NOT_REQUIRED_TO_ENTER_PARKING_MESSAGE + ".";

            // check if allowed to enter parking multiple times and go to port
            } else if ($scope.dateSlot !== undefined && $scope.dateSlot !== null && $scope.dateSlot !== ""
                && $scope.formData.timeSlot !== undefined && $scope.formData.timeSlot !== null && $scope.formData.timeSlot !== ""
                && $scope.result.dateMissionEndString !== "" && !$scope.result.isDirectToPort && $scope.result.isAllowMultipleEntries) {

                var slotTimeStr = $scope.formData.timeSlot.substring(0,2) + ":" + $scope.formData.timeSlot.substring(2);

                $scope.createTripInfoMessage = $rootScope.translation.KEY_SCREEN_TRIP_MULTIPLE_PARKING_ENTRIES_MESSAGE + " "
                + $scope.dateSlot + " " + slotTimeStr + " " + $rootScope.translation.KEY_SCREEN_DATE_TO_LABEL + " " + $scope.result.dateMissionEndString + ". "
                + $rootScope.translation.KEY_VEHICLE_REQUIRED_TO_ENTER_PARKING_MESSAGE + ".";

            // check if its a single trip that must go to parking before enter the port once
            } else if (!$scope.result.isDirectToPort && !$scope.result.isAllowMultipleEntries) {

                $scope.createTripInfoMessage = $rootScope.translation.KEY_VEHICLE_REQUIRED_TO_ENTER_PARKING_SINGLE_MESSAGE + ".";

            } else {
                $scope.createTripInfoMessage = "";
            }
        };

        $scope.openActivateVehicleModal = function() {

            $scope.activateVehicleFormData = {};

            $('#activateVehicleModal').modal('show');
        }

        $scope.closeActivateVehicleModal = function() {

            $scope.activateVehicleFormData = {};

            $('#activateVehicleModal').modal('hide');
        }

        $scope.activate = function() {

            if($scope.result.isVehicleAddedApi && !$scope.result.isVehicleApproved){
                $scope.openActivateVehicleModal();
            }else{
                $scope.activateVehicle();
            }
        }


        $scope.activateApiVehicle = function() {

            var vehicleData = {
                code : $scope.result.vehicleCode,
                registrationCountryISO : $scope.activateVehicleFormData.regCountry,
                make : $scope.activateVehicleFormData.make,
                color : $scope.activateVehicleFormData.color,
                isActive : true
            };

            vehicleService.updateVehicle(vehicleData, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_VEHICLE_ACTIVATED_SUCCESSFULLY_MESSAGE + ".");

                $scope.result.isVehicleActive = true;
                $scope.result.isVehicleApproved = true;

                $scope.closeActivateVehicleModal();

                $scope.refreshTableData();

            }, function(error) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ERROR_LABEL + ".");
            });

        }

        $scope.activateVehicle = function() {

            var urlParams = {
                code : $scope.result.vehicleCode,
                isActive : true
            };

            vehicleService.activateVehicle(urlParams, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_VEHICLE_ACTIVATED_SUCCESSFULLY_MESSAGE + ".");

                $scope.result.isVehicleActive = true;
                $scope.result.isVehicleApproved = true;

                $scope.refreshTableData();

            }, function(error) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ERROR_LABEL + ".");
            });

        }

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

        $scope.$watch('filterDateSlotFromString', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.slot_to_date.date').datepicker("setStartDate", newVal);
            }
        }, true);

        $scope.$watch('filterDateSlotToString', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.slot_from_date.date').datepicker("setEndDate", newVal);
            }
        }, true);

        $scope.$watch('dateSlot', function(newVal, oldVal) {
            if (newVal != oldVal) {

                // get list of available slot hours
                if (newVal !== undefined && newVal !== "" && $scope.formData.operatorType !== undefined && $scope.formData.operatorType !== ""
                    && $scope.result.transactionType !== undefined && $scope.result.transactionType !== "") {

                    $rootScope.startSpinner();

                    var urlParams = {
                        portOperatorId : parseInt($scope.formData.operatorType),
                        transactionType : parseInt($scope.result.transactionType),
                        dateSlotString : newVal
                    };

                    tripService.getAvailableBookingHours(urlParams, function(response) {

                        $scope.tripBookingTimeJsonList = response.dataList;

                        $rootScope.stopSpinner();

                    }, function(error) {

                        $scope.tripBookingTimeJsonList = [];
                        $scope.errorHandler(error);
                    });

                    $scope.updateTripInfoMessage();
                }
            }
        }, true);

        $scope.$watch('formData.timeSlot', function() {

            if ($scope.formData.timeSlot !== undefined && $scope.formData.timeSlot !== null && $scope.formData.timeSlot !== "") {
                $scope.updateTripInfoMessage();
            }
        });

        $scope.refreshTableData();
    } ]);
