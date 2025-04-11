var exec = require('cordova/exec');

exports.printText = function (arg0, success, error){
    exec(success, error, 'FamocoPrinter', 'printText', [arg0]);
}