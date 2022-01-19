<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html ng-app="padApp" ng-controller="PADController">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="lib/bootstrap-3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/bootstrap-datepicker-1.8.0/css/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="lib/jquery-ui-1.12.1/css/jquery-ui.min.css" />
<link rel="stylesheet" href="lib/intl-tel-input-12.0.3/css/intlTelInput.css">
<link rel="stylesheet" href="app/css/pad.css" />

<link rel="apple-touch-icon" sizes="57x57" href="app/img/favicon/apple-icon-57x57.png">
<link rel="apple-touch-icon" sizes="60x60" href="app/img/favicon/apple-icon-60x60.png">
<link rel="apple-touch-icon" sizes="72x72" href="app/img/favicon/apple-icon-72x72.png">
<link rel="apple-touch-icon" sizes="76x76" href="app/img/favicon/apple-icon-76x76.png">
<link rel="apple-touch-icon" sizes="114x114" href="app/img/favicon/apple-icon-114x114.png">
<link rel="apple-touch-icon" sizes="120x120" href="app/img/favicon/apple-icon-120x120.png">
<link rel="apple-touch-icon" sizes="144x144" href="app/img/favicon/apple-icon-144x144.png">
<link rel="apple-touch-icon" sizes="152x152" href="app/img/favicon/apple-icon-152x152.png">
<link rel="apple-touch-icon" sizes="180x180" href="app/img/favicon/apple-icon-180x180.png">
<link rel="icon" type="image/png" sizes="192x192" href="app/img/favicon/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32" href="app/img/favicon/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="96x96" href="app/img/favicon/favicon-96x96.png">
<link rel="icon" type="image/png" sizes="16x16" href="app/img/favicon/favicon-16x16.png">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage" content="app/img/favicon/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">

<title>Port Access Dakar</title>

</head>

