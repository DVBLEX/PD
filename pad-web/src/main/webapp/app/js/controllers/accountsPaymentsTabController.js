padApp.controller('AccountsPaymentsTabController', ['$scope', '$rootScope', '$filter', '$interval', '$timeout', 'accountService', 'paymentService',
    function ($scope, $rootScope, $filter, $interval, $timeout, accountService, paymentService) {

        $rootScope.startSpinner();

        $scope.filterDateFromString = "";
        $scope.filterDateToString = "";
        $scope.sortColumn = "";
        $scope.sortAsc = true;
        $scope.filterPaymentOption = "";
        $scope.filterType = "";

        $scope.formData = {};
        $scope.filterCompanyName = "";
        $scope.filterIndividualMobileNumber = "";
        $scope.makePaymentErrorResponse = "";
        $scope.topupButtonDisabled = false;
        $scope.isPaymentOptionSelected = false;
        $scope.onlinePaymentCode = "";
        $scope.onlinePaymentStatusCheckIntervalSec = 5;
        $scope.onlinePaymentStatusCheckLimitMinutes = 10;
        $scope.onlinePaymentStatusCheckCount = 0;
        $scope.isPaymentSuccessful = false;
        $scope.onlinePaymentRequestSubmitted = false;
        $scope.paymentReference = "";
        $scope.mobilePaymentOptionsStatusMap = null;
        $scope.paymentConfirmationButtonDisabled = false;
        $scope.isOnlinePaymentStatusCheckLimitReached = false;
        $scope.accountTransporter = {};
        var timer;

        $scope.errorHandler = function (error) {

            $rootScope.stopSpinner();
            console.log(error);
        };

        $scope.initiateDatepickers = function () {

            $('.filter_date_from.date').datepicker('destroy');
            $('.filter_date_to.date').datepicker('destroy');

            var currentDate = new Date();
            var dateFirstOfLastMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);

            var dateFirstOfYear = new Date(currentDate.getFullYear() - 1, 0, 1);

            $('.filter_date_from.date').datepicker({
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
            })

            $('.filter_date_to.date').datepicker({
                format: "dd/mm/yyyy",
                defaultViewDate: currentDate,
                startDate: dateFirstOfLastMonth,
                setDate: currentDate,
                todayBtn: "linked",
                autoclose: true,
                todayHighlight: true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            }).on('hide', function (ev) {
                if (!ev.date) {
                    $("#filterDateToString").val($scope.filterDateToString);
                }
            });

            $(".filter_date_from.date").datepicker("update", dateFirstOfLastMonth);
            $(".filter_date_to.date").datepicker("update", currentDate);

            $scope.filterDateFromString = $filter('date')(dateFirstOfLastMonth, "dd/MM/yyyy");
            $scope.filterDateToString = $filter('date')(currentDate, "dd/MM/yyyy");

            $("#filter_date_from").val($filter('date')(dateFirstOfLastMonth, "dd/MM/yyyy"));
            $("#filter_date_to").val($filter('date')(currentDate, "dd/MM/yyyy"));
        };

        $scope.initiateDatepickers();

        $scope.$watch('filterDateFromString', function (newVal, oldVal) {
            if (newVal !== oldVal) {
                $('.filter_date_to.date').datepicker("setStartDate", newVal);
            }
        }, true);

        $scope.$watch('filterDateToString', function (newVal, oldVal) {
            if (newVal !== oldVal) {
                $('.filter_date_from.date').datepicker("setEndDate", newVal);
            }
        }, true);

        $scope.padTable = new PADTable(function () {

            $rootScope.startSpinner();

            var urlParams = {
                accountCode: $scope.account.code,
                dateFromString: $scope.filterDateFromString,
                dateToString: $scope.filterDateToString,
                type: $scope.filterType,
                paymentOption: $scope.filterPaymentOption
            };

            accountService.countAccountPayments(urlParams, $scope.getCountCallBack, $scope.errorHandler);

        }, function (currentPage, pageCount) {

            var urlParams = {
                accountCode: $scope.account.code,
                dateFromString: $scope.filterDateFromString,
                dateToString: $scope.filterDateToString,
                type: $scope.filterType,
                paymentOption: $scope.filterPaymentOption,
                sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage: currentPage,
                pageCount: pageCount
            };

            accountService.listAccountPayments(urlParams, $scope.getCallBack, $scope.errorHandler);
        });

        $scope.getCallBack = function (response) {
            $scope.padTable.setData(response.dataList);
            $rootScope.stopSpinner();
        };

        $scope.getCountCallBack = function (response) {
            $scope.padTable.setCount(response.dataList[0]);
            $scope.mobilePaymentOptionsStatusMap = response.dataMap;
            $rootScope.stopSpinner();
        };

        $scope.refreshTableData = function () {
            $scope.padTable.reloadTable();
        };

        $scope.sort = function (sortColumn) {

            if ($scope.sortColumn === sortColumn) {
                $scope.sortAsc = !$scope.sortAsc;
            } else {
                $scope.sortAsc = true;
            }

            $scope.sortColumn = sortColumn;

            $scope.refreshTableData();
        };

        $scope.searchTypes = function () {
            $scope.refreshTableData();
        };

        $scope.clearSearchTypes = function () {
            $scope.initiateDatepickers();
            $scope.padTable.setCount(0);
            $scope.filterPaymentOption = '';
            $scope.filterType = '';
            $scope.refreshTableData();
        };

        if ($scope.$parent.$parent.account != null) {
            $scope.account = $scope.$parent.$parent.account;
            $scope.refreshTableData();
        }

        // modal window

        $scope.showAccountTopupDialog = function () {
            console.log($scope.account);

            $scope.selectedRow = $scope.account;

            $('#topupModal').modal('show');
        };

        $scope.closeAccountTopupDialog = function () {

            $scope.makePaymentErrorResponse = "";
            $scope.onlinePaymentCode = "";
            $scope.paymentReference = "";
            $scope.isPaymentSuccessful = false;
            $scope.onlinePaymentRequestSubmitted = false;
            $scope.topupButtonDisabled = false;
            $scope.paymentConfirmationButtonDisabled = false;
            $scope.topupForm.$setPristine();

            $scope.formData.paymentOption = null;
            $scope.formData.firstName = "";
            $scope.formData.lastName = "";
            $scope.formData.mobileNumber = "";
            $scope.formData.amountTopup = "";
            $scope.formData.amountCashGiven = "";
            $scope.formData.notes = "";
            $scope.onlinePaymentStatusCheckCount = 0;
            $scope.isOnlinePaymentStatusCheckLimitReached = false;

            $('#topupModal').modal('hide');

            if (timer !== undefined && timer !== null) {

                $timeout.cancel(timer);
                timer = null;
            }

            $scope.refreshTableData();
        };

        $scope.topupAccountBalance = function (row) {

            if ($scope.topupForm.paymentOption.$invalid) {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PAYMENT_OPTION_MESSAGE + ".";
                return;

            } else if ($scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_WARI + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_E_MONEY + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_ECOBANK + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE + ''
                && $scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT + '') {

                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_PAYMENT_OPTION_MESSAGE + ".";
                return;
            } else if ($scope.formData.firstName === undefined || $scope.formData.firstName === null || $scope.formData.firstName === '') {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";
                return;

            } else if ($scope.formData.lastName === undefined || $scope.formData.lastName === null || $scope.formData.lastName === '') {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";
                return;

            } else if ($scope.topupForm.mobileNumber.$invalid) {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";
                return;
            }

            var maxAmount = 0;
            var minAmount = 0;
            if ($rootScope.isFinanceOperator === 'true') {
                maxAmount = parseInt($rootScope.financeFeeMaxAmount, 10);
                minAmount = parseInt($rootScope.financeFeeMinAmount, 10);
            } else if ($rootScope.isTransporterOperator === 'true') {
                maxAmount = parseInt($rootScope.kioskFeeMaxAmount, 10);
                minAmount = parseInt($rootScope.kioskFeeMinAmount, 10);
            }

            if ($scope.topupForm.amountTopup.$invalid) {
                if ($scope.formData.paymentOption !== $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND + '') {
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TOPUP_AMOUNT_MESSAGE + ".";
                } else {
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_CASH_REFUND_AMOUNT_MESSAGE + ".";
                }
                return;
            } else if ($scope.formData.amountTopup > maxAmount) {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_MAX_AMOUNT_MESSAGE + $filter('customCurrency')(maxAmount, 0) + ".";
                return;
            } else if ($scope.formData.amountTopup < minAmount) {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_MIN_AMOUNT_MESSAGE + $filter('customCurrency')(minAmount, 0) + ".";
                return;
            } else if ($scope.formData.amountTopup % 500 !== 0) {
                $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_AMOUNT_MULTIPLE_500_MESSAGE + ".";
                return;
            }


            if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + '') {

                if ($scope.topupForm.amountCashGiven.$invalid) {
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_CASH_GIVEN_MESSAGE + ".";
                    return;

                } else if ($scope.formData.amountCashGiven < $scope.formData.amountTopup) {
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_INVALID_CASH_GIVEN_AMOUNT_FOR_TOPUP_MESSAGE + ".";
                    return;
                } else if ($scope.formData.amountCashGiven % 500 !== 0) {
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_AMOUNT_MULTIPLE_500_MESSAGE + ".";
                    return;
                }
            }


            if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT + ''
                || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND + ''
                || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER + ''
                || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE + ''
                || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT + '') {
                    
                if ($scope.topupForm.notes.$invalid) {
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_NOTES_MESSAGE + ".";
                    return;
                }


                var message = "";
                if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT + '') {
                    message = $rootScope.translation.KEY_SCREEN_ACCOUNT_CREDIT_ALERT_MESSAGE + $filter('customCurrency')($scope.formData.amountTopup, 0) + " " + $rootScope.getSymbolByCurrencyCode($scope.selectedRow.currency) + " ?";
                } else if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND + '') {
                    message = $rootScope.translation.KEY_SCREEN_CASH_REFUND_ALERT_MESSAGE + $filter('customCurrency')($scope.formData.amountTopup, 0) + " " + $rootScope.getSymbolByCurrencyCode($scope.selectedRow.currency) + " ?";
                } else if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER + '') {
                    message = $rootScope.translation.KEY_SCREEN_BANK_TRANSFER_ALERT_MESSAGE + $filter('customCurrency')($scope.formData.amountTopup, 0) + " " + $rootScope.getSymbolByCurrencyCode($scope.selectedRow.currency) + " ?";
                } else if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE + '') {
                    message = $rootScope.translation.KEY_SCREEN_CHEQUE_ALERT_MESSAGE + $filter('customCurrency')($scope.formData.amountTopup, 0) + " " + $rootScope.getSymbolByCurrencyCode($scope.selectedRow.currency) + " ?";
                }   else if($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT + '') {
                    message = $rootScope.translation.KEY_SCREEN_ACCOUNT_DEBIT_ALERT_MESSAGE + $filter('customCurrency')($scope.formData.amountTopup, 0) + " " + $rootScope.getSymbolByCurrencyCode($scope.selectedRow.currency) + " ?";
                }

                $rootScope.showGenericConfirmationModal(message, function () {
                    $scope.makeTopUp(row)
                });

            } else {
                $scope.makeTopUp(row);
            }

        };

        $scope.makeTopUp = function (row) {

            $scope.makePaymentErrorResponse = "";
            $scope.topupButtonDisabled = true;
            $scope.paymentConfirmationButtonDisabled = true;
            $scope.isOnlinePaymentStatusCheckLimitReached = false;

            var urlParams = {};

            if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + '') {
                urlParams = {
                    accountCode: row.code,
                    paymentOption: parseInt($scope.formData.paymentOption),
                    firstName: $scope.formData.firstName,
                    lastName: $scope.formData.lastName,
                    msisdn: $scope.formData.mobileNumber,
                    topupAmount: $scope.formData.amountTopup,
                    paymentAmount: $scope.formData.amountCashGiven,
                    changeDueAmount: $scope.formData.amountChangeDue
                };

            } else {
                urlParams = {
                    accountCode: row.code,
                    paymentOption: parseInt($scope.formData.paymentOption),
                    firstName: $scope.formData.firstName,
                    lastName: $scope.formData.lastName,
                    msisdn: $scope.formData.mobileNumber,
                    topupAmount: $scope.formData.amountTopup,
                    paymentNote: $scope.formData.notes
                };
            }

            $rootScope.startSpinner();

            paymentService.topupAccountBalance(urlParams, function (response) {

                $scope.makePaymentErrorResponse = "";

                if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH + ''
                    || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_CREDIT + ''
                    || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CASH_REFUND + ''
                    || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_BANK_TRANSFER + ''
                    || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_CHEQUE + ''
                    || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ACCOUNT_DEBIT + '') {


                    $scope.closeAccountTopupDialog();
                    $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_TOPUP_SUCCESSFUL_MESSAGE);

                    $scope.paymentConfirmationButtonDisabled = false;
                    $scope.refreshTableData();

                } else {
                    // mobile payment
                    $scope.onlinePaymentCode = response.data;

                    if ($scope.onlinePaymentCode !== "" && $scope.onlinePaymentCode.length == 64) {

                        if ($scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_ORANGE_MONEY + ''
                            || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_FREE_MONEY + ''
                            || $scope.formData.paymentOption === $rootScope.paymentOptionConstants.PAYMENT_OPTION_E_MONEY + '') {

                            timer = $timeout(function () {
                                $scope.checkOnlinePaymentStatus(row);

                            }, $scope.onlinePaymentStatusCheckIntervalSec * 1000);

                            $scope.onlinePaymentRequestSubmitted = true;

                        } else {
                            // payment is for wari, emoney or ecobank. In that case get the reference number of initiated payment request
                            $scope.getInitiatedPaymentReference($scope.onlinePaymentCode, row);
                        }
                    }
                }

                $rootScope.stopSpinner();

            }, function (error) {

                $rootScope.stopSpinner();

                $scope.onlinePaymentRequestSubmitted = false;
                $scope.topupButtonDisabled = false;
                $scope.paymentConfirmationButtonDisabled = false;
                $scope.makePaymentErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });

        };

        $scope.checkOnlinePaymentStatus = function (row) {

            paymentService.checkPaymentStatus({

                onlinePaymentCode: $scope.onlinePaymentCode

            }, function (response) {

                $scope.onlinePaymentStatusCheckCount = 0;
                $scope.onlinePaymentRequestSubmitted = false;
                $scope.paymentReference = response.data.referenceAggregator;
                $scope.isPaymentSuccessful = true;

                $scope.newBalanceAmount = row.balanceAmount + $scope.formData.amountTopup;
                $scope.paymentConfirmationButtonDisabled = false;

            }, function (error) {

                if (error.data.responseCode === 1191) {
                    // payment failed
                    $scope.onlinePaymentStatusCheckCount = 0;
                    $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_PAYMENT_FAILED_LABEL;

                } else {
                    // continue checking for payment status
                    $scope.onlinePaymentStatusCheckCount++;

                    if ($scope.onlinePaymentStatusCheckCount * $scope.onlinePaymentStatusCheckIntervalSec <= $scope.onlinePaymentStatusCheckLimitMinutes * 60) {

                        timer = $timeout(function () {
                            $scope.checkOnlinePaymentStatus(row);

                        }, $scope.onlinePaymentStatusCheckIntervalSec * 1000);

                    } else {
                        $scope.onlinePaymentStatusCheckCount = 0;
                        $scope.makePaymentErrorResponse = $rootScope.translation.KEY_SCREEN_PAYMENT_CONFIRMATION_PENDING + " "
                            + $rootScope.getPaymentOption($scope.formData.paymentOption) + ".";
                        $scope.makePaymentErrorResponse += " " + $rootScope.translation.KEY_SCREEN_PAYMENT_CONFIRMATION_UPDATE + ".";
                        $scope.isOnlinePaymentStatusCheckLimitReached = true;
                    }
                }
            });
        };

        $scope.getInitiatedPaymentReference = function (onlinePaymentCode, row) {

            $rootScope.startSpinner();

            paymentService.getInitiatedPaymentReference({

                onlinePaymentCode: $scope.onlinePaymentCode

            }, function (response) {

                $scope.paymentReference = response.data;
                $scope.isPaymentSuccessful = true;
                $scope.newBalanceAmount = row.balanceAmount;

                $rootScope.stopSpinner();

            }, function (error) {

                $rootScope.stopSpinner();

                $scope.paymentReference = '';
                $scope.makePaymentErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });
        };

        $scope.getCompanyPayments = function (row) {
            if (!row.showOnlinePayments) {

                $rootScope.startSpinner();

                paymentService.getCompanyPayments({

                    accountCode: row.code

                }, function (response) {

                    row.onlinePaymentsList = response.dataList;
                    $rootScope.stopSpinner();

                }, function (error) {

                    $rootScope.stopSpinner();

                    row.onlinePaymentsList = [];

                });
            }
        }
        
        $scope.isPaymentOptionEnabled = function(paymentOptionConstant) {

            if ($scope.mobilePaymentOptionsStatusMap === null) {
                return false;

            } else {
                var isActive = $scope.mobilePaymentOptionsStatusMap[paymentOptionConstant];

                return (isActive === undefined) ? false : isActive;
            }
        };

        if ($rootScope.isTransporterOperator == "true") {
            accountService.getAccount(function (response) {

                $rootScope.stopSpinner();
                $scope.accountTransporter = response.dataList[0];

            }, $scope.errorHandler);
        }

        $scope.$watch('formData.paymentOption', function () {

            if ($scope.formData.paymentOption !== undefined && $scope.formData.paymentOption !== null && $scope.formData.paymentOption !== "") {

                $scope.isPaymentOptionSelected = true;

                if ($scope.selectedRow && $scope.selectedRow.msisdn != '') {
                    $scope.formData.mobileNumber = "+" + $scope.selectedRow.msisdn;

                } else {
                    $scope.formData.mobileNumber = "";
                }

            } else {
                $scope.isPaymentOptionSelected = false;
            }
        });

        $scope.$watch('formData.amountTopup', function () {

            if ($scope.formData.amountTopup > 0 && ($scope.formData.amountCashGiven - $scope.formData.amountTopup) >= 0) {
                $scope.formData.amountChangeDue = $scope.formData.amountCashGiven - $scope.formData.amountTopup;
            } else {
                $scope.formData.amountChangeDue = 0;
            }
        });

        $scope.$watch('formData.amountCashGiven', function () {

            if ($scope.formData.amountTopup > 0 && ($scope.formData.amountCashGiven - $scope.formData.amountTopup) >= 0) {
                $scope.formData.amountChangeDue = $scope.formData.amountCashGiven - $scope.formData.amountTopup;
            } else {
                $scope.formData.amountChangeDue = 0;
            }
        });


        $rootScope.stopSpinner();

    }]);
