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

const canItemSafelyDelete = functions.https.onCall((data, context) => {
    const contentType = data.contentType;
    const id = data.id;
    console.log(id);
    console.log(contentType);
    if (contentType && id) {
        const collectionPath = "content/" + contentType + "/list";
        if (contentType === "category") {            
            return db.collection(collectionPath).where("idParent", "==", id).get().then(snapshot => {            
                if (snapshot.size > 0){
                    console.log("found in category");
                    return false;
                } else{
                    const bookCollectionPath = "content/book/list";
                    return db.collection(bookCollectionPath).get()
                }
            })
            .then(snapshot => {
                if (snapshot) {
                    var check = true;                               
                    snapshot.forEach(doc => {
                        for (index in doc.data().idCategoryList){
                            if (doc.data().idCategoryList[index] === id){
                                console.log("found in book");
                                check = false;
                                break;
                            }
                        }
                    });                        
                    if (check) console.log("all clear");                        
                    return check;
                } else {
                    return false;
                }
            })
            .catch(err => {
                console.log('Error getting documents', err);    
                throw new functions.https.HttpsError('no-content', 'Error getting collections');
            });
        } else if (contentType === "book") {
            const borrowCollectionPath = "content/borrow/list";
            return db.collection(borrowCollectionPath).where("idBook", "==", id).get().then(snapshot => {            
                if (snapshot.size > 0) console.log("found in borrow");                
                return snapshot.size < 1;
            })
            .catch(err => {
                console.log('Error getting documents', err);    
                throw new functions.https.HttpsError('no-content', 'Error getting collections');
            });
        } else if (contentType === "kid") {
            const borrowCollectionPath = "content/borrow/list";
            return db.collection(borrowCollectionPath).where("idChild", "==", id).get().then(snapshot => {            
                if (snapshot.size > 0) console.log("found in borrow");                
                return snapshot.size < 1;
            })
            .catch(err => {
                console.log('Error getting documents', err);    
                throw new functions.https.HttpsError('no-content', 'Error getting collections');
            });
        } else if (contentType === "borrow") {
            console.log('not implemented contenttype');
            throw new functions.https.HttpsError('not-valid-req', 'content type not exist');
        } else {
            console.log('Error getting contenttype');
            throw new functions.https.HttpsError('not-valid-req', 'content type not exist');
        }
    } else {
        throw new functions.https.HttpsError('not-valid-req', 'no content type in body');
    }
});

module.exports = {
    getContentCount,
    getContentWithCustomFilter,
    canItemSafelyDelete
}