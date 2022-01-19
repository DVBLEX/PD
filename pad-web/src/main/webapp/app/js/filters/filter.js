padApp.filter('to_trusted_html', [ '$sce', function($sce) {
    return function(text) {
        return $sce.trustAsHtml(text);
    };
} ]);

padApp.filter('subText', function() {
    return function(input, length) {
        if (input.length > length) {
            return input.substring(0, length) + "...";
        } else {
            return input;
        }
    }
});

padApp.filter('translate', [ '$rootScope', function($rootScope) {
    return function(text) {
        if (text !== undefined && text !== null && text !== "") {
            return $rootScope.$eval('translation.' + text);
        } else {
            return text;
        }
    };
} ]);

padApp.filter('limitChar', function() {
    return function(content, length) {
        if (content === undefined) {
            return "";
        }
        if (isNaN(length)) {
            length = 20;
        }
        var tail = "...";
        if (content.length <= length || content.length - tail.length <= length) {
            return content;
        } else {
            return String(content).substring(0, length - tail.length) + tail;
        }
    };
});

padApp.filter('unique', function() {
    return function(items, filterOn) {

        if (filterOn === false) {
            return items;
        }

        if ((filterOn || angular.isUndefined(filterOn)) && angular.isArray(items)) {
            var hashCheck = {}, newItems = [];

            var extractValueToCompare = function(item) {
                if (angular.isObject(item) && angular.isString(filterOn)) {
                    return item[filterOn];
                } else {
                    return item;
                }
            };

            angular.forEach(items, function(item) {
                var valueToCheck, isDuplicate = false;

                for (var i = 0; i < newItems.length; i++) {
                    if (angular.equals(extractValueToCompare(newItems[i]), extractValueToCompare(item))) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    newItems.push(item);
                }

            });
            items = newItems;
        }
        return items;
    };
});

padApp.filter('customCurrency', ['$filter', function ($filter) {
    return function (input, fractionSize) {
        if (isNaN(input)) {
            return input;
        } else {
            return $filter('number')(input, fractionSize).replace(/,/g, '.');
        }
    };
}]);
