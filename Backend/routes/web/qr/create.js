const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');
const jwt = require('jsonwebtoken');
const config = require('../../../config');
const middleware = require('../../../middleware');
const User = require('../../../models/user');
const {QRdoc, QRdocinfo} = require('../../../models/qr_doc');
const multer = require('multer');
const upload = multer();

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

// TODO
function companyCheck(rhs)
{
	// TODO
	// check companies in the database
	let company = rhs.map(item =>{
		return item.toUpperCase();
	});
	return company;
}

// TODO
router.post('/uploadDocument', middleware.checkToken, upload.single('file'), (req,res,next)=>{

	// RESTurl: http://localhost:3000/api/web/qr/create/uploadDocument
	// request method: POST
	// Autheorization: Bearer Token
	// Headers: No header
	// Body: Type is form-data, not raw, not json
	// key:
	// file : "Insert File Here"
	
	const owner = new QRdocinfo({
		id: req.decoded.id,
		email: req.decoded.email
	});
	console.log("owner: " + owner);
	console.log("req:");
	console.log(req.file);
	const str = req.file.originalname
	var filename = str.substring(0,str.lastIndexOf('.'));
	var extension = str.substring(str.lastIndexOf('.') + 1, str.length);
	console.log("file name: " + filename);
	console.log("extension: " + extension);

	// Right now, only excel is accepted, with xlsx format

	if(extension !== "xlsx"){
		res.status(401).json({message : "This file is not allowed"})
	}
	else{
		res.status(200).json();
	}
})

// TODO
// to be depricated
router.post('/', middleware.checkToken, (req,res,next) => {
	// Example input
	
	// correct way to reach this function
    // RESTurl: https://localhost/api/web/qr/create
	// request method: POST
	// body:
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

	const owner = new QRdocinfo({
		id: req.decoded.id,
		email: req.decoded.email
	});
	
	// TODO
	// We can execute e_info and v_info paralel
	checkInfo(req.body.e_info).then(e_info =>{
		checkInfo(req.body.v_info).then(v_info =>{
			
			// TODO
			// check companies in the database
			let e_company = companyCheck(req.body.e_company);
			let v_company = companyCheck(req.body.v_company);

			const qrdoc = new QRdoc({
				_id : new mongoose.Types.ObjectId(),
				url : uuidv4(),
				name: req.body.name,
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

});

module.exports = router;