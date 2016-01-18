(function() {
    angular
        .module("crowdsupport.routing", ["crowdsupport.controller", "crowdsupport.service.rest", "ui.router", "ui.router.title"])
        .config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
            $locationProvider.html5Mode(true);

            $urlRouterProvider.otherwise("/");

            $stateProvider
                .state("welcome", {
                    url: "/",
                    templateUrl: "/template/welcome.html",
                    controller: "WelcomeController",
                    resolve: {
                        $title: function () {
                            return "Home";
                        }
                    }
                })
                .state("state", {
                    url: "/support/:stateIdentifier",
                    templateUrl: "/template/state.html",
                    controller: "StateController",
                    resolve: {
                        $stateRest: function(Rest, $stateParams) {
                            return Rest.State.get({identifier: $stateParams.stateIdentifier});
                        },
                        $title: function ($stateRest) {
                            return $stateRest.$promise.then(function(state) {
                                return state.name;
                            });
                        }
                    }
                })
                .state("state.city", {
                    url: "^/support/:stateIdentifier/:cityIdentifier",
                    views: {
                        '@': {
                            templateUrl: "/template/city.html",
                            controller: "CityController"
                        }
                    },
                    resolve: {
                        $cityRest: function(Rest, $stateParams) {
                            return Rest.City.get({ identifier: $stateParams.cityIdentifier, stateIdentifier: $stateParams.stateIdentifier})
                        },
                        $title: function ($cityRest) {
                            return $cityRest.$promise.then(function(city) {
                                return city.name;
                            });
                        }
                    }
                })
                .state("state.city.place", {
                    url: "^/support/:stateIdentifier/:cityIdentifier/:placeIdentifier",
                    views: {
                        '@': {
                            templateUrl: "/template/place.html",
                            controller: "PlaceController as placeCtrl"
                        }
                    },
                    resolve: {
                        $placeRest: function(Rest, $stateParams) {
                            return Rest.Place.get({
                                identifier: $stateParams.placeIdentifier,
                                cityIdentifier: $stateParams.cityIdentifier,
                                stateIdentifier: $stateParams.stateIdentifier
                            });
                        },
                        $title: function ($placeRest) {
                            return $placeRest.$promise.then(function(place) {
                                return place.name;
                            });
                        }
                    }
                })
                .state("profile", {
                    url: "/profile",
                    templateUrl: "/template/profile.html",
                    resolve: {
                        $title: function () {
                            return "Profile";
                        }
                    }
                })
                .state("placeRequest", {
                    url: "/request/newPlace",
                    templateUrl: "/template/placerequest.html",
                    resolve: {
                        $title: function () {
                            return "Request new place";
                        }
                    }
                })
                .state("admin", {
                    url: "/admin",
                    templateUrl: "/template/admin/overview.html",
                    resolve: {
                        $title: function () {
                            return "Admin";
                        }
                    }
                })
                .state("admin.requestedPlaces", {
                    url: "/requestedPlaces",
                    views: {
                        '@': {
                            templateUrl: "/template/admin/requestedPlaces.html",
                            controller: "RequestedPlacesCtrl as ctrl"
                        }
                    },
                    resolve: {
                        $title: function () {
                            return "Requested places";
                        }
                    }
                });
        });
})();