(function () {
    var SERVICE_PREFIX = '/service/v1';

    angular
        .module('crowdsupport.service.rest', ['ngResource'])
        .service('Rest', function($resource) {
            return {
                State: $resource(SERVICE_PREFIX + '/state/:identifier'),
                City: $resource(SERVICE_PREFIX + '/city/:identifier'),
                Place: $resource(SERVICE_PREFIX + '/place/:identifier'),
                PlaceRequest: {
                    Request: $resource(SERVICE_PREFIX + '/place/request', {}, {
                        decline: {
                            method: 'POST',
                            url: SERVICE_PREFIX + '/place/request/:requestId/decline',
                            params: {
                                requestId: '@id'
                            }
                        },
                        accept: {
                            method: 'POST',
                            url: SERVICE_PREFIX + '/place'
                        }
                    })
                },
                DonationRequest: $resource(SERVICE_PREFIX + '/:sIdt/:cIdt/:pIdt/donationRequests'),
                User: $resource(SERVICE_PREFIX + '/user', {}, {
                    create: {
                        method: 'POST',
                        url: SERVICE_PREFIX + '/user'
                    },
                    update: {
                        method: 'PUT',
                        url: SERVICE_PREFIX + '/user/:username',
                        params: {
                            username: '@username'
                        }
                    },
                    login: {
                        method: 'POST',
                        url: SERVICE_PREFIX + '/user/login',
                        transformResponse: function(data, headers){
                            return { data: headers()['authorization'] };
                        }
                    }
                })
            };
        });
})();