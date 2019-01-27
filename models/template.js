const mongoose = require('mongoose');

const templateSchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    id : String,        // unique template id
    body: Schema.Types.Mixed
},{collection : 'T_TEMPLATE'});

module.exports = mongoose.model('Template', templateSchema);