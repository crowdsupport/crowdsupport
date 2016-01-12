(function () {
    var app = angular.module("crowdsupport", ["timeAgo", "crowdsupport.services", "crowdsupport.admin"]);

    var SERVICE_PREFIX = "/service/v1/";

    app.directive("donationRequests", function () {
        return {
            restrict: 'E',
            templateUrl: '/template/donation-requests.html',
            controller: ["$scope", "$http", function ($scope, $http) {
                var that = this;
                $scope.donationRequests = [];


                this.identifier = (function () {
                    var url = window.location.href;
                    var position = url.search(/support\//) + 8;
                    return url.substring(position);
                })();

                $http.get(SERVICE_PREFIX + this.identifier + "/donationRequests").success(function (data) {
                    that.donationRequests = data;
                });


            }],
            controllerAs: 'ctrl'
        }
    });

    app.controller("LoginCtrl", function ($scope, $rootScope) {
        $rootScope.user = null;

        $scope.setUser = function (userJson) {
            $rootScope.user = angular.fromJson(userJson);
        };
    });

    app.controller("DonationRequestCtrl", function ($scope, CommentService) {
        var that = this;
        var donationRequest = {};

        $scope.comment = "";

        this.init = function (donationRequest) {
            that.donationRequest = donationRequest;
        };

        this.addComment = function () {
            console.log("Comment for request " + that.donationRequest.id + ": " + $scope.comment);

            var commentDto = {
                "text": $scope.comment,
                "quantity": 0,
                "donationRequestId": that.donationRequest.id
            };

            CommentService.send(commentDto);

            $scope.comment = "";
        };

        CommentService.receive().then(null, null, function (message) {
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

    app.controller("PlaceRequestCtrl", function ($scope, $http) {
        $scope.city = {};
        $scope.name = "";
        $scope.identifier = "";
        $scope.location = "";

        $scope.cityname = "";
        $scope.statename = "";

        $scope.addCity = false;

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

            var url = "/service/v1/place/request";

            $http.post(url, formData).then(function(response) {
                alert("Success");
            }, function(response) {
                alert("Oh oh");
            });
        };

        $('#cty.typeahead').bind('typeahead:select', function (ev, suggestion) {
            $scope.city = suggestion;
            console.log('Selection: ' + JSON.stringify($scope.city));
        });
    });
})();