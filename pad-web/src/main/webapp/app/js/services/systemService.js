padApp.service('systemService', [ 'tcComService', function(tcComService) {

    this.getAccountDriverAndVehicleList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/list/accountdriversandvehicles", urlParams, callBackSuccess, callBackError);
    };

    this.saveSystemParameter = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/save/system/parameter", urlParams, callBackSuccess, callBackError);
    };

    this.getPortOperators = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/getPortOperators", urlParams, callBackSuccess, callBackError);
    };

    this.getParkingReleaseStats = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/getParkingReleaseStats", urlParams, callBackSuccess, callBackError);
    };

    this.getBookingSlotLimitMap = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/booking/slot/limit/map", urlParams, callBackSuccess, callBackError);
    };

    this.saveBookingSlotLimit = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/booking/slot/limit/save", urlParams, callBackSuccess, callBackError);
    };

    this.getBookingSlotLimitPeriods = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/booking/slot/limit/periods/get", urlParams, callBackSuccess, callBackError);
    };

    this.updateTransactionTypeFlags = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/update/transaction/type/flags", urlParams, callBackSuccess, callBackError);
    };
    
    this.countIssues = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/report/port/issue/count", urlParams, callBackSuccess, callBackError);
    };
    
    this.listIssues = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/report/port/issue/list", urlParams, callBackSuccess, callBackError);
    };

    this.reportPortIssue = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/report/port/issue", urlParams, callBackSuccess, callBackError);
    };
    
    this.updatePortIssue = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/report/port/issue/update", urlParams, callBackSuccess, callBackError);
    };
    
    this.resolvePortIssue = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/report/port/issue/resolve", urlParams, callBackSuccess, callBackError);
    };

    this.getPortOperatorGates = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("system/getPortOperatorGates", urlParams, callBackSuccess, callBackError);
    };

} ]);
