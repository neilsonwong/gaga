'use strict';

const CommandInterface = require('../commandInterface');
const robot = require("robotjs");

function AudioSwitcher(){}

AudioSwitcher.Commands = {
    'SWITCH AUDIO' : AudioSwitcher.switchAudio
};

AudioSwitcher.switchAudio = function(){
    robot.keyTap("f9", "control");
}

module.exports = AudioSwitcher;