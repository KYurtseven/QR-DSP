const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../../../config');

const User = require('../../../models/user');

const Invalid = {
    success: false,
    message: 'Incorrect username or password'
};

router.post('/', (req,res,next) => {
    
    // correct way to reach this function
    // RESTurl: http://localhost:3000/api/user/login
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
    //     "user":{
    //         "username":"edyth1",
    //         "fullname": "Some Full name",
    //         "email" : "email@email.com",
    //         "company": "Ford"
    //      }
    // }
    let username = req.body.username;
    let password = req.body.password;

    User.findOne({'username' : username})
    .exec()
    .then(doc =>{
        if(doc)
        {
            if(doc.validPassword(password))
            {
                // TODO
                // Set cookies etc.
                let token = jwt.sign({
                    id: doc.id, 
                    email: doc.email,
                    company: doc.company
                },
                    config.secret,
                    { expiresIn: 30*60*60*24 // expires in 30 days
                    }
                );
                res.status(200).json({
                    success: true,
                    message: 'Authentication successful!',
                    token: token,
                    user:{
                        username: doc.username,
                        fullname: doc.fullname,
                        email : doc.email,
                        company: doc.company
                    }
                });
            }
            else
                res.status(401).json(Invalid);
        }
        else
            res.status(401).json(Invalid);
    })
    .catch(err =>{
        res.send(500).json({
            success: false,
            message: 'Authentication failed! Please check the request'
        });
    })
})

module.exports = router;