'use strict';

const config = require("../config");
const CommandInterface = require('../commandInterface');
const robot = require("robotjs");

function AudioSwitcher(){}
AudioSwitcher.settings = config.appSettings.audioSwitcher;

AudioSwitcher.switchAudio = function(){
    robot.keyTap(this.settings.switchAudioDeviceHotkey.key, this.settings.switchAudioDeviceHotkey.key);
};

AudioSwitcher.Commands = ['SWITCH AUDIO'];

module.exports = AudioSwitcher;