(function () {
    var SERVICE_PREFIX = "/service/v1";

    angular
        .module("crowdsupport.service.rest", ["ngResource"])
        .service("Rest", function($resource) {
            return {
                State: $resource(SERVICE_PREFIX + "/state/:identifier"),
                City: $resource(SERVICE_PREFIX + "/city/:identifier"),
                Place: $resource(SERVICE_PREFIX + "/place/:identifier"),
                PlaceRequest: {
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
                DonationRequest: $resource(SERVICE_PREFIX + "/:sIdt/:cIdt/:pIdt/donationRequests")
            };
        });
})();