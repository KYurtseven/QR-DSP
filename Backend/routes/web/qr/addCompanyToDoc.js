const express = require('express');
const router = express.Router();
const middleware = require('../../../middleware');
const {Company,CompanyDoc} = require('../../../models/company');
const {QRdoc} = require('../../../models/qr_doc');
const {checkCompany} = require('../../../helpers/checkCompanyInDB');

/**
 * when a company is added to the QR
 * qr url should be added to Company as well
 * @param {*} lhs 
 * list of {"name" : "name of document", "url" : "url of document"}
 * @param {*} docUrl 
 * url of the doc to be added
 * @returns
 * index of the url in the list
 * -1 if not found
 */
function findDocumentInList(lhs, docUrl){
    for(let i = 0; i< lhs.length; i++)
        if(lhs[i].url === docUrl)
            return i;
    return -1;
}

/**
 * When a qr's scope is updated
 * we need to update each company's scope too.
 * 
 * This function updates company's fields.
 * When the company's role in the qr is changed, the document in the company is also changed
 * ex:
 * company has view role in the documentA. Now, company has documentA in v_docs.
 * When company's role is changed to the edit, documentA must be moved from v_docs of company
 * to e_docs.
 * 
 * @param {*} companyList 
 * list of people with ["FORD", "NISSAN"]
 * @param {*} doc 
 * qr document
 * @param {*} type 
 * edit or view
 * @returns
 * a dummy premises. We are not interested in return value of this function.
 */
function addQRToCompany(companyList, doc, type){

    // add qr to user's edit list
    if(type === "edit"){
        const premises = companyList.map(company=>{
            return Company.findOne({'name' : company})
            .exec()
            .then(com=>{
                let changeFlag = false;

                let i1 = findDocumentInList(com.v_docs, doc.url);
                if(i1 > -1){
                    // qr is in the viewlist
                    // remove from viewlist
                    // add it to editlist
                    com.v_docs.splice(i1,1);
                    com.markModified('v_docs');
                    changeFlag = true;
                }
                if(findDocumentInList(com.e_docs, doc.url) === -1){
                    // document is not in company's editlist
                    // add document to the company's editlist
                    let newdoc = new CompanyDoc({
                        name : doc.name,
                        url : doc.url
                    });
                    com.e_docs.push(newdoc);
                    com.markModified('e_docs');
                    changeFlag = true;
                }
                if(changeFlag){
                    com.save()
                    .then(saveddoc=>{
                        return saveddoc;
                    }).catch(err=>{
                        // TODO
                        console.log("Error in addQRToCompany: " + err);
                        return err;
                    })
                }
            })
        })
        return Promise.all(premises)
        .then(docs =>{
            return docs;
        })
    }
    else if(type === "view"){
        const premises = companyList.map(company=>{
            return Company.findOne({'name' : company})
            .exec()
            .then(com=>{
                let changeFlag = false;

                let i1 = findDocumentInList(com.e_docs, doc.url);
                if(i1 > -1){
                    // qr is in the editlist
                    // remove from editlist
                    // add it to viewlist
                    com.e_docs.splice(i1,1);
                    com.markModified('e_docs');
                    changeFlag = true;
                }
                if(findDocumentInList(com.v_docs, doc.url) === -1){
                    // document is not in company's viewlist
                    // add document to the company's viewlist
                    let newdoc = new CompanyDoc({
                        name : doc.name,
                        url : doc.url
                    });
                    com.v_docs.push(newdoc);
                    com.markModified('v_docs');
                    changeFlag = true;
                }
                if(changeFlag){
                    com.save()
                    .then(saveddoc=>{
                        return saveddoc;
                    }).catch(err=>{
                        // TODO
                        console.log("Error in addQRToCompany: " + err);
                        return err;
                    })
                }
            })
        });
    }
}

/**
 * when a new company is added to the QR
 * it will be checked whether this company is already in the
 * list or not
 * @param {*} lhs 
 * a list of companies ["FORD", "NISSAN"]
 * @param {*} companyName 
 * name of the company to be added
 * @returns
 * finds the index of the company name in the list
 * -1 if not found
 */
function findCompanyInList(lhs, companyName){
    for(let i = 0; i < lhs.length; i++)
        if(lhs[i] === companyName)
            return i;
    return -1;
}

/**
 * Adds list of company to the qr document
 * Then, for each company added to the document
 * it updates company's fields.
 * @CasesCovered
 * @Case1
 * Company is not in the database.
 * @Solution1
 * checkCompany function will check each company in the database
 * filter companies which are not present
 * @Case2
 * Company is in editlist. However, company will be added to viewlist
 * @Solution2
 * Remove company from e_company of the document. Add it to the v_company
 * of the document.
 * In Company, remove document from e_docs, add it to the v_docs
 * @Case3
 * Company is in viewlist. However, company will be added to editlist
 * @Solution3
 * Same as @Case2 , with switched cases
 * @Case4
 * Company is added to the document for the first time.
 * @Solution4
 * All if checks will be failed. The company will be added to the document.
 * Company document will be updated
 * 
 */
router.post('/', middleware.checkToken, (req,res,next)=>{

    // RESTurl : http://localhost:3000/api/web/qr/addCompanyToDoc
    // request method : POST
    // Authorization : Bearer Token
    // body:{
    //     "url" : "QR URL",
    //     "companyList" : ["FORD", "NIsSAn", "Opel"],
    //     "type" : "edit OR view"
    // }

    checkCompany(req.body.companyList).then(companyList =>{
        let addFlag = false;

        // fetch QR
		QRdoc.findOne({'url' : req.body.url})
		.exec()
        .then(doc =>{
            if(req.body.type === "edit"){
				companyList.forEach(company=>{

                    let ix = findCompanyInList(doc.v_company, company);
                    // delete company from view, since it will add to the edit
                    if(ix > -1){
                        doc.v_company.splice(ix,1);
                        doc.markModified('v_company');
                        addFlag = true;
                    }
                    // add company to editlist
                    if(findCompanyInList(doc.e_company, company) === -1){
                        doc.e_company.push(company);
                        doc.markModified('e_company');
                        addFlag = true;
                    }

				});
            }
            else if(req.body.type === "view")
			{
				companyList.forEach(company=>{

                    let ix = findCompanyInList(doc.e_company, company);
                    // delete company from edit, since it will add to the view
                    if(ix > -1){
                        doc.e_company.splice(ix,1);
                        doc.markModified('e_company');
                        addFlag = true;
                    }
                    // add company to viewlist
                    if(findCompanyInList(doc.v_company, company) === -1){
                        doc.v_company.push(company);
                        doc.markModified('v_company');
                        addFlag = true;
                    }
				});
            }
            if(addFlag){
                doc.save().then(docsaved =>{
                    // add qr's to the company's documents
                    addQRToCompany(companyList, doc, req.body.type)
                    .then(companySaved=>{
                        res.status(200).json({message: 'Document and companies are updated'});
                    }).catch(err=>{
                        res.status(500).json({error: "document is saved but companies might not be updated"});
                        console.log("err: " + err);
                    })
                }).catch(err=>{
                    res.status(500).json({error: err});
                })
            }
            else{
                res.status(200).json({message : 'No change is done'});
            }
        })
    })
})

module.exports = router;