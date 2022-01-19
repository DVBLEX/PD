var loginApp = angular.module('loginApp', [ 'ngResource', 'ngAnimate', 'vcRecaptcha', 'tcCom', 'angularSpinner', 'ngIntlTelInput' ])

.config([ '$httpProvider', function($httpProvider) {

    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
} ])

.config([ 'ngIntlTelInputProvider', function(ngIntlTelInputProvider) {
    ngIntlTelInputProvider.set({
        utilsScript : "lib/intl-tel-input-12.0.3/js/utils.js",
        initialCountry : 'sn',
        onlyCountries : [ 'sn', 'ml' ],
        preferredCountries : [],
        autoPlaceholder : "off",
        separateDialCode : true,
        nationalMode : false
    });
} ])

.run([ 'translationService', '$rootScope', function(translationService, $rootScope) {

    translationService.getLanguages();

    $rootScope.language = 'FR';

    $rootScope.changeLanguage = function(language) {
        $rootScope.language = language;
        switch (language) {
            case 'EN':
                $rootScope.translation = $rootScope.languageList.en.translationKeys;
                break;
            case 'FR':
                $rootScope.translation = $rootScope.languageList.fr.translationKeys;
                break;
            default:
                $rootScope.translation = $rootScope.languageList.fr.translationKeys;
        }
    };

    $rootScope.getTranslationStringByResponseCode = function(responseCode) {
        return $rootScope.$eval('translation.' + 'KEY_RESPONSE_' + responseCode);
    };
} ])

.controller('loginController', [ '$scope', '$rootScope', function($scope, $rootScope) {

    $scope.init = function(isTestEnvironment, accountType) {
        $rootScope.isTestEnvironment = isTestEnvironment;

        $scope.formData = {};

        if (accountType === "" || accountType === null || accountType === undefined) {
            $scope.formData.accountType = "1";

        } else if (accountType === "tpc") {
            $scope.formData.accountType = "1";

        } else if (accountType === "tpi") {
            $scope.formData.accountType = "2";
        }
    }

    $scope.$watch('formData.accountType', function() {
        if ($scope.formData.accountType === "" || $scope.formData.accountType === null || $scope.formData.accountType === undefined) {
            $scope.accountTypeInitials = "tpc";

        } else if ($scope.formData.accountType === "1") {
            $scope.accountTypeInitials = "tpc";

        } else if ($scope.formData.accountType === "2") {
            $scope.accountTypeInitials = "tpi";
        }
    });

    sessionStorage.removeItem('isKioskLaneConfirmationDialogAlreadyDisplayed');

} ])

