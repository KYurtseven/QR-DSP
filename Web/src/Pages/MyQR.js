import React, { Component } from 'react';
import {
  Redirect,
} from 'react-router-dom'

import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Header from '../components/Header'
import {drawerMargins} from '../_helpers/Constants';
import {MOCK_USER_QR_DOC} from '../MockData/UserQRdoc';

import UserQRItem from '../components/UserQRItem';

class MyQR extends Component {
  
  constructor(props)
  {
    super(props);
    this.state={
      redirectToReferrer: false,
      isLoading: true,
      userqr: []
    }
  }


  componentWillMount()
  {
    // TODO
    // fetch data from server
    let userqr = MOCK_USER_QR_DOC;
    
    this.setState({isLoading: false, userqr: userqr})
  }

  renderDocs(type)
  {
    let row = []
    this.state.userqr[type].forEach(function(element, i){
      row.push(
        <UserQRItem
          key = {i}
          data = {element} />
      );
    });
    return row;
  }

  render() {
    const { from } = this.props.location.state || { from: { pathname: '/' } }
    const {classes} = this.props;
    const { redirectToReferrer } = this.state
    

    if (redirectToReferrer === true){
      return <Redirect to={from} />
    }
    
    if(this.state.isLoading){
      return (<p>Loading</p>);
    }
    let own_row = this.renderDocs('o_docs');
    let edit_row = this.renderDocs('e_docs');
    let view_row = this.renderDocs('v_docs');
    

    return (
      <div>
          <Header />
          <div className={classes.drawerMargins}>
            Owned documents:
            {own_row}
            Edit documents:
            {edit_row}
            View documents:
            {view_row}

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
