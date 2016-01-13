(function () {
    angular.module("crowdsupport.service.websocket", [])
        .service("Websocket", function ($q, $timeout, $log) {
            var that = this;

            var SOCKET_URL = "/ws";
            var TOPIC_PREFIX = "/topic/";
            var BROKER_PREFIX = "/app/";

            var RECONNECT_TIMEOUT = 30000;

            var client = new SockJS(SOCKET_URL);
            var stomp = Stomp.over(client);
            stomp.onclose = function () {
                $timeout(function () {
                    $log.debug("Connection lost...");
                    that.connect();
                }, RECONNECT_TIMEOUT);
            };

            var started = false;
            this.ready = false;

            var topics = [];
            var sendBuffer = [];

            var getTopic = function (address) {
                var t = $.grep(topics, function (topic, i) {
                    return topic.address == address;
                });

                if (t.length != 0) {
                    return t[0];
                } else {
                    var newT = {
                        address: address,
                        subscription: null,
                        bufferedSubscription: null,
                        deferred: $q.defer()
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

            this.send = function (broker, data) {
                var url = BROKER_PREFIX + broker;
                connectIfNeeded();
                var sending = function() {
                    $log.debug("Sending message to " + url);
                    $log.debug(data);
                    stomp.send(BROKER_PREFIX + broker, {priority: 9}, JSON.stringify(data));
                };

                if (that.ready) {
                    sending();
                } else {
                    $log.debug("Websocket not ready yet... buffering message to " + url);
                    $log.debug(data);
                    sendBuffer.push(sending);
                }
            };

            this.when = function (address) {
                connectIfNeeded();
                var url = TOPIC_PREFIX + address;
                var topic = getTopic(url);

                var subscribe = function () {
                    $log.debug("Subscribing to " + url);
                    topic.subscription = stomp.subscribe(url, function (data) {
                        $log.debug("Received data from " + url);
                        $log.debug(data);
                        topic.deferred.notify(JSON.parse(data.body));
                    });
                };

                if (topic.subscription === null) {
                    if (that.ready) {
                        subscribe();
                    } else if (topic.bufferedSubscription === null) {
                        $log.debug("Websocket not ready yet... buffering subscription for " + url);
                        topic.bufferedSubscription = subscribe;
                    }
                }

                return topic.deferred.promise;
            };

            this.connect = function () {
                $log.debug("Starting Websocket...");
                started = true;

                stomp.connect({}, function () {
                    that.ready = true;
                    $log.debug("...Websocket ready");

                    $log.debug("Clearing subscription buffer...");
                    topics.forEach(function (topic) {
                        if (topic.bufferedSubscription !== null) {
                            topic.bufferedSubscription();
                            topic.bufferedSubscription = null;
                        }
                    });
                    $log.debug("...subscription buffer cleared");

                    $log.debug("Clearing message buffer");
                    sendBuffer.forEach(function(sendMessage) {
                        sendMessage();
                    });
                    sendBuffer.length = 0;
                    $log.debug("Message buffer cleared");
                });
            };

            this.disconnect = function () {
                that.ready = false;
                $log.debug("Disconnecting Websocket...");
                stomp.disconnect(function () {
                    started = false;
                    $log.debug("...Websocket disconnected");
                });
            };
        });
})();