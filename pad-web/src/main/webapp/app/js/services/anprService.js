padApp.service('anprService', [ 'tcComService', function(tcComService) {

    this.getAnprParameter = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("anpr/parameter/get", params, callBackSuccess, callBackError);
    }

    this.updateAnprParameter = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("anpr/parameter/update", params, callBackSuccess, callBackError);
    }

} ]);