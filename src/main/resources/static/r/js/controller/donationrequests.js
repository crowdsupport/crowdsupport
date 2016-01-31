(function () {
    angular
        .module('crowdsupport.controller.donationrequests', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.widget.search',
            'crowdsupport.service.status', 'crowdsupport.service.auth', 'crowdsupport.service.previousstate', 'ui.bootstrap',
            'ui.bootstrap.datetimepicker', 'restangular', 'ngAnimate'])
        .controller('AddDonationRequestController', function ($scope, Restangular, Status, $uibModalInstance, $timeout) {
            $scope.request = {};
            $scope.neverExpires = true;
            $scope.minDate = new Date();
            $scope.date = new Date();

            var place = $scope.$parent.place;

            $scope.create = function () {
                var r = $scope.request;
                if (!neverExpires) {
                    r.validTo = $scope.date;
                }
                r.resolved = false;

                console.log(place);
                place.post('donationRequests', r).then(function () {
                    Status.success("Successfully created new donation request");

                    $uibModalInstance.close($scope.request);
                });
            };

            $scope.openDatePicker = function () {
                $timeout(function () {
                    $scope.opened = true;
                });
            };

            $scope.timeOptions = {
                readonlyInput: false,
                showMeridian: false
            };
        })
        .controller('PlaceManagementController', function ($scope, $placeRest, $members, Restangular, Status) {
            $scope.place = $placeRest;
            $scope.members = $members;

            var place = $scope.place;

            $scope.setActiveTab = function (initial) {
                var activeClass = 'active';

                var hash = window.location.hash;
                var tabId;

                if (hash) {
                    tabId = hash.substr(1);
                } else {
                    hash = '#' + initial;
                    tabId = initial;
                }

                var liElement = $('.place-tabs li:has(a[href=' + hash + '])');
                if (liElement.length === 1) {
                    liElement.addClass(activeClass);
                    $('.tab-content #' + tabId).addClass(activeClass);
                } else {
                    $('.place-tabs li:has(a[href=#' + initial + '])').addClass(activeClass);
                    $('.tab-content #' + initial).addClass(activeClass);
                }
            };

            $scope.isSearchValid = function () {
                if (typeof $scope.search !== 'object') {
                    return false;
                }

                return $scope.members.findIndex(function (m) {
                        return m.username === $scope.search.username;
                    }) === -1;
            };

            $scope.addMember = function () {
                place.one('team', $scope.search.username).put().then(function () {
                    Status.success('Successfully added ' + $scope.search.username + ' to team');

                    $scope.members.push($scope.search);
                });
            };

            $scope.removeMember = function (username) {
                place.one('team', username).remove().then(function () {
                    Status.success('Successfully deleted ' + username + ' from team');

                    $scope.members = $.grep($scope.members, function (e) {
                        return e.username !== username;
                    });
                });
            };
        })
        .controller('PlaceController', function ($scope, $placeRest, Websocket, $rootScope, $uibModal) {
            $scope.place = $placeRest;
            $scope.f = {open: true, enroute: false, done: false};

            var identifier = getUrlAfterSupport();

            $scope.$on('$stateChangeStart', function () {
                Websocket.unsubscribe(identifier);
            });

            $scope.inTeam = function () {
                if (!$rootScope.user || !$rootScope.user.managedPlaces) {
                    return false;
                }

                return $rootScope.user.has(AuthStore.ROLE_ADMIN) || $rootScope.user.managedPlaces.findIndex(function (p) {
                        return p.identifier === $scope.place.identifier
                            && p.city.identifier === $scope.place.city.identifier
                            && p.city.state.identifier === $scope.place.city.state.identifier;
                    }) >= 0;
            };

            $scope.addRequest = function () {
                $uibModal.open({
                    animation: true,
                    templateUrl: '/r/template/addDonationRequest.html',
                    controller: 'AddDonationRequestController',
                    scope: $scope
                });
            }
        })
        .controller('DonationRequestsCtrl', function ($scope, Websocket, Restangular, Status) {
            $scope.donationRequests = $scope.$parent.place.donationRequests;

            $scope.order = function (request) {
                var o;
                switch (request.ui.state) {
                    case 'open':
                        o = 0;
                        break;
                    case 'enroute':
                        o = 1;
                        break;
                    case 'done':
                        o = 2;
                        break;
                    default:
                        o = -1;
                        break;
                }

                return [o, request.createdDateTime];
            };

            $scope.filter = function (request) {
                var f = $scope.$parent.f;
                switch (request.ui.state) {
                    case 'open':
                        return f.open;
                    case 'enroute':
                        return f.enroute;
                    case 'done':
                        return f.done;
                    default:
                        return true;
                }
            };

            var identifier = getUrlAfterSupport();

            var enhanceRequest = function (request) {
                request.ui = {};

                request.ui.commentText = '';

                request.ui.sendComment = function () {
                    console.log('Comment for request ' + request.id + ': ' + request.ui.commentText);

                    var commentDto = {
                        'text': request.ui.commentText,
                        'donationRequestId': request.id
                    };
                    if (request.quantity) {
                        commentDto.quantity = request.ui.commentQuantity;
                    }

                    Restangular.one('donationRequest', request.id).post('comments', commentDto)
                        .then(function () {
                            Status.success('Successfully sent new comment');
                        });

                    request.ui.commentText = '';
                };

                request.ui.refreshRequest = function () {
                    var state;

                    if (request.resolved) {
                        state = 'done';
                    }

                    if (request.quantity && request.comments) {
                        var qConfirmed = 0;
                        var qPromised = 0;

                        _.forEach(request.comments, function (comment) {
                            if (comment.confirmed) {
                                qConfirmed += comment.quantity;
                            } else {
                                qPromised += comment.quantity;
                            }
                        });

                        request.ui.confirmed = qConfirmed;
                        request.ui.pConfirmed = Math.round(qConfirmed / request.quantity * 100);
                        request.ui.promised = qPromised;
                        request.ui.pPromised = Math.round(qPromised / request.quantity * 100);
                        request.ui.quantityLeft = request.quantity - qConfirmed - qPromised;

                        if (!request.resolved) {
                            if (request.ui.quantityLeft <= 0) {
                                state = 'enroute';
                            } else {
                                state = 'open';
                            }
                        }
                    }

                    if (!state) {
                        state = 'open';
                    }

                    request.ui.state = state;
                };
                request.ui.refreshRequest();
                request.tags = ['Food', 'Donation'];

                return request;
            };
            _.forEach($scope.donationRequests, enhanceRequest);

            $scope.confirmComment = function (id) {
                Restangular.one('comment', id).post('confirm').then(function () {
                    Status.success('Successfully confirmed comment');
                });
            };

            $scope.deleteComment = function (id) {
                Restangular.one('comment', id).remove().then(function () {
                    Status.success('Successfully deleted comment');
                });
            };

            $scope.resolveRequest = function (id) {
                Restangular.one('donationRequest', id).post('resolve').then(function () {
                    Status.success('Successfully marked request as resolved');
                });
            };

            $scope.deleteRequest = function (id) {
                Restangular.one('donationRequest', id).remove().then(function () {
                    Status.success('Successfully deleted request');
                });
            };

            var handleCommentChange = function (message) {
                var r;

                switch (message.changeType) {
                    case 'ADD':
                    case 'REFRESH':
                        var comment = message.payload;

                        r = _.find($scope.donationRequests, 'id', comment.donationRequestId);
                        var comments = r.comments;
                        var commentIndex = _.findIndex(comments, 'id', comment.id);

                        if (commentIndex !== -1) {
                            comments[commentIndex] = comment;
                        } else {
                            comments.push(comment);
                        }

                        break;
                    case 'REMOVE':
                        var id = message.id;
                        var index = -1;

                        r = _.find($scope.donationRequests, function (request) {
                            var i = _.findIndex(request.comments, 'id', id);

                            if (i !== -1) {
                                index = i;
                                return true;
                            } else {
                                return false;
                            }
                        });

                        if (index !== -1) {
                            r.comments.splice(index, 1);
                        }

                        break;
                }

                r.ui.refreshRequest();
            };

            var handleRequestChange = function (message) {
                var rId = message.payload ? message.payload.id : message.id;
                var index = _.findIndex($scope.donationRequests, 'id', rId);

                switch (message.changeType) {
                    case 'ADD':
                    case 'REFRESH':
                        if (index !== -1) {
                            $scope.donationRequests[index] = enhanceRequest(message.payload);
                        } else {
                            $scope.donationRequests.push(enhanceRequest(message.payload));
                        }

                        break;
                    case 'REMOVE':
                        if (index !== -1) {
                            $scope.donationRequests.splice(index, 1);
                        }

                        break;
                }
            };

            Websocket.when(identifier).then(null, null, function (message) {
                if (message.entity === 'CommentDto') {
                    handleCommentChange(message);
                } else if (message.entity === 'DonationRequestDto') {
                    handleRequestChange(message);
                }
            });
        })
})();