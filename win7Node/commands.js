"use strict";

function Commands(){}

Commands.init = function (modules){
    Commands.modules = modules;
    //init commands for each module
    //sorry no hotloading

    Commands.addFunctions();
}

Commands.addFunctions = function(){
    let a, modName, cmd;
    for (modName in Commands.modules){
        try {
            Commands.modules[modName].Commands.map((cmd) => {
                Commands.CMD[cmd] = modName;
            });
        }
        catch(e){
            console.log("error");
        }
    }
};

Commands.handle = function(cmd){
    if (Commands.CMD[cmd]){
        let moduleName = Commands.CMD[cmd];
        this.modules[moduleName].handle(cmd);
    }
    else {
        console.log("unknown command " + cmd)
    }
};

Commands.CMD = {};

module.exports = Commands;