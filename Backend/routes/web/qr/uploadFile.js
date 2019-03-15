const express = require('express');
const router = express.Router();
const uuidv4 = require('uuid/v4');
const middleware = require('../../../middleware');
const {QRdoc, QRdocinfo} = require('../../../models/qr_doc');
const {SUCCESS, FAIL, uploadFileResponse} = require('../../../helpers/responses');
const mongoose = require('mongoose');

const multer = require('multer');
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
		// the file is saved to here
        cb(null, '/QRDSP/github/Backend/FILES')
    },
    filename: function (req, file, cb) {
		// the filename field is added or altered here once the file is uploaded
        cb(null, uuidv4() + '.xlsx')
  	}
})
var upload = multer({ storage: storage })

/**
 * Fetch file from form-data field of the response
 * Note that, the key of the field must be "file"
 * 
 * When the file is fetched, it is stored under destionation
 * filename is updated to uuidv4()
 * 
 * After saving, the file objet is passed to the api function below
 * 
 * TODO
 * Right now, only xlsx files are saved
 * There is no check for other types
 * 
 * TODO
 * in case of error, delete the file
 * 
 * TODO
 * add document to the user's owner
 */
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
	// visible name
	const name = req.file.originalname;
	// name of the file, which contains unique url
	const fileName = req.file.filename;
	const url = fileName.substring(0, fileName.lastIndexOf('.'));

	// TODO
	// Right now, only xlsx is supported
	const type = "xlsx";
	
	const qr = new QRdoc({
		_id : new mongoose.Types.ObjectId(),
		url : url,
		name : name,
		isPublic: false,
		type: type,
		o_info: owner,
		e_info : [],
		v_info: [],
		formdata:"",
		comments:[],
		logs:[]
	})

	qr.save()
	.then(savedqr=>{
		uploadFileResponse(res, SUCCESS,"");
	}).catch(err=>{
		uploadFileResponse(res, FAIL, err);
	})
})


module.exports = router;