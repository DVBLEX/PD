<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html ng-app="loginApp">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<title>Port Access Dakar</title>
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
<script type="text/javascript" src="lib/angular-recaptcha-4.1.0/js/angular-recaptcha.min.js"></script>
<script type="text/javascript" src="lib/angular-spinner-0.5.0/js/angular-spinner.min.js"></script>
<script type="text/javascript" src="lib/spin-2.3.2/js/spin.min-2.3.2.js"></script>
<script type="text/javascript" src="lib/intl-tel-input-12.0.3/js/intlTelInput.min.js"></script>
<script type="text/javascript" src="lib/intl-tel-input-12.0.3/js/utils.js"></script>
<script type="text/javascript" src="lib/ng-intl-tel-input-2.0.0/js/ng-intl-tel-input.min.js"></script>
<script type="text/javascript" src="app/js/login.js"></script>
</head>
<body ng-controller="passwordForgotController" ng-cloak ng-init="init('${isTestEnvironment}', '${accountType}')">

    <%@ include file="../../app/views/offlineNavbar.jsp"%>

    <h3 class="text-center">{{translation.KEY_SCREEN_FORGOT_PASSWORD_LABEL}}</h3>

    <div class="container-fluid pad-center">

        <span us-spinner="{radius:16, width:6, length: 14}" spinner-key="spinner-login"></span>

        <div ng-hide="passwdForgotSendSuccess">
            <form name="dataForm" autocomplete="off" novalidate>

                <div class="form-group" ng-show="formData.accountType != 100">
                    <label>{{translation.KEY_SCREEN_ACCOUNT_TYPE_LABEL}}</label>
                    <div>
                        <label class="label-radio"><input type="radio" name="accountType" ng-model="formData.accountType" value="1" required />
                            {{translation.KEY_SCREEN_COMPANY_LABEL}} </label>&nbsp;&nbsp;&nbsp; <label class="label-radio"><input type="radio" name="accountType"
                                ng-model="formData.accountType" value="2" required /> {{translation.KEY_SCREEN_INDIVIDUAL_LABEL}}</label>
                    </div>
                </div>

                <div class="form-group" ng-show="formData.accountType == 1">
                    <label for="email">{{translation.KEY_SCREEN_EMAIL_LABEL}}</label>
                    <input type="text" class="form-control" name="email" ng-model="formData.email" ng-pattern="emailRegexp" placeholder="{{translation.KEY_SCREEN_EMAIL_LABEL}}"
                        ng-maxlength="64" maxlength="64" title="Username" autofocus required>
                </div>

                <div class="form-group" ng-show="formData.accountType == 2">
                    <label for="mobileNumber">{{translation.KEY_SCREEN_MOBILE_NUMBER_LABEL}}</label>
                    <div style="width: 100%;">
                        <input type="text" class="form-control" name="mobileNumber" id="mobileNumber" ng-model="formData.mobileNumber" ng-intl-tel-input required>
                    </div>
                </div>

                <div class="form-group" ng-show="formData.accountType == 100">
                    <label for="email">{{translation.KEY_SCREEN_USERNAME_LABEL}}</label>
                    <input type="text" class="form-control" name="userName" ng-model="formData.userName" placeholder="{{translation.KEY_SCREEN_USERNAME_LABEL}}"
                        ng-maxlength="32" maxlength="32" title="Username" autofocus required>
                </div>

                <div class="form-group text-center">
                    {{recaptchaKey=
                    <c:out value="${recaptchaKey}" />
                    ;""}}

                    <div ng-if="language === 'EN'" vc-recaptcha key="recaptchaKey" lang="en" theme="clean" on-create="setWidgetId(widgetId)" on-success="setResponse(response)"
                         on-expire="cbExpiration()"></div>

                    <div ng-if="language === 'FR'" vc-recaptcha key="recaptchaKey" lang="fr" theme="clean" on-create="setWidgetId(widgetId)" on-success="setResponse(response)"
                         on-expire="cbExpiration()"></div>
                </div>
                <div class="form-group">
                    <div class="animate-if" ng-if="passwordForgotStep1ErrorResponse !== ''">
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span><span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span> <span>{{passwordForgotStep1ErrorResponse}}</span>
                        </div>
                    </div>
                </div>

                <button type="button" id="submitButton" class="btn btn-primary btn-block" ng-click="passwordForgotSubmit()">{{translation.KEY_SCREEN_SUBMIT_LABEL}}</button>
            </form>
        </div>

        <div ng-if="passwdForgotSendSuccess && formData.accountType == 1">
            <div class="alert alert-success">
                <h4>{{translation.KEY_SCREEN_PASSWORD_SENT_MESSAGE}}.</h4>
                <div style="padding-top: 20px;">
                    {{translation.KEY_SCREEN_CHECK_INBOX_MESSAGE}}.<br />{{translation.KEY_SCREEN_CHECK_JUNK_SPAM_MESSAGE}}.
                </div>
            </div>
        </div>

        <div ng-if="passwdForgotSendSuccess && formData.accountType == 2">
            <div class="alert alert-success">
                <h4>{{translation.KEY_SCREEN_PASSWORD_FORGOT_SMS_SENT_MESSAGE}}.</h4>
            </div>
        </div>

        <div ng-if="passwdForgotSendSuccess && formData.accountType == 100">
            <div class="alert alert-success">
                <h4>{{translation.KEY_SCREEN_PASSWORD_FORGOT_SMS_SENT_MESSAGE}}!</h4>
                <div style="padding-top: 20px;">
                    <br> <a class="alert-link" href="operatorPasswordSetUp.htm?u={{formData.userName}}">{{translation.KEY_SCREEN_GO_TO_PASS_RESET_PAGE_MESSAGE}}.</a>
                </div>
            </div>
        </div>

    </div>
</body>
</html>
