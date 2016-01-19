(function() {
    angular
        .module('crowdsupport.routing', ['crowdsupport.controller', 'crowdsupport.service.rest', 'crowdsupport.service.auth',
            'ui.router', 'ui.router.title'])
        .config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
            $locationProvider.html5Mode(true);

            $urlRouterProvider.otherwise('/');

            $stateProvider
                .state('welcome', {
                    url: '/',
                    templateUrl: '/r/template/welcome.html',
                    controller: 'WelcomeController',
                    resolve: {
                        $title: function () {
                            return 'Welcome';
                        }
                    }
                })
                .state('state', {
                    url: '/support/:stateIdentifier',
                    templateUrl: '/r/template/state.html',
                    controller: 'StateController',
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
                .state('state.city', {
                    url: '^/support/:stateIdentifier/:cityIdentifier',
                    views: {
                        '@': {
                            templateUrl: '/r/template/city.html',
                            controller: 'CityController'
                        }
                    },
                    resolve: {
                        $cityRest: function(Rest, $stateParams) {
                            return Rest.City.get({ identifier: $stateParams.cityIdentifier, stateIdentifier: $stateParams.stateIdentifier});
                        },
                        $title: function ($cityRest) {
                            return $cityRest.$promise.then(function(city) {
                                return city.name;
                            });
                        }
                    }
                })
                .state('state.city.place', {
                    url: '^/support/:stateIdentifier/:cityIdentifier/:placeIdentifier',
                    views: {
                        '@': {
                            templateUrl: '/r/template/place.html',
                            controller: 'PlaceController as placeCtrl'
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
                .state('profile', {
                    url: '/profile',
                    controller: 'ProfileController',
                    templateUrl: '/r/template/profile.html',
                    resolve: {
                        $user: function(Auth) {
                            return Auth.updateUser().$promise;
                        },
                        $title: function () {
                            return 'Profile';
                        }
                    }
                })
                .state('register', {
                    url: '/register',
                    controller: 'RegistrationController',
                    templateUrl: '/r/template/register.html',
                    resolve: {
                        $title: function() {
                            return 'Registration';
                        }
                    },
                    data: {
                        authorities: AuthStore.ROLE_USER
                    }
                })
                .state('placeRequest', {
                    url: '/request/newPlace',
                    controller: 'PlaceRequestCtrl as ctrl',
                    templateUrl: '/r/template/placerequest.html',
                    resolve: {
                        $title: function () {
                            return 'Request new place';
                        }
                    }
                })
                .state('admin', {
                    url: '/admin',
                    templateUrl: '/r/template/admin/overview.html',
                    resolve: {
                        $title: function () {
                            return 'Admin';
                        }
                    },
                    data: {
                        authorities: AuthStore.ROLE_ADMIN
                    }
                })
                .state('admin.requestedPlaces', {
                    url: '/requestedPlaces',
                    views: {
                        '@': {
                            templateUrl: '/r/template/admin/requestedPlaces.html',
                            controller: 'RequestedPlacesCtrl as ctrl'
                        }
                    },
                    resolve: {
                        $title: function () {
                            return 'Requested places';
                        }
                    }
                });
        });
})();