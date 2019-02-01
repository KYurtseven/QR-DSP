// TODO
// delete this file, only for practice
import React, { Component } from 'react'
import {connect} from 'react-redux';
import {fetchPosts} from '../actions/postAction';

// add props to proptypes
import PropTypes from 'prop-types';

import PostForm from './PostForm';

class Posts extends Component {

	componentWillMount(){
		this.props.fetchPosts();
	}

	componentWillReceiveProps(nextProps)
	{
		if(nextProps.newPost){
			this.props.posts.unshift(nextProps.newPost);
		}
	}
	
  render() {
    const postItems = this.props.posts.map(post=>(
			<div key={post.id} >
				<h3>{post.title}</h3>
				<p>{post.body}</p>
			</div>
    ));
    return (
      <div>
          <h1>Posts</h1>
					<PostForm/>
          {postItems}
      </div>
    )
  }
}

Posts.propTypes={
	fetchPosts: PropTypes.func.isRequired,
	// we mapped state to posts property
	posts: PropTypes.array.isRequired,
	newPost: PropTypes.object
}

const mapStateToProps = state =>({
	// state.posts, because reducer has posts
	// we want items from reducer
	posts: state.posts.items,
	newPost: state.posts.item
})

export default connect(mapStateToProps, {fetchPosts})(Posts);