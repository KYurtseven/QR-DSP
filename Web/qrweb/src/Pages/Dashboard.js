import React, { Component } from 'react'
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import Header from '../components/Header';

class Dashboard extends Component {
  render() {
    return (
      <div>
        <Header/>
          <p>Dashboard</p>
      </div>
    );
  }
}

const styles = theme => ({
  hi: {
    marginTop: 25
  }
});


Dashboard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Dashboard);