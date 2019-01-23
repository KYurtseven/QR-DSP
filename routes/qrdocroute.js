const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');
const jwt = require('jsonwebtoken');
const config = require('../config');
const middleware = require('../middleware');
const User = require('../models/user');
const {QRdoc, QRdocinfo} = require('../models/qr_doc');


function checkInfo(rhs)
{
	// determine whether input is correct or not
	const info = rhs.map(item =>{
		return User.findOne({'email' : item.email})
		.exec()
		.then(usr =>{
			if(usr)
			{
				let tmpuser = new QRdocinfo({
					id : usr.id,
					email : usr.email
				});
				return tmpuser;
			}
		})
		.catch(err =>{});
	})
	return Promise.all(info)
		.then(docs =>{
			// filter null values
			return docs.filter(usr => usr);
		})
}

function companyCheck(rhs)
{
	// TODO
	// check companies in the database
	let company = rhs.map(item =>{
		return item.toUpperCase();
	});
	return company;
}

router.post('/create', middleware.checkToken, (req,res,next) => {
	
	// Example input

	// {
	// 	"isPublic": false,
	// 	"e_info": [
	// 		{"id": "020281c9-58d4-4361-b051-bc3a8db2c614", "email": "edyth1@ford.com"}
	// 		], 
	// 	"v_info": [
	// 		{"email": "gus1@toyota.com"}
	// 		],
	// 	"e_company": ["Ford"],
	// 	"v_company": ["Nissan"],
	// 	"formdata": {
	// 		"type" : "pdf",
	// 		"title": "first qr doc",
	// 		"body" : "TODO BODY"
	// 	}
	// }

	const owneremail = req.decoded.email;
	User.findOne({'email': owneremail}).exec()
	.then(usr =>{
		const owner = new QRdocinfo({
			id: usr.id,
			email: usr.email
		});
		// TODO
		// We can execute e_info and v_info paralel
		checkInfo(req.body.e_info).then(e_info =>{
			checkInfo(req.body.v_info).then(v_info =>{
				
				let e_company = companyCheck(req.body.e_company);
				let v_company = companyCheck(req.body.v_company);

				const qrdoc = new QRdoc({
					_id : new mongoose.Types.ObjectId(),
					url : uuidv4(),
					isPublic: req.body.isPublic,
					o_info : owner,
					e_info : e_info,
					v_info : v_info,
					e_company: e_company,
					v_company: v_company,
					formdata : req.body.formdata
				});
				
				qrdoc
				.save()
				.then(doc =>{
						res.status(200).json({result: "QR is created"});
				})
				.catch(err => {
						console.log(err);
						res.status(500).json({error: err});
				})
				
			})
			.catch(err=>{res.status(500).json({error: err})});
		})
		.catch(err=>{res.status(500).json({error: err})});
	})
	.catch(err=>{res.status(500).json({error: err})});
});


function checkPermission(obj, email)
{
    for(let i = 0; i < obj.length; i++)
    {
        if(obj[i].user_email === email)
            return true;
    }
    return false;
}

function fillUsers(obj)
{
    let arr = [];
    for(let i = 0; i < obj.length; i++)
    {
        let curobj = {};
        curobj.user_id = obj[i].user_id;
        curobj.user_email = obj[i].user_email;
        arr.push(curobj);
    }
    return arr;
}

function getViewHandler(qr, email)
{
    var res = {};
    res.type = "view";
    res.fields = {};
    // TODO
    // We might not want to send user id
    res.fields.e_info = fillUsers(qr.e_info);
    res.fields.v_info = fillUsers(qr.v_info);

    res.fields.isPublic = qr.isPublic;
    res.fields.data = [];
    for(let i = 0; i < qr.data.length; i++)
    {
        if(checkPermission(qr.data[i].v_permission, email) || qr.data[i].isPublic)
        {
            // user can view this file
            let curobj = {};
            curobj.type = "view";
            curobj.data = {};
            curobj.data.e_permission = fillUsers(qr.data[i].e_permission);
            curobj.data.v_permission = fillUsers(qr.data[i].v_permission);
            curobj.data.isPublic = qr.data[i].isPublic;
            curobj.data.id = qr.data[i].id;
            curobj.data.title = qr.data[i].title;
            curobj.data.body = qr.data[i].body;
            
            res.fields.data.push(curobj);     
        }
    }
    return res;
}

