import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import {getCookie} from '../_helpers/cookieHelper';
export const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route {...rest} render={props => (

        getCookie('token')
            ? <Component {...props} />
            : <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
    )} />
)