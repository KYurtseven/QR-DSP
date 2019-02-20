import React from 'react'
import {
  BrowserRouter as Router,
  Route,
} from 'react-router-dom';

import Home from './Pages/Home';
import Login from './Pages/Login';
import Dashboard from './Pages/Dashboard';
import MyQR from './Pages/MyQR';
import ViewQR from './Pages/ViewQR';
import history from './_helpers/history';

import {PrivateRoute} from './components/PrivateRoute';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#E91E63', //pink500
    },
    secondary: {
      main: '#E91E63', //pink500
      // dark: will be calculated from palette.secondary.main,
    },
  },
});

const Protected = () => <h3>Protected</h3>

/*
  <ul style={{marginTop: 150}}>
    <li><Link to="/dashboard">Dashboard, private</Link></li>
    <li><Link to="/login">Login</Link></li>
    <li><Link to="/protected">Protected Page</Link></li>
  </ul>
*/
class App extends React.Component { 

  render()
  {
    return(
      <div>
        <MuiThemeProvider theme={theme}>
        <Router history = {history}>
          <div>
            <Route exact path="/" component={Home} />
            <Route path="/login" component={Login}/>
            <PrivateRoute path="/dashboard" component={Dashboard}/>
            <PrivateRoute path='/protected' component={Protected}/>
            <PrivateRoute path='/myqr' component={MyQR}/>
            <PrivateRoute path='/viewqr/:url' component={ViewQR} />
          </div>
        </Router>
        </MuiThemeProvider>
      </div>
  );
  }
}

export default App;
