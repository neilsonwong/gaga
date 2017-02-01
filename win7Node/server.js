'use strict';

const repl = require('repl');
const Clementine = require('./clementine/clementine');

const clem = new Clementine();

repl.start('> ').context.clem = clem;