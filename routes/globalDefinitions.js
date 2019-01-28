const DeploymentTypeCode = {
    Test:0,
    Prod:1,
    Local:2
}
   
const DEPLOYMENT_TYPE = DeploymentTypeCode.Local;

var methods = {
    getRoot : function(){
        switch(DEPLOYMENT_TYPE)
        {
            case DeploymentTypeCode.Test:
                return "TEST";
            case DeploymentTypeCode.Prod:
                return "PROD";
            case DeploymentTypeCode.Local:
                return "LOCAL";
        }
    }
}


module.exports = methods;
