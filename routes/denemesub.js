// In this folder, I'll try to create a nested document

const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const Denemesub = require('../models/denemesub');

router.post('/', (req,res,next) => {

    console.log('inside denemesub');
    console.log('req.body ' + JSON.stringify(req.body));

    const denemesub = new Denemesub({
        _id : new mongoose.Types.ObjectId(),
        name : req.body.name,
        isPublic: req.body.isPublic,
        o_info : req.body.o_info,
        e_info : req.body.e_info,
        data : req.body.data
    })
    denemesub
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


router.get('/:name', (req,res,next) => {

    console.log('inside denemesub get');

    const name = req.params.name;
    Denemesub.findOne({'name' : name})
    //
    //

    .exec()
    .then(doc =>{
        console.log(doc);
        res.status(200).json(doc);
    })
    .catch(err =>{
        console.log(err);
        res.status(500).json({'error': err});
    })

})
module.exports = router;