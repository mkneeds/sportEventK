
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

    const cookies = document.cookie.split('; ');

    const tokenCookie = cookies.find(cookie => cookie.startsWith('jwtToken='));

    if (tokenCookie) {
        return tokenCookie.split('=')[1];
    } else {
        return null;
    }
}

function removeToken() {
    // Устанавливаем истекшую дату для куки с токеном, что приведет к ее удалению
    document.cookie = 'jwtToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
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

document.getElementById("ProfileB").addEventListener("click", function() {

    // Получаем токен из localStorage
    var token = getToken(); // Предположим, что токен хранится в localStorage

    if (token) {

        var xhr = new XMLHttpRequest();

        xhr.open('GET', '/profile');

        xhr.setRequestHeader('Authorization', 'Bearer ' + token);

        xhr.onload = function() {
            if (xhr.status === 200) {
                window.location.href = "/profile";
            } else {
                console.error("Failed to fetch profile data");
            }
        };
        xhr.send();
    } else {
        console.error("Token not found in localStorage");
    }

});



