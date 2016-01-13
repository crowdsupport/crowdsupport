(function () {
    var app = angular.module("crowdsupport.admin", ["ui.router", "ui.bootstrap"]);

    app.config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
        $locationProvider.html5Mode(true);

        $urlRouterProvider.otherwise(function($injector, $location) {
            // that's ugly, but needed to escape ui.router /o\
            if ($location.$$url.indexOf("admin") <= -1) {
                window.location.reload(true);
            }
            return $location;
        });

        $stateProvider
            .state("overview", {
                url: "/admin",
                templateUrl: "/template/admin/overview.html"
            })
            .state("requestedPlaces", {
                url: "/admin/requestedPlaces",
                templateUrl: "/template/admin/requestedPlaces.html",
                controller: "RequestedPlacesCtrl as ctrl"
            });
    });

    app.controller("RequestedPlacesCtrl", function($http, $scope) {
        $scope.allRequests = {};

        $http.get("/service/v1/place/request").then(function (response) {
            $scope.allRequests = response.data;
        }, null);

        $scope.initRequest = function(request) {
            var citySelected = request.place.city !== null;
            request.ui = {
                citySearch: citySelected,
                stateSearch: citySelected,
                setCitySearch: function(citySearch) {
                    if (citySearch == false) {
                        // copy value to input field
                    }
                    request.ui.citySearch = citySearch;
                },
                setStateSearch: function(stateSearch) {
                    if (stateSearch == false) {
                        // copy value to input field
                    }
                    request.ui.stateSearch = stateSearch;
                }
            };
        };
    });
})();