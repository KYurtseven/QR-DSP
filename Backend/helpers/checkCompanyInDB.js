const Company = require('../models/company');

/**
 * eliminates not found companies in the list by filter
 * 
 * @param {*} companies 
 * list of companies ["FORD", "NISSAN", "OpeL"]
 * note that input can contain small letters 
 * 
 * @returns
 * list of companies found in the db(correct inputs)
 * ex:
 * suppose that "NISSAN" is not in the database
 * 
 * returnlist : ["FORD", "OPEL"]
 * 
 */
function checkCompany(companies){
    const res = companies.map(item =>{
        return Company.findOne({name : item.toUpperCase()})
        .exec()
        .then(com=>{
            if(com){
                return com;
            }
        })
        .catch(err=>{
            console.log("err in checkCompanyDb: " + err);
        })
    });

    return Promise.all(res)
    .then(docs =>{
        // filters null values
        return docs.filter(com => com)
    }).catch(err=>{
        console.log("err in checkCompanyList: " + err);
    })
}

module.exports = 
{
    checkCompany : checkCompany
}

