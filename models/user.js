const mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');
var crypto = require('crypto');

const userSchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    // TO DO
    // create id from uuid everytime a user created
    id: {
        type: String, 
        required: [true, "can't be blank"]
    },
    fullname: {
        type: String, 
        required: [true, "can't be blank"]
    },
    username: {
        type: String, 
        required: [true, "can't be blank"],
        unique: true,
        match: [/^[a-zA-Z0-9]+$/, 'is invalid'],
        index: true
    },
    email: {
        type: String, 
        unique: true ,
        required: [true, "can't be blank"], 
        match: [/\S+@\S+\.\S+/, 'is invalid'],
        index: true
    },
    company: String,
    // TO DO
    // store password in hash and add salt to table
    hash: String,
    salt: String
},{collection : 'T_USER', timestamps : true} );

// for storing hashed password
userSchema.methods.setPassword = function(password){
    this.salt = crypto.randomBytes(16).toString('hex');
    this.hash = crypto.pbkdf2Sync(password, this.salt, 10000, 512, 'sha512').toString('hex');
}

userSchema.methods.validPassword = function(password){
    var hash = crypto.pbkdf2Sync(password, this.salt, 10000, 512, 'sha512').toString('hex');

    return this.hash === hash;
}

userSchema.plugin(uniqueValidator, {message: 'is already taken.'});
module.exports = mongoose.model('User', userSchema);