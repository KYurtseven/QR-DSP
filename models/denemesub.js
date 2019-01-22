// In this folder, I'll try to create a nested document

const mongoose = require('mongoose');

const subfieldUser = mongoose.Schema({
    user_id: String,
    user_email : String
})

const subfieldData = mongoose.Schema({
    id: String,
    e_permission : [subfieldUser],
    body: String
})


const denemesubSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: String,
    isPublic: Boolean,
    o_info : subfieldUser,
    e_info : [subfieldUser],
    data : [subfieldData]
},{collection : 'DENEMENESTED'} );


// The schema is in the following:
/*

{
    id:
    name:
    isPublic:
    o_info:
    {
        id:
        name:
    }
    data:[
        {
            id:
            e_info:
            {
                id:
                name:
            }
            body:
        },
        {
            id:
            e_info:
            {
                id:
                name:
            }
            body:
        }
    ]
}
*/

module.exports = mongoose.model('Denemesub', denemesubSchema);