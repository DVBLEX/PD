padApp.service('vehicleService', [ '$resource', 'tcComService', function($resource, tcComService) {

    this.listVehicles = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("vehicle/list", urlParams, callBackSuccess, callBackError);
    };

    this.countVehicles = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("vehicle/count", urlParams, callBackSuccess, callBackError);
    };

    this.saveVehicle = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("vehicle/save", urlParams, callBackSuccess, callBackError);
    };
    
    this.updateVehicle = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("vehicle/update", urlParams, callBackSuccess, callBackError);
    };
    
    this.activateVehicle = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("vehicle/activate", urlParams, callBackSuccess, callBackError);
    };

} ]);