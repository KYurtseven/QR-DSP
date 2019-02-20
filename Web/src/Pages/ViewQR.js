import React, { Component } from 'react'
import {
  Redirect,
} from 'react-router-dom'

import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import {drawerMargins} from '../_helpers/Constants';

import * as Constants from '../_helpers/Constants';
import * as API from '../_helpers/apiHelpers';
import {getCookie} from '../_helpers/cookieHelper';
import {handleResponse} from '../_helpers/handleResponse';
import Header from '../components/Header'

class ViewQR extends Component {

	constructor(props){
		super(props);
		this.state={
			docUrl: "",
			isLoading: true,
			qrData : {}
		};

	}

	async componentWillMount()
	{
		console.log('Mounting viewqr');
		const fullUrl = this.props.match.url;
		const docUrl = fullUrl.split('/')[2]; // to get rid of /viewqr part
		this.setState({docUrl: docUrl});
		const token = getCookie('token');

		const url = Constants.getRoot() + Constants.viewqr + docUrl;
		
		let response = await API.CallApiGet(url, token)
		.then(handleResponse)
		.then(data =>{
			console.log('data: ' + JSON.stringify(data));
			this.setState({qrData: data, isLoading: false});
		})
		console.log('url: ' + url);
	}

	render() {
		const { from } = this.props.location.state || { from: { pathname: '/' } }
		const { redirectToReferrer } = this.state
		const { classes } = this.props;
		if (redirectToReferrer === true)
			return <Redirect to={from} />

		if(this.state.isLoading){
			return (<p>Loading</p>);
		}

		return (
			<div>
				<Header />
				<div className={classes.drawerMargins}>
					VIEW
					{JSON.stringify(this.state.qrData)}
				</div>			
			</div>
		)
	}
}
const styles = theme => ({
	drawerMargins: drawerMargins
});


ViewQR.propTypes = {
	classes: PropTypes.object.isRequired
};


export default withStyles(styles)(ViewQR);
