(function () {
    angular
        .module('crowdsupport.service.auth', ['crowdsupport.service.rest', 'angular-jwt', 'crowdsupport.service.status'])
        .service('Auth', function (Rest, $rootScope, $log, jwtHelper) {
            var retrieveUser = function () {
                $log.debug("Retrieving user...");
                return Rest.User.get({}, function (response) {
                    $rootScope.auth = true;
                    $log.debug("...retrieved user");
                    $log.debug(response);
                });
            };

            var initUser = function () {
                var token = localStorage.getItem('token');
                var user = null;
                if (token !== null) {
                    if (!jwtHelper.isTokenExpired(token)) {
                        $log.debug("Found non expired token!");
                        $log.debug(jwtHelper.decodeToken(token));
                        $log.debug(jwtHelper.getTokenExpirationDate(token));
                        return retrieveUser();
                    } else {
                        Status.info("Session expired. Please log in again!");
                    }
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

                return Rest.User.login({username: username, password: password}, function (response) {
                    localStorage.setItem("token", response.data);

                    $log.debug("...logged in");
                    $rootScope.user = retrieveUser();
                }).$promise;
            };

            this.updateUser = function () {
                $log.debug("Updating user model");
                $rootScope.user = retrieveUser();
                return $rootScope.user;
            };
        });
})();