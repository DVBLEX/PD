padApp.service('missionService', [ 'tcComService', function(tcComService) {

    this.listMissions = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("mission/list", urlParams, callBackSuccess, callBackError);
    };

    this.countMissions = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("mission/count", urlParams, callBackSuccess, callBackError);
    };

    this.createMission = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("mission/create", urlParams, callBackSuccess, callBackError);
    };

    this.submitMissionDetails = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("mission/update/details", urlParams, callBackSuccess, callBackError);
    };

    this.getMissionTripList = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("mission/trip/get", urlParams, callBackSuccess, callBackError);
    };
    
    this.cancelMission = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("mission/cancel", urlParams, callBackSuccess, callBackError);
    };

} ]);
