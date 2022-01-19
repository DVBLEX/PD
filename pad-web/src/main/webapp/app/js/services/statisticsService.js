padApp.service('statisticsService', [ 'tcComService', function(tcComService) {

    this.getStatistics = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("statistics/get", urlParams, callBackSuccess, callBackError);
    };

} ]);