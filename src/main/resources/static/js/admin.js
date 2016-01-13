(function () {
    var app = angular.module("crowdsupport.admin", ["ui.router", "crowdsupport.widget.search"]);

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

    app.controller("RequestedPlacesCtrl", function($http, $scope, $log) {
        $scope.allRequests = {};

        $http.get("/service/v1/place/request").then(function (response) {
            $scope.allRequests = response.data;
        }, null);

        $scope.save = function(index) {
            $log.debug("Saving donation request " + index);
            $log.debug($scope.allRequests[index]);
        };

        $scope.initRequest = function(request) {
            var citySelected = request.place.city !== null;
            if (!citySelected) {
                request.place.city = {
                    name: request.city,
                    identifier: null,
                    state: {
                        name: request.state,
                        identifier: null
                    }
                }
            }

            request.ui = {
                citySearch: citySelected,
                stateSearch: true,
                city: request.place.city,
                state: request.place.city.state,
                setCitySearch: function(citySearch) {
                    if (citySearch == false) {
                        request.place.city = {
                            state: {},
                            name: request.ui.city
                        };
                    }
                    request.ui.citySearch = citySearch;
                },
                setStateSearch: function(stateSearch) {
                    if (stateSearch == false) {
                        request.place.city.state = {
                            name: request.ui.name
                        };
                    }
                    request.ui.stateSearch = stateSearch;
                }
            };
        };
    });
})();