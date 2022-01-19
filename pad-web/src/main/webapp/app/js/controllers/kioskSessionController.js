padApp.controller('KioskSessionController', [ '$scope', '$rootScope', '$timeout', '$filter', 'operatorService', 'sessionService', 'laneService',
    function($scope, $rootScope, $timeout, $filter, operatorService, sessionService, laneService) {

        $rootScope.activeNavbar('#navbarKioskSession');

        $scope.formData = {};
        $scope.filterStaffName = null;
        $scope.filterType = null;
        $scope.filterLaneNumber = null;
        $scope.filterStatus = null;
        $scope.filterStartDateString = null;
        $scope.createKioskSessionButtonDisabled = false;
        $scope.validateKioskSessionButtonDisabled = false;
        $scope.searchSessionSubmitButtonDisabled = false;
        $scope.submitKioskSessionFormErrorResponse = "";
        $scope.kioskOperatorNamesList = [];
        $scope.amountsList = [];
        $scope.unexpectedEndAmountWarning = '';
        $scope.selectedSession = {};
        $scope.sortColumn = "";
        $scope.sortAsc = true;
        $scope.entryLanesList = [];
        $scope.kioskSessionStats = null;

        $scope.initiateDatepicker = function () {

            $('.filter_date_start.date').datepicker('destroy');

            var currentDate = new Date();
            var dateFirstOfLastMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);
            var dateFirstOfYear = new Date(currentDate.getFullYear() - 1, 0, 1);

            $('.filter_date_start.date').datepicker({
                format: "dd/mm/yyyy",
                defaultViewDate: dateFirstOfLastMonth,
                startDate: dateFirstOfYear,
                endDate: currentDate,
                setDate: dateFirstOfLastMonth,
                todayBtn: "linked",
                autoclose: true,
                todayHighlight: true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });

            $(".filter_date_start.date").datepicker("update", dateFirstOfLastMonth);

            $scope.filterStartDateString = $filter('date')(dateFirstOfLastMonth, "dd/MM/yyyy");

            $("#filter_date_start").val($filter('date')(dateFirstOfLastMonth, "dd/MM/yyyy"));
        };

        $scope.initiateDatepicker();

        $scope.padTable = new PADTable(function() {

            $rootScope.startSpinner();

            var urlParams = {
                status : $scope.filterStatus,
                type : $scope.filterType,
                laneNumber : $scope.filterLaneNumber,
                kioskOperatorName : $scope.filterStaffName,
                dateStartString : $scope.filterStartDateString
            };

            sessionService.getSessionCount(urlParams, $scope.getCountCallBack, $scope.getCountCallBackError);

        }, function(currentPage, pageCount) {

            var urlParams = {
                status : $scope.filterStatus,
                type : $scope.filterType,
                laneNumber : $scope.filterLaneNumber,
                kioskOperatorName : $scope.filterStaffName,
                dateStartString : $scope.filterStartDateString,
                sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage : currentPage,
                pageCount : pageCount
            };
            $rootScope.startSpinner();

            sessionService.getSessions(urlParams, $scope.getCallBack, $scope.getCallBackError);
        });

        $scope.getCallBack = function(response) {

            $scope.padTable.setData(response.dataList);
            $scope.searchSessionSubmitButtonDisabled = false;

            $rootScope.stopSpinner();
        }

        $scope.getCallBackError = function(response) {

            $rootScope.stopSpinner();
            console.log(response);
        }

        $scope.getCountCallBack = function(response) {

            $scope.padTable.setCount(response.dataList[0]);
            $scope.searchSessionSubmitButtonDisabled = false;

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

        $scope.getParkingEntryLanes = function() {

            laneService.getParkingEntryLanes({}, function(response) {

                $scope.entryLanesList = response.dataList;

            }, function(error) {

                $scope.getCallBackError(error);
            });
        }

        $scope.getParkingEntryLanes();

        $scope.getKioskOperatorNames = function() {

            operatorService.getKioskOperatorNames({}, function(response) {

                $scope.kioskOperatorNamesMap = response.dataMap;

                var codes = Object.keys($scope.kioskOperatorNamesMap);

                angular.forEach(codes, function(value, key) {
                    var obj = {};
                    obj.code = value;
                    obj.name = $scope.kioskOperatorNamesMap[value];

                    $scope.kioskOperatorNamesList.push(obj);
                });

                console.log($scope.kioskOperatorNamesList);


            }, function(error) {

                $scope.getCallBackError(error);
            });
        }

        $scope.getKioskOperatorNames();

        $scope.openKioskSessionAddModal = function() {

            $scope.action = "Add";
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_CREATE_KIOSK_SESSION_LABEL;

            $('#kioskSessionModal').modal({
                backdrop : 'static',
                keyboard : false,
                show : true
            });
        }

        $scope.openKioskSessionValidateModal = function(selectedRow) {

            $scope.formData = {};
            $scope.selectedSession = {};
            $scope.kioskSessionStats = null;
            $scope.submitKioskSessionFormErrorResponse = "";
            $scope.unexpectedEndAmountWarning = '';

            $scope.action = "Validate";
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_VALIDATE_KIOSK_SESSION_LABEL;

            $scope.selectedSession = selectedRow;

            // validation step 0 = NOT_VALIDATED
            // validation step 1 = PARTIALLY_VALIDATED
            // validation step 2 = VALIDATED
            if (selectedRow.validationStep === 1) {
                // the finance operator had already set the amount cash returned and then abandoned the session validation process
                $scope.formData.cashAmountEnd = selectedRow.cashAmountEnd;
                $scope.formData.cashAmountEndConfirm = selectedRow.cashAmountEnd;

                $scope.kioskSessionForm.cashAmountEnd.$invalid = false;
                $scope.formData.confirmValidate = true;
                $scope.validateKioskSession();
            }

            $('#kioskSessionModal').modal({
                backdrop : 'static',
                keyboard : false,
                show : true
            });
        }

        $scope.updateKioskSessionValidateModal = function() {

            $scope.action = "Validate Step 2";
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_VALIDATE_KIOSK_SESSION_LABEL;
        }

        $scope.closeKioskSessionDialog = function() {

            $scope.formData = {};
            $scope.selectedSession = {};
            $scope.kioskSessionStats = null;
            $scope.submitKioskSessionFormErrorResponse = "";
            $scope.unexpectedEndAmountWarning = '';

            $scope.refreshTableData();

            $('#kioskSessionModal').modal('hide');
        }

        $scope.saveKioskSession = function() {

            var minAmount = parseInt($rootScope.financeInitialFloatMinAmount, 10);
            var maxAmount = parseInt($rootScope.financeInitialFloatMaxAmount, 10);

            if ($scope.kioskSessionForm.kioskOperatorCode.$invalid) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_OPERATOR_MESSAGE + ".";

            } else if ($scope.kioskSessionForm.type.$invalid) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_TYPE_MESSAGE + ".";

            } else if ($scope.kioskSessionForm.laneId.$invalid && $scope.formData.type == $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING + '') {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_LANE_NUMBER_MESSAGE + ".";

            } else if ($scope.kioskSessionForm.cashAmountStart.$invalid) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_START_AMOUNT_MESSAGE + ".";

            } else if ($scope.formData.cashAmountStart < minAmount) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_MIN_AMOUNT_MESSAGE + $filter('customCurrency')(minAmount, 0) + ".";

            } else if ($scope.formData.cashAmountStart > maxAmount) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_MAX_AMOUNT_MESSAGE + $filter('customCurrency')(maxAmount, 0) + ".";

            } else if ($scope.formData.cashAmountStart % 500 !== 0) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_AMOUNT_MULTIPLE_500_MESSAGE + ".";

            } else if ($scope.kioskSessionForm.cashBagNumber.$invalid) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTERT_CASH_BAG_NUMBER_MESSAGE + ".";

            } else if ($scope.formData.cashBagNumber === undefined || $scope.formData.cashBagNumber === null || $scope.formData.cashBagNumber === "") {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTERT_CASH_BAG_NUMBER_MESSAGE + ".";

            } else {
                $rootScope.startSpinner();
                $scope.submitKioskSessionFormErrorResponse = "";
                $scope.createKioskSessionButtonDisabled = true;

                if ($scope.action === 'Add') {

                    var sessionData = {
                        kioskOperatorCode : $scope.formData.kioskOperatorCode,
                        type : $scope.formData.type,
                        laneId : $scope.formData.type === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING + '' ? $scope.formData.laneId : -1,
                        cashAmountStartString: $scope.formData.cashAmountStart,
                        cashBagNumber: $scope.formData.cashBagNumber
                    };

                    sessionService.addSession(sessionData, function(response) {

                        $scope.submitKioskSessionFormErrorResponse = "";
                        $scope.createKioskSessionButtonDisabled = false;

                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_KIOSK_SESSION_ADDED_SUCCESS_MESSAGE + ".");
                        $rootScope.stopSpinner();

                        $scope.closeKioskSessionDialog();

                    }, function(error) {

                        $rootScope.stopSpinner();

                        $scope.submitKioskSessionFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                        $scope.createKioskSessionButtonDisabled = false;
                    });
                }
            }
        }

        $scope.validateKioskSession = function() {

            if ($scope.kioskSessionForm.cashAmountEnd.$invalid) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_AMOUNT_END_MESSAGE + ".";

            } else if ($scope.formData.cashAmountEnd !== $scope.formData.cashAmountEndConfirm) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_AMOUNT_END_MESSAGE + ".";

            } else if (!$scope.formData.confirmValidate) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_AMOUNT_COUNTED_MESSAGE + ".";

            } else {

                $rootScope.startSpinner();
                $scope.submitKioskSessionFormErrorResponse = "";
                $scope.validateKioskSessionButtonDisabled = true;

                var sessionData = {
                    code : $scope.selectedSession.code,
                    cashAmountEndString: $scope.formData.cashAmountEnd
                };

                sessionService.validateSession(sessionData, function(response) {

                    $scope.submitKioskSessionFormErrorResponse = "";
                    $scope.validateKioskSessionButtonDisabled = false;

                    $rootScope.stopSpinner();

                    $scope.kioskSessionStats = response.dataList[0];
                    $scope.updateKioskSessionValidateModal();

                }, function(error) {

                    $rootScope.stopSpinner();

                    if (error.data.responseCode === 1178) {
                        // AMOUNT_END_NOT_EQUAL_TO_EXPECTED_CASH_AMOUNT_CODE
                        $scope.unexpectedEndAmountWarning = $rootScope.translation.KEY_SCREEN_UNEXPECTED_AMOUNT_RETURNED_REASON_MESSAGE;
                        $scope.kioskSessionStats = error.data.dataList[0];
                        $scope.updateKioskSessionValidateModal();

                    } else if (error.data.responseCode === 1094) {
                        // KIOSK_SESSION_DATA_MISMATCH_CODE
                        $scope.submitKioskSessionFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ". " + error.data.responseText;
                        $scope.kioskSessionStats = error.data.dataList[0];
                        $scope.updateKioskSessionValidateModal();

                    } else {
                        $scope.submitKioskSessionFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    }
                    $scope.validateKioskSessionButtonDisabled = false;
                });
            }
        }

        $scope.validateKioskSessionStep2 = function() {

            if ($scope.unexpectedEndAmountWarning !== '' && ($scope.formData.reason === undefined || $scope.formData.reason === '')) {
                $scope.submitKioskSessionFormErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_REASON_MESSAGE + ".";

            } else if ($scope.unexpectedEndAmountWarning !== '' && ($scope.formData.reason !== undefined && $scope.formData.reason !== '')) {

                $rootScope.startSpinner();
                $scope.submitKioskSessionFormErrorResponse = "";
                $scope.validateKioskSessionButtonDisabled = true;

                var sessionData = {
                    code : $scope.selectedSession.code,
                    cashAmountEndString: $scope.formData.cashAmountEnd,
                    reasonAmountUnexpected : $scope.formData.reason
                };

                sessionService.validateSession(sessionData, function(response) {

                    $scope.submitKioskSessionFormErrorResponse = "";
                    $scope.validateKioskSessionButtonDisabled = false;

                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_KIOSK_SESSION_VALIDATED_SUCCESS_MESSAGE + ".");
                    $rootScope.stopSpinner();

                    $scope.closeKioskSessionDialog();

                }, function(error) {

                    $rootScope.stopSpinner();

                    $scope.submitKioskSessionFormErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                    $scope.validateKioskSessionButtonDisabled = false;
                });

            } else {
                $scope.closeKioskSessionDialog();
            }
        }

        $scope.endKioskSession = function(row) {

            $rootScope.showGenericConfirmationModal($rootScope.translation.KEY_SCREEN_END_KIOSK_SESSION_CONFIRMATION_MESSAGE, function() {

                if (row.status === $rootScope.kioskSessionConstants.KIOSK_SESSION_STATUS_START || row.status === $rootScope.kioskSessionConstants.KIOSK_SESSION_STATUS_ASSIGNED) {

                    var sessionData = {
                        code : row.code
                    };

                    sessionService.endSession(sessionData, function(response) {

                        $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");

                        $scope.refreshTableData();

                    }, function(error) {
                        $scope.errorHandler(error);

                        $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
                    });
                }
            });
        };

        $scope.searchSession = function() {
            $scope.searchSessionSubmitButtonDisabled = true;
            $scope.refreshTableData();
        };

        $scope.clearSearchSession = function() {
            $scope.initiateDatepicker();
            $scope.filterStaffName = null;
            $scope.filterType = null;
            $scope.filterLaneNumber = null;
            $scope.filterStatus = null;
            $scope.padTable.setCount(0);
            $scope.refreshTableData();
        };

        $scope.refreshTableData();

} ]);
