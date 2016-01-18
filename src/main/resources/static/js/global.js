var SERVICE_PREFIX = '/service/v1/';

var nullOrEmpty = function(obj) {
    return obj === null || Object.keys(obj).length === 0;
};

var getUrlAfterSupport = function() {
    var url = window.location.href;
    var position = url.search(/support\//) + 8;
    return url.substring(position);
};

var parseUrl = function() {
    var url = window.location.href;
    var position = url.search(/support\//) + 8;

    var afterSupport = url.substring(position);
    var match = afterSupport.match(/([^\/]+)\/([^\/]+)\/([^\/]+)/);

    var result = {};
    if (match !== null) {
        if (match.length > 1) result.stateIdentifier = match[1];
        if (match.length > 2) result.cityIdentifier = match[2];
        if (match.length > 3) result.placeIdentifier = match[3];
    }

    return result;
};

var isString = function(s) {
    return typeof s === 'string' || s instanceof String;
};