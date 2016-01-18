(function () {
    angular
        .module("crowdsupport", ["timeAgo", "crowdsupport.service.websocket", "crowdsupport.service.config",
            "crowdsupport.admin", "crowdsupport.widget.search", "crowdsupport.service.rest", "ui.router",
            "crowdsupport.service.statusbar", "ui.router.title"])
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
        })
        .controller("WelcomeController", function ($scope, Rest) {
            $scope.states = Rest.State.query();
        })
        .controller("StateController", function ($scope, $stateRest) {
            $scope.state = $stateRest;
        })
        .controller("CityController", function ($scope, $cityRest) {
            $scope.city = $cityRest;
        })
        .controller("PlaceController", function ($scope, $placeRest, Websocket) {
            $scope.place = $placeRest;

            var identifier = getUrlAfterSupport() + "/comments";

            $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                Websocket.unsubscribe(identifier);
            });
        })
        .controller("LoginCtrl", function ($scope, $rootScope) {
            $rootScope.user = null;

            $scope.setUser = function (userJson) {
                $rootScope.user = angular.fromJson(userJson);
            };
        })
        .controller("DonationRequestCtrl", function ($scope, Websocket) {
            var that = this;
            var donationRequest = {};

            $scope.comment = "";

            this.init = function (donationRequest) {
                that.donationRequest = donationRequest;
            };

            var identifier = getUrlAfterSupport() + "/comments";

            this.addComment = function () {
                console.log("Comment for request " + that.donationRequest.id + ": " + $scope.comment);

                var commentDto = {
                    "text": $scope.comment,
                    "quantity": 0,
                    "donationRequestId": that.donationRequest.id
                };

                Websocket.send(identifier, commentDto);

                $scope.comment = "";
            };

            Websocket.when(identifier).then(null, null, function (message) {
                // TODO the donationRequests controller (the one which should only exist once per site) should register this,
                // and forward the comments to the appropriate donationRequest controller, so not every single donationRequest
                // listens to every single comment update
                if (message.changeType == "ADD" && message.entity == "CommentDto") {
                    message = message.payload;
                    if (that.donationRequest.id == message.donationRequestId) {
                        that.donationRequest.comments.push(message);
                    }
                }
            });
        })
        .controller("PlaceRequestCtrl", function ($scope, Rest) {
            $scope.city = {};
            $scope.name = "";
            $scope.identifier = "";
            $scope.location = "";

            $scope.cityname = "";
            $scope.statename = "";

            $scope.addCity = false;

            $scope.addCityInfo = function () {
                $scope.cityname = $scope.city;
                $scope.addCity = true;
            };

            $scope.searchCity = function () {
                $scope.addCity = false;
            };

            this.submit = function () {
                var formData = {
                    place: {
                        name: $scope.name,
                        identifier: $scope.identifier,
                        location: $scope.location
                    }
                };

                if (!$scope.addCity) {
                    formData.place.city = $scope.city;
                } else {
                    formData.city = $scope.cityname;
                    formData.state = $scope.statename;
                }

                Rest.PlaceRequest.Request.save(formData, function () {
                    alert("Success");
                }, function () {
                    alert("Oh oh");
                });
            };
        });
})();