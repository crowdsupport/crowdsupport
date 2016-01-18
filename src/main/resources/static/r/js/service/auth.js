(function () {
    angular
        .module('crowdsupport.service.auth', ['crowdsupport.service.rest', 'angular-jwt', 'crowdsupport.service.statusbar'])
        .service('Auth', function (Rest, $rootScope, $log, jwtHelper, StatusService) {
            var retrieveUser = function () {
                $log.debug("Retrieving user...");
                return Rest.User.get({}, function () {
                    $rootScope.auth = true;
                    $log.debug("...retrieved user");
                });
            };

            var initUser = function () {
                var token = localStorage.getItem('token');
                var user = null;
                if (token !== null && !jwtHelper.isTokenExpired(token)) {
                    return retrieveUser();
                }

                return user;
            };

            $rootScope.user = initUser();
            $rootScope.auth = false;

            this.logout = function () {
                localStorage.removeItem("token");
                $rootScope.user = null;
                $rootScope.auth = false;
            };

            this.login = function (username, password) {
                $log.debug("Logging in...");

                Rest.User.login({username: username, password: password}, function (response) {
                    localStorage.setItem("token", response.data);

                    $log.debug("...logged in");
                    StatusService.success("Login successful");
                    $rootScope.user = retrieveUser();
                });
            };

            this.updateUser = function () {
                $log.debug("Updating user model");
                $rootScope.user = retrieveUser();
                return $rootScope.user;
            };
        });
})();