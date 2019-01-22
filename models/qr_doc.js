// This model will store the QR data in the database


const mongoose = require('mongoose');

const qrdocinfo = mongoose.Schema({
    user_id: String,
    user_email : String
})

const qrdocdata = mongoose.Schema({
    id: String,
    type : String, //spreadsheet, pdf or form
    title : String,
    body: String, // data resides here
})

const qrcomment = mongoose.Schema({
    _id : mongoose.Schema.Types.ObjectId,
    cid: String,
    sender: qrdocinfo,
    body: String,
    parent: String,
    children: []
})

const qrdocSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    // TODO
    // generate URL from uuid when the schema is first created
    url: String,
    isPublic: Boolean,
    o_info : qrdocinfo,
    e_info : [qrdocinfo],
    v_info : [qrdocinfo],
    formdata : qrdocdata,
    comments : [qrcomment]
    // TODO
    // After demo, merge this and replace with T_QRDOC
},{collection : 'T_QRDOC'} );


module.exports = 
{
    QRdoc: mongoose.model('QRdoc', qrdocSchema),
    QRcomment: mongoose.model('QRcomment', qrcomment),
    QRdocinfo: mongoose.model('QRdocinfo', qrdocinfo)
}
