import React from 'react'
import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom';

import Login from './Pages/Login';
import Dashboard from './Pages/Dashboard';

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

class App extends React.Component { 

  render()
  {
    return(
      <div>
        <MuiThemeProvider theme={theme}>
        <Router>
          <div>
            <ul style={{marginTop: 150}}>
              <li><Link to="/dashboard">Dashboard, private</Link></li>
              <li><Link to="/login">Login</Link></li>
              <li><Link to="/protected">Protected Page</Link></li>
            </ul>
            <Route path="/login" component={Login}/>
            <PrivateRoute path="/dashboard" component={Dashboard}/>
            <PrivateRoute path='/protected' component={Protected}/>
          </div>
        </Router>
        </MuiThemeProvider>
      </div>
  );
  }
}

export default App;
