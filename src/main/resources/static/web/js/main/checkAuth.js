
function checkTokenValidity() {
    let token = getToken();
    fetch('login/validateToken', {
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
            changeButtonText(username);
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


checkTokenValidity();
