export const IS_MOCK = false;

export const LOCAL_ROOT = 'https://localhost/';
export const TEST_ROOT = 'TODO';
export const PROD_ROOT = 'TODO';

const DeploymentTypeCode = {
    Test:0,
    Prod:1,
    Local:2
}

const DEPLOYMENT_TYPE = DeploymentTypeCode.Local;

export function getRoot(){
    switch(DEPLOYMENT_TYPE)
    {
        case DeploymentTypeCode.Test:
            return TEST_ROOT;
        case DeploymentTypeCode.Prod:
            return PROD_ROOT;
        case DeploymentTypeCode.Local:
            return LOCAL_ROOT;
        default:
            return;
    }
}

export const drawerWidth = 240;

export const drawerMargins ={
    marginLeft: drawerWidth + 10,
    marginTop: 75

}

export const login = 'api/user/login/';
export const viewqr = 'api/web/qr/view/';