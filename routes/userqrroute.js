const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const user_qr = require('../models/userqr');

router.post('/create', (req,res,next) => {

    console.log("post");

    const userqrdoc = new user_qr({
        _id: new mongoose.Types.ObjectId(),
        user_id: req.body.user_id,
        email: req.body.email,
        o_docid: req.body.o_docid,
        e_docid: req.body.e_docid,
        v_docid: req.body.v_docid
    });
    userqrdoc
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

router.get('/get/:user_id', (req,res,next) =>{
    var userqr;

    user_qr.findOne({'user_id': req.params.user_id})
    .exec()
    .then( doc =>{
        console.log(doc);
        userqr = doc;
        var result={};
        result.user_id = userqr.user_id;
        result.email = userqr.email;
        result.o_docid = userqr.o_docid;
        result.e_docid = userqr.e_docid;
        result.v_docid = userqr.v_docid;

        res.status(200).json(result);
    })
    .catch(err => {
        console.log(err);
        res.status(500).json({error: err});
    })
});

router.get('/getAllOwn/:user_id', (req, res, next) =>{
    var userqr;

    user_qr.findOne({'user_id': req.params.user_id})
    .exec()
    .then( doc =>{
        console.log(doc);
        console.log("-----------------------------");
        var result={};
        result.o_docid = doc.o_docid;
        console.log("o_docid: " + result.o_docid);
        res.status(200).json(result);
    })
    .catch(err => {
        console.log(err);
        res.status(500).json({error: err});
    })
});

router.get('/getAllEdit/:user_id', (req, res, next) =>{
    var userqr;

    user_qr.findOne({'user_id': req.params.user_id})
    .exec()
    .then( doc =>{
        console.log(doc);
        console.log("-----------------------------");
        var result={};
        result.e_docid = doc.e_docid;
        console.log("e_docid: " + result.e_docid);
        res.status(200).json(result);
    })
    .catch(err => {
        console.log(err);
        res.status(500).json({error: err});
    })
});

router.get('/getAllView/:user_id', (req, res, next) =>{
    var userqr;

    user_qr.findOne({'user_id': req.params.user_id})
    .exec()
    .then( doc =>{
        console.log(doc);
        console.log("-----------------------------");
        var result={};
        result.v_docid = doc.v_docid;
        console.log("v_docid: " + result.v_docid);
        res.status(200).json(result);
    })
    .catch(err => {
        console.log(err);
        res.status(500).json({error: err});
    })
});

module.exports = router;