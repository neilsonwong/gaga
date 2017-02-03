'use strict';
function CMDInterface(){};

CMDInterface.prototype.start = function(){
    console.log('plz override this if needed');
};

CMDInterface.prototype.Commands = {
    'Not Yet Implemented': 'Not Yet Implemented'
};

CMDInterface.getInstance = function(){
    //default to no instance required, return the current object
    return this;
}

module.exports = CMDInterface;