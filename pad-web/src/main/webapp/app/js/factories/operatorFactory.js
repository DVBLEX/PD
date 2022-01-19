padApp.factory('operatorFactory', [ function() {

    var operatorObj = {};

    return {

        getOperator : function() {
            return operatorObj;
        },

        setOperator : function(operator) {
            operatorObj = operator;
        }
    }

} ]);