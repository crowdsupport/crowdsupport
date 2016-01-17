(function () {
    angular
        .module('timeAgo', [])
        .directive("timeAgo", function ($q) {
            return {
                restrict: "C",
                scope: {
                    title: '@'
                },
                link: function (scope, element, attrs) {
                    var parsedDate = $q.defer();
                    parsedDate.promise.then(function () {
                        jQuery(element).timeago();
                    });

                    attrs.$observe('title', function (newValue) {
                        parsedDate.resolve(newValue);
                    });
                }
            };
        });
})();