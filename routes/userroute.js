const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../config');
const middleware = require('../middleware');
const User = require('../models/user');
const uuidv4 = require('uuid/v4');

router.post('/create', (req,res,next) => {

    const user = new User({
        _id         : new mongoose.Types.ObjectId(),
        id         : uuidv4(),
        fullname    : req.body.fullname,
        username    : req.body.username,
        email       : req.body.email,
        company     : req.body.company
    })
    user.setPassword(req.body.password);
    console.log(JSON.stringify(user));
    user
    .save()
    .then(doc =>{
        console.log(doc);
        res.status(200).json(doc);
    })
    .catch(err => {
        console.log(err);
        res.status(500).json({error: err});
    })
});

router.post('/login', (req,res,next) => {
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

router.get('/tokentest', middleware.checkToken, (req,res,next) =>{
    myresponse = {};
    myresponse['body'] = "token test works"
    res.status(400).json(myresponse);
})

module.exports = router;