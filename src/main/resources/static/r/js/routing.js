(function () {
    angular
        .module('crowdsupport.routing', ['crowdsupport.controller', 'crowdsupport.service.auth',
            'ui.router', 'ui.router.title', 'restangular'])
        .config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
            $locationProvider.html5Mode(true);

            $urlRouterProvider.otherwise('/');

            $stateProvider
                .state('dashboard', {
                    url: '/',
                    templateUrl: '/r/template/dashboard.html',
                    controller: 'DashboardController',
                    resolve: {
                        $title: function () {
                            return 'Welcome';
                        },
                        $statistics: function(Restangular) {
                            return Restangular.one('statistics').get();
                        }
                    }
                })
                .state('states', {
                    url: '/support',
                    templateUrl: '/r/template/states.html',
                    controller: 'StatesController',
                    resolve: {
                        $title: function () {
                            return 'States';
                        },
                        $states: function (Restangular) {
                            return Restangular.all('state').getList();
                        }
                    }
                })
                .state('state', {
                    url: '/support/:stateIdentifier',
                    templateUrl: '/r/template/state.html',
                    controller: 'StateController',
                    resolve: {
                        $stateRest: function (Restangular, $stateParams) {
                            return Restangular.one('state').get({identifier: $stateParams.stateIdentifier});
                        },
                        $title: function ($stateRest) {
                            return $stateRest.name;
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
                        $cityRest: function (Restangular, $stateParams) {
                            return Restangular.one('city').get({
                                identifier: $stateParams.cityIdentifier,
                                stateIdentifier: $stateParams.stateIdentifier
                            });
                        },
                        $title: function ($cityRest) {
                            return $cityRest.name;
                        }
                    }
                })
                .state('state.city.place', {
                    url: '^/support/:stateIdentifier/:cityIdentifier/:placeIdentifier',
                    views: {
                        '@': {
                            templateUrl: '/r/template/place.html',
                            controller: 'PlaceController as placeCtrl'
                        },
                        'sidebar@': {
                            templateUrl: '/r/template/placeFilterMenu.html',
                            controller: 'PlaceFilterController as placeFilterController'
                        }
                    },
                    resolve: {
                        $placeRest: function (Restangular, $stateParams) {
                            return Restangular.one('place').get({
                                identifier: $stateParams.placeIdentifier,
                                cityIdentifier: $stateParams.cityIdentifier,
                                stateIdentifier: $stateParams.stateIdentifier
                            });
                        },
                        $title: function ($placeRest) {
                            return $placeRest.name;
                        }
                    }
                })
                .state('state.city.place.management', {
                    url: '^/support/:stateIdentifier/:cityIdentifier/:placeIdentifier/manage',
                    views: {
                        '@': {
                            templateUrl: '/r/template/placeManagement.html',
                            controller: 'PlaceManagementController as placeManagementCtrl'
                        },
                        'sidebar@': {}
                    },
                    resolve: {
                        $members: function ($placeRest) {
                            return $placeRest.all('team').getList();
                        },
                        $title: function () {
                            return "Management";
                        }
                    }
                })
                .state('profile', {
                    url: '/profile',
                    controller: 'ProfileController',
                    templateUrl: '/r/template/profile.html',
                    resolve: {
                        $user: function (Auth) {
                            return Auth.updateUser().$promise;
                        },
                        $title: function () {
                            return 'Profile';
                        }
                    },
                    data: {
                        authorities: AuthStore.ROLE_USER
                    }
                })
                .state('register', {
                    url: '/register',
                    controller: 'RegistrationController',
                    templateUrl: '/r/template/register.html',
                    resolve: {
                        $title: function () {
                            return 'Registration';
                        }
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
                        $allRequests: function (Restangular) {
                            return Restangular.all('placeRequest').getList();
                        },
                        $title: function () {
                            return 'Requested places';
                        }
                    },
                    data: {
                        authorities: [AuthStore.ROLE_ADMIN, AuthStore.PROCESS_PLACE_REQUESTS]
                    }
                })
                .state('admin.settings', {
                    url: '/settings',
                    views: {
                        '@': {
                            templateUrl: '/r/template/admin/settings.html',
                            controller: 'SettingsController as ctrl'
                        }
                    },
                    resolve: {
                        $title: function() {
                            return 'Configuration'
                        }
                    },
                    data: {
                        authorities: [AuthStore.ROLE_ADMIN, AuthStore.CONFIGURE_APPLICATION]
                    }
                })
                .state('admin.users', {
                    url: '/userManagement',
                    views: {
                        '@': {
                            templateUrl: '/r/template/admin/userManagement.html',
                            controller: 'UserManagementController as ctrl'
                        }
                    },
                    resolve: {
                        $allRoles: function (Restangular) {
                            return Restangular.all('role').getList();
                        },
                        $title: function () {
                            return 'User Management';
                        }
                    },
                    data: {
                        authorities: [AuthStore.ROLE_ADMIN, AuthStore.MANAGE_USERS]
                    }
                })
                .state('admin.roles', {
                    url: '/roleManagement',
                    views: {
                        '@': {
                            templateUrl: '/r/template/admin/roleManagement.html',
                            controller: 'RoleManagementController as ctrl'
                        }
                    },
                    resolve: {
                        $title: function () {
                            return 'Role Management';
                        },
                        $roles: function (Restangular) {
                            return Restangular.all('role').getList();
                        },
                        $permissions: function (Restangular) {
                            return Restangular.all('permission').getList();
                        }
                    },
                    data: {
                        authorities: [AuthStore.ROLE_ADMIN, AuthStore.MANAGE_ROLES]
                    }
                });
        });
})();