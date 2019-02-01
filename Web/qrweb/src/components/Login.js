import * as fakeAuth from '../GlobalPages/fakeauth';
import React from 'react'
import {
  BrowserRouter as Router,
  Redirect,
} from 'react-router-dom'

import Cookies from "universal-cookie";

import * as Constants from '../GlobalPages/Constants';
//import * as BasePage from './BasePage';

import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import CircularProgress from '@material-ui/core/CircularProgress';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';


class Login extends React.Component {
    
    constructor(props)
    {
        super(props);
        this.state={
            redirectToReferrer: false,
            username : '',
            password : '',
            isLoading : false,
            isValid : false,
            userInfo : {},
        }
        this.handleOnChange = this.handleOnChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleOnChange(event)
    {
        this.setState({[event.target.name]: event.target.value});
        //this.verifyInput();
    }

    verifyInput()
    {
        console.log('verify');
        if(this.state.username.length > 0
            && this.state.password.length > 0)
        {
            this.setState({isValid: true});
        }
        else
            this.setState({isValid: false});
    }

    setCookies(token)
    {
        const cookies = new Cookies();
        console.log('set cookies');
        //cookies.set('token', token, {path : '/'})
    }


    async handleSubmit(event)
    {
        if(this.state.isLoading)
            return;

        this.setState({isLoading: true});
        
        if(Constants.IS_MOCK)
        {
            // TODO
            //let userInfo = Constants.MOCK_LOGIN_RESPONSE;
            this.setState({isLoading: false});
            //this.setCookies(userInfo);
        }
        else
        {
            let url =   Constants.getRoot() + Constants.login;
            const userinputbody = {
                username: this.state.username,
                password: this.state.password
            }
            let response = await fetch(url, {
                method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(userinputbody)
                })
                .then(res => res.json())
                .then(data =>{
                    if(data.success)
                    {
                        console.log('Successful');
                        const token = data.token;
                        this.setCookies(token);
                        this.setState({isLoading:false});
                    }
                    else{
                        console.log('Unsuccessful');
                        this.setState({isLoading: false});
                    }
                })
                .catch((err) => {
                    console.log('err: ' + err);
                    return null;
            })

            /*
            try
            {
                let res = await BasePage.CallApiGet(url);

                if(res.status === 200 && res.error !== '')
                {
                    let restext = await res.text();
                    let resJSON = JSON.parse(restext);

                    this.setState({isLoading: false});
                    this.setCookiesAndPushRoute(resJSON);
                }
                else
                    throw(Constants.getNot200);
            }
            catch(e)
            {
                this.setState({error: e.toString()});
                console.log('Error on fetching data: ' + e);
                this.setState({isLoading: false});
            }
            */
        }
        this.setState({isLoading: false});
    }

    login()
    {
        fakeAuth.fakeAuth.authenticate(() =>{
            this.setState({redirectToReferrer: true})
        })
    }

    render() {
        const { from } = this.props.location.state || { from: { pathname: '/' } }
        const { redirectToReferrer } = this.state
        const { classes } = this.props;
        if (redirectToReferrer === true) {
            return <Redirect to={from} />
        }
    /*
    return (
        <div>
            <p>You must log in to view the page</p>
            <button onClick={this.login}>Log in</button>
        </div>
    )
    */
    return(
        <div style = {{display: 'flex', justifyContent: 'center', marginTop: 250}} >
            <Card className={classes.card}>
                <CardContent>
                    <form style={{display: 'flex',flexWrap: 'wrap',}} noValidate>
                        <TextField
                            name = "username"
                            label="Username"
                            type="text"
                            onChange={this.handleOnChange}
                            style={{flex:1}}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </form>
                    <div style={{marginTop:20}}/>
                    <form style={{display: 'flex',flexWrap: 'wrap',}} noValidate>
                        <TextField
                            name = "password"
                            label="Password"
                            type="password"
                            onChange={this.handleOnChange}
                            style={{flex: 1}}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </form>
                </CardContent>

                <CardActions style={{float: 'right'}}>
                    <div className={classes.wrapper}>

                        <Button 
                        onClick={this.handleSubmit}
                        color="secondary" 
                        autoFocus
                        disabled={!this.state.isValid || this.state.isLoading}
                        >
                            Submit
                        </Button>
                        
                        {this.state.isLoading && 
                            <CircularProgress 
                                size={24} 
                                className={classes.buttonProgress} 
                            />
                        }
                    </div>
                </CardActions>
            </Card> 
        </div>
    );

    }
}

const styles = theme => ({
    card: {
        minWidth: 500,
        alignSelf : 'flex-end'
    },
    buttonProgress: {
        color: 'secondary',
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -12,
        marginLeft: -12,
    },
    wrapper: {
        margin: theme.spacing.unit,
        position: 'relative',
      },
});


Login.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Login);
