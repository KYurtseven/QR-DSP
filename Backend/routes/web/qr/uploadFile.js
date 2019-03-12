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


// TODO
router.post('/', middleware.checkToken, upload.single('file'), (req,res,next)=>{

	// RESTurl: http://localhost:3000/api/web/qr/uploadFile/
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


module.exports = router;