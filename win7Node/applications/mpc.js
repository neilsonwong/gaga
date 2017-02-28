"use strict";

const config = require("../config");
const request = require("request");
const fs = require("fs");
const childProcess = require("child_process");
const execFile = childProcess.execFile;
const find = require("fs-find");
const CommandInterface = require("../commandInterface");
const MPCPath = "C:/Program\ Files\ (x86)/Combined\ Community\ Codec\ Pack/MPC/mpc-hc.exe";

function MPC(){}
MPC.settings = config.appSettings.mpc;

MPC.handle = function(rawCmd, extras){
    let cmd = rawCmd.substring(4)
    console.log(rawCmd);
    console.log(cmd);

    if (MPC.Commands.indexOf(rawCmd) === -1){
        console.log("I don't know how to deal with " + rawCmd);
        throw "mpc cmd error";
    }
    else {
        //see if mpc is open
        MPC.isRunning().
        then((running) => {
            if(!running){
                return MPC.start();
            }
        })
        .catch((error) => {
            console.log("problem starting mpc");
            console.log(error);
        })
        .then(() => {
            MPC.post(cmd, extras);
        })
        .catch((error) => {
            console.log("problem handling mpc command");
            console.log(error);
        });
    }
};


MPC.post = function(command, extras){
    let cmd = MPC.commandEnum[command];
    if (cmd == MPC.commandEnum.OPEN_FILE){
        //open file special case~!
        console.log(extras);
        return MPC.openFile(extras.filename);
    }
    else if (cmd){
        request.post({
            url:'http://localhost:13579/command.html', 
            form: {
                "null" : "0",
                "wm_command" : cmd 
            }
        },
        function(err, res, body){ 
            console.log(command + " executed");
        });
    }
}

MPC.openFile = function(file){
    //find file
    console.log(file);
    MPC.findFile(file, (filePath) => {
        if (filePath === null){
            //couldn't find file
            console.log("couldn't find a file matching " + file);
        }
        else {
            // open file path=E%3a%5ctemp%5c[HorribleSubs]%20Seiren%20-%2003%20[720p].mkv
            request.get({
                // headers: {'user-agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'},
                url:'http://localhost:13579/browser.html', 
                qs : {
                    // "path" : encodeURIComponent(filePath)
                    "path" : filePath
                }
            },
            function(err, res, body){ 
                // console.log(err);
                console.log("open file " + filePath + " executed");
            });
        }
    });
}

MPC.findFile = function(file, callback){
    let search = splitSearchString(file.toLowerCase());
    find(MPC.settings.library, {
            file: function(path, info){
                let stripped = info.name.toLowerCase()
                    .replace("720p", "");
                for (let s in search){
                    if (stripped.indexOf(search[s]) == -1){
                        return false;
                    }
                }
                return true;
            }
        },
        function(err, results) {
            if(err) {
                return console.error(err);
            }
            let found = results.map((object) => object.file);
            if (found.length > 0){
                return callback(found[0]);
            }
            return callback(null);
        }
    );

    function splitSearchString(str){
        return str.split(' ');
        /*.map((s) => {
            let potNum = parseInt(s);
            if (isNaN(potNum)){
                return s;
            }
            else {
                return potNum;
            }
        });
        */
    }
}

MPC.commandEnum = {
  PLAY_PAUSE: 889,
  PLAY: 887,
  PAUSE: 888,
  STOP: 890,
  SET_VOLUME: -2,
  SEEK: -1,
  OPEN_FILE: -999,
  QUICK_OPEN_FILE: 969,
  SAVE_IMAGE: 806,
  JUMP_TO_BEGINNING: 996,
  NEXT: 922,
  PREVIOUS: 921,
  NEXT_FILE: 920,
  PREVIOUS_FILE: 919,
  FULLSCREEN: 830,
  JUMP_FORWARD_SMALL: 900,
  JUMP_BACKWARD_SMALL: 899,
  JUMP_FORWARD_MEDIUM: 902,
  JUMP_BACKWARD_MEDIUM: 901,
  JUMP_FORWARD_LARGE: 904,
  JUMP_BACKWARD_LARGE: 903,
  GO_TO: 893,
  CLOSE: 804,
  EXIT: 816,
  GOTO_NEXT_SUBTITLE: 32781,
  NEXT_AUDIO: 952,
};

MPC.Commands = Object.keys(MPC.commandEnum).map(key => "MPC " + key);

MPC.isRunning = function(){
    return new Promise(function(resolve, reject){
        execFile("wmic", ["process", "list", "brief"], function(error, stdout, stderr) {
            if (error){
                reject(error);
            }
            let mpcRunning = stdout.indexOf("mpc-hc.exe") !== -1;
            console.log("mpc is " + mpcRunning ? "running" : "not running");
            resolve(mpcRunning);
        });
    });
};

MPC.start = function(){
    return new Promise(function(pass, discard) {
        let smallMPC = childProcess.spawn(MPCPath, { detached: true, stdio: "ignore" });
        smallMPC.on("error", (error) => {
            console.log("mpc start error");
            console.log(error);
        });
        smallMPC.unref();

        console.log("mpc started");
        //use a hack to simulate the startup lag, cuz it's hard to detect whether it started or not
        // waitForStart(pass, discard, 10);
        setTimeout(() => {
            pass(true);
        }, 5000);
    });
};

module.exports = MPC;