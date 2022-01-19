padApp.factory('accountDetailsFactory', [ function() {

    var vehicleDetails = [];
    var driverDetails = [];

    return {

        getVehicleDetails : function() {
            return vehicleDetails;
        },

        setVehicleDetails : function(vehicleList) {
            vehicleDetails = vehicleList;
        },

        getDriverDetails : function() {
            return driverDetails;
        },

        setDriverDetails : function(driverList) {
            driverDetails = driverList;
        }
    }

} ]);