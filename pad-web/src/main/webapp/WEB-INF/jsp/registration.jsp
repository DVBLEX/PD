<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html ng-app="registrationApp">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="format-detection" content="telephone=no">

<title>Port Access Dakar - Registration</title>

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
<script type="text/javascript" src="app/js/registration.js"></script>
</head>

<body ng-controller="registrationController" ng-cloak ng-init="init('${isTestEnvironment}')" onbeforeunload="return confirmPageReload()">

    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="login.htm?tp"> <img src="app/img/ags_logo.png" alt="" style="height: 77%; float: left; margin-top: 2px;" /> &nbsp; Port Access Dakar <c:if
                        test="${isTestEnvironment == true}">
                        <span>(Sandbox)</span>
                    </c:if>
                    <div style="position:absolute;font-size: x-small;color: gray;padding-left: 58px;">
                        <%@ include file="buildNumber.jsp"%>
                    </div>
                </a>
            </div>
            <div id="navbar" class="navbar-collapse collapse" aria-expanded="false" style="height: 1px;">
                <ul class="nav navbar-nav navbar-right">
                    <li class="navbar-select hidden-xs hidden-sm hidden-md"><span class="glyphicon glyphicon-globe" style="font-size: 14pt; margin-top: 7px; margin-right: 0px;"></span></li>
                    <li class="navbar-select"><select class="form-control select-language" style="width: inherit;" ng-change="changeLanguage(language);" ng-model="language">
                            <option value="FR">Français</option>
                            <option value="EN">English</option>
                        </select>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <h3 class="text-center">{{translation.KEY_SCREEN_REGISTRATION_LABEL}}</h3>

    <div class="container-fluid " ng-cloak>

        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div ng-hide="registrationSuccess">

                    <div class="panel" ng-class="{'panel-primary': !step1IsCompleted, 'panel-success': step1IsCompleted}">
                        <div class="panel-heading" ng-click="selectStep(0)">
                            <div class="panel-title">
                                1. {{translation.KEY_SCREEN_ACCOUNT_TYPE_LABEL}} <span class="glyphicon glyphicon-ok" ng-show="step1IsCompleted"></span>
                            </div>
                        </div>

                        <div class="panel-body" ng-show="formStepsSelected[0]">
                            <div class="col-sm-12" ng-show="!step1IsCompleted">
                                <form name="accountTypeForm" autocomplete="off" novalidate>
                                    <div class="row">

                                        <div class="form-group col-sm-12">
                                            <label class=""><input type="checkbox" name="agreeTerms" ng-model="formData.agreeTerms" required>
                                                {{translation.KEY_SCREEN_AGREE_TERMS_MESSAGE}} <a href="https://agsparking.com/" target="_blank">{{translation.KEY_SCREEN_TERMS_AND_CONDITIONS_LABEL}}</a>
                                            </label>
                                        </div>

                                        <div class="form-group col-sm-12">
                                            <label>{{translation.KEY_SCREEN_ACCOUNT_TYPE_LABEL}}</label>
                                            <div>
                                                <label class="label-radio"><input type="radio" name="accountType" ng-model="formData.accountType" value="1"
                                                        ng-disabled="isMsisdnVerified || isEmailVerified" required /> {{translation.KEY_SCREEN_COMPANY_LABEL}} </label>&nbsp;&nbsp;&nbsp; <label
                                                    class="label-radio"><input type="radio" name="accountType" ng-model="formData.accountType" value="2"
                                                        ng-disabled="isMsisdnVerified || isEmailVerified" required /> {{translation.KEY_SCREEN_INDIVIDUAL_LABEL}}</label>
                                            </div>
                                        </div>

                                        <div class="form-group col-sm-12">
                                            <div class="animate-if" ng-if="step1ErrorMessage !== ''">
                                                <div class="alert alert-danger" role="alert">
                                                    <span class="glyphicon glyphicon-remove"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                                                    {{step1ErrorMessage}}
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-sm-12">
                                            <button type="button" class="btn btn-primary btn-block" ng-click="step1Submit()">
                                                <span class="glyphicon glyphicon-chevron-right"></span> {{translation.KEY_SCREEN_NEXT_LABEL}}
                                            </button>
                                        </div>

                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>

                    <div class="panel"
                        ng-class="{'panel-primary': !step2IsCompleted || !step1IsCompleted, 'panel-success': step2IsCompleted && step1IsCompleted, 'pad-disabledAccordion': !step1IsCompleted}">
                        <div class="panel-heading" ng-click="selectStep(1)" ng-class="{'pad-linkDisabled': !step1IsCompleted}">
                            <div class="panel-title" ng-show="formData.accountType != 1 && formData.accountType != 2">2. {{translation.KEY_SCREEN_LOGIN_LABEL}} -</div>
                            <div class="panel-title" ng-show="formData.accountType == 1">
                                2. {{translation.KEY_SCREEN_LOGIN_EMAIL_VERIFICATION_LABEL}} <span class="glyphicon glyphicon-ok" ng-show="step2IsCompleted"></span>
                            </div>
                            <div class="panel-title" ng-show="formData.accountType == 2">
                                2. {{translation.KEY_SCREEN_LOGIN_MOBILE_NUMBER_VERIFICATION_LABEL}} <span class="glyphicon glyphicon-ok" ng-show="step2IsCompleted"></span>
                            </div>
                        </div>

                        <div class="panel-body" ng-show="formStepsSelected[1]">

                            <div class="col-sm-12" ng-show="step1IsCompleted || !passwordSubmittedSuccess">

                                <div class="col-sm-12" ng-show="!verifyCodeHasSent || isEmailVerified || isMsisdnVerified">

                                    <form name="verificationCodeForm" autocomplete="off" novalidate>

                                        <div class="row">
                                            <div class="form-group col-sm-6">
                                                <label for="firstName">{{translation.KEY_SCREEN_FIRST_NAME_LABEL}}</label>
                                                <input type="text" class="form-control" name="firstName" ng-model="formData.firstName" ng-maxlength="32" maxlength="32"
                                                    ng-trim="true" required avoid-special-chars avoid-numbers autofocus>
                                            </div>

                                            <div class="form-group col-sm-6">
                                                <label for="lastName">{{translation.KEY_SCREEN_LAST_NAME_LABEL}}</label>
                                                <input type="text" class="form-control" name="lastName" ng-model="formData.lastName" ng-maxlength="32" maxlength="32" ng-trim="true"
                                                    required avoid-special-chars avoid-numbers>
                                            </div>

                                            <div class="form-group col-sm-6" ng-show="formData.accountType == 1">
                                                <label for="email">{{translation.KEY_SCREEN_EMAIL_LABEL}}</label>
                                                <input type="text" class="form-control" name="email" ng-model="formData.email" ng-pattern="emailRegexp" ng-maxlength="64"
                                                    maxlength="64" required ng-trim="true" oncopy="return false" onpaste="return false" ng-disabled="isEmailVerified">
                                            </div>

                                            <div class="form-group col-sm-6" ng-show="formData.accountType == 1">
                                                <label for="email_confirm">{{translation.KEY_SCREEN_CONFIRM_EMAIL_LABEL}}</label>
                                                <input type="text" class="form-control" name="emailConfirm" ng-model="formData.emailConfirm" ng-maxlength="64" maxlength="64"
                                                    required ng-trim="true" oncopy="return false" onpaste="return false" ng-disabled="isEmailVerified">
                                            </div>

                                            <div class="form-group col-sm-6" ng-show="formData.accountType == 2">
                                                <label for="mobileNumber">{{translation.KEY_SCREEN_MOBILE_NUMBER_LABEL}}</label>
                                                <div style="width: 100%;">
                                                    <input type="text" class="form-control" name="mobileNumber" id="mobileNumber" ng-model="formData.mobileNumber"
                                                        ng-disabled="isMsisdnVerified" ng-intl-tel-input required>
                                                </div>
                                            </div>

                                            <div class="form-group col-sm-6" ng-show="formData.accountType == 2">
                                                <label for="mobileNumberConfirm">{{translation.KEY_SCREEN_MOBILE_NUMBER_CONFIRM_LABEL}}</label>
                                                <div style="width: 100%;">
                                                    <input type="text" class="form-control" name="mobileNumberConfirm" id="mobileNumberConfirm"
                                                        ng-model="formData.mobileNumberConfirm" ng-disabled="isMsisdnVerified" ng-intl-tel-input required>
                                                </div>
                                            </div>

                                            <div class="form-group col-sm-12" ng-if="!isEmailVerified && !isMsisdnVerified">
                                                {{recaptchaKey=
                                                <c:out value="${recaptchaKey}" />
                                                ;""}}

                                                <div ng-if="language === 'EN'" vc-recaptcha key="recaptchaKey" lang="en" theme="clean" on-create="setWidgetId(widgetId)" on-success="setResponse(response)"
                                                     on-expire="cbExpiration()"></div>

                                                <div ng-if="language === 'FR'" vc-recaptcha key="recaptchaKey" lang="fr" theme="clean" on-create="setWidgetId(widgetId)" on-success="setResponse(response)"
                                                     on-expire="cbExpiration()"></div>
                                            </div>

                                            <div class="form-group col-sm-12" ng-show="formData.accountType == 1">
                                                <div class="animate-if" ng-if="emailVerificationStep1ErrorResponse !== ''">
                                                    <div class="alert alert-danger" role="alert">
                                                        <span class="glyphicon glyphicon-remove"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                                                        {{emailVerificationStep1ErrorResponse}}
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group col-sm-12" ng-show="formData.accountType == 2">
                                                <div class="animate-if" ng-if="msisdnVerificationStep1ErrorResponse !== ''">
                                                    <div class="alert alert-danger" role="alert">
                                                        <span class="glyphicon glyphicon-remove"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                                                        {{msisdnVerificationStep1ErrorResponse}}
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-sm-12" ng-if="!isEmailVerified && formData.accountType == 1">
                                                <button type="button" class="btn btn-info btn-block" ng-disabled="verifyCodeHasSent" ng-click="sendEmailVerificationCode()">
                                                    <span class="glyphicon glyphicon-envelope"></span> {{translation.KEY_SCREEN_SEND_VERIFICATION_CODE_LABEL}}
                                                </button>
                                            </div>

                                            <div class="col-sm-12" ng-if="!isMsisdnVerified && formData.accountType == 2">
                                                <button type="button" class="btn btn-info btn-block" ng-disabled="verifyCodeHasSent" ng-click="sendMsisdnVerificationCode()">
                                                    <span class="glyphicon glyphicon-phone"></span> {{translation.KEY_SCREEN_SEND_VERIFICATION_CODE_LABEL}}
                                                </button>
                                            </div>

                                            <div class="col-sm-12" ng-if="isEmailVerified && formData.accountType == 1">
                                                <div class="alert alert-success" role="alert">
                                                    <span class="glyphicon glyphicon-ok"></span> <span class="sr-only">{{translation.KEY_SCREEN_SUCCESS_LABEL}}: </span> <span>{{translation.KEY_SCREEN_THE_EMAIL_ADDRESS_LABEL}}
                                                        <strong>{{formData.email}}</strong> {{translation.KEY_SCREEN_IS_SUCCESSFULLY_VERIFIED_LABEL}}!
                                                    </span>
                                                </div>

                                                <button type="button" class="btn btn-primary btn-block" ng-click="step2Submit()">
                                                    {{translation.KEY_SCREEN_NEXT_LABEL}} <span class="glyphicon glyphicon-chevron-right"></span>
                                                </button>
                                            </div>

                                            <div class="col-sm-12" ng-if="isMsisdnVerified && formData.accountType == 2">
                                                <div class="alert alert-success" role="alert">
                                                    <span class="glyphicon glyphicon-ok"></span> <span class="sr-only">{{translation.KEY_SCREEN_SUCCESS_LABEL}}: </span> <span>{{translation.KEY_SCREEN_THE_MOBILE_NUMBER_LABEL}}
                                                        <strong>{{formData.mobileNumber}}</strong> {{translation.KEY_SCREEN_IS_SUCCESSFULLY_VERIFIED_LABEL}}!
                                                    </span>
                                                </div>

                                                <button type="button" class="btn btn-primary btn-block" ng-click="step2Submit()">
                                                    {{translation.KEY_SCREEN_NEXT_LABEL}} <span class="glyphicon glyphicon-chevron-right"></span>
                                                </button>
                                            </div>

                                        </div>

                                    </form>

                                </div>

                                <div class="col-sm-12" ng-show="verifyCodeHasSent && !isEmailVerified && formData.accountType == 1">

                                    <div class="row">

                                        <div class="col-sm-12">

                                            <form name="emailCodeVerificationForm" autocomplete="off" novalidate>

                                                <div>&nbsp;</div>

                                                <div ng-if="!isEmailVerified">
                                                    <p>
                                                        {{translation.KEY_SCREEN_VERIFICATION_CODE_SENT_MESSAGE | to_trusted_html}}: <strong>{{formData.email}}</strong>.
                                                    </p>
                                                    <p>{{translation.KEY_SCREEN_CHECK_INBOX_MESSAGE}}</p>
                                                    <p>
                                                        {{translation.KEY_SCREEN_DIDNT_RECEIVE_VERIFICATION_CODE_MESSAGE}}? <a href="#" ng-click="reSendVerificationCode()"
                                                            tabindex="-1">{{translation.KEY_SCREEN_CLICK_HERE_LABEL}}</a> {{translation.KEY_SCREEN_TO_RESEND_CHANGE_EMAIL_LABEL}}.
                                                    </p>
                                                    <div class="form-group">
                                                        <label for="email">{{translation.KEY_SCREEN_VERIFICATION_CODE_LABEL}}</label>
                                                        <input type="text" class="form-control" name="emailVerificationCode" ng-readonly="isEmailVerified"
                                                            ng-model="formData.emailVerificationCode" ng-minlength="5" ng-maxlength="5" maxlength="5" ng-pattern="/^\d{5}$/"
                                                            required autofocus>
                                                    </div>

                                                    <div ng-if="emailVerificationStep2ErrorResponse !== ''">
                                                        <div class="alert alert-danger" role="alert">
                                                            <span class="glyphicon glyphicon-remove"></span> <span class="sr-only">Error: </span>
                                                            {{emailVerificationStep2ErrorResponse}}
                                                        </div>
                                                    </div>

                                                    <button type="button" class="btn btn-success btn-block" ng-disabled="isEmailVerified" ng-click="verifyEmailCode()">
                                                        <span class="glyphicon glyphicon-check"></span> {{translation.KEY_SCREEN_VERIFY_EMAIL_LABEL}}
                                                    </button>
                                                </div>

                                            </form>

                                        </div>

                                    </div>

                                </div>

                                <div class="col-sm-12" ng-show="verifyCodeHasSent && !isMsisdnVerified && formData.accountType == 2">

                                    <div class="row">

                                        <div class="col-sm-12">

                                            <form name="smsCodeVerificationForm" autocomplete="off" novalidate>

                                                <div>&nbsp;</div>

                                                <div ng-if="!isMsisdnVerified">
                                                    <p>
                                                        {{translation.KEY_SCREEN_VERIFICATION_CODE_SMS_SENT_MESSAGE | to_trusted_html}} <strong>{{formData.mobileNumber}}</strong>.
                                                    </p>
                                                    <p>
                                                        {{translation.KEY_SCREEN_DIDNT_RECEIVE_VERIFICATION_CODE_MESSAGE}}? <a href="#" ng-click="reSendVerificationCode()"
                                                            tabindex="-1">{{translation.KEY_SCREEN_CLICK_HERE_LABEL}}</a> {{translation.KEY_SCREEN_TO_RESEND_CHANGE_MSISDN_LABEL}}.
                                                    </p>
                                                    <div class="form-group">
                                                        <label for="email">{{translation.KEY_SCREEN_VERIFICATION_CODE_LABEL}}</label>
                                                        <input type="text" class="form-control" name="smsVerificationCode" ng-readonly="isMsisdnVerified"
                                                            ng-model="formData.smsVerificationCode" ng-minlength="5" ng-maxlength="5" maxlength="5" ng-pattern="/^\d{5}$/" required
                                                            autofocus>
                                                    </div>

                                                    <div ng-if="msisdnVerificationStep2ErrorResponse !== ''">
                                                        <div class="alert alert-danger" role="alert">
                                                            <span class="glyphicon glyphicon-remove"></span> <span class="sr-only">Error: </span>
                                                            {{msisdnVerificationStep2ErrorResponse}}
                                                        </div>
                                                    </div>

                                                    <button type="button" class="btn btn-success btn-block" ng-disabled="isMsisdnVerified" ng-click="verifySMSCode()">
                                                        <span class="glyphicon glyphicon-check"></span> {{translation.KEY_SCREEN_VERIFY_MSISDN_LABEL}}
                                                    </button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>

                    </div>

                    <div class="panel"
                        ng-class="{'panel-primary': !step3IsCompleted || !step1IsCompleted || !step2IsCompleted, 'panel-success': step3IsCompleted && step1IsCompleted && step2IsCompleted, 'pad-disabledAccordion': !step1IsCompleted || !step2IsCompleted}">
                        <div class="panel-heading" ng-click="selectStep(2)" ng-class="{'pad-linkDisabled': !step1IsCompleted || !step2IsCompleted}">
                            <div class="panel-title">
                                3. {{translation.KEY_SCREEN_LOGIN_PASSWORD_CREATION_LABEL}} <span class="glyphicon glyphicon-ok" ng-show="step3IsCompleted"></span>
                            </div>
                        </div>

                        <div class="panel-body" ng-show="formStepsSelected[2]">
                            <div class="col-sm-12" ng-show="step2IsCompleted || !passwordSubmittedSuccess">
                                <form name="passwordVerificationForm" autocomplete="off" novalidate>
                                    <div class="row">
                                        <div class="form-group col-sm-12">
                                            <div class="alert alert-info">
                                                <b>{{translation.KEY_SCREEN_PASSWORD_POLICY_LABEL}}:</b><br> {{translation.KEY_SCREEN_PASSWORD_POLICY}}
                                            </div>
                                        </div>
                                        <div class="form-group col-sm-6">
                                            <label for="password">{{translation.KEY_SCREEN_PASSWORD_LABEL}}</label>
                                            <input type="password" class="form-control" name="password" placeholder="Password" title="Password" ng-model="formData.password"
                                                ng-pattern="passwordRegexp" ng-maxlength="32" maxlength="32" oncopy="return false" onpaste="return false" required>
                                        </div>
                                        <div class="form-group col-sm-6">
                                            <label for="passwordConfirm">{{translation.KEY_SCREEN_CONFIRM_PASSWORD_LABEL}}</label>
                                            <input type="password" class="form-control" name="passwordConfirm" placeholder="Confirm Password" title="Password"
                                                ng-model="formData.passwordConfirm" ng-maxlength="32" maxlength="32" oncopy="return false" onpaste="return false">
                                        </div>

                                        <div class="col-sm-12" ng-if="passwordSubmitErrorResponse !== ''">
                                            <div class="alert alert-danger" role="alert">
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">Error: </span>
                                                {{passwordSubmitErrorResponse}}
                                            </div>
                                        </div>
                                        <div class="col-sm-12">
                                            <button type="button" class="btn btn-primary btn-block" ng-click="step3Submit()">
                                                {{translation.KEY_SCREEN_NEXT_LABEL}} <span class="glyphicon glyphicon-chevron-right"></span>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                    <div class="panel"
                        ng-class="{'panel-primary': !step4IsCompleted || !step1IsCompleted || !step2IsCompleted || !step3IsCompleted,
                'panel-success': step4IsCompleted && step1IsCompleted && step2IsCompleted && step3IsCompleted, 'pad-disabledAccordion': !step1IsCompleted || !step2IsCompleted || !step3IsCompleted}">
                        <div class="panel-heading" ng-click="selectStep(3)" ng-class="{'pad-linkDisabled': !step1IsCompleted || !step2IsCompleted || !step3IsCompleted}">
                            <div class="panel-title">
                                4. {{translation.KEY_SCREEN_ACCOUNT_DETAILS_LABEL}} <span class="glyphicon glyphicon-ok" ng-show="step4IsCompleted"></span>
                            </div>
                        </div>

                        <div class="panel-body" ng-show="formStepsSelected[3]">
                            <div class="col-sm-12">
                                <form name="accountDetailsForm" autocomplete="off" novalidate>
                                    <div class="row">
                                        <div class="form-group col-sm-6" ng-hide="formData.accountType != 1">
                                            <label for="companyName">{{translation.KEY_SCREEN_TRANSPORTER_COMPANY_NAME_LABEL}}</label>
                                            <input type="text" class="form-control" name="companyName" ng-model="formData.companyName" ng-maxlength="64" maxlength="64"
                                                tabindex="-1" ng-trim="true" avoid-special-chars-v2 required>
                                        </div>

                                        <div class="form-group col-sm-6" ng-hide="formData.accountType != 1">
                                            <label for="companyRegistrationNumber">{{translation.KEY_SCREEN_COMPANY_RESGISTRATION_NUMBER_MESSAGE}} <span class="optional">-
                                                    {{translation.KEY_SCREEN_OPTIONAL_LABEL}}</span></label>
                                            <input type="text" class="form-control" name="companyRegistrationNumber" ng-model="formData.companyRegistrationNumber" ng-maxlength="64"
                                                maxlength="64" tabindex="0" ng-trim="true" avoid-special-chars>
                                        </div>

                                        <div class="form-group col-sm-6">
                                            <label for="address1" ng-show="formData.accountType == 1">{{translation.KEY_SCREEN_REGISTERED_ADDRESS_LINE_LABEL}} 1</label> <label
                                                for="address1" ng-show="formData.accountType == 2">{{translation.KEY_SCREEN_ADDRESS_LINE_LABEL}} 1</label>
                                            <input type="text" class="form-control" name="address1" ng-model="formData.address1" ng-maxlength="64" maxlength="64" ng-trim="true"
                                                avoid-special-chars required>
                                        </div>

                                        <div class="form-group col-sm-6">
                                            <label for="address2" ng-show="formData.accountType == 1">{{translation.KEY_SCREEN_REGISTERED_ADDRESS_LINE_LABEL}} 2</label> <label
                                                for="address2" ng-show="formData.accountType == 2">{{translation.KEY_SCREEN_ADDRESS_LINE_LABEL}} 2</label>
                                            <input type="text" class="form-control" name="address2" ng-model="formData.address2" ng-maxlength="64" maxlength="64" ng-trim="true"
                                                avoid-special-chars required>
                                        </div>

                                        <div class="form-group col-sm-6">
                                            <label for="address3" ng-show="formData.accountType == 1">{{translation.KEY_SCREEN_REGISTERED_ADDRESS_LINE_LABEL}} 3 <span
                                                class="optional">- {{translation.KEY_SCREEN_OPTIONAL_LABEL}}</span></label> <label for="address3" ng-show="formData.accountType == 2">{{translation.KEY_SCREEN_ADDRESS_LINE_LABEL}}
                                                3 <span class="optional">- {{translation.KEY_SCREEN_OPTIONAL_LABEL}}</span>
                                            </label>
                                            <input type="text" class="form-control" name="address3" ng-model="formData.address3" ng-maxlength="64" maxlength="64" ng-trim="true"
                                                avoid-special-chars>
                                        </div>

                                        <div class="form-group col-sm-6">
                                            <label for="address4" ng-show="formData.accountType == 1">{{translation.KEY_SCREEN_REGISTERED_ADDRESS_LINE_LABEL}} 4 <span
                                                class="optional">- {{translation.KEY_SCREEN_OPTIONAL_LABEL}}</span></label> <label for="address4" ng-show="formData.accountType == 2">{{translation.KEY_SCREEN_ADDRESS_LINE_LABEL}}
                                                4 <span class="optional">- {{translation.KEY_SCREEN_OPTIONAL_LABEL}}</span>
                                            </label>
                                            <input type="text" class="form-control" name="address4" ng-model="formData.address4" ng-maxlength="64" maxlength="64" ng-trim="true"
                                                avoid-special-chars>
                                        </div>

                                        <div class="form-group col-sm-6">
                                            <label for="postcode">{{translation.KEY_SCREEN_POSTCODE_LABEL}}</label>
                                            <input type="text" class="form-control" name="postcode" ng-model="formData.postcode" ng-maxlength="16" maxlength="16" ng-trim="true"
                                                avoid-special-chars required>
                                        </div>

                                        <div class="form-group col-sm-6">
                                            <label for="country">{{translation.KEY_SCREEN_COUNTRY_LABEL}}</label>
                                            <select class="form-control" name="country" ng-model="formData.country" ng-init="formData.country='SN'" required>
                                                <option value="SN" ng-if="language === 'EN'">Senegal</option>
                                                <option value="SN" ng-if="language === 'FR'">Sénégal</option>
                                                <option value="ML" ng-if="language === 'EN'">Mali</option>
                                                <option value="ML" ng-if="language === 'FR'">Mali</option>
                                            </select>
                                        </div>

                                        <div class="form-group col-sm-6" ng-hide="formData.accountType != 1">
                                            <label for="telephoneNumber">{{translation.KEY_SCREEN_TELEPHONE_NUMBER_LABEL}}</label>
                                            <div style="width: 100%;">
                                                <input type="text" class="form-control" name="telephoneNumber" id="telephoneNumber" ng-model="formData.telephoneNumber"
                                                    ng-intl-tel-input maxlength="14" required>
                                            </div>
                                        </div>

                                        <div class="form-group col-sm-6" ng-hide="formData.accountType != 1">
                                            <label for="specialTaxStatus">{{translation.KEY_SCREEN_SPECIAL_TAX_STATUS_LABEL}}</label>
                                            <input type="checkbox" class="form-control" style="margin-top: 5px;" name="specialTaxStatus" ng-model="formData.specialTaxStatus"
                                                required>
                                        </div>

                                        <div class="form-group col-sm-6" ng-hide="formData.accountType != 2">
                                            <label for="nationality" class="text-nowrap">{{translation.KEY_SCREEN_COUNTRY_LABEL}}
                                                ({{translation.KEY_SCREEN_NATIONALITY_LABEL}})</label>
                                            <select class="form-control" name="nationality" ng-model="formData.nationality" required>
                                                <option value="SN" ng-if="language === 'EN'">Senegal</option>
                                                <option value="SN" ng-if="language === 'FR'">Sénégal</option>
                                                <option value="ML" ng-if="language === 'EN'">Mali</option>
                                                <option value="ML" ng-if="language === 'FR'">Mali</option>
                                            </select>
                                        </div>

                                        <div class="animate-if col-sm-12" ng-if="step4ErrorMessage !== ''">
                                            <div class="alert alert-danger" role="alert">
                                                <span class="glyphicon glyphicon-remove"></span><span class="sr-only">Error: </span> <span>{{step4ErrorMessage}}</span>
                                            </div>
                                        </div>

                                        <div class="col-sm-12">
                                            <button type="button" class="btn btn-primary btn-block" ng-click="step4Submit()">
                                                {{translation.KEY_SCREEN_NEXT_LABEL}} <span class="glyphicon glyphicon-chevron-right"></span>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>

                    <div class="panel"
                        ng-class="{'panel-primary': !step5IsCompleted || !step4IsCompleted || !step1IsCompleted || !step2IsCompleted || !step3IsCompleted,
                'panel-success': step5IsCompleted && step4IsCompleted && step1IsCompleted && step2IsCompleted && step3IsCompleted, 'pad-disabledAccordion': !step1IsCompleted || !step2IsCompleted || !step3IsCompleted || !step4IsCompleted}">
                        <div class="panel-heading" ng-click="selectStep(4)"
                            ng-class="{'pad-linkDisabled': !step1IsCompleted || !step2IsCompleted || !step3IsCompleted || !step4IsCompleted}">
                            <div class="panel-title">
                                5. {{translation.KEY_SCREEN_REVIEW_SUBMIT_LABEL}} <span class="glyphicon glyphicon-ok" ng-show="step5IsCompleted"></span>
                            </div>
                        </div>

                        <div class="panel-body" ng-show="formStepsSelected[4]">
                            <form name="submitDataForm" autocomplete="off" novalidate>

                                <div class="col-sm-12">
                                    <h5 class="no-margin-top">{{translation.KEY_SCREEN_REVIEW_SUBMIT_MISSION_MESSAGE}}.</h5>
                                </div>

                                <div class="col-sm-12 form-group bg-info margin-top">
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_FIRST_NAME_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.firstName}}</div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_LAST_NAME_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.lastName}}</div>
                                    </div>
                                    <div class="row" ng-hide="formData.accountType == 2">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_EMAIL_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.email}}</div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_ACCOUNT_TYPE_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{getAccountTypeText(formData.accountType)}}</div>
                                    </div>

                                    <div class="row" ng-hide="formData.accountType == 1">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_MOBILE_NUMBER_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.mobileNumber}}</div>
                                    </div>

                                    <div class="row" ng-hide="formData.accountType == 2">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_TRANSPORTER_COMPANY_NAME_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.companyName}}</div>
                                    </div>

                                    <div class="row" ng-hide="formData.accountType == 2">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_COMPANY_RESGISTRATION_NUMBER_MESSAGE}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.companyRegistrationNumber}}</div>
                                    </div>

                                    <div class="row" ng-hide="formData.accountType == 2">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_TELEPHONE_NUMBER_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">{{formData.telephoneNumber}}</div>
                                    </div>

                                    <div class="row" ng-hide="formData.accountType == 2">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_SPECIAL_TAX_STATUS_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">
                                            <span ng-show="formData.specialTaxStatus">{{translation.KEY_SCREEN_YES_LABEL}}</span><span ng-show="!formData.specialTaxStatus">{{translation.KEY_SCREEN_NO_LABEL}}</span>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-sm-3" ng-show="formData.accountType == 1">
                                            <label>{{translation.KEY_SCREEN_COMPANY_ADDRESS_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-3" ng-show="formData.accountType == 2">
                                            <label>{{translation.KEY_SCREEN_ADDRESS_LABEL}}</label>
                                        </div>
                                        <div class="col-sm-9">
                                            <span ng-bind-html="previewCompanyAddress() | to_trusted_html"></span>
                                        </div>
                                    </div>

                                    <div class="row" ng-hide="formData.accountType == 1">
                                        <div class="col-sm-3">
                                            <label>{{translation.KEY_SCREEN_COUNTRY_LABEL}} ({{translation.KEY_SCREEN_NATIONALITY_LABEL}})</label>
                                        </div>
                                        <div class="col-sm-9">{{getCountryNameByISOCode(formData.nationality)}}</div>
                                    </div>

                                </div>

                                <div class="col-sm-12 form-group  no-padding-right no-padding-left">

                                    <div class="animate-if" ng-if="registrationErrorResponse !== ''">
                                        <div class="alert alert-danger" role="alert">
                                            <span class="glyphicon glyphicon-remove"></span> <span class="sr-only">Error: </span> {{registrationErrorResponse}}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-12 no-padding-right no-padding-left">
                                    <button type="submit" class="btn btn-success btn-block" ng-click="processRegistration()" ng-disabled="regSubmitButtonDisabled">
                                        {{translation.KEY_SCREEN_SUBMIT_LABEL}} <span class="glyphicon glyphicon-send"></span>
                                    </button>
                                </div>
                            </form>
                        </div>

                    </div>

                    <script>
                                                                                    function confirmPageReload() {
                                                                                        return "Do you want to reload this site? Changes you made may not be saved.";
                                                                                    }
                                                                                </script>
                </div>
            </div>
        </div>
        <div ng-if="registrationSuccess">
            <div class="text-center alert alert-success col-sm-12">
                <h4>{{translation.KEY_SCREEN_REGISTRATION_SUCCESSFUL_MESSAGE}}!</h4>
                <div style="padding-top: 20px;" ng-show="formData.accountType == 1">
                    {{translation.KEY_SCREEN_PLEASE_LOGIN_EMAIL_PASSWORD_MESSAGE}}. <br> <br>
                    <div class="col-sm-6 col-sm-offset-3">
                        <a class="btn btn-success btn-block" href="login.htm?tp">{{translation.KEY_SCREEN_CONTINUE_LABEL}}</a>
                    </div>
                </div>
                <div style="padding-top: 20px;" ng-show="formData.accountType == 2">
                    {{translation.KEY_SCREEN_PLEASE_LOGIN_MOBILE_PASSWORD_MESSAGE}}. <br> <br>
                    <div class="col-sm-6 col-sm-offset-3">
                        <a class="btn btn-success btn-block" href="login.htm?tp">{{translation.KEY_SCREEN_CONTINUE_LABEL}}</a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</body>
</html>
