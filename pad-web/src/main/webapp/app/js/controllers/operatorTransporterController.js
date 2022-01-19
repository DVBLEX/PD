padApp.controller('OperatorTransporterController', [ '$scope', '$rootScope', '$timeout', 'operatorService', 'operatorFactory',
    function($scope, $rootScope, $timeout, operatorService, operatorFactory) {

        $rootScope.activeNavbar('#navbarOperator');

        $scope.disableSubmitButton = false;
        $scope.sortColumn = "";
        $scope.sortAsc = true;

        $scope.padTable = new PADTable(function() {

            $rootScope.startSpinner();
            operatorService.getOperatorCount({}, $scope.getCountCallBack, $scope.getCountCallBackError);

        }, function(currentPage, pageCount) {

            var params = {
            	sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage : currentPage,
                pageCount : pageCount
            };
            $rootScope.startSpinner();
            operatorService.getOperators(params, $scope.getCallBack, $scope.getCallBackError);

        });

        $scope.getCallBack = function(response) {

            $scope.padTable.setData(response.dataList);
            $rootScope.stopSpinner();
        }

        $scope.getCallBackError = function(response) {

            $rootScope.stopSpinner();
            console.log(response);
        }

        $scope.getCountCallBack = function(response) {

            $scope.padTable.setCount(response.dataList[0]);
            $rootScope.stopSpinner();
        }

        $scope.getCountCallBackError = function(response) {

            $rootScope.stopSpinner();
            console.log(response);
        }

        $scope.refreshTableData = function() {

            $scope.padTable.reloadTable();
        }
        
        $scope.sort = function(sortColumn){
        	
        	if($scope.sortColumn === sortColumn){
        		$scope.sortAsc = !$scope.sortAsc;
        	}else{
        		$scope.sortAsc = true;
        	}
        	
        	$scope.sortColumn = sortColumn;
            
            $scope.refreshTableData();
        }

        $scope.add = function() {

            operatorFactory.setOperator({});
            $rootScope.go('/operatorTransporterEdit');
        }

        $scope.edit = function(operator) {
            
            var activeCount = 0;
            angular.forEach($scope.padTable.data, function (value, key) { 
                if(value.isActive)
                    activeCount++;
            });
            
            operator.allowInactivation = (activeCount > 1 || !operator.isActive) ? true : false;

            operatorFactory.setOperator(operator);
            $rootScope.go('/operatorTransporterEdit');
        }

        $scope.passwordResetSend = function(operator) {
            
            $rootScope.startSpinner();

            $scope.disableSubmitButton = true;

            var params = {
                code : operator.code
            };

            var dest = (operator.email === '' ? operator.msisdn : operator.email);

            operatorService.passwordResetSend(params, function(response) {

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_PASSWORD_RESET_SENT_MESSAGE + " " + dest + ".");
                $rootScope.stopSpinner();
                $scope.disableSubmitButton = false;

            }, function(response) {

                $rootScope.showResultMessage(false, $rootScope.translation.KEY_SCREEN_PASSWORD_RESET_SEND_ERROR_MESSAGE + " " + dest + ".");
                $rootScope.stopSpinner();
                $scope.disableSubmitButton = false;
            });
        }

        $scope.refreshTableData();

} ]);