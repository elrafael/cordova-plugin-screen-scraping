/*global cordova, module*/

exports.aLacard = function(name, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Hello", "aLacard", [name]);
};
exports.aLacard_movemnts = function(name, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Hello", "aLacard_movements", [name]);
};

exports.caixa = function(name, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Hello", "caixa", [name]);
};