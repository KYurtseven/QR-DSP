const mongoose = require('mongoose');

const denemeSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: String,
    password: String,
    d2enemeField : String,
    hmm: String
},{collection : 'DENEME123'} );

module.exports = mongoose.model('Deneme', denemeSchema);