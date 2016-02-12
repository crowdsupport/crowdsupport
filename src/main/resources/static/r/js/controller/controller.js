(function () {
    angular
        .module('crowdsupport.controller', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.controller.admin', 'crowdsupport.controller.donationrequests', 'crowdsupport.widget.search',
            'crowdsupport.service.status', 'crowdsupport.service.auth', 'crowdsupport.service.previousstate', 'ui.bootstrap',
            'ui.bootstrap.datetimepicker', 'restangular', 'ngAnimate', 'chart.js'])
        .controller('DashboardController', function ($scope, $statistics, Websocket) {
            $scope.states = $statistics.states;
            $scope.cities = $statistics.cities;
            $scope.places = $statistics.places;

            $scope.pieLabels = ['Open', 'Closed'];
            $scope.pieData = [$statistics.openRequests, $statistics.closedRequests];
            $scope.pieColours = ['#e6e6e6', '#5cb85c'];
            $scope.pieOptions = {
                legendTemplate :
                    '<ul class="<%=name.toLowerCase()%>-legend"><% for (var i=segments.length-1;i>=0;i--){%><li><span style="background-color:<%=segments[i].fillColor%>"></span><%if(segments[i].label){%><%=segments[i].label%> ... <%=segments[i].value%><%}%></li><%}%></ul>'
            };

            $scope.totalUsers = $statistics.totalUsers;
            $scope.newVisitors = 0;

            var subReg = new Websocket.SubscriptionRegister();
            subReg.releaseAllOnStateChange($scope);

            Websocket.when('indexRequested', subReg).then(null, null, function () {
                $scope.newVisitors++;
            });
            Websocket.when('states/quantity', subReg).then(null, null, function (evt) {
                $scope.states += evt.payload;
            });
            Websocket.when('cities/quantity', subReg).then(null, null, function (evt) {
                $scope.cities += evt.payload;
            });
            Websocket.when('places/quantity', subReg).then(null, null, function (evt) {
                $scope.places += evt.payload;
            });
            Websocket.when('users/quantity', subReg).then(null, null, function (evt) {
                $scope.totalUsers += evt.payload;
            });
            Websocket.when('donationRequests/open', subReg).then(null, null, function (evt) {
                $scope.pieData[0] += evt.payload;
            });
            Websocket.when('donationRequests/closed', subReg).then(null, null, function (evt) {
                $scope.pieData[1] += evt.payload;
            });
            Websocket.when('donationRequests/confirmed', subReg).then(null, null, function (evt) {
                $scope.pieData[1] += evt.payload;
                $scope.pieData[0] -= evt.payload;
            });
        })
        .controller('StatesController', function ($scope, $states) {
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
            $scope.city = null;
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

            $scope.queryCity = function (query) {
                return Restangular.all('city').getList({query: query});
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
                    Status.success('Successfully updated user');
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