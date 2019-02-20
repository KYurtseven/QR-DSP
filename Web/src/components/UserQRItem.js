import React, { Component } from 'react';
import {
    Redirect,
    Link
  } from 'react-router-dom'
  
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import AppBar from '@material-ui/core/AppBar';
import Divider from '@material-ui/core/Divider';
import Button from '@material-ui/core/Button';

import history from '../_helpers/history';

class UserQRItem extends Component
{

    constructor(props){
        super(props);
        this.state={
            redirect: false,
        };
        this.handleRedirect = this.handleRedirect.bind(this);
    }

    handleRedirect(){
        this.setState({redirect: true});
    }

    renderOperation()
    {
        if(this.state.redirect)
        {
            const {data, type} = this.props;
            console.log('type: ' + type);
            if(type === "view")
            {
                // TODO
                // go to /viewqr/url part
                const qrUrl = '/viewqr/' + data.url;
                history.push(qrUrl);
                return <Redirect to={qrUrl} />
                //
                //console.log('if type: ' + type);
            }
            else
            {
                console.log('else type: ' + type);
                console.log('TODO userQR item');
            }
        }
    }

    render()
    {
        const {data} = this.props;

        return(
            <div style= {{width: '100%'}}>
            <ExpansionPanel>
                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                    <Typography style={{fontSize: 15}}>{data.name}</Typography>
                </ExpansionPanelSummary>
                
                <ExpansionPanelDetails>
                
                <AppBar position="static" color="default">
                    {data.url}
                    {this.renderOperation()}
                    <Button onClick={this.handleRedirect} color="secondary">
                        View
                    </Button>
                </AppBar>

                </ExpansionPanelDetails>
            </ExpansionPanel>
            <Divider light />

            </div>
        );
    }
}


export default UserQRItem;