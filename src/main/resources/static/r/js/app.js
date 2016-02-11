(function () {
    angular
        .module('crowdsupport', ['crowdsupport.routing', 'angular-jwt', 'ngMaterial'])
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
            RestangularProvider.setBaseUrl('/service/v1');
            RestangularProvider.setRestangularFields({
                selfLink: 'links[0].href'
            });
        })
        .config(function($mdThemingProvider, $mdIconProvider) {
            $mdThemingProvider.theme('default')
                .primaryPalette('blue-grey')
                .accentPalette('light-blue')
                .backgroundPalette('grey', {
                    'default': '50'
                });

            $mdIconProvider.fontSet('fa', 'fa');
        })
        .filter('posNum', function () {
            return function (input) {
                return input > 0 ? input : 0;
            };
        })
        .controller('MainController', function($scope, $mdSidenav, $mdMedia) {
            $scope.$mdMedia = $mdMedia;

            $scope.toggleNav = function () {
                $mdSidenav('sidebar').toggle();
            };
        });
})();