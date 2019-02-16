import React from 'react'
import {
  Redirect,
} from 'react-router-dom'
// HELPERS
import {handleResponse} from '../_helpers/handleResponse';
import * as API from '../_helpers/apiHelpers';
import {setCookie} from '../_helpers/cookieHelper';

import * as Constants from '../_helpers/Constants';

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
            error: '',
            isLoggingIn: false,
            isValid : true,
            userInfo : {},
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onChange(event)
    {
        this.setState({[event.target.name]: event.target.value});
    }

    async onSubmit(e)
    {
        e.preventDefault();

        this.setState({isLoggingIn: true});

        let url =   Constants.getRoot() + Constants.login;
        const j_input = {
            username: this.state.username,
            password: this.state.password
        } 
        await API.CallApiPostWithoutToken(url, j_input)
        .then(handleResponse)
        .then(data=>{
            const token = data.token;
            setCookie('token', token, '/');
            setCookie('username', this.state.username, '/');
            this.setState({
                isLoggingIn: false,
                redirectToReferrer: true
            });
        })
        .catch(err =>{
            this.setState({
                error: err,
                isLoggingIn: false
            });
        })
    }

    renderUsernameField()
    {
        return(
            <form style={{display: 'flex',flexWrap: 'wrap',}} noValidate>
                <TextField
                    name = "username"
                    label="Username"
                    type="text"
                    onChange={this.onChange}
                    style={{flex:1}}
                    InputLabelProps={{shrink: true}}
                />
            </form>
        );
    }
    renderPasswordField(){
        return(
            <form style={{display: 'flex',flexWrap: 'wrap',}} noValidate>
                <TextField
                    name = "password"
                    label="Password"
                    type="password"
                    onChange={this.onChange}
                    style={{flex: 1}}
                    InputLabelProps={{
                        shrink: true,
                    }}
                />
            </form>
        );
    }

    render() {
        const { from } = this.props.location.state || { from: { pathname: '/' } }
        const { redirectToReferrer } = this.state
        const { classes } = this.props;
        if (redirectToReferrer === true)
            return <Redirect to={from} />

        // TODO
        // if user logged in, do not open login page again
        
        return(
            <div style = {{display: 'flex', justifyContent: 'center', marginTop: 250}} >
                <Card className={classes.card}>
                    <CardContent>
                        {this.renderUsernameField()}
                        <div style={{marginTop:20}}/>
                        {this.renderPasswordField()}
                    </CardContent>

                    <div className={classes.wrapper}>                
                        {this.state.error 
                        // TODO
                        // better UI
                        }
                    </div>

                    <CardActions style={{float: 'right'}}>
                        <div className={classes.wrapper}>

                            <Button 
                            onClick={this.onSubmit}
                            color="secondary" 
                            autoFocus
                            //TODO
                            // better isValid
                            disabled={!this.state.isValid || this.state.isLoggingIn}
                            >
                                Submit
                            </Button>
                            
                            {this.state.isLoggingIn && 
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
