(function () {
    var SERVICE_PREFIX = '/service/v1';

    angular
        .module('crowdsupport.service.rest', ['ngResource'])
        .service('Rest', function($resource) {
            return {
                User: $resource(SERVICE_PREFIX + '/user/current', {}, {
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