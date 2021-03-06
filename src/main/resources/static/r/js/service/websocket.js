(function () {
    angular
        .module('crowdsupport.service.websocket', [])
        .service('Websocket', function ($q, $timeout, $log) {
            var that = this;

            var SOCKET_URL = '/ws';
            var TOPIC_PREFIX = '/topic/';
            var BROKER_PREFIX = '/app/';

            var RECONNECT_TIMEOUT = 30000;

            var stomp = Stomp.over(new SockJS(SOCKET_URL));

            var started = false;
            this.ready = false;

            stomp.onclose = function () {
                $log.debug('Connection lost...');

                topics.forEach(function (topic) {
                    topic.deferred.reject(false);
                });
                topics = [];

                started = false;
                that.ready = false;

                $timeout(that.connect, RECONNECT_TIMEOUT);
            };
            stomp.debug = $log.debug;

            var topics = [];
            var sendBuffer = [];

            var getTopic = function (address) {
                var t = $.grep(topics, function (topic) {
                    return topic.address == address;
                });

                if (t.length != 0) {
                    return t[0];
                } else {
                    var newT = {
                        address: address,
                        subscription: null,
                        bufferedSubscription: null,
                        deferred: $q.defer(),
                        semaphore: 0
                    };
                    topics.push(newT);

                    return newT;
                }
            };

            var connectIfNeeded = function () {
                if (!started) {
                    that.connect();
                }
            };

            this.SubscriptionRegister = function() {
                var addresses = [];
                var register = this;

                this.addSubscription = function (address) {
                    if (addresses.indexOf(address) === -1) {
                        addresses.push(address);
                        return true;
                    } else {
                        return false;
                    }
                };

                this.releaseAll = function () {
                    addresses.forEach(function (address) {
                        that.release(address);
                    });
                    addresses = [];
                };

                this.releaseAllOnStateChange = function ($scope) {
                    $scope.$on('$stateChangeStart', register.releaseAll);
                };
            };

            this.send = function (broker, data) {
                var url = BROKER_PREFIX + broker;
                connectIfNeeded();
                var sending = function () {
                    $log.debug('Sending message to ' + url);
                    $log.debug(data);
                    stomp.send(BROKER_PREFIX + broker, {priority: 9}, JSON.stringify(data));
                };

                if (that.ready) {
                    sending();
                } else {
                    $log.debug('Websocket not ready yet... buffering message to ' + url);
                    sendBuffer.push(sending);
                }
            };

            this.release = function (address) {
                var url = TOPIC_PREFIX + address;
                $log.debug('Releasing ' + url + '...');

                var t = $.grep(topics, function (topic) {
                    return topic.address == url;
                });

                if (t.length != 0) {
                    var topic = t[0];
                    if (--topic.semaphore <= 0) {
                        $log.debug('Last release for ' + url);
                        topic.subscription.unsubscribe();
                        topic.deferred.reject(true);

                        t = $.grep(topics, function (topic) {
                            return topic.address != url;
                        });
                        topics = t;
                    }
                }
            };

            this.when = function (address, subscriptionRegister) {
                connectIfNeeded();
                var url = TOPIC_PREFIX + address;
                var topic = getTopic(url);

                var subscribe = function () {
                    $log.debug('Subscribing to ' + url);
                    topic.subscription = stomp.subscribe(url, function (data) {
                        $log.debug('Received data from ' + url);
                        $log.debug(data);
                        topic.deferred.notify(JSON.parse(data.body));
                    });
                };

                if (topic.subscription === null) {
                    if (that.ready) {
                        subscribe();
                    } else if (topic.bufferedSubscription === null) {
                        $log.debug('Websocket not ready yet... buffering subscription for ' + url);
                        topic.bufferedSubscription = subscribe;
                    }
                } else {
                    $log.debug('Found existing subscription for ' + url);
                }

                if (subscriptionRegister && subscriptionRegister.addSubscription(address)) {
                    topic.semaphore++;
                }
                return topic.deferred.promise;
            };

            this.connect = function () {
                $log.debug('Starting Websocket...');
                started = true;

                stomp.connect({}, function () {
                    that.ready = true;
                    $log.debug('...Websocket ready');

                    $log.debug('Clearing subscription buffer...');
                    topics.forEach(function (topic) {
                        if (topic.bufferedSubscription !== null) {
                            topic.bufferedSubscription();
                            topic.bufferedSubscription = null;
                        }
                    });
                    $log.debug('...subscription buffer cleared');

                    $log.debug('Clearing message buffer');
                    sendBuffer.forEach(function (sendMessage) {
                        sendMessage();
                    });
                    sendBuffer.length = 0;
                    $log.debug('Message buffer cleared');
                });
            };

            this.disconnect = function () {
                that.ready = false;
                $log.debug('Disconnecting Websocket...');

                stomp.disconnect(function () {
                    started = false;
                    topics.forEach(function (topic) {
                        topic.deferred.reject(true);
                    });
                    topics = [];
                    $log.debug('...Websocket disconnected');
                });
            };
        });
})();