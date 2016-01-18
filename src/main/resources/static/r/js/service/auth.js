(function() {
    angular
        .module('crowdsupport.service.auth', ['crowdsupport.service.rest', 'angular-jwt'])
        .service('Auth', function(Rest, $rootScope, $log, jwtHelper) {
            $rootScope.user = null;
            $rootScope.auth = false;

            this.logout = function () {
                localStorage.removeItem("token");
                $rootScope.auth = false;
            };

            this.login = function (username, password) {
                $log.debug("Logging in...");

                Rest.User.login({username: username, password: password}, function(response) {
                    localStorage.setItem("token", response.data);

                    $log.debug("...logged in");
                    retrieveUser();
                });
            };

            var retrieveUser = function() {
                $log.debug("Retrieving user...");
                $rootScope.user = Rest.User.get({}, function() {
                    $rootScope.auth = true;
                    $log.debug("...retrieved user");
                    $log.debug($rootScope.user);
                });
            };

            (function() {
                var token = localStorage.getItem('token');
                if (token !== null && !jwtHelper.isTokenExpired(token)) {
                    retrieveUser();
                }
            })();
        });
})();