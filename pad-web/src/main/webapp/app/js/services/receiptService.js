padApp.service('receiptService', [ '$resource', 'tcComService', function($resource, tcComService) {

    this.list = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("receipt/list", urlParams, callBackSuccess, callBackError);
    };

    this.count = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("receipt/count", urlParams, callBackSuccess, callBackError);
    };
    
    this.printReceipt = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("receipt/print", urlParams, callBackSuccess, callBackError);
    };

} ]);