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
                    uibTooltip: '@',
                    datetime: '@'
                },
                link: function (scope, element, attrs) {
                    var parsedDate = $q.defer();
                    parsedDate.promise.then(function () {
                        jQuery(element).timeago();

                        if ($(element).attr('uib-tooltip')) {
                            element[0].removeAttribute('title');
                        }
                    });

                    var a = null;
                    if (scope.title) {
                        a = 'title'
                    } else if (scope.datetime) {
                        a = 'datetime';
                    } else if (scope.uibTooltip) {
                        a = 'uibTooltip';

                        if (element[0].nodeName === 'TIME') {
                            attrs.$set('datetime', scope.uibTooltip);
                        } else {
                            attrs.$set('title', scope.uibTooltip);
                        }
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