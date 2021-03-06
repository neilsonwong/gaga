"use strict";

function Commands(){}

Commands.init = function (modules){
    Commands.modules = modules;
    //init commands for each module
    //sorry no hotloading

    Commands.addFunctions();
}

Commands.getActive = function(){
    let modName;
    let maybeActive = [];
    for (modName in Commands.modules){
        try {
            if (Commands.modules[modName].isActive !== undefined){
                maybeActive.push(Commands.modules[modName].isActive());
                // console.log(modName);
            }
        }
        catch(e){
            console.log("error getting active modules");
        }
    }
    // console.log(maybeActive.length);
    return maybeActive;
}

Commands.addFunctions = function(){
    let a, modName, cmd;
    for (modName in Commands.modules){
        try {
            Commands.modules[modName].Commands.map((cmd) => {
                Commands.CMD[cmd] = modName;
                console.log(cmd + " loaded");
            });
        }
        catch(e){
            console.log("error");
        }
    }
};

Commands.handle = function(cmd, extras){
    if (Commands.CMD[cmd]){
        console.log("handling " + cmd)
        let moduleName = Commands.CMD[cmd];
        this.modules[moduleName].handle(cmd, extras);
    }
    else {
        console.log("unknown command " + cmd)
    }
};

Commands.CMD = {};

module.exports = Commands;