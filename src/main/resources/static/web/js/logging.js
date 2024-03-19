document.addEventListener("DOMContentLoaded", function() {
    var overlay = document.getElementById('overlay');
    var ContainerAuth = document.getElementById('containerAuth');
    var loginButton = document.getElementById("loginButton");

    function openModal() {
        overlay.style.display = 'block';
        ContainerAuth.style.display = 'block';
    }

    function closeModal() {
        overlay.style.display = 'none';
        ContainerAuth.style.display = 'none';
    }

    overlay.addEventListener('click', function(e) {
        if (e.target === overlay) {
            closeModal();
        }
    });

    loginButton.addEventListener('click', function() {
        openModal();
    });




const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () =>
    container.classList.add('right-panel-active'));

signInButton.addEventListener('click', () =>
    container.classList.remove('right-panel-active'));

document.getElementById("registrationForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const firstNameElement = document.getElementById("firstName");
    const lastNameElement = document.getElementById("lastName");
    const usernameElement = document.getElementById("username");
    const emailElement = document.getElementById("email");
    const passwordElement = document.getElementById("password");
    const roleElement = document.querySelector('input[name="role"]:checked');

    const formData = {
        firstName: firstNameElement.value,
        lastName: lastNameElement.value,
        username: usernameElement.value,
        email: emailElement.value,
        password: passwordElement.value,
        role: roleElement.value
    };

    fetch("auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(data => {
            alert("Регистрация прошла успешно");
            container.classList.remove('right-panel-active');
            firstNameElement.value = "";
            lastNameElement.value = "";
            usernameElement.value = "";
            emailElement.value = "";
            passwordElement.value = "";
        })
        .catch(error => {
            alert("Ошибка при регистрации: " + error.message);
        });

});

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var login = document.getElementById('loginA').value;
    var password = document.getElementById('passwordA').value;

    fetch('auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username: login, password: password })
    })
        .then(response => {

            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Ошибка при отправке запроса на авторизацию');
            }
        })
        .then(data => {
            const token = data.token;
            setToken(token)
            alert("Авторизация прошла успешно!")
            overlay.style.display = 'none';
            ContainerAuth.style.display = 'none';
            location.reload();
        })
        .catch(error => {
            alert('Произошла ошибка:', error.message);
        });

});

function setToken(token) {
        document.cookie = `jwtToken=${token}; path=/`;
}


const token = localStorage.getItem('jwtToken');

const headers = {
    ...options.headers,
    'Authorization': `Bearer ${token}`
};

});