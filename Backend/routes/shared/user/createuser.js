const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');

const User = require('../../../models/user');


router.post('/', (req,res,next) => {
    console.log("inside create user");
    // TODO
    // Check company in the database
    
    // TODO
    // validate user from the company(either PS Yazilim or nissan, ford..), not from us
    // if the company and the user exists, we can execute his queries
    // TODO
    // validate user's email before passing token

    // TODO
    // create a table for holding all of the users
    // when a user is created, add user to the this table

    // TODO
    // when a user is created, create its userqr document

    // correct way to reach this function
    // RESTurl: https://localhost/api/user/signup
    // request method: POST
    // body: 
    // {
    //     "username" : "edyth1",
    //     "password" : "123123abc",
    //     "fullname" : "Edyth",
    //     "company": "Ford",
    //     "email" : "edyth1@ford.com"
    // }

    const user = new User({
        _id         : new mongoose.Types.ObjectId(),
        id         : uuidv4(),
        fullname    : req.body.fullname,
        username    : req.body.username,
        email       : req.body.email,
        company     : req.body.company.toUpperCase()
    })
    user.setPassword(req.body.password);
    console.log(JSON.stringify(user));
    user
    .save()
    .then(doc =>{
        console.log(doc);
        // TODO
        // return meaningfull data
        res.status(200).json(doc);
    })
    .catch(err => {
        console.log(err);
        res.status(500).json({error: err});
    })
});
module.exports = router;