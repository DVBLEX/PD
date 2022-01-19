padApp.controller('HomeController', [ '$scope', '$rootScope', 'systemService', 'statisticsService', 'paymentService', 'portEntryService',
    function($scope, $rootScope, systemService, statisticsService, paymentService, portEntryService) {

    $rootScope.activeNavbar('#navbarHome');

    $scope.yearsList = [];
    $scope.countsData = [];

    var d = new Date();
    $scope.currentYear = d.getFullYear();
    $scope.currentMonth = d.getMonth() + 1;

    if ($rootScope.isParkingKioskOperator == 'true' && $rootScope.isKioskSessionAllowed == 'true'
            && ($rootScope.isKioskSessionEndRequested === undefined
            || $rootScope.isKioskSessionEndRequested === null
            || $rootScope.isKioskSessionEndRequested === "")) {
        $rootScope.go('/payment');

    } else if($rootScope.isPortExitOperator == 'true') {
        $rootScope.go('/portExit');

    } else if($rootScope.isParkingOfficeOperator == 'true') {
        $rootScope.go('/parking');

    } else if($rootScope.isParkingSupervisorOperator == 'true') {
        $rootScope.go('/parkingReleaseView');

    } else if($rootScope.isFinanceOperator == 'true') {
        $rootScope.go('/accounts');

    } else if($rootScope.isAdmin == 'true'){

        $rootScope.onlinePaymentParameterList = [];

        paymentService.getOnlinePaymentParamererList({}, function(response) {
            $rootScope.onlinePaymentParameterList = response.dataList;
        }, function(error) {
            $scope.errorHandler(error);
        });
    }

    if (!$rootScope.selectedZone) {
        $rootScope.selectedZone = "";
    }
    $scope.formDataZone = {};
    $scope.formDataZone.currentZone = "";
    $scope.searchVehicleRegSuccessResponse = "";
    $rootScope.portEntryStatus = true;

    $scope.selectZone = function () {

        if ($scope.formDataZone.currentZone === "") {
            $scope.searchVehicleRegErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_ZONE_MESSAGE + ".";
            return;
        }

        var urlParams = {
            selectedZone : $scope.formDataZone.currentZone,
        };

        portEntryService.selectZone(urlParams, function (response) {

            $rootScope.selectedZone = $scope.formDataZone.currentZone;
            $rootScope.isUpdating = false;
            $scope.searchVehicleRegErrorResponse = "";
            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ZONE_SELECTED_MESSAGE + ".");
            $rootScope.stopSpinner();

           }, function (error) {

            $rootScope.isUpdating = false;
            $scope.searchVehicleRegErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            $scope.errorHandler(error);
        });
    };

    $scope.changePortEntryStatus = function (isStatus) {
        $rootScope.portEntryStatus = isStatus;
    };

    $scope.errorHandler = function(error) {

        $rootScope.stopSpinner();
        console.log(error);
    };

    $scope.formData = {};
    $scope.isUpdating = false;
    $rootScope.onlinePaymentParameterList = [];
    $scope.portOperatorTransactionTypes = ($rootScope.portOperatorId !== undefined && $rootScope.portOperatorId !== null) ? $rootScope.portOperatorTransactionTypesMap[$rootScope.portOperatorId] : [];

    if ($rootScope.isBookingLimitCheckEnabled == 'true') {
        $scope.formData.isBookingLimitCheckEnabled = true;
    } else {
        $scope.formData.isBookingLimitCheckEnabled = false;
    }

    if ($rootScope.isPortEntryFiltering == 'true') {
        $scope.formData.isPortEntryFiltering = true;
    } else {
        $scope.formData.isPortEntryFiltering = false;
    }

    if ($rootScope.autoReleaseExitCapacityPercentage !== null && $rootScope.autoReleaseExitCapacityPercentage !== undefined && $rootScope.autoReleaseExitCapacityPercentage !== '') {
        $scope.formData.autoReleaseExitCapacityPercentage = $rootScope.autoReleaseExitCapacityPercentage + '';
    }

    if ($rootScope.dropOffEmptyNightMissionStartTime !== null && $rootScope.dropOffEmptyNightMissionStartTime !== undefined && $rootScope.dropOffEmptyNightMissionStartTime !== '') {
        $scope.formData.dropOffEmptyNightMissionStartTime = $rootScope.dropOffEmptyNightMissionStartTime;
    }

    if ($rootScope.dropOffEmptyNightMissionEndTime !== null && $rootScope.dropOffEmptyNightMissionEndTime !== undefined && $rootScope.dropOffEmptyNightMissionEndTime !== '') {
        $scope.formData.dropOffEmptyNightMissionEndTime = $rootScope.dropOffEmptyNightMissionEndTime;
    }

    if ($rootScope.dropOffEmptyTriangleMissionStartTime !== null && $rootScope.dropOffEmptyTriangleMissionStartTime !== undefined && $rootScope.dropOffEmptyTriangleMissionStartTime !== '') {
        $scope.formData.dropOffEmptyTriangleMissionStartTime = $rootScope.dropOffEmptyTriangleMissionStartTime;
    }

    if ($rootScope.dropOffEmptyTriangleMissionEndTime !== null && $rootScope.dropOffEmptyTriangleMissionEndTime !== undefined && $rootScope.dropOffEmptyTriangleMissionEndTime !== '') {
        $scope.formData.dropOffEmptyTriangleMissionEndTime = $rootScope.dropOffEmptyTriangleMissionEndTime;
    }


    $scope.saveSystemParamerer = function() {

        if ($scope.formData.isBookingLimitCheckEnabled !== undefined
            && $scope.formData.isBookingLimitCheckEnabled !== null
            && $scope.formData.isPortEntryFiltering !== undefined
            && $scope.formData.isPortEntryFiltering !== null
            && $scope.formData.autoReleaseExitCapacityPercentage !== undefined
            && $scope.formData.autoReleaseExitCapacityPercentage !== null
            && $scope.formData.autoReleaseExitCapacityPercentage !== ''
            && $scope.formData.dropOffEmptyNightMissionStartTime !== undefined
            && $scope.formData.dropOffEmptyNightMissionStartTime !== null
            && $scope.formData.dropOffEmptyNightMissionStartTime !== ''
            && $scope.formData.dropOffEmptyNightMissionEndTime !== undefined
            && $scope.formData.dropOffEmptyNightMissionEndTime !== null
            && $scope.formData.dropOffEmptyNightMissionEndTime !== ''
            && $scope.formData.dropOffEmptyTriangleMissionStartTime !== undefined
            && $scope.formData.dropOffEmptyTriangleMissionStartTime !== null
            && $scope.formData.dropOffEmptyTriangleMissionStartTime !== ''
            && $scope.formData.dropOffEmptyTriangleMissionEndTime !== undefined
            && $scope.formData.dropOffEmptyTriangleMissionEndTime !== null
            && $scope.formData.dropOffEmptyTriangleMissionEndTime !== '') {

            $rootScope.startSpinner();

            $scope.isUpdating = true;

            var systemParamData = {
                isBookingLimitCheckEnabled: $scope.formData.isBookingLimitCheckEnabled,
                isPortEntryFiltering: $scope.formData.isPortEntryFiltering,
                autoReleaseExitCapacityPercentage: parseInt($scope.formData.autoReleaseExitCapacityPercentage),
                dropOffEmptyNightMissionStartTime : $scope.formData.dropOffEmptyNightMissionStartTime,
                dropOffEmptyNightMissionEndTime : $scope.formData.dropOffEmptyNightMissionEndTime,
                dropOffEmptyTriangleMissionStartTime : $scope.formData.dropOffEmptyTriangleMissionStartTime,
                dropOffEmptyTriangleMissionEndTime : $scope.formData.dropOffEmptyTriangleMissionEndTime
            };

            systemService.saveSystemParameter(systemParamData, function(response) {

                $scope.isUpdating = false;

                $scope.formData.isBookingLimitCheckEnabled = response.dataList[0].isBookingLimitCheckEnabled;
                $scope.formData.isPortEntryFiltering = response.dataList[0].isPortEntryFiltering;
                $scope.formData.autoReleaseExitCapacityPercentage = response.dataList[0].autoReleaseExitCapacityPercentage + '';
                $scope.formData.dropOffEmptyNightMissionStartTime = response.dataList[0].dropOffEmptyNightMissionStartTime;
                $scope.formData.dropOffEmptyNightMissionEndTime = response.dataList[0].dropOffEmptyNightMissionEndTime;
                $scope.formData.dropOffEmptyTriangleMissionStartTime = response.dataList[0].dropOffEmptyTriangleMissionStartTime;
                $scope.formData.dropOffEmptyTriangleMissionEndTime = response.dataList[0].dropOffEmptyTriangleMissionEndTime;

                $rootScope.isBookingLimitCheckEnabled = $scope.formData.isBookingLimitCheckEnabled + '';
                $rootScope.isPortEntryFiltering = $scope.formData.isPortEntryFiltering + '';
                $rootScope.autoReleaseExitCapacityPercentage = $scope.formData.autoReleaseExitCapacityPercentage;
                $rootScope.dropOffEmptyNightMissionStartTime = $scope.formData.dropOffEmptyNightMissionStartTime;
                $rootScope.dropOffEmptyNightMissionEndTime = $scope.formData.dropOffEmptyNightMissionEndTime;
                $rootScope.dropOffEmptyTriangleMissionStartTime = $scope.formData.dropOffEmptyTriangleMissionStartTime;
                $rootScope.dropOffEmptyTriangleMissionEndTime = $scope.formData.dropOffEmptyTriangleMissionEndTime;

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");

                $scope.isUpdating = false;
                $scope.errorHandler(error);
            });
        }
    };

    $scope.saveOnlinePaymentParamerer = function(row) {

            $rootScope.startSpinner();

            $scope.isUpdating = true;

            var paramData = {
                mnoId: row.mnoId,
                isActive: row.isActive,
                isPrintReceipt : row.isPrintReceipt
            };

            paymentService.saveOnlinePaymentParamerer(paramData, function(response) {

                $scope.isUpdating = false;

                $rootScope.onlinePaymentParameterList = response.dataList;

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");

                $scope.isUpdating = false;
                $scope.errorHandler(error);
            });
    };

    $rootScope.$watch('translation', function() {

        if ($rootScope.isPortAuthorityOperator == 'true') {

            $scope.yearsList = [];

            for (var y=2019; y<=$scope.currentYear; y++) {
                $scope.yearsList.push(y);
            }

            $scope.getReportButtonDisabled = false;

            if ($rootScope.translation !== undefined && $rootScope.translation !== null && $scope.countsData.length === 0) {

                $scope.getStatistics();
            }
        }
    });

    $scope.getStatistics = function() {

        if ($scope.reportSearchForm.reportType.$invalid) {
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_SELECT_REPORT_TYPE_MESSAGE + ".");

        } else if ($scope.reportSearchForm.monthId.$invalid) {
            $rootScope.showResultMessage(false, $rootScope.translation.translation.KEY_SCREEN_SELECT_MONTH_MESSAGE + ".");

        } else if ($scope.reportSearchForm.year.$invalid) {
            $rootScope.showResultMessage(false, $rootScope.translation.translation.KEY_SCREEN_SELECT_YEAR_MESSAGE + ".");

        } else {

            $rootScope.startSpinner();
            $scope.getReportButtonDisabled = true;

            $scope.reportName = "";
            $scope.reportType = -1;

            switch ($scope.formData.reportType) {

                case $rootScope.reportTypeConstants.REPORT_TYPE_PARKING_ENTRY + '':
                    $scope.reportName = $rootScope.translation.KEY_SCREEN_PARKING_ENTRY_COUNTS_LABEL;
                    $scope.reportType = $rootScope.reportTypeConstants.REPORT_TYPE_PARKING_ENTRY;
                    break;

                case $rootScope.reportTypeConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR + '':
                    $scope.reportName = $rootScope.translation.KEY_SCREEN_PORT_ENTRY_OPERATOR_COUNTS_LABEL;
                    $scope.reportType = $rootScope.reportTypeConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR;
                    break;

                default:
                    $scope.reportName = "";
                    $scope.reportType = -1;
                    break;
            }

            var reportData = {
                reportType : $scope.formData.reportType,
                monthId : $scope.formData.monthId,
                year : $scope.formData.year
            };

            statisticsService.getStatistics(reportData, function(response) {

                $rootScope.stopSpinner();

                $scope.fig1Series = [];
                $scope.countsData = response.dataList[0];

                let statsDataObj = response.dataList[1][0].statsDataMap;
                let statsData = [];
                let statsDataKeys = [];

                for (let key in statsDataObj) {
                    if (key !== "TM" && key !== "ISTAMCO" && key !== "QUAI PECHE" && key !== "DAKAR TERMINAL" && key !== "TM SOUTH") {

                        statsData.push(statsDataObj[key]);
                        statsDataKeys.push(key);
                    }

                    if (+$scope.formData.reportType === $rootScope.reportTypeConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR
                        &&  (key === "QUAI PECHE" || key === "DAKAR TERMINAL" || key === "TM SOUTH")) {

                        statsData.push(statsDataObj[key]);
                        statsDataKeys.push(key);
                    }
                }

                $scope.vehicleEntryData = statsData;
                $scope.fig1Series = statsDataKeys;


                $scope.dayTotalCountParkingEntry = response.dataList[1][0].dayTotalCountParkingEntry;
                $scope.monthTotalCountParkingEntry = response.dataList[1][0].monthTotalCountParkingEntry;
                $scope.yearTotalCountParkingEntry = response.dataList[1][0].yearTotalCountParkingEntry;
                $scope.countInTheParking = response.dataList[1][0].countInTheParking;
                $scope.countInTransit = response.dataList[1][0].countInTransit;
                
                $scope.dayTotalCountPortEntry = response.dataList[1][0].dayTotalCountPortEntry;
                $scope.monthTotalCountPortEntry = response.dataList[1][0].monthTotalCountPortEntry;
                $scope.yearTotalCountPortEntry = response.dataList[1][0].yearTotalCountPortEntry;
                $scope.countEnteredPortLastHour = response.dataList[1][0].countEnteredPortLastHour;
                
                $scope.getReportButtonDisabled = false;
                $scope.fig1Labels = [];

                $scope.fig2Labels = [];
                $scope.fig2Series = [];

                // days in month - x axis
                for (var i=0; i<$scope.vehicleEntryData[0].length; i++) {
                    $scope.fig1Labels.push(i+1);
                    $scope.fig2Labels.push(i+1);
                }

                $scope.range = $rootScope.getMonthName($scope.formData.monthId) + " " + $scope.formData.year;

            }, function(error) {

                $rootScope.stopSpinner();

                $scope.getReportButtonDisabled = false;
                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
    };

    $scope.formatNumberWithCommas = function(num) {

        if (num === undefined || num === null) {
            return 0;

        } else {
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    };

    $scope.getTransactionTypesArray = function(portOperatorId) {

        return $rootScope.portOperatorTransactionTypesMap[portOperatorId];
    };

    $scope.updateTransactionTypeFlags = function (portOperatorId, transactionTypeEntry) {

        var alertMsg = "";
        var wholeNumberRegexp = /^[0-9]+$/;

        if (transactionTypeEntry.isDirectToPortUpdated || transactionTypeEntry.isAllowMultipleEntriesUpdated) {
            alertMsg = $rootScope.translation.KEY_SCREEN_FLAG_UPDATE_ALERT_MESSAGE;
        } else {
            alertMsg = $rootScope.translation.KEY_SCREEN_TRANSACTION_TYPE_UPDATE_ALERT_MESSAGE;
        }

        if (transactionTypeEntry.isMissionCancelSystemAfterMinutesUpdated) {

            if (!wholeNumberRegexp.test(transactionTypeEntry.missionCancelSystemAfterMinutes || transactionTypeEntry.missionCancelSystemAfterMinutes.length < 1)) {

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(1000) + ".");
                return;
            }

        }

        if (transactionTypeEntry.isTripCancelSystemAfterMinutesUpdated) {

            if (!wholeNumberRegexp.test(transactionTypeEntry.tripCancelSystemAfterMinutes || transactionTypeEntry.tripCancelSystemAfterMinutes.length < 1)) {

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(1000) + ".");
                return;
            }

        }

        $rootScope.showGenericConfirmationModal(alertMsg, function() {

            $rootScope.startSpinner();
            $scope.isUpdating = true;

            var requestData = {};
            requestData = {
                portOperatorId : portOperatorId,
                transactionType : transactionTypeEntry.transactionType,
                isDirectToPort : transactionTypeEntry.isDirectToPort,
                isAllowMultipleEntries : transactionTypeEntry.isAllowMultipleEntries,
                portOperatorGateId : transactionTypeEntry.operatorGate,
                missionCancelSystemAfterMinutes : transactionTypeEntry.missionCancelSystemAfterMinutes,
                isTripCancelSystem: transactionTypeEntry.isTripCancelSystem,
                tripCancelSystemAfterMinutes: transactionTypeEntry.tripCancelSystemAfterMinutes,
            };

            systemService.updateTransactionTypeFlags(requestData, function(response) {

                $scope.isUpdating = false;

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_SUCCESS_LABEL + ".");
                $rootScope.stopSpinner();

                transactionTypeEntry.isDirectToPortUpdated = false;
                transactionTypeEntry.isAllowMultipleEntriesUpdated = false;
                transactionTypeEntry.isGateUpdated = false;
                transactionTypeEntry.isMissionCancelSystemAfterMinutesUpdated = false;
                transactionTypeEntry.isTripCancelSystemUpdated = false;
                transactionTypeEntry.isTripCancelSystemAfterMinutesUpdated = false;

            }, function(error) {

                $scope.isUpdating = false;
                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        });
    };

} ]);
