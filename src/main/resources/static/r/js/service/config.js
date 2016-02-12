(function () {
    var KEY = 'debugEnabled';

    angular
        .module('crowdsupport.service.config', [])
        .config(function ($provide, $logProvider) {
            // AngularJS has debug enabled by default, but just to be sure...
            $logProvider.debugEnabled(true);

            // Disabling localStorageDebug (if not set)
            if (localStorage.getItem(KEY) === null) {
                localStorage.setItem(KEY, 'false');
            }

            // add a check for localStorageDebug before actually calling $log.debug(...)
            $provide.decorator('$log', function ($delegate) {
                var debugFunction = $delegate.debug;

                $delegate.debug = function () {
                    if (localStorage.getItem(KEY) === 'true') {
                        debugFunction.apply(undefined, arguments);
                    }
                };

                return $delegate;
            });
        })
        .config(function ($compileProvider) {
            $compileProvider.debugInfoEnabled(localStorage.getItem(KEY) === 'true');
        })
        .service('ConfigService', function ($log) {
            this.debugEnabled = function (flag) {
                if (arguments.length === 1) {
                    $log.info('Setting debugEnabled to ' + flag);
                    localStorage.setItem(KEY, flag.toString());
                } else {
                    return localStorage.getItem(KEY) === 'true';
                }
            }
        });
})();

// exposing ConfigService to global scope (be aware of possible clashes!),
// therefore making it easily accessible from the console
var debug = {
    getAngular: function (item) {
        return angular.element(document.body).injector().get(item);
    },
    status: {
        set: function(type, text) {
            debug.getAngular('Status').newStatus({type: type, message: text});
        },
        success: function (text) {
            debug.getAngular('Status').newStatus({type: 'SUCCESS', message: text});
        },
        info: function (text) {
            debug.getAngular('Status').newStatus({type: 'INFO', message: text});
        },
        error: function (text) {
            debug.getAngular('Status').newStatus({type: 'ERROR', message: text});
        }
    },
    user: function() {
        return debug.getAngular('$rootScope').user;
    },
    refresh: function(force) {
        document.location.reload(force || false);
    },
    config: function () {
        return debug.getAngular('ConfigService');
    }
};

