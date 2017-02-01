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

const ClementineClient = require('clementine-remote').Client;

function Clementine(config){
    //defaults
    this.settings = {
        host: '127.0.0.1',
        port: 5500
    };

    //override
    if (config){
        for(let setting in config){
            this.settings[setting] = config[setting]; 
        }
    }
    this.connect();
}

Clementine.prototype.connect = function(){
    this.client = ClementineClient({
        host: this.settings.host,
        port: this.settings.port
    });
    
    this.client.on('connect', function () {
        console.log('client connected');
        this.connected = true;
    }.bind(this));

    this.client.on('ready', function () {
        this.ready = true;
        console.log('client ready');
    }.bind(this));
};

Clementine.prototype.open = function(){

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

module.exports = Clementine;