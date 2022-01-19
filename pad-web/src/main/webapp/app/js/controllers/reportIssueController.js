padApp.controller('ReportIssueController', [ '$scope', '$rootScope',  'systemService',
    function($scope, $rootScope, systemService) {

        $rootScope.activeNavbar('#navbarReportIssue');

        $scope.requestData = {};
        $scope.searchSubmitButtonDisabled = false;
        $scope.reportIssueSubmitButtonDisabled = false;
        $scope.reportIssueErrorResponse = "";
        $scope.portOperatorTransactionTypes = ($rootScope.portOperatorId !== undefined && $rootScope.portOperatorId !== null) ? $rootScope.portOperatorTransactionTypesMap[$rootScope.portOperatorId] : [];
        
        $scope.sortColumn = "";
        $scope.sortAsc = true;
        
        $scope.errorHandler = function(error) {
            $rootScope.stopSpinner();
            console.log(error);
        };

        $scope.padTable = new PADTable(function() {

            $rootScope.startSpinner();

            var urlParams = {
                portOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.portOperatorId : null,
                referenceNumber : $scope.filterReferenceNumber
            };

            systemService.countIssues(urlParams, $scope.getCountCallBack, $scope.getCountCallBackError);

        }, function(currentPage, pageCount) {

            var urlParams = {
                portOperatorId : $rootScope.isPortOperator == 'true' ? $rootScope.portOperatorId : null,
                referenceNumber : $scope.filterReferenceNumber,
                sortColumn: $scope.sortColumn,
                sortAsc: $scope.sortAsc,
                currentPage : currentPage,
                pageCount : pageCount
            };
            $rootScope.startSpinner();

            systemService.listIssues(urlParams, $scope.getCallBack, $scope.getCallBackError);
        });

        $scope.getCallBack = function(response) {

            $scope.padTable.setData(response.dataList);
            $scope.searchSubmitButtonDisabled = false;

            $rootScope.stopSpinner();
        }

        $scope.getCallBackError = function(response) {

            $rootScope.stopSpinner();
            console.log(response);
        }

        $scope.getCountCallBack = function(response) {

            $scope.padTable.setCount(response.dataList[0]);
            $scope.searchSubmitButtonDisabled = false;

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
        
        $scope.search = function() {
            $scope.searchSubmitButtonDisabled = true;
            $scope.refreshTableData();
        };

        $scope.clearSearchMission = function() {
            $scope.filterReferenceNumber = null;
            $scope.refreshTableData();
        };

        $scope.showReportModal = function() {
            
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_ISSUE_REPORT_LABEL;
            $scope.modalType = 1;
            
            $('#reportIssueModal').modal('show');
        }

        $scope.closeReportDialog = function() {

           $scope.requestData = {};
           $scope.requestData.msisdnReporter = "";

            $('#reportIssueModal').modal('hide');
        }
        
        $scope.showEditModal = function(row) {
            
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_ISSUE_EDIT_LABEL;
            $scope.modalType = 2;
            
            $scope.requestData = angular.copy(row);
            
            $scope.requestData.currentWorkingCapacityPercentage = row.workingCapacityPercentage + '';
            
            $scope.requestData.issueDate = row.issueDateString.split(" ")[0];
            $scope.requestData.issueTime = row.issueDateString.split(" ")[1].replace(':', '');
            
            if(row.estimatedResolutionDateString){
                $scope.requestData.estimatedResolutionDate = row.estimatedResolutionDateString.split(" ")[0];
                $scope.requestData.estimatedResolutionTime = row.estimatedResolutionDateString.split(" ")[1].replace(':', '');
            }
            
            $('#reportIssueModal').modal('show');
        }
        
        $scope.showResolveModal = function(row) {
            
            $scope.modalTitle = $rootScope.translation.KEY_SCREEN_RESOLVE_ISSUE_LABEL;
            $scope.modalType = 3;
            
            $scope.requestData = angular.copy(row);
            
            $scope.requestData.currentWorkingCapacityPercentage = row.workingCapacityPercentage + '';
            
            $scope.requestData.issueDate = row.issueDateString.split(" ")[0];
            $scope.requestData.issueTime = row.issueDateString.split(" ")[1].replace(':', '');
            
            if(row.estimatedResolutionDateString){
                $scope.requestData.estimatedResolutionDate = row.estimatedResolutionDateString.split(" ")[0];
                $scope.requestData.estimatedResolutionTime = row.estimatedResolutionDateString.split(" ")[1].replace(':', '');
            }
            
            $('#reportIssueModal').modal('show');
        }
        
        $scope.changeOutOfOrder = function() {
            if($scope.requestData.isOutOfOrder){
                $scope.requestData.currentWorkingCapacityPercentage = '0';
            } else{
                if($scope.requestData.currentWorkingCapacityPercentage == '0'){
                    $scope.requestData.currentWorkingCapacityPercentage = '';
                }
            }
        }
        
        $scope.changeWorkingCapacity = function() {
            
            if($scope.requestData.currentWorkingCapacityPercentage == '0'){
                $scope.requestData.isOutOfOrder = true;
            }else{
                $scope.requestData.isOutOfOrder = false;
            }
        }
        
        $scope.reportPortIssue = function() {

        if ($scope.reportIssueForm.transactionType.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_TRANSACTION_TYPE_MESSAGE + ".";

        }  else if ($scope.reportIssueForm.currentWorkingCapacityPercentage.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_CURRENT_WORKING_CAPACITY_MESSAGE + ".";

        } else if ($scope.reportIssueForm.nameReporter.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_REPORTER_NAME_MESSAGE + ".";

        } else if ($scope.reportIssueForm.msisdnReporter.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_REPORTER_MOBILE_MESSAGE + ".";

        } else if ($scope.reportIssueForm.description.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_ISSUE_DESCRIPTION_MESSAGE + ".";

        } else if ($scope.reportIssueForm.issueDate.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_ISSUE_DATE_START_MESSAGE + ".";

        } else if ($scope.reportIssueForm.issueTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_ISSUE_TIME_START_MESSAGE + ".";

        } else if ($scope.reportIssueForm.estimatedResolutionDate.$invalid && !$scope.reportIssueForm.estimatedResolutionTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_EST_RESOLUTION_DATE_MESSAGE + ".";

        } else if (!$scope.reportIssueForm.estimatedResolutionDate.$invalid && $scope.reportIssueForm.estimatedResolutionTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_EST_RESOLUTION_TIME_MESSAGE + ".";

        } else {

            $rootScope.startSpinner();
            $scope.reportIssueSubmitButtonDisabled = true;
            $scope.reportIssueErrorResponse = "";
            
            var estimateTime = "";
            
            if($scope.requestData.estimatedResolutionTime){
                estimateTime = [ $scope.requestData.estimatedResolutionTime.slice(0, 2), ":", $scope.requestData.estimatedResolutionTime.slice(2) ].join('');
            }

            var requestData = {};
            requestData = {
                portOperatorId : $rootScope.portOperatorId,
                transactionType : $scope.requestData.transactionType,
                description : $scope.requestData.description,
                workingCapacityPercentage : $scope.requestData.currentWorkingCapacityPercentage,
                issueDateString : $scope.requestData.issueDate,
                issueTimeString : [ $scope.requestData.issueTime.slice(0, 2), ":", $scope.requestData.issueTime.slice(2) ].join(''),
                estimatedResolutionDateString : $scope.requestData.estimatedResolutionDate,
                estimatedResolutionTimeString : estimateTime,
                nameReporter : $scope.requestData.nameReporter,
                msisdnReporter : $scope.requestData.msisdnReporter
                
            };

            systemService.reportPortIssue(requestData, function(response) {

                $scope.reportIssueSubmitButtonDisabled = false;
                $scope.requestData = {};
                $scope.reportIssueForm.$setPristine();

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ISSUE_REPORTED_MESSAGE + ".");
                $rootScope.stopSpinner();
                
                $scope.closeReportDialog();
                $scope.refreshTableData();

            }, function(error) {

                $scope.reportIssueSubmitButtonDisabled = false;
                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
    };
    
    $scope.editPortIssue = function() {

        if ($scope.reportIssueForm.currentWorkingCapacityPercentage.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_CURRENT_WORKING_CAPACITY_MESSAGE + ".";

        } else if ($scope.reportIssueForm.nameReporter.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_REPORTER_NAME_MESSAGE + ".";

        } else if ($scope.reportIssueForm.msisdnReporter.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_REPORTER_MOBILE_MESSAGE + ".";

        } else if ($scope.reportIssueForm.description.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_ISSUE_DESCRIPTION_MESSAGE + ".";

        } else if ($scope.reportIssueForm.issueDate.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_ISSUE_DATE_START_MESSAGE + ".";

        } else if ($scope.reportIssueForm.issueTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_ISSUE_TIME_START_MESSAGE + ".";

        } else if ($scope.reportIssueForm.estimatedResolutionDate.$invalid && !$scope.reportIssueForm.estimatedResolutionTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_EST_RESOLUTION_DATE_MESSAGE + ".";

        } else if (!$scope.reportIssueForm.estimatedResolutionDate.$invalid && $scope.reportIssueForm.estimatedResolutionTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_SELECT_EST_RESOLUTION_TIME_MESSAGE + ".";

        } else {

            $rootScope.startSpinner();
            $scope.reportIssueSubmitButtonDisabled = true;
            $scope.reportIssueErrorResponse = "";
            
            var estimateTime = "";
            
            if($scope.requestData.estimatedResolutionTime){
                estimateTime = [ $scope.requestData.estimatedResolutionTime.slice(0, 2), ":", $scope.requestData.estimatedResolutionTime.slice(2) ].join('');
            }

            var requestData = {};
            
            requestData = {
                code : $scope.requestData.code,
                description : $scope.requestData.description,
                workingCapacityPercentage : $scope.requestData.currentWorkingCapacityPercentage,
                issueDateString : $scope.requestData.issueDate,
                issueTimeString : [ $scope.requestData.issueTime.slice(0, 2), ":", $scope.requestData.issueTime.slice(2) ].join(''),
                estimatedResolutionDateString : $scope.requestData.estimatedResolutionDate,
                estimatedResolutionTimeString : estimateTime,
                nameReporter : $scope.requestData.nameReporter,
                msisdnReporter : $scope.requestData.msisdnReporter
            };

            systemService.updatePortIssue(requestData, function(response) {

                $scope.reportIssueSubmitButtonDisabled = false;
                $scope.requestData = {};
                $scope.reportIssueForm.$setPristine();

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ISSUE_UPDATE_MESSAGE + ".");
                $rootScope.stopSpinner();
                
                $scope.closeReportDialog();
                $scope.refreshTableData();

            }, function(error) {

                $scope.reportIssueSubmitButtonDisabled = false;
                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
    };
    
    $scope.resolvePortIssue = function() {

        if ($scope.reportIssueForm.resolutionDate.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_RESOLUTION_DATE_MESSAGE + ".";

        }  else if ($scope.reportIssueForm.resolutionTime.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_RESOLUTION_TIME_MESSAGE + ".";

        } else if ($scope.reportIssueForm.resolutionDescription.$invalid) {
            $scope.reportIssueErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_RESOLUTION_DESCRIPTION_MESSAGE + ".";

        } else {

            $rootScope.startSpinner();
            $scope.reportIssueSubmitButtonDisabled = true;
            $scope.reportIssueErrorResponse = "";
            

            var requestData = {};
            requestData = {
                code : $scope.requestData.code,
                dateResolutionString : $scope.requestData.resolutionDate,
                timeResolutionString : [ $scope.requestData.resolutionTime.slice(0, 2), ":", $scope.requestData.resolutionTime.slice(2) ].join(''),
                resolutionDescription : $scope.requestData.resolutionDescription
            };

            systemService.resolvePortIssue(requestData, function(response) {

                $scope.reportIssueSubmitButtonDisabled = false;
                $scope.requestData = {};
                $scope.reportIssueForm.$setPristine();

                $rootScope.showResultMessage(true, $rootScope.translation.KEY_SCREEN_ISSUE_RESOLVED_MESSAGE + ".");
                $rootScope.stopSpinner();
                
                $scope.closeReportDialog();
                $scope.refreshTableData();

            }, function(error) {

                $scope.reportIssueSubmitButtonDisabled = false;
                $scope.errorHandler(error);

                $rootScope.showResultMessage(false, $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".");
            });
        }
    };
    
    $scope.submitForm = function() {
        
        if($scope.modalType == 1){
            $scope.reportPortIssue();
        }else if ($scope.modalType == 2){
            $scope.editPortIssue();
        }else if($scope.modalType == 3){
            $scope.resolvePortIssue();
        }
    }
        
    $scope.refreshTableData();
    
    } ]);
