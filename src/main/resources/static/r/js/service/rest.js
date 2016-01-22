(function () {
    var SERVICE_PREFIX = '/service/v1';

    angular
        .module('crowdsupport.service.rest', ['ngResource'])
        .service('Rest', function($resource) {
            return {
                State: $resource(SERVICE_PREFIX + '/state/:identifier'),
                City: $resource(SERVICE_PREFIX + '/city/:identifier'),
                Place: $resource(SERVICE_PREFIX + '/place/:identifier', {}),
                PlaceMembers: $resource(SERVICE_PREFIX + '/place/:sIdt/:cIdt/:pIdt/member/:username', {
                    sIdt: '@city.state.identifier',
                    cIdt: '@city.identifier',
                    pIdt: '@identifier',
                    username: '@username'
                }),
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
                DonationRequest: $resource(SERVICE_PREFIX + '/place/:id/donationRequests'),
                User: $resource(SERVICE_PREFIX + '/user/current', {}, {
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
                }),
                Role: $resource(SERVICE_PREFIX + '/role/:name', {name: '@name'}, {
                    assignPermissions: {
                        method: 'POST',
                        url: SERVICE_PREFIX + '/role/:name/assignPermissions',
                        params: {
                            name: '@name'
                        },
                        transformRequest: function(roleDto) {
                            return angular.toJson(roleDto.permissions);
                        },
                        transformResponse: function(response) {
                            var r = angular.fromJson(response);
                            var newResponse = r.data;
                            r.data = null;
                            newResponse.message = r;
                            return newResponse;
                        }
                    }
                }),
                Permission: $resource(SERVICE_PREFIX + '/permission')
            };
        });
})();