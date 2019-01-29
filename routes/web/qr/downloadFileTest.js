const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const uuidv4 = require('uuid/v4');

const User = require('../../../models/user');
const imagepath = "./IMAGES/";

router.get('/', (req,res,next) => {

    console.log("downlaod 1.png");

    console.log("inside download test file");
    //res.status(200).json({});
    res.download(imagepath + '1.png');
});

module.exports = router;