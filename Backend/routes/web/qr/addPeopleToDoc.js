const express = require('express');
const router = express.Router();
const middleware = require('../../../middleware');
const User = require('../../../models/user');
const {QRdoc, QRdocinfo} = require('../../../models/qr_doc');
const {UserQR, UserQRDoc} = require('../../../models/userqr');
const {checkUser} = require('../../../helpers/checkUserInDB');


/**
 * when a new person is added to the QR
 * it will be checked whether this user is already in the
 * list or not
 * @param {*} lhs 
 * a list of {"id" : "user id", "email": "user email" }
 * @param {*} email 
 * email of the user to be added
 * @returns
 * finds the index of the user's email in the list
 * -1 if not found
 */
function findEmailInList(lhs, email){
    for(let i = 0; i < lhs.length; i++)
        if(lhs[i].email === email)
            return i;
    return -1;
}

/**
 * when a person is added to the QR
 * qr url should be added to UserQR as well
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
 * we need to update each user's userqr.
 * This list(userqr) will be used when the user wants to see his documents
 * 
 * This function updates userqr fields.
 * When the user's role in the qr is changed, the document in the userqr is also changed
 * ex:
 * user has view role in the documentA. Now, userqr has documentA in v_docs of userqr.
 * When user's role is changed to the edit, documentA must be moved from v_docs of userqr
 * to e_docs of the userqr.
 * 
 * @param {*} emailList 
 * list of people with {"id" : "user id", "email" : "user email"}
 * @param {*} doc 
 * qr document
 * @param {*} type 
 * edit or view
 * @returns
 * a dummy premises. We are not interested in return value of this function.
 */
