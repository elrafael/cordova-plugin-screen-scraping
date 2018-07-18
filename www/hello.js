/*global cordova, module*/

exports.aLacard = function(name, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Hello", "aLacard", [name]);
};

exports.caixa = function(name, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Hello", "caixa", [name]);
};