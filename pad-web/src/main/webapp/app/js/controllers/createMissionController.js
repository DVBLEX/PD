padApp.controller('CreateMissionController', [ '$scope', '$rootScope', '$timeout', 'missionService', 'accountService', 'systemService',
    function($scope, $rootScope, $timeout, missionService, accountService, systemService) {

        $rootScope.activeNavbar('#navbarMissions');

        $scope.formData = {};
        $scope.createMissionButtonDisabled = false;
        $scope.searchMissionSubmitButtonDisabled = false;
        $scope.createMissionErrorResponse = "";
        $scope.createMissionSuccessResponse = "";
        $scope.createMissionInfoMessage = "";
        $scope.accountNamesMap = [];
        $scope.accountNamesList = [];
        $scope.portOperatorTransactionTypes = [];
        $scope.selectedTransporters = [];
        $scope.filterReferenceNumber = "";
        $scope.filterPortOperator = "";
        $scope.filterTransactionType = -1;
        $scope.filterStatus = "";
        $scope.sortColumn = "";
        $scope.sortAsc = true;
        $scope.independentOperators = [];

        $scope.errorHandler = function(error) {
            $rootScope.stopSpinner();
            console.log(error);
        };

        $scope.padTable = new PADTable(function() {

            $rootScope.startSpinner();

            var urlParams = {
                portOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.portOperatorId : $scope.filterPortOperator,
                independentPortOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.independentPortOperatorId : null,
                referenceNumber : $scope.filterReferenceNumber,
                transactionType : parseInt($scope.filterTransactionType),
                status : $scope.filterStatus
            };

            missionService.countMissions(urlParams, $scope.getCountCallBack, $scope.getCountCallBackError);

        }, function(currentPage, pageCount) {

            var urlParams = {
                portOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.portOperatorId : $scope.filterPortOperator,
                independentPortOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.independentPortOperatorId : null,
                referenceNumber : $scope.filterReferenceNumber,
                transactionType : parseInt($scope.filterTransactionType),
                status : $scope.filterStatus,
                sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage : currentPage,
                pageCount : pageCount
            };
            $rootScope.startSpinner();

            missionService.listMissions(urlParams, $scope.getCallBack, $scope.getCallBackError);

        });

        $scope.getCallBack = function(response) {

            $scope.padTable.setData(response.dataList);
            $scope.searchMissionSubmitButtonDisabled = false;

            $rootScope.stopSpinner();
        }

        $scope.getCallBackError = function(response) {

            $rootScope.stopSpinner();
            console.log(response);
        }

        $scope.getCountCallBack = function(response) {

            $scope.padTable.setCount(response.dataList[0]);
            $scope.searchMissionSubmitButtonDisabled = false;

            $rootScope.stopSpinner();
        }

        $scope.getCountCallBackError = function(response) {

            $rootScope.stopSpinner();
            console.log(response);
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

        $scope.showCreateMissionModal = function() {

            $('#createMissionModal').modal('show');

            if ($rootScope.independentPortOperatorCode !== "") {
                const independentPortOperatorObj = $rootScope.getIndependentOperator($rootScope.portOperatorId, $rootScope.independentPortOperatorCode);
                $scope.formData.independentOperatorName = independentPortOperatorObj.label;
                $scope.formData.independentOperatorCode = independentPortOperatorObj.value;
            }

            $scope.getActiveAccountNames();
        }

        $scope.closeCreateMissionDialog = function() {

            if ($rootScope.isParkingOfficeOperator == 'true') {
                $scope.operatorType = "";
                $scope.independentOperators = [];

            } else {
                $scope.operatorType = $rootScope.portOperatorId;
            }

            $scope.formData.gateNumber = "";
            $scope.formData.transactionType = "";
            $scope.dateFrom = "";
            $scope.dateTo = "";
            $scope.createMissionErrorResponse = "";
            $scope.createMissionErrorCompanyName = "";
            $scope.createMissionSuccessResponse = "";
            $scope.createMissionInfoMessage = "";
            $scope.selectedTransporters = [];

            $('#createMissionModal').modal('hide');
        }

        $scope.addTranporter = function(transporterName) {

            var transporterCode = undefined;

            for ( var accountCode in $scope.accountNamesList) {
                var accountObj = $scope.accountNamesList[accountCode];
                if (accountObj.name === transporterName) {
                    transporterCode = accountObj.code;
                    break;
                }
            }

            if (!transporterCode) {
                return;
            }

            var match = $scope.selectedTransporters.reduce(function(prev, curr) {
                return (transporterCode === curr) || prev;
            }, false);

            if (!match)
                $scope.selectedTransporters.push(transporterCode);

            $scope.formData.companyAccountCode = "";
        };

        $scope.removeTransporter = function(index) {
            $scope.selectedTransporters.splice(index, 1)
        }

        $scope.createMission = function() {

            if ($scope.missionCreateForm.operatorType.$invalid) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_OPERATOR_MESSAGE + ".";

            } else if ($scope.independentOperators.length !== 0 &&
                ($scope.formData.independentOperatorName === undefined || $scope.formData.independentOperatorName === null || $scope.formData.independentOperatorName === '')) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

            } else if ($scope.independentOperators.length !== 0 &&
                ($scope.formData.independentOperatorCode === undefined || $scope.formData.independentOperatorCode === null || $scope.formData.independentOperatorCode === '')) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_INDEPENDENT_PORT_OPERATOR_MESSAGE + ".";

            } else if ($scope.missionCreateForm.transactionType.$invalid) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TRANSACTION_TYPE_MESSAGE + ".";

            } else if ($scope.missionCreateForm.referenceNumber.$invalid) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_BAD_NUMBER_MESSAGE + ".";

            } else if ($scope.missionCreateForm.dateFrom.$invalid) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_START_DATE_MESSAGE + ".";

            } else if ($scope.missionCreateForm.dateTo.$invalid) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_END_DATE_MESSAGE + ".";

            } else if ($scope.selectedTransporters.length <= 0) {
                $scope.createMissionErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_TRANSPORTER_MESSAGE + ".";

            } else {
                $rootScope.startSpinner();
                $scope.createMissionErrorResponse = "";
                $scope.createMissionErrorCompanyName = "";
                $scope.createMissionButtonDisabled = true;

                var dateMissionStartString = "";
                var dateMissionEndString = "";

                if (parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT) {

                    dateMissionStartString = $scope.dateFrom + " " + $rootScope.dropOffEmptyNightMissionStartTime;
                    dateMissionEndString = $scope.dateTo + " " + $rootScope.dropOffEmptyNightMissionEndTime;

                } else if (parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE) {

                    dateMissionStartString = $scope.dateFrom + " " + $rootScope.dropOffEmptyTriangleMissionStartTime;
                    dateMissionEndString = $scope.dateTo + " " + $rootScope.dropOffEmptyTriangleMissionEndTime;

                } else {
                    dateMissionStartString = $scope.dateFrom;
                    dateMissionEndString = $scope.dateTo;
                }

                var missionData = {
                    portOperatorId : $scope.operatorType,
                    independentPortOperatorCode : $scope.formData.independentOperatorCode,
                    gateId : $rootScope.getGateIdFromSelectedPortOperatorAndTransactionType($scope.operatorType, $scope.formData.transactionType),
                    transactionType : parseInt($scope.formData.transactionType),
                    referenceNumber : $scope.formData.referenceNumber,
                    dateMissionStartString : dateMissionStartString,
                    dateMissionEndString : dateMissionEndString,
                    accountCodes : $scope.selectedTransporters
                };

                missionService.createMission(missionData, function(response) {

                    if (parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT
                        || parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE) {
                        // if drop off empty - night/triangle, clear the start/end date from the create mission form
                        $scope.dateFrom = "";
                        $scope.dateTo = "";
                    }

                    $scope.createMissionInfoMessage = "";
                    $scope.createMissionSuccessResponse = $rootScope.translation.KEY_SCREEN_MISSION_CREATED_SUCCESS_MESSAGE + ".";
                    $scope.formData.transactionType = '';
                    $scope.formData.gateNumber = '';
                    $scope.formData.referenceNumber = '';
                    $scope.formData.companyAccountCode = '';
                    $scope.selectedTransporters = [];

                    var timer;
                    $timeout.cancel(timer);
                    timer = $timeout(function() {
                        $scope.createMissionSuccessResponse = "";
                    }, 4500);

                    $rootScope.stopSpinner();

                    $scope.createMissionButtonDisabled = false;
                    $scope.refreshTableData();

                }, function(error) {

                    var regExp = /\(([^)]+)\)/;
                    var matches = regExp.exec(error.data.responseText);

                    $rootScope.stopSpinner();
                    $scope.createMissionErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    if (matches) {
                        $scope.createMissionErrorCompanyName = $scope.accountNamesMap[matches[1]];
                    }
                    $scope.createMissionButtonDisabled = false;
                });
            }
        }

        $scope.cancelMission = function(row) {

            $rootScope.showGenericConfirmationModal($rootScope.translation.KEY_SCREEN_MISSION_CANCEL_ALERT_MESSAGE, function() {

                var urlParams = {
                    code : row.code
                };

                missionService.cancelMission(urlParams, function(response) {

                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");

                    $scope.refreshTableData();

                }, function(error) {
                    $scope.errorHandler(error);
                    $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");

                });
            });
        };

        $scope.getActiveAccountNames = function() {

            accountService.getActiveAccountNames({}, function(response) {

                $scope.accountNamesList = [];

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

        $scope.getTransactionTypes = function() {

            $scope.formData.transactionType = '';
            $scope.formData.gateNumber = '';

            var portOperatorId = parseInt($scope.operatorType);
            $scope.portOperatorTransactionTypes = $rootScope.portOperatorTransactionTypesMap[portOperatorId];
        };

        $scope.searchMission = function() {

            $scope.searchMissionSubmitButtonDisabled = true;
            $scope.refreshTableData();
        };

        $scope.clearSearchMission = function() {

            $scope.filterReferenceNumber = null;
            $scope.filterPortOperator = "";
            $scope.filterTransactionType = -1;
            $scope.filterStatus = "";
            $scope.refreshTableData();
        };

        $scope.clearTransactionType = function () {
            if ($scope.filterPortOperator === "") {
                $scope.filterTransactionType = -1;
            }
        }

        $scope.showTMPortOperators = function(portOperator) {
            if ($rootScope.independentPortOperatorId && $rootScope.portOperatorId === $rootScope.portOperatorConstants.PORT_OPERATOR_TM) {
                return function(portOperator) {
                    return portOperator.name.includes('TM');
                }
            } else {
                return portOperator;
            }

        };

        $scope.$watch('dateFrom', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.mission_date_to.date').datepicker("setStartDate", newVal);
            }

            // if transaction type is drop off empty - night/triangle, for mission start/end time we use different time. mission range is restricted to a single day
            if ($scope.formData.transactionType !== undefined && $scope.formData.transactionType !== null && $scope.formData.transactionType !== ""
            && (parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT
                || parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE)) {

                if ($scope.dateFrom !== undefined && $scope.dateFrom !== null && $scope.dateFrom !== "") {

                    var dateFromPlus1Day = $('.mission_date_from').datepicker('getDate');
                    if (dateFromPlus1Day !== null) {
                        dateFromPlus1Day.setDate(dateFromPlus1Day.getDate()+1);

                        $scope.dateTo = dateFromPlus1Day.toLocaleString('en-GB').split(',')[0];

                        if (parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT) {
                            $scope.createMissionInfoMessage = $rootScope.translation.KEY_SCREEN_MISSION_VALIDITY_PERIOD_MESSAGE + " " + $scope.dateFrom
                                + " " + $rootScope.translation.KEY_SCREEN_DATE_TO_LABEL + " " + $scope.dateTo + " " + $rootScope.translation.KEY_SCREEN_BETWEEN_HOURS_MESSAGE
                                + " " + $rootScope.dropOffEmptyNightMissionStartTime + " " + $rootScope.translation.KEY_SCREEN_AND_MESSAGE + " " + $rootScope.dropOffEmptyNightMissionEndTime;

                        } else if (parseInt($scope.formData.transactionType) === $rootScope.transactionTypeConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE) {
                            $scope.createMissionInfoMessage = $rootScope.translation.KEY_SCREEN_MISSION_VALIDITY_PERIOD_MESSAGE + " " + $scope.dateFrom
                                + " " + $rootScope.translation.KEY_SCREEN_DATE_TO_LABEL + " " + $scope.dateTo + " " + $rootScope.translation.KEY_SCREEN_BETWEEN_HOURS_MESSAGE
                                + " " + $rootScope.dropOffEmptyTriangleMissionStartTime + " " + $rootScope.translation.KEY_SCREEN_AND_MESSAGE + " " + $rootScope.dropOffEmptyTriangleMissionEndTime;
                        }
                    }
                }
            } else {
                $scope.createMissionInfoMessage = "";
            }

        }, true);

        $scope.$watch('dateTo', function(newVal, oldVal) {
            if (newVal != oldVal) {
                $('.mission_date_from.date').datepicker("setEndDate", newVal);
            }
        }, true);

        $scope.$watch('operatorType', function() {

                if ($scope.operatorType !== undefined && $scope.operatorType !== null && $scope.operatorType !== "") {
                    $scope.getTransactionTypes();

                    if (!$rootScope.independentPortOperatorId) {
                        $scope.independentOperators = $rootScope.getIndependentOperators($scope.operatorType);

                        $scope.formData.independentOperatorName = "";
                        $scope.formData.independentOperatorCode = "";

                        $("#independentOperatorName").autocomplete({
                            source: $scope.independentOperators,
                            focus: function (event, ui) {
                                // prevent autocomplete from updating the textbox
                                event.preventDefault();
                                $(this).val(ui.item.label);
                            },
                            select: function (event, ui) {
                                // prevent autocomplete from updating the textbox
                                event.preventDefault();
                                $(this).val(ui.item.label);
                                $scope.formData.independentOperatorName = ui.item.label;
                                $scope.formData.independentOperatorCode = ui.item.value;
                            }
                        });
                    }

                }

        });

        $scope.$watch('formData.transactionType', function() {

            if ($scope.formData.transactionType !== undefined && $scope.formData.transactionType !== null && $scope.formData.transactionType !== "") {
                $scope.formData.gateNumber = $rootScope.getGateNumberFromSelectedPortOperatorAndTransactionType($scope.operatorType, $scope.formData.transactionType);

                $scope.dateFrom = "";
                $scope.dateTo = "";
                $scope.createMissionInfoMessage = "";
            }
        });

        $scope.refreshTableData();
    } ]);
