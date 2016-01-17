(function() {
    angular
        .module("crowdsupport.service.statusbar", [])
        .service("StatusService", function($q, $log) {
            var defaultTemplate = function(status) {
                var glyph;
                var glyphify = function(sign) {
                    return '<span class="glyphicon glyphicon-' + sign + '" aria-hidden="true"></span> ';
                };

                switch (status.type) {
                    case "SUCCESS":
                        glyph = glyphify("ok-sign");
                        break;
                    case "INFO":
                        glyph = glyphify("info-sign");
                        break;
                    case "ERROR":
                        glyph = glyphify("remove-sign");
                        break;
                    default:
                        glyph = "";
                        break;
                }

                return '<div class="message">' + glyph + status.message + '</div>';
            };

            var pastStatus = [];
            var currentStatus = {};

            var deferred = $q.defer();

            this.statusPromise = deferred.promise;

            this.newStatus = function(newStatus) {
                $log.debug("New status: " + JSON.stringify(newStatus));

                newStatus.defaultHtml = defaultTemplate(newStatus);

                pastStatus.push(currentStatus);
                currentStatus = newStatus;
                deferred.notify(newStatus);
            };
        })
        .directive('statusbar', function() {
            return {
                restrict: 'C',
                controller: function(StatusService) {
                    StatusService.statusPromise.then(null, null, function(newStatus) {
                        $('.statusbar .message').last().addClass('old');
                        $('.statusbar').append(newStatus.defaultHtml);
                    });
                }
            }
        });
})();