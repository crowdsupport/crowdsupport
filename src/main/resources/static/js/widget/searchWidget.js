(function () {
    var SearchWidget = function (serviceUrl, templateUrl, formatResult) {
        return {
            restrict: "E",
            transclude: true,
            scope: {
                updateOnSelect: "=",
                ngModel: "=",
                inputClass: "@",
                inputId: "@"
            },
            controller: function ($scope, $http) {
                $scope.queryCities = function (query) {
                    return $http.get(serviceUrl, {
                        params: {
                            query: query
                        }
                    }).then(function (response) {
                        return response.data;
                    });
                };

                $scope.formatInput = function (model) {
                    return nullOrEmpty(model) ? "" : formatResult(model);
                };

                $scope.onSelect = function ($item) {
                    $.extend($scope.updateOnSelect, $item);
                }
            },
            templateUrl: templateUrl
        };
    };

    angular.module('crowdsupport.widget.search', ['ui.bootstrap'])
        .directive("citySearch", function () {
            var format = function (model) {
                return model.name + " (" + model.state.name + ")";
            };

            return SearchWidget(SERVICE_PREFIX + "city", "/template/widget/citySearch/citySearch.html", format);
        })
        .directive("stateSearch", function () {
            var format = function (model) {
                return model.name;
            };

            return SearchWidget(SERVICE_PREFIX + "state", "/template/widget/stateSearch/stateSearch.html", format);
        });
})();