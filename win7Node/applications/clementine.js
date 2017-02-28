"use strict";
/*
basic
*start the music player
*exit
play
pause
next
prev
vol +
vol -

nice to have
play artist -> make playlist, play
play song -> make playlist, play
shuffle
repeat
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

const config = require("../config");
const net = require('net');
const childProcess = require("child_process");
const execFile = childProcess.execFile;
const CommandInterface = require("../commandInterface");
const ClementineClient = require("clementine-remote").Client;
const ClementinePath = "C:/Program\ Files\ (x86)/Clementine/clementine.exe";

//init clementine command interface
function Clementine(overrideConf){
    //defaults
    this.settings = {
        host: config.appSettings.clementine.host,
        port: config.appSettings.clementine.port
    };

    //override
    if (overrideConf){
        for(let setting in overrideConf){
            this.settings[setting] = overrideConf[setting]; 
        }
    }
}

Clementine.prototype = Object.create(CommandInterface);
Clementine.prototype.getInstance = function(){
    if (this.instance){
        return this;
    }
    return this.start();
}

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
        this.ready = false;
        this.instance = null;
        this.connected = false;
    });

    this.client.on("error", (err) => {
        console.log("client error");
        console.log(err);
    });

    this.client.on("ready", () => {
        this.ready = true;
        console.log("client ready");
        this.instance = this;

        //bind commands
        if (resolve) {
            resolve(this);
        }
    });
};

Clementine.prototype.start = function(){
    //implement this later
    let waitForStart = (started, failed, timeout) => {
        let client = net.connect({port: 5500});
        client.on("connect", () => {
            console.log('client connected');
            client.end();
        });

        client.on("error", (err) => {
            if (err.toString().indexOf("EISCONN") !== -1){
                //success
                return started();
            }
            else if (timeout <= 0){
                //failed
                return failed();
            }
            else {
                console.log("timeout left: " + timeout);
                setTimeout(() => {
                    timeout--;
                    client.connect();
                }, 1000);
            }
        });
    };

    return new Promise((resolve, reject) => {
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
                        // waitForStart(pass, discard, 10);
                        setTimeout(() => {
                            pass(true);
                        }, 5000);
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
    });
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

Clementine.prototype.louder = function(){
    let newVol = Math.min(this.client.volume + 10, 100);
    return this.changeVolume(newVol)
};

Clementine.prototype.softer = function(){
    let newVol = Math.max(this.client.volume - 10, 0);
    return this.changeVolume(newVol)
};

Clementine.prototype.changeVolume = function(vol){
    console.log("changing vol to "+vol);
    return this.client.set_volume(vol);
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
            let clemRunning = stdout.indexOf("clementine.exe") !== -1;
            console.log("clem is " + clemRunning ? "running" : "not running");
            resolve(clemRunning);
        });
    });
};

Clementine.prototype.Commands = [
    "CLEMENTINE PLAY",
    "CLEMENTINE PAUSE",
    "CLEMENTINE NEXT",
    "CLEMENTINE PREV",
    "CLEMENTINE VOL UP",
    "CLEMENTINE VOL DOWN",
];

Clementine.prototype.handle = function(cmd){
    if (this.Commands.indexOf(cmd) === -1){
        console.log("I don't know how to deal with " + cmd);
        throw "clementine cmd error";
    }
    else {
        //we need an instance to handle anything
        Promise.resolve(this.getInstance())
        .then(function(instance){
            switch(cmd){
                case "CLEMENTINE PLAY":
                    instance.play();
                    break;
                case "CLEMENTINE PAUSE":
                    instance.pause();
                    break;
                case "CLEMENTINE NEXT":
                    instance.next();
                    break;
                case "CLEMENTINE PREV":
                    instance.prev();
                    break;
                case "CLEMENTINE VOL UP":
                    instance.louder();
                    break;
                case "CLEMENTINE VOL DOWN":
                    instance.softer();
                    break;
                default:
                    console.log(cmd + " not yet implemented in clementine");
            }
        });
    }
}


let portInUse = function(port, callback) {
    var server = net.createServer(function(socket) {
        socket.write('Echo server\r\n');
        socket.pipe(socket);
    });

    server.listen(port, 'localhost');
        server.on('error', function (e) {
        callback(true);
    });
    server.on('listening', function (e) {
        server.close();
        callback(false);
    });
};

module.exports = Clementine;