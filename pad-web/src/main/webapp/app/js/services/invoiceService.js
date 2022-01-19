padApp.service('invoiceService', [ '$resource', 'tcComService', function($resource, tcComService) {

    this.listInvoices = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("invoice/list", urlParams, callBackSuccess, callBackError);
    };

    this.countInvoices = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("invoice/count", urlParams, callBackSuccess, callBackError);
    };

} ]);