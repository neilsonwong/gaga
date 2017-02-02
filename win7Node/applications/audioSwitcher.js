'use strict';

const CommandInterface = require('../commandInterface');
const robot = require("robotjs");

function AudioSwitcher(){}

AudioSwitcher.switchAudio = function(){
    robot.keyTap("f9", "control");
};

AudioSwitcher.Commands = {
    'SWITCH AUDIO' : AudioSwitcher.switchAudio
};

module.exports = AudioSwitcher;