padApp.service('operatorService', [ 'tcComService', function(tcComService) {

    this.getOperators = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/list", params, callBackSuccess, callBackError);
    }

    this.getOperatorCount = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/count", params, callBackSuccess, callBackError);
    }

    this.addOperator = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/add", params, callBackSuccess, callBackError);
    }

    this.unlockOperator = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/unlock", params, callBackSuccess, callBackError);
    }

    this.updateOperator = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/update", params, callBackSuccess, callBackError);
    }

    this.passwordResetSend = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/password/reset/send", params, callBackSuccess, callBackError);
    }

    this.getKioskOperatorNames = function(params, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("operator/getKioskOperatorNames", params, callBackSuccess, callBackError);
    }

} ]);
