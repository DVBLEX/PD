padApp.service('translationService', [ '$rootScope', '$resource', function($rootScope, $resource) {

    this.getLanguages = function(language) {

        var api = $resource("translate/get");

        api.save().$promise.then(function(response) {

            $rootScope.languageList = response.dataMap;
            
            $rootScope.changeLanguage(language);

        }, function(error) {

            $rootScope.languageList = [];
        });

    }

} ]);
