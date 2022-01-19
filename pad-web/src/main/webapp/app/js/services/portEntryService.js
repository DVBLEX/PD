padApp.service('portEntryService', [ 'tcComService', function(tcComService) {

    this.vehiclePortEntry = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callAPI("port/access/vehicle/entry", urlParams, callBackSuccess, callBackError);
    };
    
    this.vehicleWhitelistPortEntry = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callAPI("port/access/vehicle/whitelist/entry", urlParams, callBackSuccess, callBackError);
    };

    this.vehiclePortDeny = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callAPI("port/access/vehicle/deny", urlParams, callBackSuccess, callBackError);
    };
    
    this.listWhitelist = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/whitelist/list", urlParams, callBackSuccess, callBackError);
    };

    this.countWhitelist = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/whitelist/count", urlParams, callBackSuccess, callBackError);
    };
    
    this.createWhitelist = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/whitelist/create", urlParams, callBackSuccess, callBackError);
    };
    
    this.deleteWhitelist = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/whitelist/delete", urlParams, callBackSuccess, callBackError);
    };
    
    this.listEntry = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/entry/list", urlParams, callBackSuccess, callBackError);
    };

    this.countEntry = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/entry/count", urlParams, callBackSuccess, callBackError);
    };
    
    this.countPortEntryByPortOperator = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/entry/portoperator/count", urlParams, callBackSuccess, callBackError);
    };

    this.checkLastEntry = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/vehicle/checkLastEntry", urlParams, callBackSuccess, callBackError);
    };

    this.selectZone = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("port/access/entry/selectZone", urlParams, callBackSuccess, callBackError);
    };

} ]);