.controller(
    'passwordForgotController',
    [ '$scope', '$rootScope', '$resource', 'vcRecaptchaService', 'tcComService', 'usSpinnerService',
        function($scope, $rootScope, $resource, vcRecaptchaService, tcComService, usSpinnerService) {

            $scope.init = function(isTestEnvironment, accountType) {
                $rootScope.isTestEnvironment = isTestEnvironment;

                $scope.formData = {};

                if (accountType === "" || accountType === null || accountType === undefined) {
                    $scope.formData.accountType = "1";

                } else if (accountType === "tpc") {
                    $scope.formData.accountType = "1";

                } else if (accountType === "tpi") {
                    $scope.formData.accountType = "2";

                } else if (accountType === "op") {
                    $scope.formData.accountType = "100";
                }
            }

            $scope.emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;
            $scope.passwdForgotSendSuccess = false;
            $scope.passwordForgotStep1ErrorResponse = "";
            usSpinnerService.stop('spinner-login');

            $scope.setResponse = function(response) {
                $scope.response = response;
            };

            $scope.setWidgetId = function(widgetId) {
                $scope.widgetId = widgetId;
            };

            $scope.cbExpiration = function() {
                vcRecaptchaService.reload($scope.widgetId);
            };

            $scope.errorHandler = function(error) {
                usSpinnerService.stop('spinner-login');
                vcRecaptchaService.reload($scope.widgetId);
            };

            $scope.passwordForgotSubmit = function() {

                if ($scope.dataForm.email.$invalid && $scope.formData.accountType == '1') {
                    $scope.passwordForgotStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_EMAIL_MESSAGE + ".";
                    return;
                }

                if ($scope.dataForm.mobileNumber.$invalid && $scope.formData.accountType == '2') {
                    $scope.passwordForgotStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";
                    return;
                }

                if ($scope.dataForm.userName.$invalid && $scope.formData.accountType == '100') {
                    $scope.passwordForgotStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_USERNAME_MESSAGE + ".";
                    return;
                }

                if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                    $scope.passwordForgotStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_RESOLVE_CAPTCHA_MESSAGE + ".";
                    return;
                }

                usSpinnerService.spin('spinner-login');

                var username = "";

                if ($scope.formData.accountType == '1') {
                    username =  $scope.formData.email;

                } else if ($scope.formData.accountType == '2') {
                    username =  $scope.formData.mobileNumber;

                } else if ($scope.formData.accountType == '100') {
                    username =  $scope.formData.userName;
                }

                var urlParams = {
                    accountType : $scope.formData.accountType,
                    input1 : username,
                    language : $rootScope.language,
                    recaptchaResponse : vcRecaptchaService.getResponse($scope.widgetId)
                };

                tcComService.callApiWithJSONParam("login/password/forgot/send", urlParams, function(response) {

                    usSpinnerService.stop('spinner-login');
                    $scope.passwdForgotSendSuccess = true;
                    $scope.passwordForgotStep1ErrorResponse = "";

                }, function(error) {

                    usSpinnerService.stop('spinner-login');

                    if (error.data.responseCode == 1200) {
                        $scope.passwordForgotStep1ErrorResponse = $rootScope.translation.KEY_RESPONSE_1200 + ".";

                        vcRecaptchaService.reload($scope.widgetId);

                    } else {
                        $scope.passwdForgotSendSuccess = true;
                    }
                });
            };

            $(function() {
                $("body").keypress(function(e) {
                    if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
                        $('#submitButton').click();
                        return false;
                    } else {
                        return true;
                    }
                });
            });

        } ])

