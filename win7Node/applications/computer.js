"use strict";
//UNTESTED

const childProcess = require("child_process");

function Computer(){}

Computer.shutdown = function shutdown(callback){
    let cmd = "shutdown -s -t 0";
    childProcess.exec(cmd, function (err, stdout, stderr) {
        if (callback)
            callback(err, stdout, stderr);
    });
};

Computer.sleep = function sleep(callback){
    let cmd = "rundll32.exe powrprof.dll,SetSuspendState 0,1,0";
    childProcess.exec(cmd, function (err, stdout, stderr) {
        if (callback)
            callback(err, stdout, stderr);
    });
};

Computer.Commands = ["COMPUTER SHUTDOWN, COMPUTER SLEEP"];

Computer.handle = function(cmd){
    if (Computer.Commands.indexOf(cmd) === -1){
        console.log("I don't know how to deal with " + cmd);
        throw "computer(node interface) cmd error";
    }
    switch(cmd){
        case 'COMPUTER SHUTDOWN':
            Computer.shutdown();
            break;
        case 'COMPUTER SLEEP':
            Computer.sleep();
            break;
        default:
            console.log(cmd + " not yet implemented in computer(node interface)");
    }
};

module.exports = Computer;