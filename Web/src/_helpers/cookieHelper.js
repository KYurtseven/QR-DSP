import Cookies from "universal-cookie";

export function setCookie(name, val, path)
{
    const cookies = new Cookies();
    console.log('set cookies');
    cookies.set(name, val, {
        path : path,
        maxAge: 30*24*60*60  // 30 days
    });
}

export function getCookie(name)
{
    const cookies = new Cookies();
    try{
        return cookies.get(name);
    }
    catch{
        return {}
    }
}

export function deleteAllCookies()
{
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}