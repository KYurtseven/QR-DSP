const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const Deneme = require('../models/deneme');
//const JSON = require('circular-json');

router.post('/', (req,res,next) => {

    console.log('inside denemes');
    console.log('req.body ' + JSON.stringify(req.body));

    console.log("field: " + JSON.stringify(req.body.d2enemeField));

    const deneme = new Deneme({
        _id: new mongoose.Types.ObjectId(),
        name: req.body.name,
        password: req.body.password,
        d2enemeField: req.body.d2enemeField,
        hmm : req.body.hmm
    });
    deneme
    .save()
    .then(result => {
        console.log(result);
    })
    .catch(err => console.log(err));
    
    res.status(201).json({
        message: "Handling POST request to /deneme",
        createdProduct: deneme
    });
});

module.exports = router;