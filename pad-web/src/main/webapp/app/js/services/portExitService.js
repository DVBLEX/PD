padApp.service('portExitService', [ 'tcComService', function(tcComService) {

    this.vehiclePortExit = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/vehicle/exit", urlParams, callBackSuccess, callBackError);
    };

    this.getEnteredVehicleRegistrationList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/entered/vehicle/registration/list", urlParams, callBackSuccess, callBackError);
    };

    this.searchEnteredVehicleReg = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/entered/vehicle/registration/get", urlParams, callBackSuccess, callBackError);
    };

} ]);