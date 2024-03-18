
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

