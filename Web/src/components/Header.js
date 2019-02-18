import React from 'react';
// material ui
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { withRouter } from 'react-router';
import Drawer from '@material-ui/core/Drawer';
import CssBaseline from '@material-ui/core/CssBaseline';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';
import { withStyles } from '@material-ui/core';

import logo from '../img/qrdsp_logo.png'
import ExitToApp from '@material-ui/icons/ExitToApp'

import PropTypes from 'prop-types';
import { getCookie, deleteAllCookies } from '../_helpers/cookieHelper';
import {drawerWidth} from '../_helpers/Constants';
import qrcode from '../img/qrcode.png';
import dashboard from '../img/dashboard.svg';

class Header extends React.Component
{  
    constructor(props)
    {
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
        this.handleMyQR = this.handleMyQR.bind(this);
        this.handleDashboard = this.handleDashboard.bind(this);
    }

    componentWillMount()
    {
        const username = getCookie('username');
        this.setState({username : username});
        console.log('username: ' + username);
    }

    handleLogout()
    {
        // TODO
        console.log('logout');
        deleteAllCookies(()=> this.props.history.push('/'));
    }

    handleMyQR()
    {
      if(this.props.history.location.pathname !== '/myqr')
        this.props.history.push('/myqr');
    }

    handleDashboard()
    {
      if(this.props.history.location.pathname !== '/dashboard')
        this.props.history.push('/dashboard');
    }

    render()
    {
        const {classes} = this.props;

        return (
            <div className={classes.root}>
              <CssBaseline />
                <AppBar position="fixed" className={classes.appBar}>
                  <Toolbar>
                      <Typography variant="title" color="inherit"
                          style={{flex : 1, }}>
                          Project Synergy
                      </Typography>
                  </Toolbar>
              </AppBar>
              <Drawer
                className={classes.drawer}
                variant="permanent"
                classes={{paper: classes.drawerPaper}}
                anchor = 'left'
              >
                <div className={classes.toolbar}>
                  <div className={classes.logo}>  
                    
                    <img src ={logo} style={{height: '50px'}} alt='logo'/>

                    <h5>Welcome</h5>
                    <Typography variant="title" color="inherit">
                         {this.state.username}
                    </Typography>
                  </div>
                </div>

                <List>
                  {['All mail', 'Trash', 'Spam'].map((text, index) => (
                    <ListItem button key={text}>
                      <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <MailIcon />}</ListItemIcon>
                      <ListItemText primary={text} />
                    </ListItem>
                  ))}
                </List>
                
                <ListItem button key="dashboard" onClick={this.handleDashboard}>
                  <ListItemIcon>
                    <div style={{height:24, width:24}}>
                      <img src={dashboard} alt="dashboard icon" className={classes.customlogo}/>
                    </div>  
                  </ListItemIcon>
                  <ListItemText primary="Dashboard" />
                </ListItem>

                
                <ListItem button key="myqr" onClick={this.handleMyQR}>
                  <ListItemIcon>
                    <div style={{height:24, width:24}}>
                      <img src ={qrcode} className={classes.customlogo} alt ='qr icon'/>
                    </div>  
                  </ListItemIcon>
                  <ListItemText primary="My QR Codes" />
                </ListItem>

                <Divider />

                <ListItem button key="logout" onClick={this.handleLogout}>
                    <ListItemIcon>
                        <ExitToApp />
                    </ListItemIcon>
                    <ListItemText primary="Log out" />
                </ListItem>

              </Drawer>
             </div>
          );
    }
  
}

const styles = theme => ({
    root: {
        display: 'flex',
      },
      appBar: {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: drawerWidth,
      },
      drawer: {
        width: drawerWidth,
        flexShrink: 0,
      },
      drawerPaper: {
        width: drawerWidth,
      },
      toolbar: theme.mixins.toolbar,
      content: {
        flexGrow: 1,
        backgroundColor: theme.palette.background.default,
        padding: theme.spacing.unit * 3,
      },
      logo:{
        backgroundColor: 'red',  
      },
      customlogo:{
        height: '18px',
        marginLeft:'3px',
        marginRight:'3px',
        marginBottom:'3px',
        marginTop: '3px'
      }
});

Header.propTypes = {
    classes: PropTypes.object.isRequired
};

export default withStyles(styles)(withRouter(Header));