const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');

const User = require('../../../models/user');
const {UserQR} = require('../../../models/userqr');

const {SUCCESS, FAIL, signupResponse} = require('../../../helpers/responses');

/**
 * When the user is created in the database,
 * this function creates a UserQR object in db
 * Later, this document can be used for retrieving all of documents
 * of the user with one simply query
 * @param {*} uid 
 * id of user
 * @param {*} email 
 * email of the user
 */
function createUserQR(uid, email){
    let userqr = new UserQR({
        _id : new mongoose.Types.ObjectId(),
        owner_id : uid,
        email : email,
        o_docs : [],
        e_docs : [],
        v_docs : []
    });

    return userqr.save()
    .then(saveddoc=>{
        console.log("userqr is created");
    }).catch(err=>{
        console.log("err: " + err);
        console.log("cannot save");
    })
}


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

    user
    .save()
    .then(doc =>{
        createUserQR(user.id, user.email)
        .then(savedDoc=>{
            signupResponse(res, SUCCESS,"");
        })
        .catch(err=>{
            signupResponse(res, FAIL, err);
        })
    })
    .catch(err => {
        signupResponse(res, FAIL, err);
    })
});
module.exports = router;