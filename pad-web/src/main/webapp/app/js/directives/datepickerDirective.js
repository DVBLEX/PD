padApp.directive('tripSlotApprovedFilterFromDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.trip_slot_approved_filter_from.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('tripSlotApprovedFilterToDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.trip_slot_approved_filter_to.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('missionDateFromDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.mission_date_from.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'top',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('missionDateToDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.mission_date_to.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : false,
                orientation : 'top',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('slotStartDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.slot_start_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'top',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('slotFromDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.slot_from_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('slotToDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.slot_to_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('whitelistDateFromDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            var endDate = new Date();
            endDate.setDate(endDate.getDate() + 30);
            $('.whitelist_date_from.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                endDate : endDate,
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('whitelistDateToDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            var endDate = new Date();
            endDate.setDate(endDate.getDate() + 30);
            $('.whitelist_date_to.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                endDate : endDate,
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : false,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('filterWhitelistDateFromDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.filter_whitelist_date_from.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('filterWhitelistDateToDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.filter_whitelist_date_to.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : false,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('adHocTripSlotStartDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            var endDate = new Date();

            if ($rootScope.kioskSessionType === $rootScope.kioskSessionTypeConstants.KIOSK_SESSION_TYPE_VIRTUAL) {
                endDate.setDate(endDate.getDate() + 1);
            } else {
                endDate.setDate(endDate.getDate() + 1);
            }

            $('.ad_hoc_trip_slot_start_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                endDate : endDate,
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('invoicePaymentDate', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            var endDate = new Date();
            endDate.setDate(endDate.getDate() + 30);
            $('.invoice_payment.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : false,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('countFromDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.count_from_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('countToDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.count_to_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation: 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('estimatedResolutionDateDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.estimated_resolution_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : new Date(),
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('issueDateDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.issue_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});

padApp.directive('resolutionDateDP', function() {
    return {
        scope : {
            onSelect : "&"
        },
        restrict : 'EA',
        controller : [ "$rootScope", "$filter", function($rootScope, $filter) {
            $('.resolution_date.date').datepicker({
                format : "dd/mm/yyyy",
                startDate : "01-01-1979",
                todayBtn : "linked",
                autoclose : true,
                todayHighlight : true,
                orientation : 'bottom',
                language: $filter('lowercase')($rootScope.language)
            });
        }]
    }
});
