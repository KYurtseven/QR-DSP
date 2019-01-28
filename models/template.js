const mongoose = require('mongoose');

const templateSchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    id : String,                        // unique template id
    name : String,                      // visible name for user
    body: mongoose.Schema.Types.Mixed,  // complex body
    company: String
},{collection : 'T_TEMPLATE'});

module.exports = mongoose.model('Template', templateSchema);