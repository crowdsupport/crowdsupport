(function () {
    angular
        .module('crowdsupport.controller', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.controller.admin', 'crowdsupport.controller.donationrequests', 'crowdsupport.widget.search',
            'crowdsupport.service.status', 'crowdsupport.service.auth', 'crowdsupport.service.previousstate', 'ui.bootstrap',
            'ui.bootstrap.datetimepicker', 'restangular', 'ngAnimate'])
        .controller('WelcomeController', function ($scope, $states) {
            $scope.states = $states;
        })
        .controller('StateController', function ($scope, $stateRest) {
            $scope.state = $stateRest;
        })
        .controller('CityController', function ($scope, $cityRest) {
            $scope.city = $cityRest;
        })
        .controller('UserController', function ($scope, $rootScope, Auth, Status) {
            $scope.username = '';
            $scope.password = '';

            $scope.login = function () {
                Auth.login($scope.username, $scope.password).then(function (response) {
                    $scope.username = '';
                    Status.success('Successfully logged in');
                }, function (response) {
                    Status.error('Could not log in - are your credentials correct?');
                });

                $scope.password = '';
            };

            $scope.logout = Auth.logout;
        })
        .controller('PlaceRequestCtrl', function ($scope, Restangular, $previousState, Status) {
            $scope.city = {};
            $scope.name = '';
            $scope.identifier = '';
            $scope.location = '';

            $scope.cityname = '';
            $scope.statename = '';

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

                Restangular.all('placeRequest').post(formData).then(function () {
                    Status.success('Successfully requested place');
                    $previousState.backOrHome();
                }, function () {
                    Status.error('Error while requesting place');
                });
            };
        })
        .controller('ProfileController', function ($scope, $user, Auth, Restangular, $log, Status) {
            $scope.user = {};
            $scope.user.username = $user.username;
            $scope.user.email = $user.email;

            $scope.submit = function () {
                $log.debug('Submitting profile data');
                Restangular.one('user', 'current').patch($scope.user).then(function () {
                    Status.success('Successfully updated new user');
                    Auth.updateUser();
                });
            };
        })
        .controller('RegistrationController', function ($scope, Auth, Restangular, Status, $previousState) {
            $scope.user = {};

            $scope.register = function () {
                Restangular.all('user').post($scope.user).then(function () {
                    var e = angular.element('#login .password');
                    e.scope().username = $scope.user.username;
                    e.focus();

                    $previousState.backOrHome();

                    Status.success('Successfully registered new user');
                });
            };
        });
})();