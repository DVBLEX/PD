padApp.service('paymentService', [ 'tcComService', function(tcComService) {

    this.makeTopup = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/topup", urlParams, callBackSuccess, callBackError);
    };

    this.topupAccountBalance = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/topup/account", urlParams, callBackSuccess, callBackError);
    };

    this.checkPaymentStatus = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/status/check", urlParams, callBackSuccess, callBackError);
    };

    this.getInitiatedPaymentReference = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/reference/get", urlParams, callBackSuccess, callBackError);
    };
    
    this.saveOnlinePaymentParamerer = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/save/online/payment/parameter", urlParams, callBackSuccess, callBackError);
    };
    
    this.getCompanyPayments = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/company/get", urlParams, callBackSuccess, callBackError);
    };
    
    this.getOnlinePaymentParamererList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("payment/online/payment/parameter/list", urlParams, callBackSuccess, callBackError);
    };

} ]);