(function () {
    var app = angular.module("crowdsupport", ["timeAgo", "crowdsupport.services"]);

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

    app.controller("DonationRequestCtrl", ["$scope", "CommentService", function ($scope, CommentService) {
        var that = this;
        var donationRequest = {};

        $scope.comment = "";

        this.init = function(donationRequest) {
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

            if (that.donationRequest.id == message.donationRequestId) {
                that.donationRequest.comments.push(message);
            }
        });
    }]);
})();