(function () {
    angular
        .module('crowdsupport.controller.admin', ['crowdsupport.widget.search', 'restangular', 'crowdsupport.service.status'])
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

            $scope.createState = function (request) {
                Restangular.all('state').post(request.place.city.state).then(function (response) {
                    $log.debug('State successfully created');
                    request.place.city.state = response;
                    request.ui.state = response;
                    request.ui.setStateSearch(true);

                    Status.success('Successfully created new state');
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
        })
        .controller('UserManagementController', function ($scope, $log, $allRoles, Restangular, Status, Auth, $rootScope) {
            $scope.allRoles = $allRoles;
            $scope.editUser = {};

            $scope.isSearchValid = function () {
                return typeof $scope.search === 'object';
            };

            $scope.save = function () {
                console.log($scope.editUser);
                $scope.editUser.patch($scope.editUser).then(function (response) {
                    Status.success('Successfully edited user');

                    // edited your own user?
                    if ($scope.search.username == $rootScope.user.username) {
                        // kept username the same?
                        if ($scope.search.username == response.username) {
                            Auth.updateUser();
                        } else {
                            Auth.logout();
                            Status.info('You\'ve changed your username to ' + response.username + ', please relogin')
                        }
                    }
                });
            };
        })
        .controller('RoleManagementController', function ($roles, $permissions, $scope, $log, Restangular, Status, Auth, $uibModal) {
            $scope.allRoles = $roles;
            $scope.allPermissions = $permissions;

            $scope.selectedRole = $scope.allRoles[0];

            $scope.addRole = function () {
                $uibModal.open({
                    animation: true,
                    templateUrl: 'roleCreateModal.html',
                    controller: 'RoleCreateController'
                }).result.then(function () {
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
                console.log($scope.selectedRole);
                Restangular.one('role', $scope.selectedRole.name)
                Restangular.one('role', $scope.selectedRole.name).customPUT($scope.selectedRole.permissions, 'permissions').then(function () {
                    Status.success('Successfully changed permissions');
                    Auth.updateUser();
                });
            };
        })
        .controller('RoleCreateController', function ($scope, Restangular, Status, $uibModalInstance) {
            $scope.create = function () {
                Restangular.one('role', $scope.roleName).put().then(function (response) {
                    Status.success('Successfully created role ' + $scope.roleName);
                    $uibModalInstance.close(response);
                });
            };
        })
        .controller('SettingsController', function ($scope, Restangular, Status, Auth) {
            $scope.refreshToken = function () {
                Restangular.one('setting', 'refreshApplicationSecret').post().then(function () {
                    Auth.logout();
                    Status.success('Refreshed application token - you have to login again!');
                });
            };
        });
})();