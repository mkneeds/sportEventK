document.addEventListener('DOMContentLoaded', function () {
    var editButtons = document.querySelectorAll('.btn-warning');
    editButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            var row = button.closest('tr');
            var productIdCell = row.querySelector('.product-id');
            var productId = productIdCell.textContent.trim();
            var productNameCell = row.querySelector('.product-name');
            var currentProductName = productNameCell.textContent.trim();
            var productDescriptionCell = row.querySelector('.product-description');
            var currentProductDescription = productDescriptionCell.textContent.trim();
            var productPriceCell = row.querySelector('.product-price');
            var currentProductPrice = productPriceCell.textContent.trim();
            var productDateCell = row.querySelector('.product-date');
            var currentProductDate = productDateCell.textContent.trim();
            var productImgCell = row.querySelector('.product-img');
            var currentProductImg = productImgCell.textContent.trim();
            showEditForm(productId, currentProductName, currentProductDescription, currentProductPrice, currentProductDate, currentProductImg);
        });
    });


    var deleteButtons = document.querySelectorAll('.btn-danger');
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            var row = button.closest('tr');
            var productIdCell = row.querySelector('.product-id');
            var productId = productIdCell.textContent.trim();
            deleteProduct(productId);
        });
    });


    function showEditForm(productId, currentProductName, currentProductDescription, currentProductPrice, currentProductDate, currentProductImg) {
        document.getElementById('editProductId').value = productId;
        document.getElementById('editProductName').value = currentProductName;
        document.getElementById('editProductDescription').value = currentProductDescription;
        document.getElementById('editProductPrice').value = currentProductPrice;
        document.getElementById('editProductDate').value = currentProductDate;
        document.getElementById('editProductImageUrl').value = currentProductImg;
        document.getElementById('editProductModal').style.display = 'block';
    }

    function deleteProduct(productId) {

        fetch('/product/delete/' + productId, {
            method: 'DELETE'
        })
            .then(function (response) {
                if (response.ok) {
                    document.getElementById('productRow_' + productId).remove();
                } else {
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

    var closeButtons = document.getElementById('closeModal');
    closeButtons.addEventListener('click', function () {
        var modal = document.getElementById("editProductModal");
        modal.style.display = "none";
    });
    var editProductSubmitButton = document.getElementById('editProductSubmit');
    var editProductModal = document.getElementById('editProductModal');

    function closeEditModal() {
        editProductModal.style.display = 'none';
    }

    editProductSubmitButton.addEventListener('click', function () {
        var productId = document.getElementById('editProductId').value;
        var updatedProduct = {
            name: document.getElementById('editProductName').value,
            description: document.getElementById('editProductDescription').value,
            price: document.getElementById('editProductPrice').value,
            date: document.getElementById('editProductDate').value,
            category: document.getElementById('editProductCategory').value,
            imageUrl: document.getElementById('editProductImageUrl').value
        };

        fetch('/product/edit/' + productId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedProduct)
        })
            .then(function (response) {
                if (response.ok) {
                    closeEditModal();

                } else {
                    alert('Error occurred while updating product');
                }
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert('Error occurred while updating product');
            });
    });

    function showImagePreview() {
        var imageUrl = document.getElementById('editProductImageUrl').value;
        var imagePreview = document.getElementById('imagePreview');
        imagePreview.innerHTML = '<img src="' + imageUrl + '" style="max-width: 100px; max-height: 100px;" />';
    }


    document.getElementById('editProductImageUrl').addEventListener('change', showImagePreview);
    document.getElementById('addProductButton').addEventListener('click', function () {
        document.getElementById('addProductModal').style.display = 'block';
    });
    document.getElementById('closeAddModal').addEventListener('click', function () {
        document.getElementById('addProductModal').style.display = 'none';
    });

    document.getElementById('addProductSubmit').addEventListener('click', function () {
        var productName = document.getElementById('addProductName').value;
        var productDescription = document.getElementById('addProductDescription').value;
        var productPrice = document.getElementById('addProductPrice').value;
        var productDate = document.getElementById('addProductDate').value;
        var productCategory = document.getElementById('addProductCategory').value;
        var productImageUrl = document.getElementById('addProductImageUrl').value;

        if (productName === '' || productDescription === '' || productPrice === '' || productDate === '' || productCategory === '') {
            alert('Пожалуйста, заполните все обязательные поля.');
            return;
        }

        var formData = {
            name: document.getElementById('addProductName').value,
            description: document.getElementById('addProductDescription').value,
            price: document.getElementById('addProductPrice').value,
            date: document.getElementById('addProductDate').value,
            category: document.getElementById('addProductCategory').value,
            imageUrl: document.getElementById('addProductImageUrl').value
        };


        fetch('/product/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(function (response) {
                if (response.ok) {

                    location.reload();
                } else {

                    alert('Ошибка при добавлении продукта: Имя неуникально', response.statusText);
                }
            })
            .catch(function (error) {
                console.error('Ошибка при отправке запроса:', error);
            });
    });

    function uploadImageToImgBB(imageFile, apiKey) {
        return new Promise((resolve, reject) => {
            var reader = new FileReader();

            reader.onloadend = function () {
                var base64data = reader.result.split(',')[1]; // Получаем строку base64 без префикса "data:image/jpeg;base64,"

                var formData = new FormData();
                formData.append('key', apiKey);
                formData.append('image', base64data);

                fetch('https://api.imgbb.com/1/upload', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            resolve(data.data.url);
                            document.getElementById('addProductImageUrl').value = data.data.url;
                        } else {
                            reject(new Error('Ошибка при загрузке изображения на ImgBB: ' + data.error.message));
                        }
                    })
                    .catch(error => reject(error));
            };

            reader.readAsDataURL(imageFile);
        });
    }

    function handleImageUpload() {
        var apiKey = '023ab14602431f15b7496e67be7e40ec';
        var imageFile = document.getElementById('imageInput').files[0];

        if (imageFile instanceof Blob) {
            uploadImageToImgBB(imageFile, apiKey)
                .then(imageUrl => {
                    console.log('Ссылка на загруженное изображение:', imageUrl);
                    // Здесь можно использовать ссылку на изображение
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                });
        } else {
            console.error('Файл не выбран или не является объектом Blob.');
        }
    }

// Назначаем обработчик события change элементу input
    document.getElementById('imageInput').addEventListener('change', handleImageUpload);



});

