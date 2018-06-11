const functions =  require("firebase-functions");
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const storage = require('./trigger-storage');
const directCall = require('./trigger-direct-call');
const httpsCall = require('./trigger-https');
module.exports = {
    storage, // trigger storage
    directCall, // trigger direct call
    httpsCall // trigger https
}