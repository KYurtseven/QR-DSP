import React, { Component } from 'react';

import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import AppBar from '@material-ui/core/AppBar';
import Divider from '@material-ui/core/Divider';
class UserQRItem extends Component
{

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
                </AppBar>

                </ExpansionPanelDetails>
            </ExpansionPanel>
            <Divider light />

            </div>
        );
    }
}


export default UserQRItem;