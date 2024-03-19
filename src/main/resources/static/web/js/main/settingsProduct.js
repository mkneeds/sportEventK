document.addEventListener('DOMContentLoaded', function () {
    // Находим все кнопки "Edit"
    var editButtons = document.querySelectorAll('.btn-warning');

    // Добавляем обработчик события для каждой кнопки "Edit"
    editButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            // Находим родительскую строку (tr)
            var row = button.closest('tr');
            // Находим ячейку с идентификатором продукта
            var productIdCell = row.querySelector('.product-id');
            // Получаем идентификатор продукта из ячейки
            var productId = productIdCell.textContent.trim();
            // Получаем ячейку с именем продукта
            var productNameCell = row.querySelector('.product-name');
            // Получаем текущее значение имени продукта
            var currentProductName = productNameCell.textContent.trim();
            // Получаем ячейку с описанием продукта
            var productDescriptionCell = row.querySelector('.product-description');
            // Получаем текущее значение описания продукта
            var currentProductDescription = productDescriptionCell.textContent.trim();
            // Получаем ячейку с ценой продукта
            var productPriceCell = row.querySelector('.product-price');
            // Получаем текущее значение цены продукта
            var currentProductPrice = productPriceCell.textContent.trim();

            // Показываем форму редактирования продукта
            showEditForm(productId, currentProductName, currentProductDescription, currentProductPrice);
        });
    });

    // Находим все кнопки "Delete"
    var deleteButtons = document.querySelectorAll('.btn-danger');

    // Добавляем обработчик события для каждой кнопки "Delete"
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            // Находим родительскую строку (tr)
            var row = button.closest('tr');
            // Находим ячейку с идентификатором продукта
            var productIdCell = row.querySelector('.product-id');
            // Получаем идентификатор продукта из ячейки
            var productId = productIdCell.textContent.trim();

            // Отправляем AJAX-запрос на удаление продукта
            deleteProduct(productId);
        });
    });

    // Функция для показа формы редактирования продукта
    function showEditForm(productId, currentProductName, currentProductDescription, currentProductPrice) {
        // Заполняем форму данными о продукте
        document.getElementById('editProductId').value = productId;
        document.getElementById('editProductName').value = currentProductName;
        document.getElementById('editProductDescription').value = currentProductDescription;
        document.getElementById('editProductPrice').value = currentProductPrice;

        // Показываем модальное окно редактирования продукта
        document.getElementById('editProductModal').style.display = 'block';
    }

    // Функция для отправки AJAX-запроса на удаление продукта
    function deleteProduct(productId) {
        // Отправляем запрос на удаление продукта
        fetch('/product/delete/' + productId, {
            method: 'DELETE'
        })
            .then(function (response) {
                if (response.ok) {
                    // Если запрос выполнен успешно, удаляем строку из таблицы
                    document.getElementById('productRow_' + productId).remove();
                } else {
                    // Выводим сообщение об ошибке
                    alert('Error occurred while deleting product');
                }
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert('Error occurred while deleting product');
            });
    }
    window.onclick = function (event) {
        var modal = document.getElementById('editProductModal');
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    function closeEditProductModal() {
        var modal = document.getElementById('editProductModal');
        modal.style.display = "none";
    }
    var closeButtons = document.querySelectorAll('.close');
    closeButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            var modalId = button.getAttribute('data-target');
            var modal = document.getElementById(modalId);
            modal.style.display = "none";
        });
    });
});
