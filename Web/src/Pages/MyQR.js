import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Header from '../components/Header'
import {drawerMargins} from '../_helpers/Constants';


class MyQR extends Component {
  render() {
    const {classes} = this.props;

    return (
      <div>
          <Header />
          <div className={classes.drawerMargins}>
            Hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiissssssssssssssssssssssssssssssssssssss
          </div>
          
      </div>
    )
  }
}

const styles = theme => ({
    drawerMargins: drawerMargins
});


MyQR.propTypes = {
    classes: PropTypes.object.isRequired
};


export default withStyles(styles)(MyQR);
