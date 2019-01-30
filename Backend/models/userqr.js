//this model stores user_qr document 

const mongoose = require('mongoose');

const userqrSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    user_id: String,    
    email: String,                  // email for tracking documents, email change
    o_docid: [String],              // ID list of owned documents
    e_docid: [String],              // ID list of editable documents
    v_docid: [String]               // ID list of viewable documents
},{collection: 'T_USER_QR'});

module.exports = mongoose.model('user_qr', userqrSchema);