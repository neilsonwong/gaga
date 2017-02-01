"use strict";

function Commands(){}

Commands.addFunction = function(){
    let a, fun, cmd;
    for (a = 0; a < arguments.length; ++a){
        try {
            fun = arguments[a];
            for (cmd in fun.Commands){
                Commands.CMD[cmd] = fun.Commands[cmd];
            }
        }
        catch(e){
            console.log("error");
        }
    }
};

Commands.handle = function(cmd){
    if (Commands.CMD[cmd]){
        Commands.CMD[cmd]();
    }
    else {
        console.log("unknown command " + cmd)
    }
};

Commands.CMD = {};

module.exports = Commands;