function editCategory(categoryId) {
    var row = document.querySelector('[data-category-id="' + categoryId + '"]');
    if (!row) {
        console.error('Category row not found');
        return;
    }
    var newName = row.querySelector('td:nth-child(2)').textContent.trim();
    fetch('/admin/categories/edit/' + categoryId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: newName }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(data => {
            alert("Категория успешно изменена")

        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}


function deleteCategory(categoryId) {
    if (!confirm("Вы уверены, что хотите удалить эту категорию?")) {
        return;
    }

    fetch('/admin/categories/delete/' + categoryId, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            var row = document.querySelector('[data-category-id="' + categoryId + '"]');
            if (row) {
                row.remove();
            }
            alert("Категория успешно удалена");
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
function addCategory() {
    var newName = prompt("Введите название новой категории:");
    if (!newName) {
        return;
    }

    fetch('/admin/categories/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: newName }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(data => {
            alert("Категория успешно добавлена");
            location.reload();
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
function changeRole(userId) {
    var selectElement = document.getElementById(userId);
    var roleId = selectElement.value;
    console.log(roleId)
    fetch('/admin/users/change-role/' + userId + '?roleId=' + roleId, {
        method: 'POST',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            alert("Роль пользователя успешно изменена");
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}



