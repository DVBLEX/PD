padApp.service('driverService', [ 'tcComService', function(tcComService) {

    this.listDrivers = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("driver/list", urlParams, callBackSuccess, callBackError);
    };

    this.countDrivers = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("driver/count", urlParams, callBackSuccess, callBackError);
    };

    this.saveDriver = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("driver/save", urlParams, callBackSuccess, callBackError);
    };

    this.removeDriverAssociation = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("driver/remove/association", urlParams, callBackSuccess, callBackError);
    };
} ]);