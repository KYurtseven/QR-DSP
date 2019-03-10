const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../../../config');

const middleware = require('../../../middleware');

const {UserQR} = require('../../../models/userqr');

router.get('/',middleware.checkToken,  (req,res,next) => {
    
    // correct way to reach this function
    // RESTurl: http://localhost:3000/api/qr/listDocuments
    // request method: GET

    // Returns list of documents of the user
    // "o_docs": [
    //     {
    //         "name": "Deneme QR",
    //         "url": "d2509e92-d56f-4607-a99d-f8a2c628bea9"
    //     }
    // ],
    // "e_docs": [],
    // "v_docs": []

    const user_id = req.decoded.id;

    UserQR.findOne({"owner_id" : user_id})
    .exec()
    .then(documents =>{
        const r = {
            o_docs : documents.o_docs,
            e_docs : documents.e_docs,
            v_docs : documents.v_docs
        }
        res.status(200).json(r);
    })
    .catch(err=>{
        console.log("err: " + err);
        res.status(500).json({'error': err});
    })
})

module.exports = router;