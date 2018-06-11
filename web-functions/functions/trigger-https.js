const functions =  require("firebase-functions");
const admin = require('firebase-admin');
const db = admin.firestore();

const downloadAllData = functions.https.onRequest((req, res) => {
    
});

module.exports = {
    downloadAllData
}