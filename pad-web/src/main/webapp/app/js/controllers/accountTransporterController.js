padApp.controller('AccountTransporterController', [ '$scope', '$rootScope', '$sce', 'accountService', function($scope, $rootScope, $sce, accountService) {

    $scope.formData = {
        emailListInvoiceStatement: []
    };
    $scope.emailList = [];

    $rootScope.activeNavbar('#navbarAccount');

    $scope.errorHandler = function(error) {
        $rootScope.stopSpinner();
        console.log(error);
    };

    accountService.getAccount(function(response) {

        $rootScope.stopSpinner();
        
        $scope.formData = response.dataList[0];
        $scope.newAmountLowAccountBalance = $scope.formData.amountLowAccountBalanceWarn;
        
        if (!Array.isArray(response.dataList[0].emailListInvoiceStatement)) {
            $scope.formData.emailListInvoiceStatement = [];
        }
        
        $scope.emailList = Array.isArray(response.dataList[0].emailListInvoiceStatement) ? Array.from(response.dataList[0].emailListInvoiceStatement) : [];
        $scope.address = $scope.previewCompanyAddress($scope.formData);

    }, $scope.errorHandler);

    $scope.addEmail = function () {
        const emailListValidator = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;

        for (var i = 0; i < $scope.formData.emailListInvoiceStatement.length; i++) {
            if ($scope.formData.emailListInvoiceStatement[i] === $scope.newEmail) {
                  $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ACCOUNT_UPDATE_REPEATED_EMAILS + ".");
                  return;
            }
        }
        
        if (!emailListValidator.test($scope.newEmail)) {
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ENTER_VALID_EMAIL_MESSAGE + ".");

        } else{
            $scope.formData.emailListInvoiceStatement.unshift($scope.newEmail);
            $scope.newEmail = "";
        }
    }

    $scope.updateEmailListInvoiceStatement = function() {

        if ($scope.formData.emailListInvoiceStatement.length === 0) {
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ACCOUNT_UPDATE_FULLY_CLEARED_EMAILS_LIST + ".");

        } else if ($scope.emailList.toString() === $scope.formData.emailListInvoiceStatement.toString()){
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ACCOUNT_UPDATE_UNCHANGED_DATA + ".");
        } else {

            $rootScope.startSpinner();

            var requestData = {
                code : $scope.formData.code,
                emailListInvoiceStatement : $scope.formData.emailListInvoiceStatement
            };

            accountService.updateEmailListInvoiceStatement(requestData, function(response) {

                $scope.emailList = Array.from($scope.formData.emailListInvoiceStatement);
                $scope.newEmail = "";
                $scope.editEmailListInvoiceStatement = false;

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
    };

    $scope.closeEditEmailListInvoiceStatement = function (){
        $scope.newEmail = "";
        $scope.formData.emailListInvoiceStatement = Array.from($scope.emailList);
        $scope.editEmailListInvoiceStatement = false;
    }

    $scope.removeEmail = function (index) {
        if ($scope.formData.emailListInvoiceStatement.length === 1) {
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ACCOUNT_UPDATE_FULLY_CLEARED_EMAILS_LIST + ".");
        } else {
            $scope.formData.emailListInvoiceStatement.splice(index, 1);
        }
    }
    
    $scope.updateCompanyTelephone = function() {
        
        if ($scope.submitDataForm.companyTelephone.$invalid) {
            
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ENTER_TELEPHONE_NUMBER_MESSAGE + ".");

        } else{
            
            $rootScope.startSpinner();
            
            var requestData = {
                code : $scope.formData.code,
                companyTelephone : $scope.companyTelephone
            };

            accountService.updateCompanyTelephone(requestData, function(response) {

                $scope.formData.companyTelephone = $scope.companyTelephone;
                $scope.companyTelephone = "";
                $scope.editCompanyTelephone = false;
                
                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
        
    };
    
    $scope.submitForm = function() {
        
        if ($scope.addressForm.address1.$invalid) {
            $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_ADDRESS_MESSAGE + " 1 .";

        } else if ($scope.addressForm.address2.$invalid) {
            $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_ADDRESS_MESSAGE + " 2 .";

        } else if ($scope.addressForm.postcode.$invalid) {
            $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_ENTER_POSTCODE_MESSAGE + ".";

        } else if ($scope.addressForm.country.$invalid) {
            $scope.step4ErrorMessage = $rootScope.translation.KEY_SCREEN_SELECT_COUNTRY_MESSAGE + ".";

        } else{
            
            $rootScope.startSpinner();
            
            var requestData = {
                code : $scope.formData.code,
                address1 : $scope.addressData.address1,
                address2 : $scope.addressData.address2,
                address3 : $scope.addressData.address3,
                address4 : $scope.addressData.address4,
                postCode : $scope.addressData.postcode,
                countryCode : $scope.addressData.country
            };

            accountService.updateAddress(requestData, function(response) {

                $scope.formData.address1 = $scope.addressData.address1;
                $scope.formData.address2 = $scope.addressData.address2;
                $scope.formData.address3 = $scope.addressData.address3;
                $scope.formData.address4 = $scope.addressData.address4;
                $scope.formData.postCode = $scope.addressData.postcode;
                $scope.formData.registrationCountryISO = $scope.addressData.country;
                
                $scope.address = $scope.previewCompanyAddress($scope.formData);
                
                $scope.closeAddressDialog();
                
                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
        
    };
    
    $scope.showAddressModal = function() {
        
        $scope.addressData = {};
        $scope.addressData.address1 = $scope.formData.address1;
        $scope.addressData.address2 = $scope.formData.address2;
        $scope.addressData.address3 = $scope.formData.address3;
        $scope.addressData.address4 = $scope.formData.address4;
        $scope.addressData.postcode = $scope.formData.postCode;
        $scope.addressData.country = $scope.formData.registrationCountryISO;
            
        $('#addressModal').modal('show');
    }

    $scope.closeAddressDialog = function() {

        $scope.addressData = {};

        $('#addressModal').modal('hide');
    }
    
    $scope.updateMsisdn = function() {
        
        if ($scope.submitDataForm.msisdn.$invalid) {
            
            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ENTER_VALID_MOBILE_NUMBER_MESSAGE + ".");

        } else{
            
            $rootScope.startSpinner();
            
            var requestData = {
                code : $scope.formData.code,
                msisdn : $scope.msisdn
            };

            accountService.updateMsisdn(requestData, function(response) {

                $scope.formData.msisdn = $scope.msisdn;
                $scope.msisdn = "";
                $scope.editMsisdn = false;

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
                $rootScope.stopSpinner();

            }, function(error) {

                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
    };

    $scope.updateIsDeductCreditRegisteredTrucks = function() {
        $scope.isUpdatingDeductCreditRegisteredTrucks = true;
        $rootScope.startSpinner();
        var requestData = {
            code : $scope.formData.code,
            isDeductCreditRegisteredTrucks : $scope.formData.isDeductCreditRegisteredTrucks
        };

        accountService.updateIsDeductCreditRegisteredTrucks(requestData, function(response) {

            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
            $rootScope.stopSpinner();
            $scope.isUpdatingDeductCreditRegisteredTrucks = false;
        }, function(error) {

            $scope.errorHandler(error);
            $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            $scope.isUpdatingDeductCreditRegisteredTrucks = false;
        });
    };

    $scope.updateIsTripApprovedEmail = function() {
        $scope.isUpdating = true;
        $rootScope.startSpinner();
        var requestData = {
            code : $scope.formData.code,
            isTripApprovedEmail : $scope.formData.isTripApprovedEmail
        };

        accountService.updateIsTripApprovedEmail(requestData, function(response) {

            $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
            $rootScope.stopSpinner();
            $scope.isUpdating = false;
        }, function(error) {

            $scope.errorHandler(error);
            $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            $scope.isUpdating = false;
        });
    };

    $scope.sendWarnMessage = function(isWarnMessageChange) {

        $scope.newAmountLowAccountBalance = isWarnMessageChange ? $scope.formData.amountLowAccountBalanceWarn : $scope.newAmountLowAccountBalance;

        if (!isWarnMessageChange && $scope.submitDataForm.newAmountLowAccountBalance.$invalid) {

            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ENTER_VALID_AMOUNT_MESSAGE + ".");

        } else if (!isWarnMessageChange && $scope.newAmountLowAccountBalance === $scope.formData.amountLowAccountBalanceWarn) {

            $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_ACCOUNT_UPDATE_UNCHANGED_DATA + ".");

        } else {

            $scope.isUpdatingWarnMessage = true;
            $rootScope.startSpinner();


            var requestData = {
                code : $scope.formData.code,
                isSendLowAccountBalanceWarn : $scope.formData.isSendLowAccountBalanceWarn,
                amountLowAccountBalanceWarn : $scope.newAmountLowAccountBalance
            };

            accountService.updateLowAccountBalanceWarn(requestData, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY_MESSAGE + ".");
                $rootScope.stopSpinner();
                $scope.formData.amountLowAccountBalanceWarn = $scope.newAmountLowAccountBalance;
                $scope.editAmountLowAccountBalanceWarn = false;
                $scope.isUpdatingWarnMessage = false;

            }, function(error) {

                $scope.errorHandler(error);
                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
                $scope.newAmountLowAccountBalance = $scope.formData.amountLowAccountBalanceWarn;
                $scope.isUpdatingWarnMessage = false;
            });

        }
    }

    $scope.closeAmountLowAccountBalance = function () {
        $scope.editAmountLowAccountBalanceWarn = false;
        $scope.newAmountLowAccountBalance = $scope.formData.amountLowAccountBalanceWarn;
    }

} ]);
