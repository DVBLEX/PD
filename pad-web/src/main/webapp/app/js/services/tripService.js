padApp.service('tripService', [ 'tcComService', function(tcComService) {

    this.getVehicleRegistrationListFromTrips = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/vehicle/registration/list", urlParams, callBackSuccess, callBackError);
    };

    this.searchTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/get", urlParams, callBackSuccess, callBackError);
    };

    this.addTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/add", urlParams, callBackSuccess, callBackError);
    };

    this.cancelTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/cancel", urlParams, callBackSuccess, callBackError);
    };

    this.approveTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/approve", urlParams, callBackSuccess, callBackError);
    };

     this.rejectTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/reject", urlParams, callBackSuccess, callBackError);
    };

    this.saveAdhocTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/adhoc/create", urlParams, callBackSuccess, callBackError);
    };

    this.checkBalanceForTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/balance/check", urlParams, callBackSuccess, callBackError);
    };

    this.calcAmountDueForTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/calc/amount/due", urlParams, callBackSuccess, callBackError);
    };

    this.updateDriverMobileNumber = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/update/driver/mobile", urlParams, callBackSuccess, callBackError);
    };

    this.updateVehicleRegCountryISO = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/update/vehicle/country/iso", urlParams, callBackSuccess, callBackError);
    };

    this.listTrips = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/list", urlParams, callBackSuccess, callBackError);
    };

    this.countTrips = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/count", urlParams, callBackSuccess, callBackError);
    };

    this.updateTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/update", urlParams, callBackSuccess, callBackError);
    };

    this.validateReferenceNumber = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/validate/referencenumber", urlParams, callBackSuccess, callBackError);
    };

    this.getAvailableBookingHours = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/get/available/booking/hours", urlParams, callBackSuccess, callBackError);
    };

    this.cancelAdHocTrip = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/cancel/adhoc", urlParams, callBackSuccess, callBackError);
    };

    this.chargeTripFeeToTransporter = function(urlParams, callBackSuccess, callBackError) {

        tcComService.callApiWithJSONParam("trip/charge/fee", urlParams, callBackSuccess, callBackError);
    };

} ]);