.controller('passwordForgotChangeController',
    [ '$scope', '$rootScope', 'vcRecaptchaService', 'tcComService', 'usSpinnerService', function($scope, $rootScope, vcRecaptchaService, tcComService, usSpinnerService) {

        $scope.init = function(isTestEnvironment) {
            $rootScope.isTestEnvironment = isTestEnvironment;
        }

        $scope.passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;
        $scope.passwdChangeSuccess = false;
        $scope.passwordForgotStep2ErrorResponse = "";
        $scope.passwordForgotStep2SubmitButtonDisabled = false;
        usSpinnerService.stop('spinner-login');
        $scope.passwdFocused = true;

        $scope.setResponse = function(response) {
            $scope.response = response;
        };

        $scope.setWidgetId = function(widgetId) {
            $scope.widgetId = widgetId;
        };

        $scope.cbExpiration = function() {
            vcRecaptchaService.reload($scope.widgetId);
        };

        $scope.errorHandler = function(error) {

            usSpinnerService.stop('spinner-login');
            vcRecaptchaService.reload($scope.widgetId);
        };

        $scope.passwordForgotChange = function() {

            if ($scope.dataForm.password.$invalid) {
                $scope.passwordForgotStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_PASSWORD_MESSAGE + ".";

            } else if ($scope.dataForm.passwordConfirm.$invalid) {
                $scope.passwordForgotStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_PASSWORD_MESSAGE + ".";

            } else if ($scope.formData.password !== $scope.formData.passwordConfirm) {
                $scope.passwordForgotStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_PASSWORD_NOT_MATCH_MESSAGE + ".";

            } else if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                $scope.passwordForgotStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_RESOLVE_CAPTCHA_MESSAGE + ".";

            } else {
                $scope.passwordForgotStep2ErrorResponse = "";
                $scope.passwordForgotStep2SubmitButtonDisabled = true;
                usSpinnerService.spin('spinner-login');

                var urlParams = {
                    accountType : $scope.accountType,
                    input1 : $scope.input1,
                    input2 : $scope.formData.password,
                    input3 : $scope.formData.passwordConfirm,
                    input4 : $scope.input4,
                    recaptchaResponse : vcRecaptchaService.getResponse($scope.widgetId)
                };

                tcComService.callApiWithJSONParam("login/password/forgot/change", urlParams, function(response) {

                    usSpinnerService.stop('spinner-login');
                    $scope.passwdChangeSuccess = true;
                    $scope.passwordForgotStep2ErrorResponse = "";

                }, function(error) {

                    $scope.passwordForgotStep2SubmitButtonDisabled = true;
                    usSpinnerService.stop('spinner-login');
                    vcRecaptchaService.reload($scope.widgetId);
                    $scope.passwordForgotStep2ErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

        $(function() {
            $("body").keypress(function(e) {
                if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
                    $('#submitButton').click();
                    return false;
                } else {
                    return true;
                }
            });
        });

    } ])

.controller('passwordSetUpController', [ '$scope', '$rootScope', 'tcComService', 'usSpinnerService', function($scope, $rootScope, tcComService, usSpinnerService) {

    $scope.init = function(isTestEnvironment) {
        $rootScope.isTestEnvironment = isTestEnvironment;
    }

    $scope.passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;
    $scope.passwdSetUpSuccess = false;
    $scope.passwordSetUpErrorResponse = "";
    $scope.passwordSetUpSubmitButtonDisabled = false;
    usSpinnerService.stop('spinner-login');
    $scope.passwdFocused = true;

    $scope.passwordSetUp = function() {

        if ($scope.dataForm.password.$invalid) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_PASSWORD_MESSAGE + ".";

        } else if ($scope.dataForm.passwordConfirm.$invalid) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_PASSWORD_MESSAGE + ".";

        } else if ($scope.formData.password !== $scope.formData.passwordConfirm) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_PASSWORD_NOT_MATCH_MESSAGE + ".";

        } else {
            $scope.passwordSetUpErrorResponse = "";
            $scope.passwordSetUpSubmitButtonDisabled = true;
            usSpinnerService.spin('spinner-login');

            var urlParams = {
                input1 : $scope.input1,
                input2 : $scope.formData.password,
                input3 : $scope.formData.passwordConfirm,
                input4 : $scope.input4
            };

            tcComService.callApiWithJSONParam("login/password/setup", urlParams, function(response) {

                usSpinnerService.stop('spinner-login');
                $scope.passwdSetUpSuccess = true;
                $scope.passwordSetUpErrorResponse = "";

            }, function(error) {

                $scope.passwordSetUpSubmitButtonDisabled = false;
                usSpinnerService.stop('spinner-login');
                $scope.passwordSetUpErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });
        }
    };

} ])

.controller('operatorPasswordSetUpController', [ '$scope', '$rootScope', 'tcComService', 'usSpinnerService', function($scope, $rootScope, tcComService, usSpinnerService) {

    $scope.init = function(isTestEnvironment) {
        $rootScope.isTestEnvironment = isTestEnvironment;
    }

    $scope.passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;
    $scope.passwdSetUpSuccess = false;
    $scope.passwordSetUpErrorResponse = "";
    $scope.passwordSetUpSubmitButtonDisabled = false;
    usSpinnerService.stop('spinner-login');
    $scope.passwdFocused = true;

    $scope.passwordSetUp = function() {

        if ($scope.dataForm.smsVerificationCode.$invalid) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_VALID_VERIFICATION_CODE_MESSAGE + ".";

        } else if ($scope.dataForm.password.$invalid) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_PASSWORD_MESSAGE + ".";

        } else if ($scope.dataForm.passwordConfirm.$invalid) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_PASSWORD_MESSAGE + ".";

        } else if ($scope.formData.password !== $scope.formData.passwordConfirm) {
            $scope.passwordSetUpErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_PASSWORD_NOT_MATCH_MESSAGE + ".";

        } else {
            $scope.passwordSetUpErrorResponse = "";
            $scope.passwordSetUpSubmitButtonDisabled = true;
            usSpinnerService.spin('spinner-login');

            var urlParams = {
                input1 : $scope.input1,
                input2 : $scope.formData.password,
                input3 : $scope.formData.passwordConfirm,
                input4 : $scope.formData.smsVerificationCode
            };

            tcComService.callApiWithJSONParam("login/password/setup", urlParams, function(response) {

                usSpinnerService.stop('spinner-login');
                $scope.passwdSetUpSuccess = true;
                $scope.passwordSetUpErrorResponse = "";

            }, function(error) {

                $scope.passwordSetUpSubmitButtonDisabled = false;
                usSpinnerService.stop('spinner-login');
                $scope.passwordSetUpErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });
        }
    };

} ])

