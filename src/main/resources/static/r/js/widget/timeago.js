(function () {
    var p = function(s, n, p) {
        return _.padLeft(s, n || 2, p || '0')
    };

    angular
        .module('timeAgo', [])
        .directive('timeAgo', function ($q) {
            return {
                restrict: 'C',
                scope: {
                    title: '@',
                    datetime: '@'
                },
                link: function (scope, element, attrs) {
                    var parsedDate = $q.defer();
                    parsedDate.promise.then(function () {
                        jQuery(element).timeago();

                        if ($(element).has('md-tooltip')) {
                            element[0].removeAttribute('title');
                        }
                    });

                    var a = null;
                    if (scope.title) {
                        a = 'title'
                    } else if (scope.datetime) {
                        a = 'datetime';
                    }

                    if (a) {
                        attrs.$observe(a, function () {
                            parsedDate.resolve();
                        });
                    }
                }
            };
        })
        .filter('time', function () {
            return function (input) {
                var d = new Date(input);

                return p(d.getDate()) + '.' + p(d.getMonth() + 1) + '.' + d.getFullYear() + ' '
                    + p(d.getHours()) + ':' + p(d.getMinutes());
            }
        });
})();