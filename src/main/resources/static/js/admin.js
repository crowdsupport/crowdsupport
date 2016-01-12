(function () {
    var app = angular.module("crowdsupport.admin", ["ui.router"]);

    app.config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
        $locationProvider.html5Mode(true);

        $urlRouterProvider.otherwise(function($injector, $location) {
            // that's ugly, but needed to escape ui.router /o\
            if ($location.$$url.indexOf("admin") <= -1) {
                window.location.reload(true);
            }
            return $location;
        });

        $stateProvider
            .state("overview", {
                url: "/admin",
                templateUrl: "/template/admin/overview.html"
            })
            .state("requestedPlaces", {
                url: "/admin/requestedPlaces",
                templateUrl: "/template/admin/requestedPlaces.html"
            });
    });
})();