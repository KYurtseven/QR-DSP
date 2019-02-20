import React, { Component } from 'react'
import {
  Link
} from 'react-router-dom';

class Home extends Component {
  render() {
    return (
      <div>
				<ul style={{marginTop: 150}}>
					<li><Link to="/login">Login</Link></li>
					<li><Link to="/dashboard">Dashboard</Link></li>
					<li><Link to="/myqr">List of QR</Link></li>
				</ul>
      </div>
    )
  }
}

export default Home;