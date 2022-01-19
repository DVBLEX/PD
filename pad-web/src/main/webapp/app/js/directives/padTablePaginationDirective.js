padApp.directive("padTablePagination", function() {
    return {
        restrict : 'E',
        templateUrl : 'app/views/directives/padTablePager.html',
        replace : true,
        require : 'ngModel',
        require : '^?form',
        link : function($scope, element, attrs) {
            $scope.padTableChangePage = function(sourceId) {

                var currentPage = $scope.padTable.getCurrentPage();
                if (currentPage != undefined && currentPage !== "") {

                    currentPage += "";
                    if (currentPage.match(/^\d+$/)) {

                        if (sourceId === $scope.padTable.SOURCE_NEXT_PAGE) {
                            currentPage++;
                        } else if (sourceId === $scope.padTable.SOURCE_PREVIOUS_PAGE) {
                            currentPage--;
                        } else if (sourceId === $scope.padTable.SOURCE_LAST_PAGE) {
                            currentPage = $scope.padTable.getPageCount();
                        } else if (sourceId === $scope.padTable.SOURCE_FIRST_PAGE) {
                            currentPage = 1;
                        }

                        if (currentPage > $scope.padTable.getPageCount()) {
                            currentPage = $scope.padTable.getPageCount();
                        } else if (currentPage < 1) {
                            currentPage = 1;
                        }

                    } else {
                        currentPage = 1;
                    }

                } else {
                    currentPage = 1;
                }

                $scope.padTable.setCurrentPage(currentPage);
                $scope.padTable.updateControls();
                $scope.padTable.gotoCurrentPage();
            }
        }
    }
});