(function() {
    angular
        .module('crowdsupport.controller', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.admin', 'crowdsupport.widget.search', 'crowdsupport.service.rest',
            'crowdsupport.service.statusbar'])
        .controller('WelcomeController', function ($scope, Rest) {
            $scope.states = Rest.State.query();
        })
        .controller('StateController', function ($scope, $stateRest) {
            $scope.state = $stateRest;
        })
        .controller('CityController', function ($scope, $cityRest) {
            $scope.city = $cityRest;
        })
        .controller('PlaceController', function ($scope, $placeRest, Websocket) {
            $scope.place = $placeRest;

            var identifier = getUrlAfterSupport() + '/comments';

            $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                Websocket.unsubscribe(identifier);
            });
        })
        .controller('LoginCtrl', function ($scope, $rootScope) {
            $rootScope.user = null;

            $scope.setUser = function (userJson) {
                $rootScope.user = angular.fromJson(userJson);
            };
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
        .controller('PlaceRequestCtrl', function ($scope, Rest) {
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
                    alert('Success');
                }, function () {
                    alert('Oh oh');
                });
            };
        });
})();