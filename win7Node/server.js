"use strict";

const express = require('express');
const repl = require("repl");
const Commands = require("./commands");
const app = express()

/* application plugins */
const Clementine = require("./applications/clementine");
const Computer = require("./applications/computer");
const AudioSwitcher = require("./applications/audioSwitcher");
const MPC = require("./applications/mpc");

Commands.init({
	"clementine" : new Clementine(),
	"computer" : Computer,
	"audioSwitcher" : AudioSwitcher,
	"mpc" : MPC 
});

//repl.start("> ").context.c = Commands;

app.get("/", function (req, res) {
  res.send('Hello World!')
});

app.get("/:application/:action", function(req, res){
	let app = (req.params.application).toUpperCase();
	let action = (req.params.action).toUpperCase();
	let extras = req.query || null;
	let cmd = app + " " + action;

	Commands.handle(cmd, extras);
	res.send("OK");
});

app.get("/alive", function (req, res) {
  res.send("living");
});

app.listen(3000, function () {
  console.log("server listening on port 3000!")
});
