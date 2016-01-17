var SERVICE_PREFIX = "/service/v1/";

var nullOrEmpty = function(obj) {
    return obj === null || Object.keys(obj).length === 0;
};

var getUrlAfterSupport = function() {
    var url = window.location.href;
    var position = url.search(/support\//) + 8;
    return url.substring(position);
};

var isString = function(s) {
    return typeof s === 'string' || s instanceof String;
};