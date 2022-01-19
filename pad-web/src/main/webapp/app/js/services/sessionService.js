padApp.service('sessionService', [ 'tcComService', function(tcComService) {

    this.getSessions = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("session/list", params, callBackSuccess, callBackError);
    }

    this.getSessionCount = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("session/count", params, callBackSuccess, callBackError);
    }

    this.addSession = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("session/add", params, callBackSuccess, callBackError);
    }

    this.endSession = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("session/end", params, callBackSuccess, callBackError);
    }

    this.validateSession = function(params, callBackSuccess, callBackError) {
        
        tcComService.callApiWithJSONParam("session/validate", params, callBackSuccess, callBackError);
    }

} ]);