(function () {
    angular
        .module('crowdsupport.widget.search', ['ui.bootstrap', 'restangular'])
        .factory('SearchWidget', function () {
            return function (entity, templateUrl, formatResult) {
                return {
                    restrict: 'E',
                    transclude: true,
                    scope: {
                        updateOnSelect: '=',
                        ngModel: '=',
                        inputClass: '@',
                        inputId: '@',
                        loading: '=',
                        noResults: '=',
                        omitNoResults: '@',
                        onKeypress: '='
                    },
                    controller: function ($scope, Restangular) {
                        $scope.query = function (query) {
                            return Restangular.all(entity).getList({query: query});
                        };

                        $scope.formatInput = function (model) {
                            return nullOrEmpty(model) ? '' : formatResult(model);
                        };

                        $scope.onSelect = function ($item) {
                            $.extend($scope.updateOnSelect, $item);
                        };

                        $scope.key = function(event) {
                            if ($scope.onKeypress) {
                                $scope.onKeypress(event);
                            }
                        };
                    },
                    templateUrl: templateUrl
                };
            };
        })
        .directive('citySearch', function (SearchWidget) {
            var format = function (model) {
                return model.name + ' (' + model.state.name + ')';
            };

            return new SearchWidget('city', '/r/template/widget/citySearch/citySearch.html', format);
        })
        .directive('stateSearch', function (SearchWidget) {
            var format = function (model) {
                return model.name;
            };

            return new SearchWidget('state', '/r/template/widget/stateSearch/stateSearch.html', format);
        })
        .directive('userSearch', function(SearchWidget) {
            var format = function (model) {
                return model.username;
            };

            return new SearchWidget('user', '/r/template/widget/userSearch/userSearch.html', format);
        })
        .directive('tagSearch', function(SearchWidget) {
            var format = function (tag) {
                return tag.name;
            };

            return new SearchWidget('tag', '/r/template/widget/tagSearch/tagSearch.html', format);
        });
})();