<body
    ng-init="nameUser = '${nameUser}'; init('${nameUser}', '${operatorCode}', '${isTransporterOperator}', '${isPortOperator}', '${portOperatorId}',
            '${independentPortOperatorId}', '${independentPortOperatorCode}',
            '${isKioskSessionAllowed}', '${kioskSessionType}', '${kioskSessionCode}', '${kioskSessionLaneNumber}', '${kioskSessionLaneId}', '${kioskSessionAmountCollected}',
            '${isParkingOperator}', '${isParkingKioskOperator}', '${isParkingOfficeOperator}', '${isPortAuthorityOperator}', '${isPortEntryOperator}',
            '${isPortExitOperator}', '${isParkingSupervisorOperator}', '${isFinanceOperator}', '${isAdmin}', '${isTestEnvironment}', '${taxPercentage}', '${kioskFeeMinAmount}',
            '${kioskFeeMaxAmount}', '${financeFeeMinAmount}', '${financeFeeMaxAmount}', '${financeInitialFloatMinAmount}', '${financeInitialFloatMaxAmount}',
            '${maximumOverdraftLimitMinAmount}', '${maximumOverdraftLimitMaxAmount}', '${transporterAccountNumber}', '${transporterAccountStatus}', '${transporterAccountType}',
            '${transporterAccountPaymentTermsType}', '${isBookingLimitCheckEnabled}', '${isPortEntryFiltering}', '${operatorLanguage}',
            '${tripsPendingApprovalCount}', '${entryLaneVideoFeedUrl}', '${autoReleaseExitCapacityPercentage}', '${dropOffEmptyNightMissionStartTime}', '${dropOffEmptyNightMissionEndTime}',
            '${dropOffEmptyTriangleMissionStartTime}', '${dropOffEmptyTriangleMissionEndTime}', '${pendingTripsTransporterCount}')"
    ng-cloak>

    <div class="divLoadingBackground" ng-if="isLoading"></div>
    
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href=""> <img src="app/img/ags_logo.png" alt="" style="height: 77%; float: left; margin-top: 2px;" /> &nbsp; Port Access Dakar <c:if
                        test="${isTestEnvironment == true}">
                        <span>(Sandbox)</span>
                    </c:if>
                    <div style="position:absolute;font-size: x-small;color: gray;padding-left: 58px;">
                        <%@ include file="buildNumber.jsp"%>
                    </div>
                </a>
            </div>

            <div id="navbar" class="navbar-collapse collapse" aria-expanded="false" style="height: 1px;">
                <ul class="nav navbar-nav">

                    <c:choose>

                        <c:when test="${isTransporterOperator == true}">
                            <li id="navbarHome" class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                            <li id="navbarMissionTrip" ng-if="transporterAccountStatus == accountStatusConstants.ACCOUNT_STATUS_ACTIVE"><a href=""
                                ng-click="go('/missionTrip')">{{translation.KEY_SCREEN_TRIPS_LABEL}} <span class="badge">{{pendingTripsTransporterCount}}</span></a></li>
                            <li id="navbarMissions"><a href="" ng-click="go('/transporterMissions')">{{translation.KEY_NAVBAR_MISSIONS}}</a></li>
                            <li id="navbarVehicle"><a href="" ng-click="go('/vehicle')">{{translation.KEY_NAVBAR_VEHICLES}}</a></li>
                            <li id="navbarDriver"><a href="" ng-click="go('/driver')">{{translation.KEY_NAVBAR_DRIVERS}}</a></li>
                            <li ng-hide="transporterAccountPaymentTermsType == accountPaymentTermsTypeConstants.ACCOUNT_PAYMENT_NO_TERMS_TYPE" id="navbarInvoice"><a href="" ng-click="go('/invoice')">
                                <span ng-if="transporterAccountPaymentTermsType == accountPaymentTermsTypeConstants.ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY">{{translation.KEY_NAVBAR_INVOICE}}</span>
                                <span ng-if="transporterAccountPaymentTermsType == accountPaymentTermsTypeConstants.ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY">{{translation.KEY_SCREEN_STATEMENTS_LABEL}}</span>
                            </a></li>
                            <li id="navbarPaymentTransporter" ng-if="transporterAccountPaymentTermsType != accountPaymentTermsTypeConstants.ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY"><a
                                href="" ng-click="go('/paymentTransporter')">{{translation.KEY_SCREEN_PAYMENTS_LABEL}}</a></li>
                            <li id="navbarOperator"
                                ng-if="transporterAccountStatus == accountStatusConstants.ACCOUNT_STATUS_ACTIVE && transporterAccountType == accountTypeConstants.ACCOUNT_TYPE_COMPANY"><a
                                href="" ng-click="go('/operatorTransporter')">{{translation.KEY_SCREEN_USER_ACCOUNTS_LABEL}}</a></li>
                            <li id="navbarTopupTransporter" ng-if="transporterAccountPaymentTermsType == accountPaymentTermsTypeConstants.ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY"><a
                                href="" ng-click="go('/topupTransporter')">{{translation.KEY_SCREEN_TOPUP_LABEL}}</a></li>
                            <li id="navbarAccount"><a href="" ng-click="go('/account')">{{translation.KEY_SCREEN_ACCOUNT_LABEL}}</a></li>
                        </c:when>

                        <c:when test="${isPortOperator == true}">
                            <li id="navbarHome" class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                            <li id="navbarMissions"><a href="" ng-click="go('/missions')">{{translation.KEY_NAVBAR_MISSIONS}}</a></li>
                            <li id="navbarPortOperatorShowTrip"><a href="" ng-click="go('/showTrip')">{{translation.KEY_SCREEN_TRIPS_LABEL}}</a></li>
                            <!-- <li id="navbarReportIssue"><a href="" ng-click="go('/reportIssue')">{{translation.KEY_SCREEN_ISSUE_REPORT_LABEL}}</a></li> -->
                            <!-- <li id="navbarPortWhitelist"><a href="" ng-click="go('/portWhitelist')">{{translation.KEY_NAVBAR_PORT_WHITELIST}}</a></li>  -->
                        </c:when>

                        <c:when test="${isParkingOperator == true}">
                            <li id="navbarParkingOperator" class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                        </c:when>

                        <c:when test="${isParkingKioskOperator == true && isKioskSessionAllowed == true}">
                            <li id="navbarKioskPayment" class="active"><a href="" ng-click="go('/payment')">{{translation.KEY_NAVBAR_PAYMENT}}</a></li>
                            <li id="navbarExitOnly" ng-if="kioskSessionType === kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING"><a href="" ng-click="go('/exitOnly')">{{translation.KEY_NAVBAR_PARKING_EXIT_ONLY}}</a></li>
                        </c:when>

                        <c:when test="${isParkingOfficeOperator == true}">
                            <li id="navbarParking" class="active"><a href="" ng-click="go('/parking')">{{translation.KEY_NAVBAR_PARKING}}</a></li>
                            <li id="navbarParkingPort"><a href="" ng-click="go('/parkingPort')">{{translation.KEY_SCREEN_OPERATOR_TYPE_PORT_ENTRY_LABEL}}</a></li>
                            <li id="navbarOfficeOperatorApproveTrip"><a href="" ng-click="go('/trip')">{{translation.KEY_SCREEN_TRIPS_LABEL}} <span class="badge">{{tripsPendingApprovalCount}}</span></a></li>
                            <li id="navbarMissions"><a href="" ng-click="go('/createMissions')">{{translation.KEY_NAVBAR_MISSIONS}}</a></li>
                            <li id="navbarAccountsActivate"><a href="" ng-click="go('/accountsActivate')">{{translation.KEY_SCREEN_ACCOUNTS_LABEL}}</a></li>
                            <li id="navbarBookingLimits"><a href="" ng-click="go('/bookingLimits')">{{translation.KEY_SCREEN_BOOKING_LIMITS_LABEL}}</a></li>
                            <!-- <li id="navbarParkingRelease"><a href="" ng-click="go('/parkingReleaseView')">{{translation.KEY_SCREEN_PARKING_RELEASE_LABEL}}</a></li> -->
                        </c:when>

                        <c:when test="${isPortAuthorityOperator == true}">
                            <li id="navbarHome" class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                            <li id="navbarTrips"><a href="" ng-click="go('/trips')">{{translation.KEY_SCREEN_TRIPS_LABEL}}</a></li>
                        </c:when>

                        <c:when test="${isPortEntryOperator == true}">
                            <li class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                        </c:when>

                        <c:when test="${isPortExitOperator == true}">
                            <li class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                        </c:when>

                        <c:when test="${isParkingSupervisorOperator == true}">
                            <li id="navbarParkingRelease" class="active"><a href="" ng-click="go('/parkingReleaseView')">{{translation.KEY_SCREEN_PARKING_RELEASE_LABEL}}</a></li>
                        </c:when>

                        <c:when test="${isFinanceOperator == true}">
                            <li id="navbarAccounts"><a href="" ng-click="go('/accounts')">{{translation.KEY_SCREEN_ACCOUNTS_LABEL}}</a></li>
                            <!-- <li id="navbarPayment"><a href="" ng-click="go('/paymentFinance')">{{translation.KEY_NAVBAR_PAYMENT}}</a></li> -->
                            <li id="navbarInvoice"><a href="" ng-click="go('/invoiceFinance')">{{translation.KEY_NAVBAR_INVOICE + '/' + translation.KEY_SCREEN_STATEMENTS_LABEL}}</a></li>
                            <li id="navbarKioskSession" class=""><a href="" ng-click="go('/kioskSession')">{{translation.KEY_SCREEN_KIOSK_SESSIONS_LABEL}}</a></li>
                        </c:when>

                        <c:when test="${isAdmin == true}">
                            <li id="navbarHome" class="active"><a href="" ng-click="go('/')">{{translation.KEY_NAVBAR_HOME}}</a></li>
                            <li id="navbarOperator" class=""><a href="" ng-click="go('/operator')">{{translation.KEY_SCREEN_STAFF_USERS_LABEL}}</a></li>
                            <li id="navbarTrips"><a href="" ng-click="go('/trips')">{{translation.KEY_SCREEN_TRIPS_LABEL}}</a></li>
                            <li id="navbarAnpr"><a href="" ng-click="go('/anpr')">ANPR</a></li>
                            <li id="navbarBookingLimits"><a href="" ng-click="go('/bookingLimits')">{{translation.KEY_SCREEN_BOOKING_LIMITS_LABEL}}</a></li>
                            <li id="navbarVehicleCounter"><a href="" ng-click="go('/vehicleCounter')">{{translation.KEY_SCREEN_VEHICLE_COUNTER_LABEL}}</a></li>
                            <li id="navbarLane"><a href="" ng-click="go('/lane')">{{translation.KEY_SCREEN_LANES_LABEL}}</a></li>
                        </c:when>

                    </c:choose>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="navbar-select hidden-xs hidden-sm hidden-md" style="margin-right: 25px; margin-top: 16px;" ng-if="isTransporterOperator == 'true'">{{translation.KEY_SCREEN_ACCOUNT_NUMBER_LABEL}}: {{transporterAccountNumber}}</li>
                    <li class="navbar-select hidden-xs hidden-sm hidden-md"><span class="glyphicon glyphicon-globe"
                        style="font-size: 14pt; margin-top: 7px; margin-right: 0px;"></span></li>
                    <li class="navbar-select"><select class="form-control select-language" style="width: inherit;" ng-change="changeLanguage(language);" ng-model="language">
                            <option value="FR">Français</option>
                            <option value="EN">English</option>
                        </select></li>
                    <li class="dropdown"><a href="" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" title="${nameUser}"><span
                            class="glyphicon glyphicon-user"></span> ${displayUsername} <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="" ng-click="go('/changePassword');"><span class="glyphicon glyphicon-lock"></span>{{translation.KEY_NAVBAR_CHANGE_PASSWORD}}</a></li>
                            <li role="separator" class="divider"></li>

                            <c:choose>
                                <c:when test="${isParkingKioskOperator == true && isKioskSessionAllowed == true}">
                                    <li><a href="" ng-click="showKioskSessionEndConfirmationDialog();"><span class="glyphicon glyphicon-check"></span>{{translation.KEY_SCREEN_END_KIOSK_SESSION_LABEL}}</a></li>
                                    <li role="separator" class="divider"></li>
                                </c:when>
                            </c:choose>

                            <c:choose>
                                <c:when test="${isTransporterOperator == true}">
                                    <li><a href="logout.htm?tp" data-method="delete" rel="nofollow"><span class="glyphicon glyphicon-log-out"></span>{{translation.KEY_NAVBAR_LOGOUT}}</a></li>
                                </c:when>

                                <c:when test="${isTransporterOperator == false}">
                                    <li><a href="logout.htm?op" data-method="delete" rel="nofollow"><span class="glyphicon glyphicon-log-out"></span>{{translation.KEY_NAVBAR_LOGOUT}}</a></li>
                                </c:when>
                            </c:choose>

                        </ul></li>
                </ul>
            </div>
        </div>
    </nav>

    <div ng-if="showSuccess" class="alert alert-success showResult showSuccessResult">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <p class="font-size-16">
            <span class="glyphicon glyphicon-ok font-size-16">&nbsp;</span>{{showSuccess}}
        </p>
    </div>

    <div ng-if="showError" class="alert alert-danger showResult showErrorResult">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <p class="font-size-16">
            <span class="glyphicon glyphicon-ban-circle font-size-16">&nbsp;</span>{{showError}}
        </p>
    </div>

    <div class="modal fade" id="kioskSessionEndConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="kioskSessionEndConfirmationModalLabel"
        ng-form="sessionEndConfirmationForm">
        <fieldset>
            <div class="modal-dialog modal-dialog-record" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="kioskSessionEndConfirmationModalLabel">{{translation.KEY_SCREEN_END_KIOSK_SESSION_LABEL}}</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="panel panel-info">
                                    <div class="panel-heading font-size-18"></div>
                                    <div class="panel-body font-size-18">
                                        <form name="sessionEndConfirmationForm" autocomplete="off" novalidate>
                                            <div class="col-md-12 form-group" style="padding: 0px;" ng-hide="isKioskSessionEndRequested">
                                                <div class="row" ng-show="kioskSessionType === kioskSessionTypeConstants.KIOSK_SESSION_TYPE_PARKING">
                                                    {{translation.KEY_SCREEN_LANE_NUMBER_LABEL}}: {{kioskSessionLaneNumber}}
                                                </div>
                                                <div class="row" ng-show="kioskSessionType === kioskSessionTypeConstants.KIOSK_SESSION_TYPE_VIRTUAL">
                                                    {{translation.KEY_SCREEN_END_KIOSK_SESSION_CONFIRMATION_MESSAGE}}
                                                </div>
                                                <br>
                                            </div>
                                            <div class="col-md-12 form-group" style="padding: 0px;" ng-show="isKioskSessionEndRequested">
                                                <div class="row">{{translation.KEY_SCREEN_ENDED_KIOSK_SESSION_MESSAGE}}</div>
                                                <br>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer text-alight-right">
                        <button type="button" class="btn btn-success outline-none width-modal-btn" ng-click="endKioskSession()" ng-show="!isKioskSessionEndRequested">
                            <b><span class="glyphicon glyphicon-ok"></span> {{translation.KEY_SCREEN_CONFIRM_LABEL}}</b>
                        </button>
                        <button type="button" class="btn btn-danger outline-none width-modal-btn" ng-click="closeKioskSessionEndConfirmationDialog()">
                            <b><span class="glyphicon glyphicon-remove"></span> {{translation.KEY_SCREEN_CANCEL_MESSAGE}}</b>
                        </button>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>

    <div class="modal fade" id="genericConfirmationModal" tabindex="-1" style="z-index: 9999;" role="dialog" aria-labelledby="genericConfirmationModalLabel">
        <div class="modal-dialog modal-dialog-record" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" ng-click="closeGenericConfirmationModal()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>

                    <h4 class="modal-title" ng-if="!isWarning">{{translation.KEY_SCREEN_CONFIRMATION_LABEL}}</h4>
                    <h4 class="modal-title" ng-if="isWarning">
                        <span class="glyphicon glyphicon-warning-sign red" style="font-size: 30px; vertical-align: bottom;"></span>
                        {{translation.KEY_SCREEN_WARNING_MESSAGE}}
                    </h4>

                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="panel" ng-class="{'panel-info': !isWarning, 'panel-danger': isWarning}">
                                <div class="panel-heading font-size-18"></div>
                                <div class="panel-body">
                                    <form name="confirmationForm" autocomplete="off" novalidate>
                                        <div class="col-md-12 form-group" style="padding: 0px;">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <b>{{confirmationModalText}}</b>
                                                </div>
                                                <br> <br>
                                            </div>

                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <form name="modalButtonsForm" autocomplete="off" novalidate>
                    <div class="modal-footer text-alight-right">
                        <button ng-if="!isWarning" ng-click="modalButtonsForm.$invalid=true;callbackFunction();" class="btn btn-success outline-none width-modal-btn" data-ng-disabled="modalButtonsForm.$invalid">
                            <span class="glyphicon glyphicon-ok" ></span> {{translation.KEY_SCREEN_CONFIRM_LABEL}}
                        </button>

                        <button ng-if="isWarning" ng-click="modalButtonsForm.$invalid=true;callbackFunction();" class="btn btn-success outline-none width-modal-btn" data-ng-disabled="modalButtonsForm.$invalid">
                            <span class="glyphicon glyphicon-ok" ></span> {{translation.KEY_SCREEN_ACCEPT_MESSAGE}}
                        </button>

                        <button type="button" class="btn btn-danger outline-none width-modal-btn" ng-click="closeGenericConfirmationModal()">
                            <b><span class="glyphicon glyphicon-remove"></span> {{translation.KEY_SCREEN_CANCEL_MESSAGE}}</b>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div ng-view></div>

    <span us-spinner="{radius:24, width:8, length: 25}" spinner-key="pad-spinner"></span>

    <script type="text/javascript" src="lib/jquery-2.2.4/js/jquery.min-2.2.4.js"></script>
    <script type="text/javascript" src="lib/jquery-ui-1.12.1/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="lib/angular-1.7.8/js/angular.min.js"></script>
    <script type="text/javascript" src="lib/angular-1.7.8/js/angular-resource.min.js"></script>
    <script type="text/javascript" src="lib/angular-1.7.8/js/angular-route.min.js"></script>
    <script type="text/javascript" src="lib/angular-spinner-0.5.0/js/angular-spinner.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap-3.3.7/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap-datepicker-1.8.0/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap-datepicker-1.8.0/js/bootstrap-datepicker.fr.js"></script>
    <script type="text/javascript" src="lib/spin-2.3.2/js/spin.min-2.3.2.js"></script>
    <script type="text/javascript" src="lib/intl-tel-input-12.0.3/js/intlTelInput.min.js"></script>
    <script type="text/javascript" src="lib/intl-tel-input-12.0.3/js/utils.js"></script>
    <script type="text/javascript" src="lib/ng-intl-tel-input-2.0.0/js/ng-intl-tel-input.min.js"></script>
    <script type="text/javascript" src="lib/chartjs-2.8.0/js/chart.min.js"></script>
    <script type="text/javascript" src="lib/angular-chart-1.1.1/js/angular-chart.min.js"></script>
    <script type="text/javascript" src="app/js/pad.js"></script>
</body>
</html>
