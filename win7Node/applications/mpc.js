"use strict";

const request = require("request");
const config = require("../config");
const CommandInterface = require("../commandInterface");

function MPC(){}
MPC.settings = config.appSettings.MPC;

// MPC.switchAudio = function(){
// };


MPC.handle = function(cmd){
    if (MPC.Commands.indexOf(cmd) === -1){
        console.log("I don't know how to deal with " + cmd);
        throw "audio switcher cmd error";
    }
    switch(cmd){
        case 'SWITCH AUDIO':
            MPC.switchAudio();
            break;
        default:
            console.log(cmd + " not yet implemented in MPC");
    }
};

MPC.commandEnum = {
  PLAY_PAUSE: 889,
  PLAY: 887,
  PAUSE: 888,
  STOP: 890,
  SET_VOLUME: -2,
  SEEK: -1,
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

MPC.Commands = Object.keys(MPC.commandEnum);

module.exports = MPC;