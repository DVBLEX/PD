padApp.service('laneService', [ 'tcComService', function(tcComService) {

    this.list = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("lane/list", urlParams, callBackSuccess, callBackError);
    };

    this.count = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("lane/count", urlParams, callBackSuccess, callBackError);
    };
    
    this.save = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("lane/save", urlParams, callBackSuccess, callBackError);
    };
    
    this.update = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("lane/update", urlParams, callBackSuccess, callBackError);
    };
    
    this.activate = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("lane/activate", urlParams, callBackSuccess, callBackError);
    };
    
    this.getParkingEntryLanes = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("lane/parking/entry/list", urlParams, callBackSuccess, callBackError);
    };

} ]);