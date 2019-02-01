import React from 'react'
import {
  BrowserRouter as Router,
  Route,
  Link,
  Redirect,
  withRouter
} from 'react-router-dom';

import {Provider} from 'react-redux';
import store from './store';

import * as fakeAuth from './GlobalPages/fakeauth';
import Login from './components/Login';
// TODO
// delete this, only for practice
import Posts from './components/Posts';

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

const Public = () => <h3>Public</h3>
const Protected = () => <h3>Protected</h3>

const PrivateRoute = ({ component: Component, ...rest }) => (
  <Route {...rest} render={(props) => (
    fakeAuth.fakeAuth.isAuthenticated === true
      ? <Component {...props} />
      : <Redirect to={{
          pathname: '/login',
          state: { from: props.location }
        }} />
  )} />
)

const AuthButton = withRouter(({ history }) => (
  fakeAuth.fakeAuth.isAuthenticated ? (
    <p>
      Welcome! <button onClick={() => {
        fakeAuth.fakeAuth.signout(() => history.push('/'))
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
          <Provider store={store}>
            <Main />
          </Provider>
        </MuiThemeProvider>
      </div>
  );
  }
}


// TODO
// delete /posts, only for practice

const Main = () => (
  <Router>
    <div>
      <AuthButton/>
      <ul>
        <li><Link to="/public">Public Page</Link></li>
        <li><Link to="/posts">Posts</Link></li>
        <li><Link to="/protected">Protected Page</Link></li>
      </ul>
      <Route path="/public" component={Public}/>
      <Route path="/login" component={Login}/>
      <Route path='/posts' component ={Posts}/>
      <PrivateRoute path='/protected' component={Protected} />
    </div>
  </Router>
)

export default App;
