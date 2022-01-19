padApp.directive('convertToNumber', function() {
    return {
        require : 'ngModel',
        link : function(scope, element, attrs, ngModel) {
            ngModel.$parsers.push(function(val) {
                return val != null ? parseInt(val, 10) : null;
            });
            ngModel.$formatters.push(function(val) {
                return val != null ? '' + val : null;
            });
        }
    };
});

padApp.directive('capitalize', [ function() {
    return {
        require : 'ngModel',
        link : function(scope, element, attrs, modelCtrl) {
            var capitalize = function(inputValue) {
                if (inputValue == undefined)
                    inputValue = '';
                var capitalized = inputValue.toUpperCase();
                if (capitalized !== inputValue) {
                    // see where the cursor is before the update so that we can set it back
                    var selection = element[0].selectionStart;
                    modelCtrl.$setViewValue(capitalized);
                    modelCtrl.$render();
                    // set back the cursor after rendering
                    element[0].selectionStart = selection;
                    element[0].selectionEnd = selection;
                }
                return capitalized;
            }
            modelCtrl.$parsers.push(capitalize);
            capitalize(scope[attrs.ngModel]);
        }
    };
} ]);

padApp.directive('alphanumeric', function() {
    return {
        restrict : 'A',
        require : 'ngModel',
        link : function(scope, element, attr, ctrl) {
            ctrl.$parsers.unshift(function(viewValue) {
                var reg = new RegExp('^[a-zA-Z0-9]*$');
                if (reg.test(viewValue)) {
                    return viewValue;
                } else {
                    var overrideValue = (reg.test(ctrl.$modelValue) ? ctrl.$modelValue : '');
                    element.val(overrideValue);
                    return overrideValue;
                }
            });
        }
    };
});

padApp.directive("limitTo", [ function() {
    return {
        restrict : "A",
        link : function(scope, elem, attrs) {
            var limit = parseInt(attrs.limitTo);
            angular.element(elem).on("keypress", function(e) {
                if (this.value.length == limit)
                    e.preventDefault();
            });
        }
    }
} ]);

padApp.directive('avoidSpecialChars', [ function() {
    function link(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
            var reg = /^[^!@#$%\&*()_+={}|[\]\\:;"<>?,./]*$/;
            if (viewValue.match(reg)) {
                return viewValue;
            }
            var transformedValue = ngModel.$modelValue;
            ngModel.$setViewValue(transformedValue);
            ngModel.$render();
            return transformedValue;
        });
    }

    return {
        restrict : 'A',
        require : 'ngModel',
        link : link
    };
} ]);

padApp.directive('avoidNumbers', [ function() {
    function link(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
            var reg = /^[^0-9]*$/;
            if (viewValue.match(reg)) {
                return viewValue;
            }
            var transformedValue = ngModel.$modelValue;
            ngModel.$setViewValue(transformedValue);
            ngModel.$render();
            return transformedValue;
        });
    }

    return {
        restrict : 'A',
        require : 'ngModel',
        link : link
    };
} ]);

padApp.directive('tooltipLoader', [function() {
    return function(scope, element, attrs) {
        element.tooltip({
            trigger: "hover click",
            placement: "right"
        });
    };
} ]);

padApp.directive('inputCurrency', ['$filter', function($filter) {

    // For input validation
    var isValid = function(val) {
        return angular.isNumber(val) && !isNaN(val);
    };

    var toModel = function(val) {
        val = val.replace(/,/g, '').replace(/\./g, '').trim();
        return parseInt(Math.abs(val) , 10);
    };

    // Displayed in the input to users
    var toView = function(val) {
        var myFilter = $filter('number');
        val = myFilter(val, 0);
        return typeof val === 'undefined' ? val : val.replace(/,/g, '.');
    };

    // Link to DOM
    var link = function($scope, $element, $attrs, $ngModel) {
        $ngModel.$formatters.push(toView);
        $ngModel.$parsers.push(toModel);
        $ngModel.$validators.currency = isValid;

        $element.on('keyup', function() {
            $ngModel.$viewValue = toView($ngModel.$modelValue);
            $ngModel.$render();
        });
    };

    return {
        'restrict': 'A',
        'require': 'ngModel',
        'link': link
    };
}]);

padApp.directive('number', function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, ctrl) {
            ctrl.$parsers.push(function (input) {
                if (input == undefined) return ''
                var inputNumber = input.toString().replace(/[^0-9]/g, '');
                if (inputNumber != input) {
                    ctrl.$setViewValue(inputNumber);
                    ctrl.$render();
                }
                return inputNumber;
            });
        }
    };
});
