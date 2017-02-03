"use strict";

const repl = require("repl");
const Commands = require("./commands");
const Clementine = require("./applications/clementine");
const AudioSwitcher = require("./applications/audioSwitcher");

Commands.init({
	"clementine" : new Clementine(),
	"audioSwitcher" : AudioSwitcher
});

repl.start("> ").context.c = Commands;