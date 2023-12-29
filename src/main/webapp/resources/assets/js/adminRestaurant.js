document.addEventListener('DOMContentLoaded', function() {

	updateRestaurant();

	function updateRestaurant() {
		const message = document.getElementById('message');
		const error = document.getElementById('error');
		const addMessage = document.getElementById('addMessage');
		const addError = document.getElementById('addError');
		const errorLogo = document.getElementById('errorLogo');
		const errorImage = document.getElementById('errorImage');
		const restaurantId = document.getElementById('restaurantId');

		if (error.innerText != '') {

			document.getElementById('update-' + restaurantId.value).click();
		}
		if (message.innerText != '') {

			document.getElementById('update-' + restaurantId.value).click();
		}
		if (errorImage.innerText != '') {

			document.getElementById('image-' + restaurantId.value).click();
		}
		if (errorLogo.innerText != '') {

			document.getElementById('logo-' + restaurantId.value).click();
		}

	}

})

function openDeleteRestaurantModal(button) {
	$('#deleteId').val(button.getAttribute("data-restaurantid"));
	$('#deleteName').text(
		button.getAttribute("data-restaurantname"));
	$('#deleteRestaurantModal').modal('show');
}

function openUpdateRestaurantModal(button) {
	$('#updateId').val(button.getAttribute("data-restaurantid"));
	$('#updateName')
		.val(button.getAttribute("data-restaurantname"));
	$('#updateEmail').val(
		button.getAttribute("data-restaurantemail"));
	$('#updatePhoneNumber').val(
		button.getAttribute("data-restaurantphone_number"));
	$('#updateAddress').val(
		button.getAttribute("data-restaurantaddress"));
	$('#updateDescription').val(
		button.getAttribute("data-restaurantdescription"));
	$('#updateRestaurantModal').modal('show');
}

function openUpdateRestaurantImageModal(button) {
	$('#updateImageRestaurantId').val(button.getAttribute("data-restaurantid"));
	$('#updateRestaurantImageModal').modal('show');
};

function openUpdateRestaurantLogoModal(button) {

	$('#updateLogoRestaurantId').val(button.getAttribute("data-restaurantid"));
	$('#updateRestaurantLogoModal').modal('show');
};

