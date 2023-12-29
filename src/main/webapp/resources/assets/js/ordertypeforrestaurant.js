
document.addEventListener('DOMContentLoaded', function() {

	updateOrderType();

	function updateOrderType() {
		const addError = document.getElementById('addError');

		if (addError.innerText != '') {
			document.getElementById('addOrderType').click();
		}
	}


})




function openDeleteOrdertypeModal(button) {
	$('#deleteId').val(button.getAttribute("data-ordertypeid"));
	$('#deleteName').text(button.getAttribute("data-ordertypename"));
	$('#deleteOrdertypeModal').modal('show');
}
function openAddOrderTypeModal() {
	$('#addOrderTypeModal').modal('show');
}
const orderTypeNames = Array.from(document.querySelectorAll('tbody td:nth-child(2)')).map(td => td.textContent.trim());

// Check if all three order types are present
const deliveryExists = orderTypeNames.includes('delivery');
const pickupExists = orderTypeNames.includes('pickup');
const dineInExists = orderTypeNames.includes('dinein');

// Disable the "Add Order Type" button if all three order types exist
const addButton = document.querySelector('.btn.btn-warning');

if (deliveryExists && pickupExists && dineInExists) {
	addButton.style.display = 'none';
} else {
	addButton.style.display = 'block'; // Show the button if any order type is missing
}

function showHideFields() {
	var selectBox = document.getElementById("select");
	var phoneNumberField = document.getElementById("phoneNumberField");
	var bankAccountField = document.getElementById("bankAccountField");

	if (selectBox.value === "1" || selectBox.value === "3") {
		phoneNumberField.style.display = "block";
		bankAccountField.style.display = "none";
	} else if (selectBox.value === "4") {
		phoneNumberField.style.display = "none";
		bankAccountField.style.display = "block";
	} else {
		phoneNumberField.style.display = "none";
		bankAccountField.style.display = "none";
	}
}