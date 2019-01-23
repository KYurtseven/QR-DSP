// This model will store the QR data in the database


const mongoose = require('mongoose');

const qrdocinfo = mongoose.Schema({
    id: String,
    email : String
})

const qrdocdata = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    type : String, //spreadsheet, pdf or form
    title : String,
    body: String, // data resides here
})

const qrcomment = mongoose.Schema({
    _id : mongoose.Schema.Types.ObjectId,
    id: String,
    sender: qrdocinfo,
    body: String,
    parent: String,
    children: []
})

const qrlog = mongoose.Schema({
    _id : mongoose.Schema.Types.ObjectId,
    logger: qrdocinfo || String, // TODO, try this
    date: Date,
    type: String // view or edit
})

const qrdocSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    url: String,            // unique url for reaching the document
    isPublic: Boolean,      // whether the document is public or not
    o_info : qrdocinfo,     // info of person who owns the document
    e_info : [qrdocinfo],   // list of people who can edit
    e_company: [String],    // list of companies who can edit
    v_info : [qrdocinfo],   // list of people who can view
    v_company: [String],    // list of companies who can view
    formdata : qrdocdata,   // usefull data
    comments : [qrcomment], // nested comments
    logs: [qrlog]           // list of logs
},{collection : 'T_QRDOC'} );


module.exports = 
{
    QRdoc: mongoose.model('QRdoc', qrdocSchema),
    QRcomment: mongoose.model('QRcomment', qrcomment),
    QRdocinfo: mongoose.model('QRdocinfo', qrdocinfo),
    QRlog: mongoose.model('QRlog', qrlog)
}
