(function () {
    var SERVICE_PREFIX = "/service/v1";

    var parseUrl = function() {
        var url = window.location.href;
        var position = url.search(/support\//) + 8;

        var afterSupport = url.substring(position);
        var match = afterSupport.match(/([^\/]+)\/([^\/]+)\/([^\/]+)/);

        return {
            stateIdentifier: match[1],
            cityIdentifier: match[2],
            placeIdentifier: match[3]
        };
    };


    angular.module("crowdsupport.service.rest", ["ngResource"])
        .service("Rest", function($resource) {
            return {
                State: $resource(SERVICE_PREFIX + "/state"),
                City: $resource(SERVICE_PREFIX + "/city"),
                Place: {
                    Request: $resource(SERVICE_PREFIX + "/place/request", {}, {
                        decline: {
                            method: "POST",
                            url: SERVICE_PREFIX + "/place/request/:requestId/decline",
                            params: {
                                requestId: "@id"
                            }
                        },
                        accept: {
                            method: "POST",
                            url: SERVICE_PREFIX + "/place"
                        }
                    })
                },
                DonationRequest: $resource(SERVICE_PREFIX + "/:sIdt/:cIdt/:pIdt/donationRequests",
                    {sIdt: parseUrl().stateIdentifier, cIdt: parseUrl().cityIdentifier, pIdt: parseUrl().placeIdentifier})
            };
        });
})();