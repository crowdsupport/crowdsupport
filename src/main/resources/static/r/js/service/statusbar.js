(function() {
    angular
        .module('crowdsupport.service.status', [])
        .service('Status', function($q, $log, $mdToast) {
            var defaultTemplate = function(status) {
                var icon;
                var iconify = function(sign) {
                    return '<md-icon flex="none" md-font-set="fa" md-font-icon="fa-' + sign + '"></md-icon>&nbsp;';
                };

                switch (status.type) {
                    case 'SUCCESS':
                        icon = iconify('check');
                        break;
                    case 'INFO':
                        icon = iconify('info');
                        break;
                    case 'ERROR':
                        icon = iconify('times');
                        break;
                    default:
                        icon = '';
                        break;
                }

                return icon + '<span flex class="status-message">' + status.message + '</span>';
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

            // default listener
            this.statusPromise.then(null, null, function(newStatus) {
                $mdToast.show({
                    template: '<md-toast layout>' + newStatus.defaultHtml + '</md-toast>',
                    position: 'bottom'
                });
            });
        });
})();