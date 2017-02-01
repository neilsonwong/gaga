"use strict";

const repl = require("repl");
const Commands = require("./commands");
const Clementine = require("./applications/clementine");
const AudioSwitcher = require("./applications/audioSwitcher");

// let clem = new Clementine().then((clemReady) => {
// 	clem = clemReady;
// 	Commands.addFunction(clem);
// });

Promise.all([
		new Clementine(),
		AudioSwitcher,
	])
	.then(funcs => {
	funcs.forEach(function(val){
		Commands.addFunction(val);
	});
});

repl.start("> ").context.c = Commands;