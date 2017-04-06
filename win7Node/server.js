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
		Promise.all(Commands.getActive())
		.then((apps) => { 
			console.log("alives: " + apps);
			console.log(JSON.stringify(apps));
			//no more races :(
			let app = null;
			let livings = "living";
			for (let i = 0; i < apps.length; ++i){
				if (apps[i].active === true){
					app = apps[i];
					break;
				}
				else if (apps[i].active === false){
					app = apps[i];
				}
			}
			if (app && app.name){
				livings += app.name;
			}
			res.send(livings);
		})
		.catch(function(e){
			console.log("huh");
			console.log(e);
		});
});

app.listen(3000, function () {
  console.log("server listening on port 3000!")
});


// repl.start("> ").context.c = Commands;