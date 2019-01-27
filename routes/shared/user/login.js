const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');
const jwt = require('jsonwebtoken');
const config = require('../../../config');

const User = require('../../../models/user');

router.post('/', (req,res,next) => {
    
    // correct way to reach this function
    // RESTurl: https://localhost/login
    // request method: POST
    // body: 
    // {
    //     "username" : "edyth1",
    //     "password" : "123123abc"
    // }

    // returns:
    // {
    //     "success": true,
    //     "message": "Authentication successful!",
    //     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImVkeXRoMSIsImVtYWlsIjoiZWR5dGgxQGZvcmQuY29tIiwiaWF0IjoxNTQ4NTg3MDk4LCJleHAiOjE1NDg2NzM0OTh9.bAqrKcW7NSOP5a6LFJfyS9Oj83ged-AA35kbPBNUAyE"
    // }

    console.log("inside login");
    let username = req.body.username;
    let password = req.body.password;

    User.findOne({'username' : username})
    .exec()
    .then(doc =>{
        if(doc.validPassword(password))
        {
            //console.log("doc: " + JSON.stringify(doc));
            // TODO
            // Set cookies etc.
            let token = jwt.sign({username: username, email: doc.email},
                config.secret,
                { expiresIn: '24h' // expires in 24 hours
                }
            );
            res.status(200).json({
                success: true,
                message: 'Authentication successful!',
                token: token
            });

            //res.status(200).json(doc);
        }
        else{
            // invalid credentials
            res.send(403).json({
                success: false,
                message: 'Incorrect username or password'
            });
        }
    })
    .catch(err =>{
        console.log(err);
        res.send(400).json({
            success: false,
            message: 'Authentication failed! Please check the request'
        });
    })
})

module.exports = router;