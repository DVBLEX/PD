padApp.service('vehicleCounterService', [ 'tcComService', function(tcComService) {

    this.list = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("counter/vehicle/list", urlParams, callBackSuccess, callBackError);
    };

    this.count = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("counter/vehicle/count", urlParams, callBackSuccess, callBackError);
    };
    
    this.getSessionList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("counter/vehicle/session/list", urlParams, callBackSuccess, callBackError);
    };

} ]);