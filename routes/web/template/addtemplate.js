const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../../../config');
const middleware = require('../../../middleware');

const {Company} = require('../../../models/company');
const {Template} = require('../../../models/template');

router.post('/', middleware.checkToken, (req, res, next) =>
{
    // check if company exists
    

})

module.exports = router;