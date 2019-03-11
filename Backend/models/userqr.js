//this model stores user_qr document 

const mongoose = require('mongoose');

const user_qr_doc = mongoose.Schema({
    name : String,                  // user can see this name
    url: String,                    // url to fetch the data
},{_id: false});

const userqrSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    owner_id: String,
    email: String,                      // email for tracking documents, email change
    o_docs: [user_qr_doc],             // list of owned documents
    e_docs: [user_qr_doc],             // list of editable documents
    v_docs: [user_qr_doc]              // list of viewable documents
},{collection: 'T_USER_QR'});

module.exports = 
{
    UserQR: mongoose.model('user_qr', userqrSchema),
    UserQRDoc : mongoose.model('user_qr_doc', user_qr_doc)
}