function addQRToUsers(emailList, doc, type){
    // add qr to user's edit list
    if(type === "edit"){
        const premises = emailList.map(email=>{
            return UserQR.findOne({'email' : email.email})
            .exec()
            .then(usrqr=>{
                let changeFlag = false;
                // qr is not in user's owned documents
                if(findDocumentInList(usrqr.o_docs, doc.url) === -1){
                    let i1 = findDocumentInList(usrqr.v_docs, doc.url);
                    if(i1 > -1){
                        // qr is in the viewlist
                        // remove from viewlist
                        // add it to editlist
                        usrqr.v_docs.splice(i1,1);
                        usrqr.markModified('v_docs');
                        changeFlag = true;
                    }
                    if(findDocumentInList(usrqr.e_docs, doc.url) === -1){
                        // document is not in user's editlist
                        // add document to the users editlist
                        let newdoc = new UserQRDoc({
                            name : doc.name,
                            url : doc.url
                        });
                        usrqr.e_docs.push(newdoc);
                        usrqr.markModified('e_docs');
                        changeFlag = true;
                    }
                }
                // else
                // this document belongs to owner
                // dont need to add document to the userqr
                if(changeFlag){
                    usrqr.save()
                    .then(saveddoc=>{
                        return saveddoc;
                    }).catch(err=>{
                        // TODO
                        console.log("Error in addQRToUsers: " + err);
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
        const premises = emailList.map(email=>{
            return UserQR.findOne({'email' : email.email})
            .exec()
            .then(usrqr=>{
                let changeFlag = false;
                // qr is not in user's owned documents
                if(findDocumentInList(usrqr.o_docs, doc.url) === -1){
                    let i1 = findDocumentInList(usrqr.e_docs, doc.url);
                    if(i1 > -1){
                        // qr is in the editlist
                        // remove from editlist
                        // add it to viewlist
                        usrqr.e_docs.splice(i1,1);
                        usrqr.markModified('e_docs');
                        changeFlag = true;
                    }
                    if(findDocumentInList(usrqr.v_docs, doc.url) === -1){
                        // document is not in user's viewlist
                        // add document to the users viewlist
                        let newdoc = new UserQRDoc({
                            name : doc.name,
                            url : doc.url
                        });
                        usrqr.v_docs.push(newdoc);
                        usrqr.markModified('v_docs');
                        changeFlag = true;
                    }
                }
                // else
                // this document belongs to owner
                // dont need to add document to the userqr
                if(changeFlag){
                    usrqr.save()
                    .then(saveddoc=>{
                        return saveddoc;
                    }).catch(err=>{
                        // TODO
                        console.log("Error in addQRToUsers: " + err);
                        return err;
                    })
                }
            })
        });
    }
}

/**
 * Adds list of people to the qr document
 * Then, for each user added to the document
 * it updates user's userqr.
 * @CasesCovered
 * @Case1
 * User's email is not in the database.
 * @Solution1
 * checkUser function will check each user in the database
 * filter users which are not present
 * @Case2
 * Trying to add a user which is already owner of the document
 * @Solution2
 * The owner of the document will be skipped
 * @Case3
 * User is in editlist. However, user will be added to viewlist
 * @Solution3
 * Remove user from editlist of the document. Add it to the viewlist
 * of the document.
 * In UserQR, remove document from editlist, add it to the viewlist
 * @Case4
 * User is in viewlist. However, user will be added to editlist
 * @Solution4
 * Same as @Case3 , with switched cases
 * @Case5
 * User is added to the document for the first time.
 * @Solution5
 * All if checks will be failed. The user will be added to the document.
 * User's UserQR document will be updated
 * 
 */
router.post('/', middleware.checkToken, (req,res,next)=>{
	
	// RESTurl: http://localhost:3000/api/web/qr/addPeopleToDoc
	// request method: POST
	// Autheorization: Bearer Token
	// body:{
	// 	"url" : "QR URL",
	// 	"userList": [
	// 		{"email": "someemail1@email.com"},
	// 		{"email": "someemail2@email.com"}
	// 	],
	// 	"type" : "edit OR view"
	// }

	// check emails
	checkUser(req.body.userList).then(emailList=>{
        let addFlag = false;

		// fetch QR
		QRdoc.findOne({'url' : req.body.url})
		.exec()
		.then(doc =>{ 
			if(req.body.type === "edit"){
				emailList.forEach(emailToBeAdded=>{
					// email is owner's email
					if(doc.o_info.email !== emailToBeAdded.email){
                        let ix = findEmailInList(doc.v_info, emailToBeAdded.email);
                        // delete user from view, since it will add to the edit
                        if(ix > -1){
                            doc.v_info.splice(ix,1);
                            doc.markModified('v_info');
                            addFlag = true;
                        }
                        // add user to editlist
                        if(findEmailInList(doc.e_info, emailToBeAdded.email) === -1){
                            doc.e_info.push(emailToBeAdded);
                            doc.markModified('e_info');
                            addFlag = true;
                        }
                    }
                    // else
                    // this email belongs to owner of the document
                    // dont need to add user to the document
				});
			}
			else if(req.body.type === "view")
			{
				emailList.forEach(emailToBeAdded=>{
					// email is owner's email
					if(doc.o_info.email !== emailToBeAdded.email){
                        let ix = findEmailInList(doc.e_info, emailToBeAdded.email);
                        // delete user from edit, since it will add to the view
                        if(ix > -1){
                            doc.e_info.splice(ix,1);
                            doc.markModified('e_info');
                            addFlag = true;
                        }
                        // add user to viewlist
                        if(findEmailInList(doc.v_info, emailToBeAdded.email) === -1){
                            doc.v_info.push(emailToBeAdded);
                            doc.markModified('v_info');
                            addFlag = true;
                        }
					}
				});
			}
            if(addFlag){
                doc.save().then(docsaved =>{
                    // add qr's to the user's documents
                    addQRToUsers(emailList, doc, req.body.type)
                    .then(usersSaved=>{
                        res.status(200).json({message: 'Document and users are updated'});
                    }).catch(err=>{
                        res.status(500).json({error: "document is saved but users might not be updated"});
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
		.catch(err=>{
			res.status(500).json({error: err});
		})
	});
});


module.exports = router;