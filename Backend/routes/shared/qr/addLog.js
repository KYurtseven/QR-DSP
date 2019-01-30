const {QRlog} = require('../../../models/qr_doc');
const {QRdocinfo} = require('../../../models/qr_doc');

function addLog(doc, type, email, id){
    // We don't have user
    const usr = new QRdocinfo();
    // TODO
    // check public case thorougly
    // TODO
    // Edit case for web
    // TODO
    // what happens when an error occurs in saving part,
    // we do not wait for save to be finished
    // we immediately send res200 in response 
    // TODO
    // What happens when someone floods document with view
    // Too large document?
    if(type !== "PUBLIC")
    {
        usr.id = id;
        usr.email = email;
    }
    const log = new QRlog({
        type : type,
        logger: usr,
        date : Date.now(),      // Number of miliseconds
    });
    console.log("doc: " + JSON.stringify(doc));
    doc.logs.push(log);
    doc.markModified('logs');
    doc.save().catch(err =>{
        console.log("err: " + err);
    });
}



module.exports = addLog;
