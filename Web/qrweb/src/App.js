import React from 'react'
import {
  BrowserRouter as Router,
  Route,
  Link,
  withRouter
} from 'react-router-dom';

import * as fakeAuth from './GlobalPages/fakeauth';
import Login from './components/Login';
import { getCookie, deleteAllCookies } from './_helpers/cookieHelper';
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

const Dashboard = () => <h3>Dashboard</h3>
const Protected = () => <h3>Protected</h3>

const AuthButton = withRouter(({ history }) => (
  getCookie('token')? (
    <p>
      Welcome {getCookie('username')} <button onClick={() => {
        deleteAllCookies(() => history.push('/'))
      }}>Sign out</button>
    </p>
  ) : (
    <p>You are not logged in.</p>
  )
))
/*
class App extends React.Component {
  render() {
    return (
      <Router>
      <div>
        <AuthButton/>
        <ul>
          <li><Link to="/public">Public Page</Link></li>
          <li><Link to="/protected">Protected Page</Link></li>
        </ul>
        <Route path="/public" component={Public}/>
        <Route path="/login" component={Login}/>
        <PrivateRoute path='/protected' component={Protected} />
      </div>
    </Router>
      );
  }
}
*/

class App extends React.Component { 
  render()
  {
    return(
      <div>
        <MuiThemeProvider theme={theme}>
            <Main />
        </MuiThemeProvider>
      </div>
  );
  }
}

const Main = () => (
  <Router>
    <div>
      <AuthButton/>
      <ul>
        <li><Link to="/dashboard">Dashboard, private</Link></li>
        <li><Link to="/login">Login</Link></li>
        <li><Link to="/protected">Protected Page</Link></li>
      </ul>
      <Route path="/login" component={Login}/>
      <PrivateRoute path="/dashboard" component={Dashboard}/>
      <PrivateRoute path='/protected' component={Protected} />
    </div>
  </Router>
)

export default App;
