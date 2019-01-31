const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');
const jwt = require('jsonwebtoken');
const config = require('../../../config');
const middleware = require('../../../middleware');

const {Company,CompanyTemplateListElement} = require('../../../models/company');
const Template = require('../../../models/template');

router.post('/', middleware.checkToken, (req, res, next) =>
{
    // TODO
    // not every company user allow to add a new template

    // correct way to reach this function
    // RESTurl: https://localhost/api/web/createTemplate
    
    // request method: POST
    // body: 
    // {
    //  "name": "Visible template name",
    //  "body": {
    //         "field1": "a field",
    //         "field2": ["hi", "hello"]
    //      }
    // }
    console.log("req:" + JSON.stringify(req.decoded));
    console.log("inside add template");

    const company = req.decoded.company.toUpperCase();
    
    const template = new Template({
        _id         : new mongoose.Types.ObjectId(),
        id          : uuidv4(),
        name        : req.body.name,
        body        : req.body.body,
        company     : company
    });

    template.save()
    .then(doc =>{
        const tid = doc.id;
        Company.findOne({'name' : company}).exec()
        .then(com=>{
            console.log("com: " + JSON.stringify(com));
            console.log("tid: " + tid);

            var tmp = new CompanyTemplateListElement({
                name : req.body.name,
                id : tid
            });
            com.templates.push(tmp);

            com.save().then(doc2=>{
                res.status(200).json();
            })
            .catch(err =>{res.status(500).json({error: err})})
            
        })
        .catch(err =>{res.status(500).json({error: err})})
    })
    .catch(err =>{res.status(500).json({error: err})})

})

module.exports = router;