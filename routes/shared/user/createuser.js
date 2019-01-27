const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');

const User = require('../../../models/user');


router.post('/', (req,res,next) => {
    console.log("inside create user");
    // TODO
    // Check company in the database
    // If the company exists, and mail matches then add
    // How about asd@fordnissanopel.com ?
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
module.exports = router;