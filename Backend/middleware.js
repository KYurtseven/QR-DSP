let jwt = require('jsonwebtoken');
const config = require('./config.js');

let checkToken = (req, res, next) => {
  let token = req.headers['x-access-token'] || req.headers['authorization']; // Express headers are auto converted to lowercase
  if (token.startsWith('Bearer ')) {
    // Remove Bearer from string
    token = token.slice(7, token.length);
  }

  if (token) {
    jwt.verify(token, config.secret, (err, decoded) => {
      if (err) {
        return res.json({
          success: false,
          message: 'Token is not valid'
        });
      } else {
        req.decoded = decoded;
        next();
      }
    });
  } else {
    return res.json({
      success: false,
      message: 'Auth token is not supplied'
    });
  }
};

// TODO
// this function is for viewing without token
// rename it
function checkTokenFunction(req)
{   
    // We need token check for some public end points
    try
    {
        let token = req.headers['x-access-token'] || req.headers['authorization']; // Express headers are auto converted to lowercase
        if (token.startsWith('Bearer ')) {
            // Remove Bearer from string
            token = token.slice(7, token.length);
        }
        if (token) {
            return jwt.verify(token, config.secret, (err, decoded) => {
                if (err) {
                    return false;
                } 
                else {
                    req.decoded = decoded;
                    return true;
                }
            });
        } 
        else {
            return false;
        }
    }
    catch(err){
        console.log("Error in checktoken function: " + err);
        return false;
    }

}

module.exports = {
  checkToken: checkToken,
  checkTokenFunction: checkTokenFunction
}