"use strict";

const config = require("../config");
const CommandInterface = require("../commandInterface");
const robot = require("robotjs");

function AudioSwitcher(){}
AudioSwitcher.settings = config.appSettings.audioSwitcher;

AudioSwitcher.switchAudio = function(){
    robot.keyTap(this.settings.switchAudioDeviceHotkey.key, this.settings.switchAudioDeviceHotkey.modifier);
};

AudioSwitcher.Commands = ["SWITCH AUDIO"];

AudioSwitcher.handle = function(cmd){
    if (AudioSwitcher.Commands.indexOf(cmd) === -1){
        console.log("I don't know how to deal with " + cmd);
        throw "audio switcher cmd error";
    }
    switch(cmd){
        case 'SWITCH AUDIO':
            AudioSwitcher.switchAudio();
            break;
        default:
            console.log(cmd + " not yet implemented in audioSwitcher");
    }
};

module.exports = AudioSwitcher;