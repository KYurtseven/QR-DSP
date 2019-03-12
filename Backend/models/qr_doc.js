// This model will store the QR data in the database
const mongoose = require('mongoose');

/**
 * @param {String} id
 * user id
 * @param {String} email
 * user email
 */
const qrdocinfo = mongoose.Schema({
    id: String,
    email : String
},{_id: false});

const qrcomment = mongoose.Schema({
    id: String,
    sender: qrdocinfo,
    body: String,
    parent: String,
    children: []
},{_id: false});

const qrlog = mongoose.Schema({
    type: String,
    logger: qrdocinfo,
    date: Date,
},{_id: false});

const qrdocSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    url: String,            // unique url for reaching the document
    name: String,           // a visible name for end users
    type: String,           // xlsx, pdf or dynamicform
    formdata : String,      // if the type is dynamicform, it contains the form data
    isPublic: Boolean,      // whether the document is public or not
    o_info : qrdocinfo,     // info of person who owns the document
    e_info : [qrdocinfo],   // list of people who can edit
    e_company: [String],    // list of companies who can edit
    v_info : [qrdocinfo],   // list of people who can view
    v_company: [String],    // list of companies who can view
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
