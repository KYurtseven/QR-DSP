/**
 * Filters the list of people in the list
 * checks DB for each user to make sure it(user) exists
 * @param {*} rhs 
 * list of emails
 * @returns
 * list of correct people with {"id" : "user id", "email" : "user email"}
 */
function checkUser(rhs)
{
	const info = rhs.map(item =>{
        // appends Users to info
		return User.findOne({'email' : item.email})
		.exec()
		.then(usr =>{
			if(usr)
			{
				let tmpuser = new QRdocinfo({
					id : usr.id,
					email : usr.email
				});
				return tmpuser;
			}
		})
		.catch(err =>{});
    })
    // when the above function finishes
    // checkInfo function returns promise
    // Promise.all for waiting all of users to be checked
	return Promise.all(info)
		.then(docs =>{
			// filter null values
			return docs.filter(usr => usr);
		})
}

module.exports = 
{
    checkUser : checkUser
}