.controller('credentialsExpiredController',
    [ '$scope', '$rootScope', 'vcRecaptchaService', 'tcComService', 'usSpinnerService', function($scope, $rootScope, vcRecaptchaService, tcComService, usSpinnerService) {

        $scope.init = function(isTestEnvironment) {
            $rootScope.isTestEnvironment = isTestEnvironment;
        }

        $scope.passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;
        $scope.passwdUpdateSuccess = false;
        $scope.credentialsExpiredUpdateErrorResponse = "";
        $scope.formData = {};

        $scope.setResponse = function(response) {
            $scope.response = response;
        };

        $scope.setWidgetId = function(widgetId) {
            $scope.widgetId = widgetId;
        };

        $scope.cbExpiration = function() {
            vcRecaptchaService.reload($scope.widgetId);
        };

        $scope.errorHandler = function(error) {

            $scope.showSpinner = false;
            vcRecaptchaService.reload($scope.widgetId);
            console.log(error);
        };

        $scope.passwordUpdate = function() {

            if ($scope.dataForm.passwordOld.$invalid) {
                $scope.credentialsExpiredUpdateErrorResponse = $rootScope.translation.KEY_SCREEN_OLD_PASSWORD_INVALID_MESSAGE + ".";

            } else if ($scope.dataForm.password.$invalid) {
                $scope.credentialsExpiredUpdateErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_PASSWORD_MESSAGE + ".";

            } else if ($scope.dataForm.passwordConfirm.$invalid) {
                $scope.credentialsExpiredUpdateErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_PASSWORD_MESSAGE + ".";

            } else if ($scope.formData.password !== $scope.formData.passwordConfirm) {
                $scope.credentialsExpiredUpdateErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_PASSWORD_NOT_MATCH_MESSAGE + ".";

            } else if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                $scope.credentialsExpiredUpdateErrorResponse = $rootScope.translation.KEY_SCREEN_RESOLVE_CAPTCHA_MESSAGE + ".";

            } else {

                usSpinnerService.spin('spinner-login');

                var urlParams = {
                    input1 : $scope.userName,
                    input2 : $scope.formData.passwordOld,
                    input3 : $scope.formData.password,
                    input4 : $scope.formData.passwordConfirm,
                    input5 : $scope.t1,
                    input6 : $scope.t2,
                    accountType : $scope.accountType,
                    recaptchaResponse : vcRecaptchaService.getResponse($scope.widgetId)
                };

                tcComService.callApiWithJSONParam("login/password/expired/update", urlParams, function(response) {

                    usSpinnerService.stop('spinner-login');
                    $scope.passwdUpdateSuccess = true;
                    $scope.credentialsExpiredUpdateErrorResponse = "";

                }, function(error) {

                    usSpinnerService.stop('spinner-login');
                    vcRecaptchaService.reload($scope.widgetId);
                    $scope.credentialsExpiredUpdateErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

    } ]);

loginApp.service('translationService', [ '$rootScope', '$resource', function($rootScope, $resource) {

    this.getLanguages = function() {

        var api = $resource("translate/get");

        api.save().$promise.then(function(response) {

            $rootScope.languageList = response.dataMap;

            if ($rootScope.isTestEnvironment == 'true') {
                $rootScope.changeLanguage("EN");

            } else {
                $rootScope.changeLanguage("FR");
            }

        }, function(error) {

            $rootScope.languageList = [];
        });
    }

} ]);

loginApp.directive('compareTo', function() {
    return {
        require : "ngModel",
        scope : {
            otherModelValue : "=compareTo"
        },
        link : function(scope, element, attributes, ngModel) {

            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };

            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
});
