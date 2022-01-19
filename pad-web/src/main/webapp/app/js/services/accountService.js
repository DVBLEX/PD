padApp.service('accountService', [ 'tcComService', function(tcComService) {

    this.getAccount = function(callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/get", {}, callBackSuccess, callBackError);
    };

    this.activateAccount = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/activate", urlParams, callBackSuccess, callBackError);
    };

    this.countFilteredAccounts = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/count", urlParams, callBackSuccess, callBackError);
    };

    this.listFilteredAccounts = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/list", urlParams, callBackSuccess, callBackError);
    };

    this.getActiveAccountNames = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/getActiveAccountNames", params, callBackSuccess, callBackError);
    };
    
    this.denyAccount = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/deny", urlParams, callBackSuccess, callBackError);
    };
    
    this.updateEmailListInvoiceStatement = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/companyEmail", urlParams, callBackSuccess, callBackError);
    };

    this.updateCompanyTelephone = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/companyTelephone", urlParams, callBackSuccess, callBackError);
    };
    
    this.updateMsisdn = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/msisdn", urlParams, callBackSuccess, callBackError);
    };
    
    this.updateAddress = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/address", urlParams, callBackSuccess, callBackError);
    };

    this.updateIsDeductCreditRegisteredTrucks  = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/isDeductCreditRegisteredTrucks", urlParams, callBackSuccess, callBackError);
    };

    this.updateIsTripApprovedEmail = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/isTripApprovedEmail", urlParams, callBackSuccess, callBackError);
    };

    this.updateLowAccountBalanceWarn = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update/lowAccountBalanceWarn", urlParams, callBackSuccess, callBackError);
    };

    this.countAccountStatements = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/statement/count", urlParams, callBackSuccess, callBackError);
    };

    this.listAccountStatements = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/statement/list", urlParams, callBackSuccess, callBackError);
    };

    this.countAccountPayments = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/payment/count", urlParams, callBackSuccess, callBackError);
    };

    this.listAccountPayments = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/payment/list", urlParams, callBackSuccess, callBackError);
    };

    this.countAccountInvoices = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/invoice/count", urlParams, callBackSuccess, callBackError);
    };

    this.listAccountInvoices = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/invoice/list", urlParams, callBackSuccess, callBackError);
    };

    this.update = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("account/update", urlParams, callBackSuccess, callBackError);
    };
} ]);
