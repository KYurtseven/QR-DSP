const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../../../config');
const middleware = require('../../../middleware');

const {QRdoc} = require('../../../models/qr_doc');
const {QRcomment} = require('../../../models/qr_doc');
const {QRdocinfo} = require('../../../models/qr_doc');
const User = require('../../../models/user');
const uuidv4 = require('uuid/v4');

function findComment(comment, body, usr)
{
    comment.forEach(element => {
        if(element.children.length != 0)
        {
            if(element.id === body.parent)
            {
                // found, insert
                var qrcomment = new QRcomment();
                
                qrcomment.id = uuidv4();
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
            if(element.id === body.parent)
            {
                // found, insert
                var qrcomment = new QRcomment();
                
                qrcomment.id = uuidv4();
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

router.post('/:url', middleware.checkToken, (req, res, next)=>
{

    // correct way to reach this function
    // RESTurl: https://localhost/qr/addComment/d2509e92-d56f-4607-a99d-f8a2c628bea9
    // request method: POST
    // body: 
    // {
    //     "parent" : "f6f052ba-a651-43a2-8d30-bdd7d950997a",
    //     "comment" : "Second level comment"
    // }

    // if no parent is present, comment is added to root level
    // parent is the comment's parent id

    // returns:
    // whole document

    console.log("inside addcomment");
    const email = req.decoded.email;
    
    // find the user in the database
    User.findOne({'email' : email})
    .exec()
    .then(u =>{
        if(u != null && u != undefined)
            // u exists
            console.log('')
        else
            res.status(404).json({'error': 'the specified user is not found'});               
        
        usr = new QRdocinfo()
        usr.id = u.id;
        usr.email = u.email;

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
                qrcomment.id = uuidv4();
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
    
})

module.exports = router;