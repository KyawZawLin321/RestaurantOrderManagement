document.addEventListener('DOMContentLoaded', function() {

	updatefood();

	function updatefood() {
		const message = document.getElementById('message');
		const error = document.getElementById('error');
		const errorDelete = document.getElementById('errorDelete');
		const messageImage = document.getElementById('messageImage');
		const errorImage = document.getElementById('errorImage');
		const foodId = document.getElementById('foodId');

		if (error.innerText != '') {

			document.getElementById('update-' + foodId.value).click();
		}
		if (message.innerText != '') {

			document.getElementById('update-' + foodId.value).click();
		}
		if (errorImage.innerText != '') {

			document.getElementById('image-' + foodId.value).click();
		}
		if (messageImage.innerText != '') {

			document.getElementById('image-' + foodId.value).click();
		}
		if (errorDelete.innerText != '') {

			document.getElementById('delete-' + foodId.value).click();
		}
	

	}

})

function openUpdateFoodModal(button) {
	$('#updateId').val(button.getAttribute("data-foodid"));
	$('#updateName').val(button.getAttribute("data-foodname"));
	$('#updatedescription').val(button.getAttribute("data-fooddescription"));

	$('#updatePrice').val(button.getAttribute("data-foodprice"));
	$('#updateFoodId').val(button.getAttribute("data-categoryId"));
	$('#updateFoodId').text(button.getAttribute("data-foodselect"));
	$('#updateQuantity').val(button.getAttribute("data-foodmaxquantity"));
	var availableValue = button.getAttribute("data-foodavaliable");
	if (availableValue == 'true') {
		$('#availableYes').prop('checked', true); // Check 'Yes' radio button
	} else {
		$('#availableNo').prop('checked', true); // Check 'No' radio button
	}


	$('#updateFoodModel').modal('show');
};

function openUpdateFoodImageModal(button) {
	$('#updateImageFoodId').val(button.getAttribute("data-foodid"));
	$('#updateFoodImageModel').modal('show');
};

function openDeleteFoodModal(button) {
	$('#deleteId').val(button.getAttribute("data-foodid"));
	$('#deleteName').text(button.getAttribute("data-foodname"));
	$('#deleteFoodModal').modal('show');
}