function getEditHandler(qr, email)
{
    var res = {};
    res.type = "edit";
    res.fields = {};
    // TODO
    // We might not want to send user id
    res.fields.e_info = fillUsers(qr.e_info);
    res.fields.v_info = fillUsers(qr.v_info);

    res.fields.isPublic = qr.isPublic;
    res.fields.data = [];
    for(let i = 0; i < qr.data.length; i++)
    {
        if(checkPermission(qr.data[i].e_permission, email))
        {
            // user can edit this file
            let curobj = {};
            curobj.type = "edit";
            curobj.data = {};
            curobj.data.e_permission = fillUsers(qr.data[i].e_permission);
            curobj.data.v_permission = fillUsers(qr.data[i].v_permission);
            curobj.data.isPublic = qr.data[i].isPublic;
            curobj.data.id = qr.data[i].id;
            curobj.data.title = qr.data[i].title;
            curobj.data.body = qr.data[i].body;

            res.fields.data.push(curobj);
        }
        else if(checkPermission(qr.data[i].v_permission, email) || qr.data[i].isPublic)
        {
            // user can view this file
            let curobj = {};
            curobj.type = "view";
            curobj.data = {};
            curobj.data.e_permission = fillUsers(qr.data[i].e_permission);
            curobj.data.v_permission = fillUsers(qr.data[i].v_permission);
            curobj.data.isPublic = qr.data[i].isPublic;
            curobj.data.id = qr.data[i].id;
            curobj.data.title = qr.data[i].title;
            curobj.data.body = qr.data[i].body;
            
            res.fields.data.push(curobj);     
        }
    }
    return res;
}

function getPublicHandler(qr)
{
    var res = {};
    res.type = "public";
    res.fields = {};
    // TODO
    // We might not want to send user id
    res.fields.e_info = fillUsers(qr.e_info);
    res.fields.v_info = fillUsers(qr.v_info);

    res.fields.isPublic = qr.isPublic;
    res.fields.data = [];
    for(let i = 0; i < qr.data.length; i++)
    {
        if(qr.data[i].isPublic)
        {
            // user can view this file
            let curobj = {};
            curobj.type = "public";
            curobj.data = {};
            curobj.data.e_permission = fillUsers(qr.data[i].e_permission);
            curobj.data.v_permission = fillUsers(qr.data[i].v_permission);
            curobj.data.isPublic = qr.data[i].isPublic;
            curobj.data.id = qr.data[i].id;
            curobj.data.title = qr.data[i].title;
            curobj.data.body = qr.data[i].body;
            
            res.fields.data.push(curobj);     
        }
    }
    return res;
}

router.post('/get/:url', (req,res,next) => {

    var qr;

    QRdoc.findOne({'url' : req.params.url})
    .exec()
    .then(doc =>{
        console.log(doc);
        //res.status(200).json(doc);
        qr = doc;
        console.log("qr: " + JSON.stringify(qr));

        // a legitimate user tries to access this
        console.log("------------");

        console.log("body: " + JSON.stringify(req.body));
        if(req.body != null && req.body != undefined)
        {
            let email = req.body.user_email;

            // TODO
            // we don't send email in the body, we send username and token
            // for simplicity, right now we are checking it with email

            // TODO
            // do we need to send all data?
            if(qr.o_info.user_email === email)
                res.status(200).json(qr);
            else
            {
                // TODO
                // for better code practicing remove repeating code 
                // in getedit,view and public handler
                if(checkPermission(qr.e_info, email))
                {
                    console.log("can edit");
                    let result = getEditHandler(qr, email);
                    res.status(200).json(result);
                }
                else if(checkPermission(qr.v_info, email))
                {
                    console.log("can view");
                    
                    let result = getViewHandler(qr, email);
                    res.status(200).json(result);
                }
                else if(qr.isPublic){
                    console.log("public");
                    let result = getPublicHandler(qr);
                    res.status(200).json(result);
                }
                else{
                    // cannot see this file
                    // TODO
                    res.status(400).json({'body' : 'Delete this, unatuhorized attempt'});
                }
            }
        }
        // a public user tries to access this
        else
        {
            if(qr.isPublic)
            {
                // find public fields in qr and return that object to the person
                let result = getPublicHandler(qr);
                res.status(200).json(result);
            }
            else
            {
                // public cannot see this
                // TODO
                res.status(400).json({'body' : 'Delete this, unatuhorized attempt'});
            }
        }
    })
    .catch(err =>{
        console.log(err);
        res.status(500).json({'error': err});
    });

    
})


module.exports = router;