(function () {
    angular.module('crowdsupport.widget.stateSearch', ['ui.bootstrap'])
        .directive("stateSearch", function () {
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
                    $scope.queryStates = function (query) {
                        return $http.get(SERVICE_PREFIX + "state", {
                            params: {
                                query: query
                            }
                        }).then(function (response) {
                            return response.data;
                        });
                    };

                    $scope.formatInput = function (model) {
                        return nullOrEmpty(model) ? model.name : "";
                    };

                    $scope.onSelect = function($item) {
                        $.extend($scope.updateOnSelect, $item);
                    };
                },
                templateUrl: "/template/widget/stateSearch/stateSearch.html"
            };
        });
})();