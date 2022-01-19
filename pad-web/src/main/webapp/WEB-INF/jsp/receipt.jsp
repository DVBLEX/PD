<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html ng-app="receiptApp">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

<link rel="stylesheet" href="lib/bootstrap-3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="app/css/pad.css" />
<link rel="stylesheet" href="lib/intl-tel-input-12.0.3/css/intlTelInput.css">

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

<script type="text/javascript" src="lib/jquery-2.2.4/js/jquery.min-2.2.4.js"></script>
<script type="text/javascript" src="lib/jquery-ui-1.12.1/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="lib/bootstrap-3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="lib/angular-1.7.8/js/angular.min.js"></script>
<script type="text/javascript" src="lib/angular-1.7.8/js/angular-resource.min.js"></script>
<script type="text/javascript" src="lib/angular-1.7.8/js/angular-animate.min.js"></script>
<script type="text/javascript" src="lib/angular-spinner-0.5.0/js/angular-spinner.min.js"></script>
<script type="text/javascript" src="lib/spin-2.3.2/js/spin.min-2.3.2.js"></script>
<script type="text/javascript" src="lib/intl-tel-input-12.0.3/js/intlTelInput.min.js"></script>
<script type="text/javascript" src="lib/intl-tel-input-12.0.3/js/utils.js"></script>
<script type="text/javascript" src="lib/ng-intl-tel-input-2.0.0/js/ng-intl-tel-input.min.js"></script>
<script type="text/javascript" src="app/js/receipt.js"></script>

<title>AGS - Port Access Dakar</title>

</head>

<body ng-controller="ReceiptController" ng-cloak ng-init="init('${isTestEnvironment}', '${receiptNumber}', '${accountLanguage}', '${expireLinkDateString}')">

    <%@ include file="../../app/views/offlineNavbar.jsp"%>

    <div class="container-fluid form-horizontal" ng-form="dataForm">
        <span us-spinner="{radius:16, width:6, length: 14}" spinner-key="spinner-receipt"></span>
        
        <div class="row margin-top-15px">
            <div class="col-sm-12 text-center">{{translation.KEY_SCREEN_RECEIPT_LINK_EXPIRE_LABEL}} <b>{{expireLinkDateString}}</b>. {{translation.KEY_SCREEN_RECEIPT_NOT_AVAILABLE_LABEL}}</div>
        </div>

        <div class="row margin-top-15px" ng-hide="isMobileNumberValid">
            <div class="col-sm-12">
                <label for="mobileNumber">{{translation.KEY_SCREEN_ENTER_MOBILE_USED_PAYMENT_LABEL}}</label>
                <input type="text" class="form-control" name="mobileNumber" id="mobileNumber" ng-model="mobileNumber" ng-change="receiptErrorResponse = ''" ng-disabled="isDownloadLock" ng-intl-tel-input
                    required>
            </div>
        </div>
        <div class="row margin-top-15px" ng-show="receiptErrorResponse">
            <div class="col-sm-12">
                <div class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-lock" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span> {{receiptErrorResponse}}
                </div>
            </div>
        </div>
        <div class="row margin-top-15px" ng-hide="isMobileNumberValid">
            <div class="col-sm-12">
                <button ng-click="validateMobileNumber()" class="btn btn-primary" style="width: 100%;" ng-hide="isDownloadLock">{{translation.KEY_SCREEN_VALIDATE_MOBILE_LABEL}}</button>
            </div>
        </div>

        <div class="row margin-top-15px" ng-show="isMobileNumberValid">
            <div class="col-sm-12 text-center">
                <form target="_blank" method="post" action="/pad/receipt/download/link" id="receiptDonwloadForm" class="form-inline">
                    <button ng-click="downloadReceipt()" class="btn btn-info" ng-disabled="downloaded" title="{{translation.KEY_SCREEN_DOWNLOAD_LABEL}}">
                        <span class="glyphicon glyphicon-download-alt"></span> {{translation.KEY_SCREEN_DOWNLOAD_RECEIPT_LABEL}}
                    </button>
                    <input type="hidden" name="number" ng-value="receiptNumber" />
                </form>
            </div>
        </div>
    </div>
</body>
</html>
