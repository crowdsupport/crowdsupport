(function () {
    angular
        .module('crowdsupport.controller.donationrequests', ['timeAgo', 'crowdsupport.service.websocket', 'crowdsupport.service.config',
            'crowdsupport.widget.search',
            'crowdsupport.service.status', 'crowdsupport.service.auth', 'crowdsupport.service.previousstate', 'ui.bootstrap',
            'ui.bootstrap.datetimepicker', 'restangular', 'ngAnimate'])
        .controller('AddDonationRequestController', function ($scope, Restangular, Status, $uibModalInstance, $timeout) {
            $scope.request = {};
            $scope.request.tags = [];
            $scope.neverExpires = true;
            $scope.minDate = new Date();
            $scope.date = new Date();

            var place = $scope.$parent.place;

            $scope.addTag = function (keyEvent) {
                var input = $scope.inputTag;
                if (typeof input !== 'undefined' && (typeof keyEvent === 'undefined' || keyEvent.which === 32)) {
                    if (isString(input)) {
                        input = input.trim();
                        if (input.length !== 0) {
                            if (_.findIndex($scope.request.tags, 'name', input) === -1) {
                                $scope.request.tags.push({name: input});
                            }
                        }
                    } else {
                        if (_.findIndex($scope.request.tags, 'name', input.name) === -1) {
                            $scope.request.tags.push(input);
                        }
                    }

                    $scope.inputTag = null;
                    if (keyEvent) {
                        keyEvent.preventDefault();
                    }
                }
            };

            $scope.removeTag = function (index) {
                var removed = $scope.request.tags.splice(index, 1);
                if (removed.length >= 1) {
                    $scope.inputTag = removed[0];
                }
            };

            $scope.create = function () {
                var r = $scope.request;
                if (!neverExpires) {
                    r.validTo = $scope.date;
                }
                r.resolved = false;
                $scope.addTag();

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
        .controller('PlaceFilterController', function($scope, $rootScope) {
            $scope.$watchCollection('statusfilter', function (newFilter) {
                $rootScope.$broadcast('statusFilterChange', newFilter);
            });
            $scope.$watch('tagfilter', function (newFilter) {
                $rootScope.$broadcast('tagFilterChange', newFilter);
            });

            $scope.statusfilter = {open: true, enroute: false, done: false};

            $scope.tagfilter = '';

            $scope.$on('addTagToFilterEvent', function (evt, tag) {
                tag = tag.name;

                if ($scope.tagfilter.length !== 0 && $scope.tagfilter.slice(-1) !== ' ') {
                    tag = ' ' + tag;
                }
                $scope.tagfilter += tag;
            });
        })
        .controller('PlaceController', function ($scope, $placeRest, $rootScope, $uibModal, $timeout) {
            $scope.place = $placeRest;

            $scope.speedDial = {
                isOpen: false
            };
            $scope.$watch('speedDial.isOpen', function(isOpen) {
                if (isOpen) {
                    $timeout(function() {
                        $scope.speedDial.tooltipVisible = $scope.speedDial.isOpen;
                    }, 1000);
                } else {
                    $scope.speedDial.tooltipVisible = $scope.speedDial.isOpen;
                }
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
                    templateUrl: 'addRequest.html',
                    controller: 'AddDonationRequestController',
                    scope: $scope
                });
            }
        })
        .controller('DonationRequestsCtrl', function ($scope, Websocket, Restangular, Status, $rootScope) {
            $scope.donationRequests = $scope.$parent.place.donationRequests;

            var statusfilter = {};
            var tagfilter = '';

            $scope.$on('statusFilterChange', function (event, newFilter) {
                statusfilter = newFilter;
            });
            $scope.$on('tagFilterChange', function (event, newFilter) {
                tagfilter = newFilter;
            });

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
                var show = false;

                // apply state filter
                switch (request.ui.state) {
                    case 'open':
                        show |= statusfilter.open;
                        break;
                    case 'enroute':
                        show |= statusfilter.enroute;
                        break;
                    case 'done':
                        show |= statusfilter.done;
                        break;
                    default:
                       show = true;
                }

                // apply tag filter
                if (show && tagfilter && request.tags) {
                    var tags = _.words(tagfilter);
                    tags.forEach(function (fTag) {
                        var filterMatches = false;
                        var regex = new RegExp('.*' + fTag + '.*', 'i');

                        request.tags.forEach(function (rTag) {
                            filterMatches |= regex.test(rTag.name);
                        });

                        show &= filterMatches;
                    });
                }

                return show;
            };

            var identifier = getUrlAfterSupport();

            var enhanceRequest = function (request) {
                request.ui = {};
                request.ui.commentText = '';
                request.ui.showComments = false;
                request.ui.rows = 1;

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
                    request.ui.commentQuantity = '';
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

                return request;
            };
            _.forEach($scope.donationRequests, enhanceRequest);

            $scope.toggleComments = function (request) {
                request.ui.showComments = !request.ui.showComments;

                request.ui.rows = request.ui.showComments ? 2 : 1;
            };

            $scope.addTagToFilter = function (tag) {
                $rootScope.$broadcast('addTagToFilterEvent', tag);
            };

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
                    case 'CREATE':
                    case 'UPDATE':
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
                    case 'DELETE':
                        var id = message.payload.id;
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
                    case 'CREATE':
                    case 'UPDATE':
                        if (index !== -1) {
                            $scope.donationRequests[index] = enhanceRequest(message.payload);
                        } else {
                            $scope.donationRequests.push(enhanceRequest(message.payload));
                        }

                        break;
                    case 'DELETE':
                        if (index !== -1) {
                            $scope.donationRequests.splice(index, 1);
                        }

                        break;
                }
            };

            var subReg = new Websocket.SubscriptionRegister();
            subReg.releaseAllOnStateChange($scope);
            Websocket.when(identifier, subReg).then(null, null, function (message) {
                if (message.payloadType === 'CommentDto') {
                    handleCommentChange(message);
                } else if (message.payloadType === 'DonationRequestDto') {
                    handleRequestChange(message);
                }
            });
        })
})();