(function () {
    angular
        .module('crowdsupport.controller', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.admin', 'crowdsupport.widget.search',
            'crowdsupport.service.status', 'crowdsupport.service.auth', 'crowdsupport.service.previousstate', 'ui.bootstrap',
            'ui.bootstrap.datetimepicker', 'restangular'])
        .controller('WelcomeController', function ($scope, $states) {
            $scope.states = $states;
        })
        .controller('StateController', function ($scope, $stateRest) {
            $scope.state = $stateRest;
        })
        .controller('CityController', function ($scope, $cityRest) {
            $scope.city = $cityRest;
        })
        .controller('PlaceController', function ($scope, $placeRest, Websocket, $rootScope, $uibModal) {
            $scope.place = $placeRest;

            var identifier = getUrlAfterSupport() + '/comments';

            $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                Websocket.unsubscribe(identifier);
            });

            $scope.inTeam = function () {
                if (!$rootScope.user || !$rootScope.user.managedPlaces) {
                    return false;
                }

                return $rootScope.user.managedPlaces.findIndex(function (p) {
                        return p.identifier === $scope.place.identifier
                            && p.city.identifier === $scope.place.city.identifier
                            && p.city.state.identifier === $scope.place.city.state.identifier;
                    }) >= 0;
            };

            $scope.addRequest = function () {
                $uibModal.open({
                    animation: true,
                    templateUrl: '/r/template/addDonationRequest.html',
                    controller: 'AddDonationRequestController',
                    scope: $scope
                });
            }
        })
        .controller('AddDonationRequestController', function ($scope, Restangular, Status, $uibModalInstance, $timeout) {
            $scope.request = {};
            $scope.neverExpires = true;
            $scope.minDate = new Date();
            $scope.date = new Date();

            var place = $scope.$parent.place;

            $scope.create = function () {
                var r = $scope.request;
                if (!neverExpires) {
                    r.validTo = $scope.date;
                }
                r.active = true;

                console.log(place);
                place.post('donationRequests', r).then(function () {
                    Status.success("Successfully created new donation request");

                    $uibModalInstance.close($scope.request);
                });
            };

            $scope.openDatePicker = function () {
                $timeout(function () {
                    $scope.opened = true;
                });
            };

            $scope.timeOptions = {
                readonlyInput: false,
                showMeridian: false
            };
        })
        .controller('PlaceManagementController', function ($scope, $placeRest, $members, Restangular, Status) {
            $scope.place = $placeRest;
            $scope.members = $members;

            var place = $scope.place;

            $scope.setActiveTab = function (initial) {
                var activeClass = 'active';

                var hash = window.location.hash;
                var tabId;

                if (hash) {
                    tabId = hash.substr(1);
                } else {
                    hash = '#' + initial;
                    tabId = initial;
                }

                var liElement = $('.place-tabs li:has(a[href=' + hash + '])');
                if (liElement.length === 1) {
                    liElement.addClass(activeClass);
                    $('.tab-content #' + tabId).addClass(activeClass);
                } else {
                    $('.place-tabs li:has(a[href=#' + initial + '])').addClass(activeClass);
                    $('.tab-content #' + initial).addClass(activeClass);
                }
            };

            $scope.isSearchValid = function () {
                if (typeof $scope.search !== 'object') {
                    return false;
                }

                return $scope.members.findIndex(function (m) {
                        return m.username === $scope.search.username;
                    }) === -1;
            };

            $scope.addMember = function () {
                place.one('team', $scope.search.username).put().then(function () {
                    Status.success('Successfully added ' + $scope.search.username + ' to team');

                    $scope.members.push($scope.search);
                });
            };

            $scope.removeMember = function (username) {
                place.one('team', username).remove().then(function () {
                    Status.success('Successfully deleted ' + username + ' from team');

                    $scope.members = $.grep($scope.members, function (e) {
                        return e.username !== username;
                    });
                });
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
        .controller('DonationRequestCtrl', function ($scope, Websocket, Restangular, Status) {
            var that = this;
            var donationRequest = {};
            var place = $scope.$parent.place;

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

                Restangular.one('donationRequest', that.donationRequest.id).post('comments', commentDto)
                    .then(function () {
                        Status.success('Successfully created new comment');
                    });

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