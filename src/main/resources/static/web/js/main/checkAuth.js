
function checkTokenValidity() {
    let token = getToken();
    fetch('auth/login/validateToken', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token: token })
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Ошибка при проверке токена: ' + response.statusText);
            }
        })
        .then(data => {
            var username = data.message;
            replaceElement(username)
        })
        .catch(error => {
            console.error('Ошибка при отправке запроса на проверку токена:', error);
        });
}

function getToken() {
    return localStorage.getItem('jwtToken');
}

function removeToken() {
    localStorage.removeItem('jwtToken');
}
function changeButtonText(username) {
    var loginButton = document.getElementById("loginButton");
    if (loginButton) {
        loginButton.textContent = username;
    }
}
function replaceElement(username) {
    var loginButton = document.getElementById("loginButton");
    loginButton.style.display='none';
    var profileLogIn = document.querySelector('.profileLogIn');
    var dropdownButton = document.getElementById('dropdownButton');
    profileLogIn.style.display = 'block';
    dropdownButton.querySelector('p').textContent = username;
}




checkTokenValidity();


var logoutButton = document.getElementById('LogOutB');
logoutButton.addEventListener('click', function(event) {
    event.preventDefault();
    removeToken()
    location.reload();
});

var forgotPasswordLink = document.getElementById('forgotPasswordLink');
var modal = document.getElementById('forgotPasswordModal');
var closeBtn = modal.querySelector('.close');
forgotPasswordLink.addEventListener('click', function(event) {
    modal.style.display = 'block';
});
closeBtn.addEventListener('click', function(event) {
    modal.style.display = 'none';
});

var sendResetLinkBtn = document.getElementById('sendResetLink');
sendResetLinkBtn.addEventListener('click', function(event) {
    var formData = new URLSearchParams();
    let email = document.getElementById("emailF").value;
    console.log(email)
    formData.append('email', email);

    fetch('/auth/reset-password-email', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded' // Устанавливаем правильный Content-Type
        },
        body: formData.toString()
    })
        .then(response => {
            if (response.ok) {
                alert('Ссылка для восстановления пароля отправлена на ваш email.');
                modal.style.display = 'none';
            } else {
                alert('Ошибка при отправке ссылки для восстановления пароля.');
            }
        })
        .catch(error => {
            console.error('Error occurred:', error);
            alert('Произошла ошибка при отправке запроса.');
        });
});


