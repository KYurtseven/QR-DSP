const mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');

// When the user wants to see stored templates,
// it can see them by a simple query.
// then the user can select correct template
const CompanyTemplateListElementSchema = mongoose.Schema({
    id: String,                 // template id
    name: String                // template's visible name
},{_id: false});

const companySchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: {
        type: String, 
        required: [true, "can't be blank"],
        unique: true,
        index: true
    },                          // company names stored in uppercase
    templates: [CompanyTemplateListElementSchema]         // list of template id's
},{collection : 'T_COMPANY'} );

companySchema.plugin(uniqueValidator, {message: 'is already exist'});

module.exports = 
{
    Company: mongoose.model('Company', companySchema),
    CompanyTemplateListElement : mongoose.model('CompanyTemplateListElement', CompanyTemplateListElementSchema)
}