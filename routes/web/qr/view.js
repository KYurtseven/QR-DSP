const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../../../config');
const middleware = require('../../../middleware');

const {QRdoc} = require('../../../models/qr_doc');
const addLog = require('./addLog');

function checkPermission(obj, email)
{
    for(let i = 0; i < obj.length; i++)
    {
        if(obj[i].user_email === email)
            return true;
    }
    return false;
}

router.get('/:url', (req, res, next) =>{

    console.log("WEB viewqr");
    // TODO

    // TODO

    // TODO
    // WRITE THIS FUNCTION FOR HANDLING WEB REQUESTS













    // TODO
    // add logging
    // TODO
    // return editing information(e_info, v_info...) too

    // correct way to reach this function
    // RESTurl: https://localhost/qr/view/d2509e92-d56f-4607-a99d-f8a2c628bea9
    // request method: GET
    // body: 
    // {
	// "user_email": "abiding1880@yandex.com"
    // }

    // returns:
    // {
    //     "type": "owner",
    //     "url": "FIRST ONE",
    //     "comments": [] //nested list of comments
    //     "formdata": {
    //         "_id": "5c29a2b1fba77530e0320949",
    //         "id": "0",
    //         "title": "Document title",
    //         "body": "Here is very complex body",
    //         "type": "spreadsheet"
    //     }
    // }
    let url = req.params.url;
    if(middleware.checkTokenFunction(req))
    {
        // we have token
        let email = req.decoded.email;
        let id = req.decoded.id;
        
        QRdoc.findOne({'url' : url})
        .exec()
        .then(doc =>{
            var qr = {};
            let canSee = true;
            
            console.log("LOG:" + addLog(doc, "AUTHVIEW", email, id));

            if(doc.o_info.email === email)
                qr.type = "owner";
            else
            {
                if(checkPermission(doc.e_info, email))
                    qr.type = "edit";
                else if(checkPermission(doc.v_info, email))
                    qr.type = "view";
                else if(doc.isPublic)
                    qr.type = "public";
                else
                    canSee = false;
            }

            if(canSee)
            {
                qr.url = doc.url;
                qr.comments = doc.comments;
                qr.formdata = doc.formdata;
                res.status(200).json(qr);
            }
            else
                res.status(403).json({'error' : 'You cannot see this document'});
        })
        .catch(err =>{
            console.log(err);
            res.status(500).json({'error': err});
        });
    }
    else
    {
        // public access or invalid token.
        console.log("inside invalid token access");
       
        QRdoc.findOne({'url' : url})
        .exec()
        .then(doc =>{
            var qr = {};
            let canSee = true;

            if(doc.isPublic)
            {
                qr.type = "public";
                qr.url = doc.url;
                qr.comments = doc.comments;
                qr.formdata = doc.formdata;
                res.status(200).json(qr);
            }
            else
            res.status(403).json({'error' : 'You cannot see this document'});
        })
        .catch(err =>{
            console.log(err);
            res.status(500).json({'error': err});
        });
    }
})

module.exports = router;