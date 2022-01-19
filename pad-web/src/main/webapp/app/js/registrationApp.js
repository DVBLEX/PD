var registrationApp = angular.module('registrationApp', [ 'ngAnimate', 'vcRecaptcha', 'tcCom', 'ngIntlTelInput' ])

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

} ]);

registrationApp.controller('registrationController', [ '$scope', '$rootScope', '$window', 'vcRecaptchaService', 'registrationService',
    function($scope, $rootScope, $window, vcRecaptchaService, registrationService) {

        $scope.init = function(isTestEnvironment) {
            $rootScope.isTestEnvironment = isTestEnvironment;
        }

        $scope.formData = {};
        $scope.registrationSuccess = false;
        $scope.step1IsCompleted = false;
        $scope.step2IsCompleted = false;
        $scope.step3IsCompleted = false;
        $scope.step4IsCompleted = false;
        $scope.step5IsCompleted = false;
        $scope.formStepsSelected = [ true, false, false, false, false ];
        $scope.verifyCodeHasSent = false;
        $scope.step1ErrorMessage = "";
        $scope.emailVerificationStep1ErrorResponse = "";
        $scope.emailVerificationStep2ErrorResponse = "";
        $scope.msisdnVerificationStep1ErrorResponse = "";
        $scope.msisdnVerificationStep2ErrorResponse = "";
        $scope.passwordSubmittedSuccess = false;
        $scope.passwordSubmitErrorResponse = "";
        $scope.isEmailVerified = false;
        $scope.isMsisdnVerified = false;
        $scope.step4ErrorMessage = "";
        $scope.registrationErrorResponse = "";
        $scope.regSubmitButtonDisabled = false;
        $scope.emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;
        $scope.passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;

        $scope.selectStep = function(stepIndex) {
            if ($scope.formStepsSelected[stepIndex] == false) {

                $scope.step1IsCompleted = stepIndex > 0;
                $scope.step2IsCompleted = stepIndex > 1;
                $scope.step3IsCompleted = stepIndex > 2;
                $scope.step4IsCompleted = stepIndex > 3;
                $scope.step5IsCompleted = stepIndex > 4;

                for (var i = 0; i < $scope.formStepsSelected.length; i++) {
                    $scope.formStepsSelected[i] = false;
                }
                $scope.formStepsSelected[stepIndex] = true;
            }
        };

        $scope.step1Submit = function() {

            if (!$scope.formData.agreeTerms) {
                $scope.step1ErrorMessage = $rootScope.translation.KEY_SCREEN_AGREE_TERMS_ERROR_MESSAGE + ".";

            } else if ($scope.accountTypeForm.accountType.$invalid) {
                $scope.step1ErrorMessage = $rootScope.translation.KEY_SCREEN_SELECT_ACCOUNT_TYPE_MESSAGE + ".";

            } else {
                $scope.step1ErrorMessage = "";
                $scope.step1IsCompleted = true;
                $scope.selectStep(1);
            }
        };

        $scope.step2Submit = function() {

            if ($scope.formData.accountType == 1) {

                if ($scope.verificationCodeForm.firstName.$invalid) {
                    $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

                } else if ($scope.verificationCodeForm.lastName.$invalid) {
                    $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

                } else if (!$scope.isEmailVerified) {
                    $scope.emailVerificationStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_EMAIL_MESSAGE + ".";

                } else {
                    $scope.emailVerificationStep1ErrorResponse = "";
                    $scope.emailVerificationStep2ErrorResponse = "";
                    $scope.selectStep(2);
                }

            } else if ($scope.formData.accountType == 2) {

                if ($scope.verificationCodeForm.firstName.$invalid) {
                    $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

                } else if ($scope.verificationCodeForm.lastName.$invalid) {
                    $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

                } else if (!$scope.isMsisdnVerified) {
                    $scope.msisdnVerificationStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";

                } else {
                    $scope.msisdnVerificationStep1ErrorResponse = "";
                    $scope.msisdnVerificationStep2ErrorResponse = "";
                    $scope.selectStep(2);
                }
            }
        };

        $scope.step3Submit = function() {

            if ($scope.passwordVerificationForm.password.$invalid) {
                $scope.passwordSubmitErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_PASSWORD_MESSAGE + ".";

            } else if ($scope.passwordVerificationForm.passwordConfirm.$invalid) {
                $scope.passwordSubmitErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_PASSWORD_MESSAGE + ".";

            } else if ($scope.formData.password !== $scope.formData.passwordConfirm) {
                $scope.passwordSubmitErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_PASSWORD_NOT_MATCH_MESSAGE + ".";

            } else {
                $scope.passwordSubmitErrorResponse = "";
                $scope.passwordSubmittedSuccess = true;
                $scope.step3IsCompleted = true;
                $scope.selectStep(3);
            }

        };

        $scope.step4Submit = function() {

            if ($scope.formData.accountType == 1) {

                if ($scope.accountDetailsForm.companyName.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_COMPANY_NAME_MESSAGE + ".";

                } else if ($scope.accountDetailsForm.address1.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_REGISTERED_ADDRESS_MESSAGE + " 1 .";

                } else if ($scope.accountDetailsForm.address2.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_REGISTERED_ADDRESS_MESSAGE + " 2 .";

                } else if ($scope.accountDetailsForm.postcode.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_POSTCODE_MESSAGE + ".";

                } else if ($scope.accountDetailsForm.country.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_MESSAGE + ".";

                } else if ($scope.accountDetailsForm.telephoneNumber.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_TELEPHONE_NUMBER_MESSAGE + ".";

                } else {
                    $scope.step4IsCompleted = true;
                    $scope.step4ErrorMessage = "";
                    $scope.selectStep(4);
                }

            } else if ($scope.formData.accountType == 2) {

                if ($scope.accountDetailsForm.address1.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_ADDRESS_MESSAGE + " 1 .";

                } else if ($scope.accountDetailsForm.address2.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_ADDRESS_MESSAGE + " 2 .";

                } else if ($scope.accountDetailsForm.postcode.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_POSTCODE_MESSAGE + ".";

                } else if ($scope.accountDetailsForm.country.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_MESSAGE + ".";

                } else if ($scope.accountDetailsForm.nationality.$invalid) {
                    $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_MESSAGE + "( " + $rootScope.translation.KEY_SCREEN_NATIONALITY_LABEL + ").";

                } else {
                    $scope.step4IsCompleted = true;
                    $scope.step4ErrorMessage = "";
                    $scope.selectStep(4);
                }
            }
        };

        $scope.$watch('formData.accountType', function() {

            if ($scope.formData.accountType !== undefined && $scope.formData.accountType !== "" && $scope.formData.accountType !== null) {

                vcRecaptchaService.reload($scope.widgetId);

                $scope.verifyCodeHasSent = false;
                $scope.passwordSubmittedSuccess = false;
                $scope.isEmailVerified = false;
                $scope.isMsisdnVerified = false;
            }
        });

        $scope.setResponse = function(response) {
            $scope.response = response;
        };
        $scope.setWidgetId = function(widgetId) {
            $scope.widgetId = widgetId;
        };
        $scope.cbExpiration = function() {
            vcRecaptchaService.reload($scope.widgetId);
        };

        $scope.sendEmailVerificationCode = function() {

            $scope.verifyCodeHasSent = false;

            if ($scope.verificationCodeForm.firstName.$invalid) {
                $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

            } else if ($scope.verificationCodeForm.lastName.$invalid) {
                $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

            } else if ($scope.verificationCodeForm.email.$invalid) {
                $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_EMAIL_MESSAGE + ".";

            } else if ($scope.verificationCodeForm.emailConfirm.$invalid) {
                $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_EMAIL_MESSAGE + ".";

            } else if ($scope.formData.email !== $scope.formData.emailConfirm) {
                $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_EMAIL_DOES_NOT_MATCH_MESSAGE + ".";

            } else if (vcRecaptchaService.getResponse() === "" || vcRecaptchaService.getResponse() == undefined || vcRecaptchaService.getResponse() == null) {
                $scope.emailVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_RESOLVE_CAPTCHA_MESSAGE + ".";

            } else {

                $scope.showSpinner = true;

                var urlParams = {
                    email : $scope.formData.email,
                    firstName : $scope.formData.firstName,
                    lastName : $scope.formData.lastName,
                    language : $rootScope.language,
                    recaptchaResponse : vcRecaptchaService.getResponse()
                };

                registrationService.sendEmailVerificationCode(urlParams, function(response) {

                    $scope.verifyCodeHasSent = true;
                    $scope.showSpinner = false;
                    $scope.emailVerificationStep1ErrorResponse = "";

                }, function(error) {

                    $scope.showSpinner = false;
                    vcRecaptchaService.reload($scope.widgetId);
                    $scope.emailVerificationStep1ErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

        $scope.reSendVerificationCode = function() {

            vcRecaptchaService.reload($scope.widgetId);
            $scope.verifyCodeHasSent = false;
            $scope.formData.emailVerificationCode = "";
            $scope.formData.smsVerificationCode = "";
            $scope.emailCodeVerificationForm.$setPristine();
            $scope.smsCodeVerificationForm.$setPristine();
            $scope.emailVerificationStep1ErrorResponse = "";
            $scope.emailVerificationStep2ErrorResponse = "";
            $scope.msisdnVerificationStep1ErrorResponse = "";
            $scope.msisdnVerificationStep2ErrorResponse = "";
        };

        $scope.verifyEmailCode = function() {

            if ($scope.emailCodeVerificationForm.$invalid) {

                $scope.emailVerificationStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_VALID_VERIFICATION_CODE_MESSAGE + ".";

            } else {

                $scope.showSpinner = true;

                var urlParams = {
                    email : $scope.formData.email,
                    code : $scope.formData.emailVerificationCode
                };

                registrationService.verifyEmailCode(urlParams, function(response) {

                    $scope.isEmailVerified = true;
                    $scope.step2IsCompleted = true;
                    $scope.t = response.data;
                    $scope.showSpinner = false;
                    $scope.emailVerificationStep2ErrorResponse = "";

                }, function(error) {

                    $scope.showSpinner = false;
                    $scope.emailVerificationStep2ErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

        $scope.sendMsisdnVerificationCode = function() {

            $scope.verifyCodeHasSent = false;

            if ($scope.verificationCodeForm.firstName.$invalid) {
                $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_FIRST_NAME_MESSAGE + ".";

            } else if ($scope.verificationCodeForm.lastName.$invalid) {
                $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_LAST_NAME_MESSAGE + ".";

            } else if ($scope.verificationCodeForm.mobileNumber.$invalid) {
                $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";

            } else if ($scope.verificationCodeForm.mobileNumberConfirm.$invalid) {
                $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_MOBILE_NUMBER_MESSAGE + ".";

            } else if ($scope.formData.mobileNumber !== $scope.formData.mobileNumberConfirm) {
                $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_MOBILE_NUMBER_NOT_MATCH_MESSAGE + ".";

            } else if (vcRecaptchaService.getResponse() === "" || vcRecaptchaService.getResponse() == undefined || vcRecaptchaService.getResponse() == null) {
                $scope.msisdnVerificationStep1ErrorResponse = $rootScope.translation.KEY_SCREEN_RESOLVE_CAPTCHA_MESSAGE + ".";

            } else {

                $scope.showSpinner = true;

                var urlParams = {
                    mobileNumber : $scope.formData.mobileNumber,
                    firstName : $scope.formData.firstName,
                    lastName : $scope.formData.lastName,
                    language : $rootScope.language,
                    recaptchaResponse : vcRecaptchaService.getResponse()
                };

                registrationService.sendMsisdnVerificationCode(urlParams, function(response) {

                    $scope.verifyCodeHasSent = true;
                    $scope.showSpinner = false;
                    $scope.msisdnVerificationStep1ErrorResponse = "";

                }, function(error) {

                    $scope.showSpinner = false;
                    vcRecaptchaService.reload($scope.widgetId);
                    $scope.msisdnVerificationStep1ErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

        $scope.verifySMSCode = function() {

            if ($scope.smsCodeVerificationForm.$invalid) {

                $scope.msisdnVerificationStep2ErrorResponse = $rootScope.translation.KEY_SCREEN_VALID_VERIFICATION_CODE_MESSAGE + ".";

            } else {

                $scope.showSpinner = true;

                var urlParams = {
                    mobileNumber : $scope.formData.mobileNumber,
                    code : $scope.formData.smsVerificationCode
                };

                registrationService.verifySMSCode(urlParams, function(response) {

                    $scope.isMsisdnVerified = true;
                    $scope.step2IsCompleted = true;
                    $scope.t = response.data;
                    $scope.showSpinner = false;
                    $scope.msisdnVerificationStep2ErrorResponse = "";

                }, function(error) {

                    $scope.showSpinner = false;
                    $scope.msisdnVerificationStep2ErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                });
            }
        };

        $rootScope.getCountryNameByISOCode = function(isoCode) {
            if (isoCode === "SN") {
                return ($rootScope.language === 'EN') ? 'Senegal':'Sénégal';
            } else if (isoCode === "ML") {
                return 'Mali';
            }
        };

        $rootScope.getAccountTypeText = function(accountType) {
            if (accountType === "1" || accountType === 1) {
                return $rootScope.translation.KEY_SCREEN_COMPANY_LABEL;
            } else if (accountType === "2" || accountType === 2) {
                return $rootScope.translation.KEY_SCREEN_INDIVIDUAL_LABEL;
            }
        };

        $scope.previewCompanyAddress = function() {

            var companyAddrPreview = $scope.formData.address1;
            companyAddrPreview += "<br>" + $scope.formData.address2;

            if ($scope.formData.address3 != undefined && $scope.formData.address3 != "") {
                companyAddrPreview += "<br>" + $scope.formData.address3;
            }
            if ($scope.formData.address4 != undefined && $scope.formData.address4 != "") {
                companyAddrPreview += "<br>" + $scope.formData.address4;
            }

            companyAddrPreview += "<br>" + $scope.formData.postcode;

            if ($scope.formData.country == "SN") {

                if ($rootScope.language === 'FR') {
                    companyAddrPreview += "<br>Sénégal";
                } else {
                    companyAddrPreview += "<br>Senegal";
                }

            } else if ($scope.formData.country == "ML") {
                companyAddrPreview += "<br>Mali";
            }

            return companyAddrPreview;
        };

        $scope.processRegistration = function() {

            $scope.regSubmitButtonDisabled = true;
            $scope.showSpinner = true;

            var urlParams = {
                firstName : $scope.formData.firstName,
                lastName : $scope.formData.lastName,
                password : $scope.formData.password,
                accountType : $scope.formData.accountType,
                address1 : $scope.formData.address1,
                address2 : $scope.formData.address2,
                address3 : $scope.formData.address3,
                address4 : $scope.formData.address4,
                postCode : $scope.formData.postcode,
                countryCode : $scope.formData.country,

                // account type company
                email : $scope.formData.email,
                companyName : $scope.formData.companyName,
                companyRegistration : $scope.formData.companyRegistrationNumber,
                companyTelephone : $scope.formData.telephoneNumber,
                specialTaxStatus : $scope.formData.specialTaxStatus,

                // account type individual
                mobileNumber : $scope.formData.mobileNumber,
                nationalityCountryISO : $scope.formData.nationality,
                language : $rootScope.language,
                token : $scope.t
            };

            registrationService.processRegistration(urlParams, function(response) {

                $window.onbeforeunload = null;
                $scope.step4IsCompleted = true;
                $scope.showSpinner = false;
                $scope.registrationSuccess = true;
                $scope.registrationErrorResponse = "";

            }, function(error) {

                $scope.regSubmitButtonDisabled = false;
                $scope.showSpinner = false;
                $scope.registrationErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
            });

        };

        $(function() {
            $('[data-toggle="tooltip"]').tooltip()
        });

    } ]);

registrationApp.service('registrationService', [ 'tcComService', function(tcComService) {

    this.sendEmailVerificationCode = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("registration/sendemailcode", urlParams, callBackSuccess, callBackError);
    };

    this.sendMsisdnVerificationCode = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("registration/sendsmscode", urlParams, callBackSuccess, callBackError);
    };

    this.verifyEmailCode = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("registration/verifyemailcode", urlParams, callBackSuccess, callBackError);
    };

    this.verifySMSCode = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("registration/verifysmscode", urlParams, callBackSuccess, callBackError);
    };

    this.processRegistration = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("registration/processregistration", urlParams, callBackSuccess, callBackError);
    };

} ]);

registrationApp.service('translationService', [ '$rootScope', '$resource', function($rootScope, $resource) {

    this.getLanguages = function() {

        var api = $resource("translate/get");

        api.save().$promise.then(function(response) {

            $rootScope.languageList = response.dataMap;

            if($rootScope.isTestEnvironment == 'true') {
                $rootScope.changeLanguage("EN");

            } else {
                $rootScope.changeLanguage("FR");
            }

        }, function(error) {

            $rootScope.languageList = [];
        });
    }

} ]);

registrationApp.filter('split', function() {
    return function(input, splitChar, splitIndex) {
        if (angular.isDefined(input))
            return input.split(splitChar)[splitIndex];
    }
});

registrationApp.filter('to_trusted_html', [ '$sce', function($sce) {
    return function(text) {
        return $sce.trustAsHtml(text);
    };
} ]);

registrationApp.directive('avoidSpecialChars', [ function() {
    function link(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
            var reg = /^[^!@#$%\&*()_+={}|[\]\\:;"<>?,./]*$/;
            if (viewValue.match(reg)) {
                return viewValue;
            }
            var transformedValue = ngModel.$modelValue;
            ngModel.$setViewValue(transformedValue);
            ngModel.$render();
            return transformedValue;
        });
    }

    return {
        restrict : 'A',
        require : 'ngModel',
        link : link
    };
} ]);

registrationApp.directive('avoidSpecialCharsV2', [ function() {
    function link(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
            var reg = /^[^!@#$%\*()_+={}|[\]\\:;"<>?,./]*$/;
            if (viewValue.match(reg)) {
                return viewValue;
            }
            var transformedValue = ngModel.$modelValue;
            ngModel.$setViewValue(transformedValue);
            ngModel.$render();
            return transformedValue;
        });
    }

    return {
        restrict : 'A',
        require : 'ngModel',
        link : link
    };
} ]);

registrationApp.directive('avoidNumbers', [ function() {
    function link(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
            var reg = /^[^0-9]*$/;
            if (viewValue.match(reg)) {
                return viewValue;
            }
            var transformedValue = ngModel.$modelValue;
            ngModel.$setViewValue(transformedValue);
            ngModel.$render();
            return transformedValue;
        });
    }

    return {
        restrict : 'A',
        require : 'ngModel',
        link : link
    };
} ]);

registrationApp.directive('alphanumeric', function() {
    return {
        restrict : 'A',
        require : 'ngModel',
        link : function(scope, element, attr, ctrl) {
            ctrl.$parsers.unshift(function(viewValue) {
                var reg = new RegExp('^[a-zA-Z0-9]*$');
                if (reg.test(viewValue)) {
                    return viewValue;
                } else {
                    var overrideValue = (reg.test(ctrl.$modelValue) ? ctrl.$modelValue : '');
                    element.val(overrideValue);
                    return overrideValue;
                }
            });
        }
    };
});
