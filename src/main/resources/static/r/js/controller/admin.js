(function () {
    angular
        .module('crowdsupport.controller.admin', ['restangular', 'crowdsupport.service.status'])
        .controller('RequestedPlacesCtrl', function ($scope, $allRequests, Restangular, Status, $log) {
            $scope.allRequests = $allRequests;

            $scope.save = function (index) {
                $log.debug('Saving donation request ' + index);
                var request = $scope.allRequests[index];

                request.post('accept', request).then(function () {
                    $log.debug('Place successfully posted');
                    Status.success('Place successfully published');
                    $scope.removeRequest(index);
                }, function () {
                    $log.debug('Error while saving');
                    Status.error('Error while saving place');
                });
            };

            $scope.removeRequest = function (index) {
                $scope.allRequests.splice(index, 1);
            };

            $scope.decline = function (index) {
                var request = $scope.allRequests[index];

                request.remove().then(function () {
                    $log.debug('Request successfully declined');
                    Status.success('Request successfully declined');
                    $scope.removeRequest(index);
                });
            };

            $scope.queryCity = function (query) {
                return Restangular.all('city').getList({query: query});
            };

            $scope.queryState = function (query) {
                return Restangular.all('state').getList({query: query});
            };

            $scope.createState = function (request) {
                Restangular.all('state').post(request.place.city.state).then(function (response) {
                    $log.debug('State successfully created');
                    request.place.city.state = response;
                    request.ui.state = response;
                    request.ui.setStateSearch(true);

                    Status.success('Successfully created new state');
                }, function (response) {
                    $log.debug(response);
                    Status.error('Could not create new state');
                });
            };

            $scope.createCity = function (request) {
                if (request.ui.stateSearch) {
                    Restangular.all('city').post(request.place.city).then(function (response) {
                        $log.debug('City successfully created');
                        request.ui.city = response;
                        request.place.city = response;
                        request.ui.setCitySearch(true);

                        Status.success('Successfully created new city');
                    }, function (response) {
                        $log.debug(response);
                        Status.error('Could not create new city');
                    });
                }
            };

            $scope.initRequest = function (request) {
                var citySelected = request.place.city !== null;
                if (!citySelected) {
                    request.place.city = {
                        name: request.city,
                        identifier: null,
                        imagePath: '/image/placeholder.jpg',
                        state: {
                            name: request.state,
                            identifier: null,
                            imagePath: '/image/placeholder.jpg'
                        }
                    }
                }

                request.ui = {
                    citySearch: citySelected,
                    stateSearch: true,
                    city: request.place.city,
                    state: request.place.city.state,
                    setCitySearch: function (citySearch) {
                        if (!citySearch) {
                            request.place.city = {
                                state: '',
                                name: request.ui.city
                            };
                        }

                        request.ui.citySearch = citySearch;
                    },
                    setStateSearch: function (stateSearch) {
                        if (!stateSearch) {
                            request.place.city.state = {
                                name: request.ui.state
                            };
                        }
                        request.ui.stateSearch = stateSearch;
                    }
                };
            };
        })
        .controller('UserManagementController', function ($scope, $log, $allRoles, Restangular, Status, Auth, $rootScope) {
            $scope.allRoles = $allRoles;

            $scope.query = function (query) {
                return Restangular.all('user').getList({query: query});
            };

            $scope.save = function () {
                $scope.edit.patch($scope.edit).then(function (response) {
                    Status.success('Successfully edited user');

                    // edited your own user?
                    if ($scope.edit.id == $rootScope.user.id) {
                        // kept username the same?
                        if ($scope.edit.username == response.username) {
                            Auth.updateUser();
                        } else {
                            Auth.logout();
                            Status.info('You\'ve changed your username to ' + response.username + ', please relogin')
                        }
                    }
                }, function (response) {
                    $log.debug(response);
                    Status.error('Could not change user details');
                });
            };
        })
        .controller('RoleManagementController', function ($roles, $permissions, $scope, $log, Restangular, Status, Auth, $mdDialog) {
            $scope.allRoles = $roles;
            $scope.allPermissions = $permissions;

            $scope.selectedRole = $scope.allRoles[0];

            $scope.addRole = function () {
                $mdDialog.show({
                    controller: 'RoleCreateController',
                    templateUrl: 'roleCreateModal.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    escapeToClose: true,
                    fullscreen: true,
                    openFrom: 'role-management__add-role',
                    closeTo: 'role-management__add-role'
                }).then(function () {
                    return Restangular.all('role').getList();
                }).then(function (roles) {
                    $scope.allRoles = roles;
                    $scope.selectedRole = $scope.allRoles[0];
                });
            };

            $scope.removeSelectedRole = function () {
                Restangular.one('role', $scope.selectedRole.name).remove().then(function () {
                    Status.success('Successfully deleted role');

                    $scope.selectedRole = $scope.allRoles[0];
                    Auth.updateUser();
                    Restangular.all('role').getList().then(function (roles) {
                        $scope.allRoles = roles;
                        $scope.selectedRole = $scope.allRoles[0];
                    });
                });
            };

            $scope.assignPermissions = function () {
                Restangular.one('role', $scope.selectedRole.name).customPUT($scope.selectedRole.permissions, 'permissions').then(function () {
                    Status.success('Successfully changed permissions');
                    Auth.updateUser();
                }, function (response) {
                    $log.debug(response);
                    Status.error('Could not assign permissions');
                });
            };
        })
        .controller('RoleCreateController', function ($scope, Restangular, Status, $mdDialog, $log) {
            $scope.create = function () {
                Restangular.one('role', $scope.roleName).put().then(function (response) {
                    Status.success('Successfully created role ' + $scope.roleName);
                    $mdDialog.hide(response);
                }, function (response) {
                    $log.debug(response);
                    Status.error('Could not createnew role');
                });
            };
        })
        .controller('SettingsController', function ($scope, Restangular, Status, Auth, $log) {
            $scope.refreshToken = function () {
                Restangular.one('setting', 'refreshApplicationSecret').post().then(function () {
                    Auth.logout();
                    Status.success('Refreshed application token - you have to login again!');
                }, function (response) {
                    $log.debug(response);
                    Status.error('Could not refresh token');
                });
            };
        });
})();