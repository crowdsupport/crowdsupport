(function () {
    var app = angular.module("crowdsupport.services", []);

    app.service("CommentService", function($q, $timeout) {

        var service = {}, listener = $q.defer(), socket = {
            client: null,
            stomp: null
        };

        service.RECONNECT_TIMEOUT = 30000;
        service.IDENTIFIER = (function(){
            var url = window.location.href;
            var position = url.search(/support\//) + 8;
            return url.substring(position);
        })();

        service.SOCKET_URL = "/ws";
        service.CHAT_TOPIC = "/topic/" + service.IDENTIFIER + "/comments";
        service.CHAT_BROKER = "/app/" + service.IDENTIFIER + "/comments";

        service.receive = function() {
            return listener.promise;
        };

        service.send = function(comment) {
            socket.stomp.send(service.CHAT_BROKER, {
                priority: 9
            }, JSON.stringify(comment));
        };

        var reconnect = function() {
            $timeout(function() {
                initialize();
            }, this.RECONNECT_TIMEOUT);
        };

        var getComment = function(data) {
            return JSON.parse(data);
        };

        var startListener = function() {
            socket.stomp.subscribe(service.CHAT_TOPIC, function(data) {
                listener.notify(getComment(data.body));
            });
        };

        var initialize = function() {
            socket.client = new SockJS(service.SOCKET_URL);
            socket.stomp = Stomp.over(socket.client);
            socket.stomp.connect({}, startListener);
            socket.stomp.onclose = reconnect;
        };

        initialize();
        return service;
    })
})();