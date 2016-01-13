(function () {
    angular.module('crowdsupport.widget.citySearch', ['ui.bootstrap'])
        .directive("citySearch", function () {
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
                        return $http.get(SERVICE_PREFIX + "city", {
                            params: {
                                query: query
                            }
                        }).then(function (response) {
                            return response.data;
                        });
                    };

                    $scope.formatInput = function (model) {
                        return nullOrEmpty(model) ?
                        model.name + " (" + model.state.name + ")": "";
                    };

                    $scope.onSelect = function($item) {
                        $.extend($scope.updateOnSelect, $item);
                    }
                },
                templateUrl: "/template/widget/citySearch/citySearch.html"
            };
        });
})();