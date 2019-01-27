const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../../../config');
const middleware = require('../../../middleware');

const Company = require('../../../models/company');
const Template = require('../../../models/template');

router.post('/', middleware.checkToken, (req, res, next) =>
{
    // TODO
    // only admin can add company right now
    // this code will be commented out after initial companies are added
    const prod = false;

    if(prod)
    {
        res.status(403).json();
    }
    else
    {
        // check if company exists   
        
        // correct way to reach this function
        // RESTurl: https://localhost/createcompany
        // request method: POST
        // body: 
        // {
        // "name": "FOrD"
        // }
        var name = req.body.name.toUpperCase();
        const company = new Company({
            _id         : new mongoose.Types.ObjectId(),
            name        : name,
            templates   : []
        })

        company
        .save()
        .then(doc =>{
            console.log(doc);
            res.status(200).json(doc);
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({error: err});
        })
    }
})

module.exports = router;
