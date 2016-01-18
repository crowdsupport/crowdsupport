(function () {
    angular
        .module('crowdsupport.service.previousstate', [])
        .service("$previousState", function ($rootScope, $state) {
            var previous = null;
            var memos = {};

            var lastPrevious = null;

            $rootScope.$on("$stateChangeStart", function (evt, toState, toStateParams, fromState, fromStateParams) {
                lastPrevious = previous;
                previous = {state: fromState, params: fromStateParams};
            });

            $rootScope.$on("$stateChangeError", function () {
                previous = lastPrevious;
                lastPrevious = null;
            });

            $rootScope.$on("$stateChangeSuccess", function () {
                lastPrevious = null;
            });

            var $previousState = {
                backOrHome: function() {
                    if (!$previousState.get().state.abstract) {
                        return $previousState.go();
                    } else {
                        return $state.go("welcome");
                    }
                },
                get: function (memoName) {
                    return memoName ? memos[memoName] : previous;
                },
                go: function (memoName) {
                    var to = $previousState.get(memoName);
                    return $state.go(to.state, to.params)
                },
                memo: function (memoName) {
                    memos[memoName] = previous;
                }
            };

            return $previousState;
        })
        .run(function ($previousState) {
            // Inject in .run so it can register $rootScope.$on.
        });
})();