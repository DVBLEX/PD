var receiptApp = angular.module('receiptApp', [ 'ngAnimate', 'tcCom', 'ngIntlTelInput', 'angularSpinner'])

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

receiptApp.controller('ReceiptController', [ '$scope', '$rootScope', '$timeout', 'tcComService', 'usSpinnerService', function($scope, $rootScope, $timeout, tcComService, usSpinnerService) {

    $scope.init = function(isTestEnvironment, receiptNumber, accountLanguage, expireLinkDateString) {
        $rootScope.isTestEnvironment = isTestEnvironment;
        $rootScope.accountLanguage = accountLanguage;
        $scope.receiptNumber = receiptNumber;
        $scope.expireLinkDateString = expireLinkDateString;
        $scope.receiptErrorResponse = "";
        $scope.downloaded = false;
        $scope.isDownloadLock = false;

        $scope.isMobileNumberValid = false;
    }
    
    usSpinnerService.stop('spinner-receipt');
    
    $scope.validateMobileNumber = function() {

        if ($scope.dataForm.mobileNumber.$invalid) {
            $scope.receiptErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".";
            return;
        }


        usSpinnerService.spin('spinner-receipt');

        var urlParams = {
            number : $scope.receiptNumber,
            msisdn : $scope.mobileNumber
        };

        tcComService.callApiWithJSONParam("receipt/validate/msisdn", urlParams, function(response) {

            usSpinnerService.stop('spinner-receipt');
            $scope.isMobileNumberValid = true;
            $scope.receiptErrorResponse = "";

        }, function(error) {

            usSpinnerService.stop('spinner-receipt');
            $scope.isMobileNumberValid = false;
            $scope.receiptErrorResponse = $rootScope.$eval('translation.' + 'KEY_RESPONSE_' + error.data.responseCode);
            
            if(error.data.responseCode == 1197){
                $scope.isDownloadLock = true;
            }
            
        });
    };
    
    $scope.downloadReceipt = function() {
        
        var form = angular.element('#receiptDonwloadForm')
        form.submit();

        $scope.downloaded = true;
        
        $timeout(function(){
            $scope.downloaded = false;
        }, 60000);
        
        return true;
    }


} ]);

receiptApp.service('translationService', [ '$rootScope', '$resource', function($rootScope, $resource) {

    this.getLanguages = function() {

        var api = $resource("translate/get");

        api.save().$promise.then(function(response) {

            $rootScope.languageList = response.dataMap;

            if ($rootScope.isTestEnvironment == 'true' || $rootScope.accountLanguage == 'EN') {
                $rootScope.changeLanguage("EN");
            } else {
                $rootScope.changeLanguage("FR");
            }

        }, function(error) {

            $rootScope.languageList = [];
        });
    }

} ]);



