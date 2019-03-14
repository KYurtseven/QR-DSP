const mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');

// Company wide documents
const company_doc = mongoose.Schema({
    name : String,                  // users can see this name
    url: String,                    // url to fetch the data
},{_id: false});


const companySchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: {                     // company names stored in uppercase
        type: String, 
        required: [true, "can't be blank"],
        unique: true,
        index: true
    },                          
    e_docs : [company_doc],     // list of documents that are globally editable in the company
    v_docs: [company_doc]       // list of documents that are globally viewable in the company
},{collection : 'T_COMPANY'} );

companySchema.plugin(uniqueValidator, {message: 'is already exist'});

module.exports = 
{
    Company: mongoose.model('Company', companySchema),
    CompanyDoc : mongoose.model('CompanyDoc', company_doc)
}