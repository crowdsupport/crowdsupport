(function() {
    angular
        .module('crowdsupport.service.status', [])
        .service('Status', function($q, $log) {
            var defaultTemplate = function(status) {
                var glyph;
                var glyphify = function(sign) {
                    return '<span class="glyphicon glyphicon-' + sign + '" aria-hidden="true"></span> ';
                };

                switch (status.type) {
                    case 'SUCCESS':
                        glyph = glyphify('ok-sign');
                        break;
                    case 'INFO':
                        glyph = glyphify('info-sign');
                        break;
                    case 'ERROR':
                        glyph = glyphify('remove-sign');
                        break;
                    default:
                        glyph = '';
                        break;
                }

                return '<div class="message">' + glyph + status.message + '</div>';
            };

            var pastStatus = [];
            this.currentStatus = {};

            var deferred = $q.defer();

            this.statusPromise = deferred.promise;

            this.success = function(message) {
                this.newStatus({type: "SUCCESS", message: message});
            };

            this.error = function(message) {
                this.newStatus({type: "ERROR", message: message});
            };

            this.info = function(message) {
                this.newStatus({type: "INFO", message: message});
            };

            this.newStatus = function(newStatus) {
                $log.debug('New status: ' + JSON.stringify(newStatus));

                newStatus.defaultHtml = defaultTemplate(newStatus);

                pastStatus.push(this.currentStatus);
                this.currentStatus = newStatus;
                deferred.notify(newStatus);
            };
        })
        .directive('statusbar', function() {
            return {
                restrict: 'C',
                controller: function(Status) {
                    var addStatus = function(newStatus) {
                        $('.statusbar .message').last().addClass('old');
                        $('.statusbar').append(newStatus.defaultHtml);
                    };

                    Status.statusPromise.then(null, null, addStatus);

                    if (!nullOrEmpty(Status.currentStatus)) {
                        addStatus(Status.currentStatus);
                    }
                }
            }
        });
})();