const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../config');
const middleware = require('../middleware');

const {QRdoc} = require('../models/qr_doc');
const {QRcomment} = require('../models/qr_doc');
const {QRdocinfo} = require('../models/qr_doc');
const User = require('../models/user');
const uuidv4 = require('uuid/v4');

function checkPermission(obj, email)
{
    for(let i = 0; i < obj.length; i++)
    {
        if(obj[i].user_email === email)
            return true;
    }
    return false;
}

// TODO
// new method for mobile device
router.get('/viewQR/:url', (req, res, next) =>{
    // TODO
    // add logging
    
    // correct way to reach this function
    // RESTurl: localhost:3000/mobile/viewQR/FIRST ONE
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

        let username = req.decoded.username;
        let email = req.decoded.email;
        //console.log("inside token access");
        //console.log("req.decoded:" + JSON.stringify(req.decoded));

        QRdoc.findOne({'url' : url})
        .exec()
        .then(doc =>{
            var qr = {};
            let canSee = true;
            
            if(doc.o_info.user_email === email)
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

function findComment(comment, body, usr)
{
    comment.forEach(element => {
        if(element.children.length != 0)
        {
            if(element.cid === body.parent)
            {
                // found, insert
                var qrcomment = new QRcomment();
                
                qrcomment.cid = uuidv4();
                qrcomment.sender = usr;
                qrcomment.body = body.comment;
                qrcomment.parent = body.parent;

                element.children.push(qrcomment);
                return true;
            }

            if(findComment(element.children, body, usr))
                return true;
        }
        else
        {
            // this is the last depth
            if(element.cid === body.parent)
            {
                // found, insert
                var qrcomment = new QRcomment();
                
                qrcomment.cid = uuidv4();
                qrcomment.sender = usr;
                qrcomment.body = body.comment;
                qrcomment.parent = body.parent;

                element.children.push(qrcomment);
                return true;
            }
            return false;
        }
    });
}

router.post('/addcomment/:url', (req, res, next)=>{
    // find the user who added a comment
    if(req.body != null && req.body != undefined)
    {
        // find the user in the database
        User.findOne({'email' : req.body.user_email})
        .exec()
        .then(u =>{
            if(u != null && u != undefined)
                // u exists
                console.log('')
            else
                res.status(404).json({'error': 'the specified user is not found'});               
            
            usr = new QRdocinfo()
            usr.user_id = u.uid;
            usr.user_email = u.email;

            QRdoc.findOne({'url' : req.params.url})
            .exec()
            .then(doc =>{
                
                if(req.body.parent != null && req.body.parent != undefined)
                {
                    // find correct position and insert
                    findComment(doc.comments, req.body, usr);
                    doc.markModified('comments');
                    doc
                    .save()
                    .then(docres =>{
                        res.status(200).json(docres);
                    })
                    .catch(err => {
                        console.log(err);
                        res.status(500).json({error: err});
                    })
                }
                else
                {
                    //Add comment to the root level
                    var qrcomment = new QRcomment();
                    qrcomment.cid = uuidv4();
                    qrcomment.sender = usr;
                    qrcomment.body = req.body.comment;
                    qrcomment.parent = "";

                    doc.comments.push(qrcomment);
                    doc.markModified('comments');
                    // save to DB
                    doc
                    .save()
                    .then(docres =>{
                        res.status(200).json(docres);
                    })
                    .catch(err => {
                        console.log(err);
                        res.status(500).json({error: err});
                    })
                    }
            })
            .catch(err =>{
                console.log(err);
                res.status(500).json({'error': err});
            });

        })
        .catch(err =>{
            console.log(err);
            res.status(500).json({'error': err});
        });
    }
})

module.exports = router;