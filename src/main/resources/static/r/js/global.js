var SERVICE_PREFIX = '/service/v1/';

var nullOrEmpty = function (obj) {
    return obj === null || obj === undefined || Object.keys(obj).length === 0;
};

var getUrlAfterSupport = function () {
    var url = window.location.href;
    var position = url.search(/support\//) + 8;
    return url.substring(position);
};

var parseUrl = function () {
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

var isResourceUrl = function (url) {
    return url.startsWith('/r') || url === 'favicon.ico';
};

var isString = function (s) {
    return typeof s === 'string' || s instanceof String;
};

var isObject = function(obj) {
    return typeof obj === 'object';
}

// polyfills
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function (searchString, position) {
        position = position || 0;
        return this.indexOf(searchString, position) === position;
    };
}

if (!String.prototype.endsWith) {
    String.prototype.endsWith = function (searchString, position) {
        var subjectString = this.toString();
        if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
            position = subjectString.length;
        }
        position -= searchString.length;
        var lastIndex = subjectString.indexOf(searchString, position);
        return lastIndex !== -1 && lastIndex === position;
    };
}

var containsAll = function (haystack, needles) {
    if (Array.isArray(haystack)) {
        if (Array.isArray(needles)) {
            // we are looking for multiple needles in a big haystack
            return needles.every(function (v) {
                return haystack.indexOf(v) !== -1;
            });
        } else {
            // we are only looking for one needle in a big haystack
            return haystack.indexOf(needles) !== -1;
        }
    } else {
        if (Array.isArray(needles)) {
            // we won't find multiple needles in a single straw
            return false;
        } else {
            // we got a needle and a straw - maybe we are lucky!
            return haystack == needles;
        }
    }
};

var AuthStore = {
    PROCESS_PLACE_REQUESTS: 'PROCESS_PLACE_REQUESTS',
    MANAGE_ROLES: 'MANAGE_ROLES',
    MANAGE_USERS: 'MANAGE_USERS',
    MANAGE_CITIES: 'MANAGE_CITIES',
    MANAGE_STATES: 'MANAGE_STATES',
    MANAGE_PLACES: 'MANAGE_PLACES',
    QUERY_USERS: 'QUERY_USERS',
    ROLE_ADMIN: 'ROLE_ADMIN',
    ROLE_USER: 'ROLE_USER'
};