function login(email) {
    const notLogged = document.getElementsByClassName('loggedOut');
    const logged = document.getElementsByClassName('loggedIn');
    const navEmail = document.getElementById('userLogged');
    const sideNavEmail =document.getElementById('user-side')

    for(let i = 0; i<notLogged.length; i++){
        notLogged[i].style.display = "none";
    }

    for(let i = 0; i<logged.length; i++){
        logged[i].style.display = "block";
    }
    navEmail.innerHTML = email
    sideNavEmail.innerHTML = email
}

function logoff() {
    location.reload();
}

// Auth
async function postLogin(){
    const email = document.getElementById('usernameLogin').value;
    const password = document.getElementById('senhaLogin').value;
    await fetch(url + `/api/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            password: password
        }),
    }).then(res => {
        console.log(res)
        if(res.ok) login(email)
    }).catch(err => console.log(err))
}

async function postSignup(){
    const email = document.getElementById('usernameSignup').value;
    const password = document.getElementById('senhaSignup').value;
    await fetch('/api/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            password: password
        }),
    }).then(res => {
        console.log(res)
        if(res.ok) location.reload();
    }).catch(err => console.log(err))


}

async function delLogout(){
    await fetch(url + '/api/auth/logout', {
        method: 'DELETE',
    }).catch(err => console.log(err))

    logoff();
}

