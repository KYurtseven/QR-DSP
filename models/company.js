const mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');

const companySchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: {
        type: String, 
        required: [true, "can't be blank"],
        unique: true,
        index: true
    },                          // company names stored in uppercase
    templates: [String]         // list of template id's
},{collection : 'T_COMPANY', timestamps : true} );

companySchema.plugin(uniqueValidator, {message: 'is already exist'});
module.exports = mongoose.model('Company', companySchema);