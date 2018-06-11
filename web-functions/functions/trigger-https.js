const functions =  require("firebase-functions");
const admin = require('firebase-admin');
const db = admin.firestore();
const excel = require('exceljs');

const downloadAllData = functions.https.onRequest((req, res) => {
    const workbook = new excel.Workbook();
    // setup meta data
    workbook.creator = 'Muhammad Alif Akbar';
    workbook.lastModifiedBy = 'Selasar Imaji Server';
    workbook.created = new Date();

    // create sheets
    const contentTypes = ["category", "book", "kid", "borrow"]
    var promises = []
    for (name in contentTypes){
        promises.push(createSheet(contentTypes[name], workbook));
    }
    
    // respond
    res.setHeader('Content-Type', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
    res.setHeader('Content-Disposition', 'attachment; filename=perpus-imaji-data.xlsx');
    Promise.all(promises)
    .then((sheets) => {
        console.log("writing xlsx stream");
        return workbook.xlsx.write(res);
    })
    .then((data) => {
        console.log('File write done........');        
        return res.end();
    })
    .catch(err => {
        console.log('Error getting documents', err);    
        throw new functions.https.HttpsError('no-content', 'Error getting collections');
    });
});

function createSheet(contentType, workbook){
    var sheet = workbook.addWorksheet(contentType);    
    const collectionPath = "content/" + contentType + "/list";
    console.log("creating " + collectionPath);
    return db.collection(collectionPath).get()
            .then(snapshot => {
                var isFirst = true;                
                snapshot.forEach(doc => {                       
                    var row = {};
                    if(isFirst){
                        isFirst = false;
                        var columns = [];
                        for (columnName in doc.data()){
                            // setup header
                            columns.push({ header: columnName, key: columnName });
                        }
                        sheet.columns = columns;
                    }
                    for (columnName in doc.data()){
                        row[columnName] = doc.data()[columnName];
                    }
                    console.log(doc.id);
                    row.id = doc.id;
                    sheet.addRow(row);
                });
                return sheet;
            })
}

module.exports = {
    downloadAllData
}