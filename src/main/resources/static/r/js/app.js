(function () {
    angular
        .module('crowdsupport', ['crowdsupport.routing', 'angular-jwt'])
        .config(function Config($httpProvider, jwtInterceptorProvider) {
            jwtInterceptorProvider.tokenGetter = function(config) {
                // Skip authentication for any requests ending in .html
                if (isResourceUrl(config.url)) {
                    return null;
                }

                return localStorage.getItem('token');
            };

            $httpProvider.interceptors.push('jwtInterceptor');
        })
        .config(function(RestangularProvider) {
            RestangularProvider.setBaseUrl('/service/v2');
            RestangularProvider.setRestangularFields({
                selfLink: 'links[0].href'
            });
        });
})();