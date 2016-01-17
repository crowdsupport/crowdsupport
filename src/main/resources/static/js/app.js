(function () {
    var app = angular.module("crowdsupport", ["timeAgo", "crowdsupport.service.websocket", "crowdsupport.service.config",
        "crowdsupport.admin", "crowdsupport.widget.search", "crowdsupport.service.rest"]);

    app.directive("donationRequests", function () {
        return {
            restrict: 'E',
            templateUrl: '/template/donation-requests.html',
            controller: function ($scope, Rest) {
                $scope.donationRequests = Rest.DonationRequest.query();
            },
            controllerAs: 'ctrl'
        }
    });

    app.controller("LoginCtrl", function ($scope, $rootScope) {
        $rootScope.user = null;

        $scope.setUser = function (userJson) {
            $rootScope.user = angular.fromJson(userJson);
        };
    });

    app.controller("DonationRequestCtrl", function ($scope, Websocket) {
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
    });

    app.controller("PlaceRequestCtrl", function ($scope, Rest) {
        $scope.city = {};
        $scope.name = "";
        $scope.identifier = "";
        $scope.location = "";

        $scope.cityname = "";
        $scope.statename = "";

        $scope.addCity = false;

        $scope.addCityInfo = function() {
            $scope.cityname = $scope.city;
            $scope.addCity = true;
        };

        $scope.searchCity = function() {
            $scope.addCity = false;
        }

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

            Rest.Place.Request.save(formData, function () {
                alert("Success");
            }, function () {
                alert("Oh oh");
            });
        };
    });
})();