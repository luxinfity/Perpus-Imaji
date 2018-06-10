const functions =  require("firebase-functions");
const admin = require('firebase-admin');
const db = admin.firestore();

const getContentCount = functions.https.onCall((data, context) => {
    const contentType = data.contentType;
    if (contentType) {
        const collectionPath = "content/" + contentType + "/list";
        return db.collection(collectionPath).get()
            .then(snapshot => {        
                console.log("size: " + snapshot.size);
                return snapshot.size;
            })
            .catch(err => {
                console.log('Error getting documents', err);    
                throw new functions.https.HttpsError('no-content', 'Error getting collections');
            });
    } else {
        throw new functions.https.HttpsError('not-valid-req', 'no content type in body');
    }
});

const getContentWithCustomFilter = functions.https.onCall((data, context) => {
    const contentType = data.contentType;
    const filters = data.filter;
    if (contentType) {
        const collectionPath = "content/" + contentType + "/list";
        return db.collection(collectionPath).get()
            .then(snapshot => {
                var filteredData = [];
                snapshot.forEach(doc => {
                    console.log(doc);
                    var check = true;
                    if (filters) {
                        for (key in filters){
                            if (doc.data()[key].indexOf(filters[key]) === -1){
                                check = false;
                                break;
                            }
                        }
                    }
                    if (check) filteredData.push(doc.data());
                });
                console.log("data: " + filteredData.size);                
                return filteredData;
            })
            .catch(err => {
                console.log('Error getting documents', err);    
                throw new functions.https.HttpsError('no-content', 'Error getting collections');
            });
    } else {
        throw new functions.https.HttpsError('not-valid-req', 'no content type in body');
    }
});

module.exports = {
    getContentCount,
    getContentWithCustomFilter
}