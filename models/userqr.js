//this model stores user_qr document 

const mongoose = require('mongoose');

const userqrSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    user_id: String,
    email: String,
    o_docid: [String],
    e_docid: [String],
    v_docid: [String]
},{collection: 'T_USER_QR'});

module.exports = mongoose.model('user_qr', userqrSchema);