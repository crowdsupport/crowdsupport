(function () {
    var app = angular.module("crowdsupport.admin", ["ui.router", "crowdsupport.widget.search"]);

    app.config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
        $locationProvider.html5Mode(true);

        $urlRouterProvider.otherwise(function ($injector, $location) {
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

    app.controller("RequestedPlacesCtrl", function ($http, $scope, $log) {
        $scope.allRequests = {};

        $http.get("/service/v1/place/request").then(function (response) {
            $scope.allRequests = response.data;
        }, null);

        $scope.save = function (index) {
            $log.debug("Saving donation request " + index);
            var r = $scope.allRequests[index];
            $log.debug(r);

            $http.post("/service/v1/place", r).then(function (response) {
                $log.debug("Place successfully posted");
                $log.debug(response);
                $scope.removeRequest(index);
            }, function (response) {
                $log.debug("Error while saving");
                $log.debug(response);
            });
        };

        $scope.removeRequest = function (index) {
            $scope.allRequests.splice(index, 1);
        };

        $scope.decline = function (index) {
            $http.post(SERVICE_PREFIX + "place/request/" + $scope.allRequests[index].id + "/decline").then(function (response) {
                $log.debug("Request successfully declined");
                $scope.removeRequest(index);
            });
        };

        $scope.createState = function (request) {
            $http.post(SERVICE_PREFIX + "state", request.place.city.state).then(function (response) {
                $log.debug("State successfully created");
                request.place.city.state = response.data;
                request.ui.state = response.data;
                request.ui.setStateSearch(true);
            });
        };

        $scope.createCity = function (request) {
            if (request.ui.stateSearch) {
                $http.post(SERVICE_PREFIX + "city", request.place.city).then(function (response) {
                    $log.debug("City successfully created");
                    request.ui.city = response.data;
                    request.place.city = response.data;
                    request.ui.setCitySearch(true);
                });
            }
        };

        $scope.initRequest = function (request) {
            var citySelected = request.place.city !== null;
            if (!citySelected) {
                request.place.city = {
                    name: request.city,
                    identifier: null,
                    imagePath: "/image/placeholder.jpg",
                    state: {
                        name: request.state,
                        identifier: null,
                        imagePath: "/image/placeholder.jpg"
                    }
                }
            }

            request.ui = {
                citySearch: citySelected,
                stateSearch: true,
                city: request.place.city,
                state: request.place.city.state,
                setCitySearch: function (citySearch) {
                    if (citySearch == false) {
                        request.place.city = {
                            state: request.place.city.state,
                            name: request.ui.city
                        };
                    }
                    request.ui.citySearch = citySearch;
                },
                setStateSearch: function (stateSearch) {
                    if (stateSearch == false) {
                        request.place.city.state = {
                            name: request.ui.state
                        };
                    }
                    request.ui.stateSearch = stateSearch;
                }
            };
        };
    });
})();