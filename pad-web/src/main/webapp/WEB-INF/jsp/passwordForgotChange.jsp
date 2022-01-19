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
<body ng-controller="passwordForgotChangeController" ng-cloak ng-init="init('${isTestEnvironment}')">

    <%@ include file="../../app/views/offlineNavbar.jsp"%>

    <h3 class="text-center">{{translation.KEY_NAVBAR_CHANGE_PASSWORD}}</h3>

    <div class="container-fluid pad-center">

        <span us-spinner="{radius:16, width:6, length: 14}" spinner-key="spinner-login"></span>

        <div ng-hide="passwdChangeSuccess">
            <form name="dataForm" autocomplete="off" novalidate>
                <div class="form-group">
                    <div class="alert alert-info">
                        <b>{{translation.KEY_SCREEN_PASSWORD_POLICY_LABEL}}:</b><br> {{translation.KEY_SCREEN_PASSWORD_POLICY}}
                    </div>
                </div>
                <div class="form-group">
                    <label for="password">{{translation.KEY_SCREEN_PASSWORD_LABEL}}</label>
                    <input type="password" class="form-control" name="password" placeholder="{{translation.KEY_SCREEN_PASSWORD_LABEL}}" ng-model="formData.password"
                        ng-pattern="passwordRegexp" ng-maxlength="32" maxlength="32" oncopy="return false" onpaste="return false" autofocus required>
                </div>
                <div class="form-group">
                    <label for="passwordConfirm">{{translation.KEY_SCREEN_CONFIRM_PASSWORD_LABEL}}</label>
                    <input type="password" class="form-control" name="passwordConfirm" placeholder="{{translation.KEY_SCREEN_CONFIRM_PASSWORD_LABEL}}"
                        ng-model="formData.passwordConfirm" ng-maxlength="32" maxlength="32" oncopy="return false" onpaste="return false">
                </div>
                <div class="form-group">
                    {{recaptchaKey=
                    <c:out value="${recaptchaKey}" />
                    ;""}}

                    <div ng-if="language === 'EN'" vc-recaptcha key="recaptchaKey" lang="en" theme="clean" on-create="setWidgetId(widgetId)" on-success="setResponse(response)"
                         on-expire="cbExpiration()"></div>

                    <div ng-if="language === 'FR'" vc-recaptcha key="recaptchaKey" lang="fr" theme="clean" on-create="setWidgetId(widgetId)" on-success="setResponse(response)"
                         on-expire="cbExpiration()"></div>
                </div>
                <div ng-if="passwordForgotStep2ErrorResponse !== ''">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <span class="sr-only">{{translation.KEY_SCREEN_ERROR_LABEL}}: </span>
                        {{passwordForgotStep2ErrorResponse}}
                    </div>
                </div>

                <button type="button" id="submitButton" class="btn btn-primary btn-block" ng-disabled="passwordForgotStep2SubmitButtonDisabled || passwdChangeSuccess"
                    ng-click="passwordForgotChange()">{{translation.KEY_NAVBAR_CHANGE_PASSWORD}}</button>

                {{input1=
                <c:out value="${userName}" />
                ;""}} {{input4=
                <c:out value="${key}" />
                ;""}} {{accountType=
                <c:out value="${accountType}" />
                ;""}}

            </form>
        </div>

        <div ng-if="passwdChangeSuccess">
            <div class="alert alert-success">
                <h4>{{translation.KEY_SCREEN_PASSWORD_UPDATED_MESSAGE}}!</h4>
                <div style="padding-top: 20px;">
                    {{translation.KEY_SCREEN_LOGIN_SYSTEM_NEW_PASSWORD_MESSAGE}}. <br> <br> <a class="alert-link" href="login.htm">{{translation.KEY_SCREEN_CLICK_HERE_LOGIN_LABEL}}!</a>
                </div>
            </div>
        </div>

    </div>
</body>
</html>
