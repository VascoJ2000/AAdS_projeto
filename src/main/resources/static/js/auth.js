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
    sessionStorage.clear()
    location.reload();
}

// Auth
async function postLogin(){
    const email = document.getElementById('usernameLogin').value;
    const password = document.getElementById('senhaLogin').value;
    await fetch(url + `/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            password: password
        }),
    })
    .then(res => res.json())
    .then(data => {
        console.log(data)
        sessionStorage.setItem("token", data.token)
        login(email)
        showSection('chat')
        connect()
    }).catch(err => console.log(err))
}

async function postSignup(){
    const email = document.getElementById('usernameSignup').value;
    const password = document.getElementById('senhaSignup').value;
    await fetch('/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            password: password
        }),
    }).then(res => res.json())
    .then(data => {
        console.log(data)
        sessionStorage.setItem("token", data.token)
        login(email)
        showSection('chat')
        connect()
    }).catch(err => console.log(err))


}

async function postLogout(){
    await fetch(url + '/auth/logout', {
        method: 'POST',
    }).catch(err => console.log(err))

    logoff();
}

