angular.module('timeAgo', [])
    .directive("timeAgo", function($q) {
        return {
            restrict: "C",
            scope: {
                title: '@'
            },
            link: function(scope, element, attrs) {

                // Using deferred to assert we only initialize timeago() once per
                // directive.
                var parsedDate = $q.defer();
                parsedDate.promise.then(function() {
                    jQuery(element).timeago();
                });

                attrs.$observe('title', function(newValue) {
                    parsedDate.resolve(newValue);
                });

            }
        };
    });