'use strict';
/*
basic
*start the music player
*exit
play
pause
next
prev

nice to have
play artist -> make playlist, play
play song -> make playlist, play
*/

/*built into client
change_song(playlist, song)
disconnect
end
get_library
next
pause
play
playpause
previous
set_track_position
shuffle_playlist
stop
*/

const childProcess = require("child_process");
const execFile = childProcess.execFile;
const CommandInterface = require("../commandInterface");
const ClementineClient = require("clementine-remote").Client;
const ClementinePath = "C:/Program\ Files\ (x86)/Clementine/clementine.exe";

function Clementine(config){
    //defaults
    this.settings = {
        host: "127.0.0.1",
        port: 5500
    };

    //override
    if (config){
        for(let setting in config){
            this.settings[setting] = config[setting]; 
        }
    }
    return new Promise((resolve, reject) => {
        this.start(resolve, reject);
    });
}

Clementine.prototype = Object.create(CommandInterface);

Clementine.prototype.connect = function(resolve, reject){
    this.connecting = true;
    this.client = ClementineClient({
        host: this.settings.host,
        port: this.settings.port
    });
    
    this.client.on("connect", () => {
        console.log("client connected");
        this.connected = true;
        this.connecting = false;
    });

    this.client.on("disconnect", (data) => {
        console.log("client disconnecting", data);
        this.connected = false;
    });

    this.client.on("end", () => {
        console.log("client disconnected");
        this.connected = false;
    });


    this.client.on("ready", () => {
        this.ready = true;
        console.log("client ready");

        //bind commands
        this.Commands = {
            "CLEMENTINE PLAY" : this.play.bind(this),
            "CLEMENTINE PAUSE" : this.pause.bind(this),
            "CLEMENTINE NEXT" : this.next.bind(this),
            "CLEMENTINE PREV" : this.prev.bind(this),
        };

        if (resolve) {
            resolve(this);
        }
    });
};

Clementine.prototype.start = function(resolve, reject){
    if (this.client && (this.client.connected || this.client.connecting)){
        //already connecting, no need to fire twice
        console.log("already connecting to clementine");
        resolve(this);
    }
    else {
        //not connected yet, we will try to start clementine then connect to it
        //open clementine

        let isRunning = this.isRunning()
        .then((running) => {
            if(!running){
                console.log("clementine is not running");
                //clementine is not running, we need to start it
                return new Promise(function(pass, discard) {
                    let smallOrange = childProcess.spawn(ClementinePath, { detached: true, stdio: "ignore" });
                    smallOrange.on("error", (error) => {
                        console.log("clementine start error");
                        console.log(error);
                    });
                    smallOrange.unref();
                    
                    console.log("clementine started");
                    //use a hack to simulate the startup lag, cuz it's hard to detect whether it started or not
                    setTimeout(() => {
                        pass(true);
                    }, 2000);
                });
            }
            else {
                console.log("clementine is running");
                return true;
            }
        })
        .then((previousStatus) => {
            //connect to control server
            this.connect(resolve, reject);
        });

    }
};

Clementine.prototype.play = function(){
    return this.primitive("play");
};

Clementine.prototype.pause = function(){
    return this.primitive("pause");
};

Clementine.prototype.next = function(){
    return this.primitive("next");
};

Clementine.prototype.prev = function(){
    return this.primitive("previous");
};

Clementine.prototype.primitive = function(action){
    return new Promise(function(resolve, reject){
        if (this.ready){
            this.client[action]();
            return resolve(true);
        }
        else {
            reject("clementine not ready")
        }
    }.bind(this));
};

Clementine.prototype.isRunning = function(){
    return new Promise(function(resolve, reject){
        execFile("wmic", ["process", "list", "brief"], function(error, stdout, stderr) {
            if (error){
                reject(error);
            }
            resolve(stdout.indexOf("clementine.exe") !== -1);
        });
    });
};

module.exports = Clementine;