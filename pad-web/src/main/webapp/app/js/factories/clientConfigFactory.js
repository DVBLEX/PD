padApp.factory('clientConfigFactory', [ function() {

    var clientConfig = {};

    return {
        getClientConfig : function() {
            return clientConfig;
        },

        setClientConfig : function(newClientConfig) {
            clientConfig = newClientConfig;
        }
    }
} ]);