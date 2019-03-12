
const SUCCESS = "success";
const FAIL = "fail";

function signupResponse(res, type, details){

    if(type === SUCCESS){
        res.status(200).json({
            message: "User is created"
        });
    }
    else if(type === FAIL){
        if(details.name === "ValidationError"){
            res.status(409).json({
                message: "Username or email is already taken"
            });
        }
        else{
            console.log("signup response error" + details);
            res.status(500).json({
                message: "Unknown error"
            });
        }
    }
    else{
        // TODO
        console.log("signup type error" + details);
        res.status(500).json({
            message: "Unknown error"
        })
    }
}

module.exports = {
    SUCCESS : SUCCESS,
    FAIL : FAIL,
    signupResponse : signupResponse
}