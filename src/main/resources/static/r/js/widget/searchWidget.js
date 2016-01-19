(function () {
    angular
        .module('crowdsupport.widget.search', ['ui.bootstrap'])
        .factory('SearchWidget', function () {
            return function (serviceUrl, templateUrl, formatResult) {
                return {
                    restrict: 'E',
                    transclude: true,
                    scope: {
                        updateOnSelect: '=',
                        ngModel: '=',
                        inputClass: '@',
                        inputId: '@',
                        loading: '=',
                        noResults: '='
                    },
                    controller: function ($scope, $http) {
                        $scope.query = function (query) {
                            return $http.get(serviceUrl, {
                                params: {
                                    query: query
                                }
                            }).then(function (response) {
                                return response.data;
                            });
                        };

                        $scope.formatInput = function (model) {
                            return nullOrEmpty(model) ? '' : formatResult(model);
                        };

                        $scope.onSelect = function ($item) {
                            $.extend($scope.updateOnSelect, $item);
                        }
                    },
                    templateUrl: templateUrl
                };
            };
        })
        .directive('citySearch', function (SearchWidget) {
            var format = function (model) {
                return model.name + ' (' + model.state.name + ')';
            };

            return new SearchWidget(SERVICE_PREFIX + 'city', '/r/template/widget/citySearch/citySearch.html', format);
        })
        .directive('stateSearch', function (SearchWidget) {
            var format = function (model) {
                return model.name;
            };

            return new SearchWidget(SERVICE_PREFIX + 'state', '/r/template/widget/stateSearch/stateSearch.html', format);
        })
        .directive('userSearch', function(SearchWidget) {
            var format = function (model) {
                return model.username;
            };

            return new SearchWidget(SERVICE_PREFIX + 'user', '/r/template/widget/userSearch/userSearch.html', format);
        });
})();