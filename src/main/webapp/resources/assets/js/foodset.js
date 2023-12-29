document.addEventListener('DOMContentLoaded', function() {

	updatefoodset();

	function updatefoodset() {
		const message = document.getElementById('message');
		const error = document.getElementById('error');
		const messageImage = document.getElementById('messageImage');
		const errorImage = document.getElementById('errorImage');
		const foodSetId = document.getElementById('foodSetId');

		if (error.innerText != '') {

			document.getElementById('update-' + foodSetId.value).click();
		}
		if (message.innerText != '') {

			document.getElementById('update-' + foodSetId.value).click();
		}
		if (errorImage.innerText != '') {

			document.getElementById('image-' + foodSetId.value).click();
		}
		if (messageImage.innerText != '') {

			document.getElementById('image-' + foodSetId.value).click();
		}

	}

})



function openUpdateFoodSetModal(button) {
	$('#updateId').val(button.getAttribute("data-foodSetid"));
	$('#updateName').val(button.getAttribute("data-foodSetname"));
	$('#updatedescription').val(button.getAttribute("data-foodSetdescription"));
	$('#updateFoodSetId').val(button.getAttribute("data-foodSetselect"));

	$('#updatePrice').val(button.getAttribute("data-foodSetprice"));
	$('#updateQuantity').val(button.getAttribute("data-foodSetmaxquantity"));
	var availableValue = button.getAttribute("data-foodSetavaliable");
	console.log('dd',availableValue);
	if (availableValue=='true') {
		console.log('ee',availableValue);
		$('#availableYes').prop('checked', true); // Check 'Yes' radio button
	} else  {
		$('#availableNo').prop('checked', true); // Check 'No' radio button
	}

	$('#updateFoodSetModel').modal('show');
};
function openUpdateFoodSetImageModal(button) {
	$('#updateImageFoodSetId').val(button.getAttribute("data-foodSetid"));
	$('#updateFoodSetImageModel').modal('show');
}

function openDeleteFoodSetModal(button) {
	$('#deleteId').val(button.getAttribute("data-foodSetid"));
	$('#deleteName').text(button.getAttribute("data-foodSetname"));
	$('#deleteFoodSetModal').modal('show');
}