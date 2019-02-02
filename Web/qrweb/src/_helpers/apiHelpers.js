export async function CallApiGet(apiUrl, token) {

	return await fetch(apiUrl, {
        method: 'GET',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': 'Bearer '+token
        }	                          
    });
}

export async function CallApiPost(apiUrl, token, body) {

	return await fetch(apiUrl, {
        method: 'POST',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': 'Bearer '+token
        },
        body : body	                          	                         
    }).catch((error) => {
            return null;
    });
}

export async function CallApiPostWithoutToken(apiUrl, body) {

	return await fetch(apiUrl, {
        method: 'POST',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json;charset=UTF-8'
        },
        body : JSON.stringify(body)	                          	                         
    }).catch((error) => {
            return null;
    });
}
