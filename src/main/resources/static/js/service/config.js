(function () {
    var KEY = "debugEnabled";

    angular.module("crowdsupport.service.config", [])
        .config(function ($provide) {
            if (localStorage.getItem(KEY) === null) {
                localStorage.setItem(KEY, "false");
            }

            $provide.decorator('$log', function ($delegate) {
                var debugFunction = $delegate.debug;

                $delegate.debug = function () {
                    if (localStorage.getItem(KEY) !== "false") {
                        debugFunction.apply(null, [].slice.call(arguments))
                    }
                };

                return $delegate;
            });
        })
        .service("Config", function ($log) {
            this.debugEnabled = function (flag) {
                $log.info("Setting debugEnabled to " + flag);
                localStorage.setItem(KEY, flag.toString());
            }
        });
})();

var config;
window.onload = function () {
    config = angular.element(document.body).injector().get("Config");
};