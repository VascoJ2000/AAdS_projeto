const url = window.location.origin;

const modalLogin = document.getElementById("modalLogin");
const bsModalLogin = new bootstrap.Modal(modalLogin, (backdrop = "static")); // Pode passar opções
const modalSignup = document.getElementById("modalSignup");
const bsModalSignup = new bootstrap.Modal(modalSignup, (backdrop = "static")); // Pode passar opções

const btnSignup = document.getElementById('btnNavSignup');
const btnLogin = document.getElementById('btnNavLogin');

btnSignup.addEventListener('click', () => {
    bsModalSignup.show()
});

btnLogin.addEventListener('click', () => {
    bsModalLogin.show()
});

pSignup.addEventListener("click", () => {
    bsModalLogin.hide()
    bsModalSignup.show()
});

pLogin.addEventListener("click", () => {
    bsModalSignup.hide()
    bsModalLogin.show()
});

function showSection(section) {
    const sections = document.getElementsByTagName('section')

    for(let i = 0; i<sections.length; i++){
        sections[i].style.display = "none"
    }

    document.getElementById(section).style.display = 'block'
}

window.onload = showSection('home');
