(function () {
    angular
        .module('crowdsupport.controller', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.admin', 'crowdsupport.widget.search', 'crowdsupport.service.rest',
            'crowdsupport.service.status', 'crowdsupport.service.auth', 'crowdsupport.service.previousstate'])
        .controller('WelcomeController', function ($scope, Rest) {
            $scope.states = Rest.State.query();
        })
        .controller('StateController', function ($scope, $stateRest) {
            $scope.state = $stateRest;
        })
        .controller('CityController', function ($scope, $cityRest) {
            $scope.city = $cityRest;
        })
        .controller('PlaceController', function ($scope, $placeRest, Websocket, $rootScope) {
            $scope.place = $placeRest;

            var identifier = getUrlAfterSupport() + '/comments';

            $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                Websocket.unsubscribe(identifier);
            });

            $scope.inTeam = function () {
                if (!$rootScope.user || !$rootScope.user.managedPlaces) {
                    return false;
                }

                return $rootScope.user.managedPlaces.findIndex(function(p) {
                        return p.identifier === $scope.place.identifier
                            && p.city.identifier === $scope.place.city.identifier
                            && p.city.state.identifier === $scope.place.city.state.identifier;
                }) >= 0;
            };
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
        .controller('DonationRequestCtrl', function ($scope, Websocket) {
            var that = this;
            var donationRequest = {};

            $scope.comment = '';

            this.init = function (donationRequest) {
                that.donationRequest = donationRequest;
            };

            var identifier = getUrlAfterSupport() + '/comments';

            this.addComment = function () {
                console.log('Comment for request ' + that.donationRequest.id + ': ' + $scope.comment);

                var commentDto = {
                    'text': $scope.comment,
                    'quantity': 0,
                    'donationRequestId': that.donationRequest.id
                };

                Websocket.send(identifier, commentDto);

                $scope.comment = '';
            };

            Websocket.when(identifier).then(null, null, function (message) {
                // TODO the donationRequests controller (the one which should only exist once per site) should register this,
                // and forward the comments to the appropriate donationRequest controller, so not every single donationRequest
                // listens to every single comment update
                if (message.changeType == 'ADD' && message.entity == 'CommentDto') {
                    message = message.payload;
                    if (that.donationRequest.id == message.donationRequestId) {
                        that.donationRequest.comments.push(message);
                    }
                }
            });
        })
        .controller('PlaceRequestCtrl', function ($scope, Rest, $previousState, Status) {
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

                Rest.PlaceRequest.Request.save(formData, function () {
                    Status.success("Successfully requested place");
                    $previousState.backOrHome();
                }, function () {
                    Status.error("Error while requesting place");
                });
            };
        })
        .controller('ProfileController', function ($scope, $user, Auth, Rest, $log, Status) {
            $scope.user = {};
            $scope.user.username = $user.username;
            $scope.user.email = $user.email;

            $scope.submit = function () {
                $log.debug("Submitting profile data");
                Rest.User.update($scope.user, function (response) {
                    Status.newStatus(response);
                    Auth.updateUser();
                });
            };
        })
        .controller('RegistrationController', function ($scope, Auth, Rest, Status, $previousState) {
            $scope.user = {};

            $scope.register = function () {
                Rest.User.create($scope.user, function (response) {
                    var e = angular.element("#login .password");
                    e.scope().username = $scope.user.username;
                    e.focus();

                    $previousState.backOrHome();

                    Status.newStatus(response);
                });
            };
        });
})();