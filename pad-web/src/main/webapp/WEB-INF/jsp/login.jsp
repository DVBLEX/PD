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
<body ng-controller="loginController" ng-cloak ng-init="init('${isTestEnvironment}', '${accountType}')">

    <%@ include file="../../app/views/offlineNavbar.jsp"%>

    <div class="container-fluid pad-center">
        <c:if test="${tp == true}">
            <h3 class="text-left">{{translation.KEY_SCREEN_TRANSPORTER_LOGIN_LABEL}}</h3>
            <form name="transporterLoginForm" autocomplete="off" method="post" action="processLogin" onsubmit="return validateForm()" novalidate>

                <div class="form-group">
                    <label>{{translation.KEY_SCREEN_ACCOUNT_TYPE_LABEL}}</label>
                    <div>
                        <label class="label-radio"><input type="radio" name="accountType" ng-model="formData.accountType" value="1" required />
                            {{translation.KEY_SCREEN_COMPANY_LABEL}} </label>&nbsp;&nbsp;&nbsp; <label class="label-radio"><input type="radio" name="accountType"
                                ng-model="formData.accountType" value="2" required /> {{translation.KEY_SCREEN_INDIVIDUAL_LABEL}}</label>
                    </div>
                </div>

                <div class="form-group" ng-if="formData.accountType == 1">
                    <label for="input1">{{translation.KEY_SCREEN_EMAIL_LABEL}}</label>
                    <input type="text" class="form-control" name="input1" placeholder="{{translation.KEY_SCREEN_EMAIL_LABEL}}" title="Username" required>
                </div>

                <div class="form-group" ng-if="formData.accountType == 2" style="margin: 0px;">
                    <label for="input1">{{translation.KEY_SCREEN_MOBILE_NUMBER_LABEL}}</label>
                    <input type="hidden" class="form-control" name="input1" ng-value="formData.mobileNumber">
                </div>

                <div class="form-group" ng-show="formData.accountType == 2">
                    <div style="width: 100%;">
                        <input type="text" class="form-control" name="mobileNumber" id="mobileNumber" ng-model="formData.mobileNumber" ng-intl-tel-input required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="input2" class="pull-left">{{translation.KEY_SCREEN_PASSWORD_LABEL}}</label> <label class="pull-right"><a
                        href="passwordForgot.htm?a={{accountTypeInitials}}" tabindex="-1">{{translation.KEY_SCREEN_FORGOT_PASSWORD_LABEL}}?</a></label>
                    <input type="password" class="form-control" name="input2" placeholder="{{translation.KEY_SCREEN_PASSWORD_LABEL}}" title="Password" required>
                </div>

                <c:if test="${not empty failure}">
                    <div id="displayLoginFormServerErrorResponse" style="display: block;" class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_FAILURE_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty logout}">
                    <div id="displayLoginFormServerErrorResponse" style="display: block;" class="alert alert-success" role="alert">
                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_SUCCESS_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_LOGOUT_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty denied}">
                    <div id="displayLoginFormServerErrorResponse" style="display: block;" class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_DENIED_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty locked}">
                    <div id="displayLoginFormServerErrorResponse" style="display: block;" class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-lock" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_LOCKED_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty disabled}">
                    <div id="displayLoginFormServerErrorResponse" style="display: block;" class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_DISABLED_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty accountDenied}">
                    <div id="displayLoginFormServerErrorResponse" style="display: block;" class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_ACCOUNT_DENIED_MESSAGE}}
                    </div>
                </c:if>

                <div id="displayLoginFormErrorResponse" style="display: none;" class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only"> {{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                    {{translation.KEY_SCREEN_LOGIN_FAILURE_MESSAGE}}
                </div>

                <button type="submit" class="btn btn-primary btn-block">{{translation.KEY_SCREEN_LOGIN_LABEL}}</button>

                <div>
                    <hr>
                    <div class="form-group">
                        <label>{{translation.KEY_SCREEN_DO_NOT_HAVE_ACCOUNT_LABEL}}?</label> <a href="registration.htm" class="btn btn-primary btn-block">{{translation.KEY_SCREEN_CREATE_ACCOUNT_LABEL}}</a>
                    </div>
                </div>

                <div><br><br><br>
                    <hr>
                    <div class="form-group">
                	       <label class="ng-binding">{{translation.KEY_SCREEN_OPERATOR_LOGIN_LABEL}}?</label> <a href="login.htm?op" class="btn btn-primary btn-block ng-binding">{{translation.KEY_SCREEN_OPERATOR_LOGIN_LABEL}}</a>
                    </div>
                </div>

            </form>
        </c:if>

        <c:if test="${op == true}">
            <h3 class="text-left">{{translation.KEY_SCREEN_OPERATOR_LOGIN_LABEL}}</h3>
            <form name="operatorLoginForm" autocomplete="off" method="post" action="processLogin">

                <input type="hidden" name="accountType" value="100" />

                <div class="form-group">
                    <label for="input1">{{translation.KEY_SCREEN_USERNAME_LABEL}}</label>
                    <input type="text" class="form-control" name="input1" placeholder="{{translation.KEY_SCREEN_USERNAME_LABEL}}" title="Username" required>
                </div>

                <div class="form-group">
                    <label for="input2" class="pull-left">{{translation.KEY_SCREEN_PASSWORD_LABEL}}</label>
                    <label class="pull-right"><a href="passwordForgot.htm?a=op" tabindex="-1">{{translation.KEY_SCREEN_FORGOT_PASSWORD_LABEL}}?</a></label>
                    <input type="password" class="form-control" name="input2" placeholder="{{translation.KEY_SCREEN_PASSWORD_LABEL}}" title="Password" required>
                </div>

                <c:if test="${not empty failure}">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_FAILURE_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty logout}">
                    <div class="alert alert-success" role="alert">
                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_SUCCESS_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_LOGOUT_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty denied}">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_DENIED_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty locked}">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-lock" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_LOCKED_MESSAGE}}
                    </div>
                </c:if>
                <c:if test="${not empty disabled}">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{translation.KEY_SCREEN_LOGIN_DISABLED_MESSAGE}}
                    </div>
                </c:if>

                <button type="submit" class="btn btn-primary btn-block">{{translation.KEY_SCREEN_LOGIN_LABEL}}</button>
            </form>
        </c:if>
    </div>

</body>
</html>
