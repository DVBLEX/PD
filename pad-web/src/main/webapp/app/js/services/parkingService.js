padApp.service('parkingService', [ 'tcComService', function(tcComService) {

    this.searchEnteredVehicleReg = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/entered/vehicle/registration/get", urlParams, callBackSuccess, callBackError);
    };

    this.searchExitedVehicleReg = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/exited/vehicle/registration/get", urlParams, callBackSuccess, callBackError);
    };

    this.vehicleExit = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/vehicle/exit", urlParams, callBackSuccess, callBackError);
    };

    this.getEnteredVehicleRegistrationList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/entered/vehicle/registration/list", urlParams, callBackSuccess, callBackError);
    };

    this.getExitedVehicleRegistrationList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/exited/vehicle/registration/list", urlParams, callBackSuccess, callBackError);
    };

    this.saveParkingEntry = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/save", urlParams, callBackSuccess, callBackError);
    };

    this.saveParkingExitOnly = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/exitonly", urlParams, callBackSuccess, callBackError);
    };

    this.listParking = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/list", urlParams, callBackSuccess, callBackError);
    };

    this.countParking = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/count", urlParams, callBackSuccess, callBackError);
    };

    this.countParkingByPortOperator = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/portoperator/count", urlParams, callBackSuccess, callBackError);
    };

    this.sendExitParkingSms = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/send/exit/sms", urlParams, callBackSuccess, callBackError);
    };

    this.toggleAutoRelease = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/toggle/auto/release", urlParams, callBackSuccess, callBackError);
    };

    this.triggerManualRelease = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/trigger/manual/release", urlParams, callBackSuccess, callBackError);
    };

    this.updateParkingSupervisorReadOnlyFlag = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/update/parkingsupervisor/readonly/flag", urlParams, callBackSuccess, callBackError);
    };

    this.updateVehicleState = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("parking/update/vehicle/state", urlParams, callBackSuccess, callBackError);
    };
} ]);
