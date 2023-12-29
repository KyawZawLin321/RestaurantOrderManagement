document.addEventListener('DOMContentLoaded', function() {

	updatePaymentType();

	function updatePaymentType() {
		const addError = document.getElementById('addError');
		const error = document.getElementById('error');
		const message = document.getElementById('message');
		const paymentTypeId = document.getElementById('paymentTypeId');
		console.log('da', addError)
		if (addError.innerText != '') {
			document.getElementById('addPaymentType').click();
			console.log('da', addError)
		}
		if (error.innerText != '') {
			document.getElementById('update-' + paymentTypeId.value).click();
		}
		if (message.innerText != '') {
			document.getElementById('update-' + paymentTypeId.value).click();
		}
	}


})





function openDeletePaymenttypeModal(button) {
	$('#deletePayMentId').val(button.getAttribute("data-paymenttypeid"));
	$('#deletePayMentName').text(button.getAttribute("data-paymenttypename"));
	$('#deletePaymenttypeModal').modal('show');
}

function openUpdatePaymenttypeModal(button) {
	$('#updateId').val(button.getAttribute("data-paymenttypeid"));
	$('#updatePayMentacc').val(button.getAttribute("data-paymenttypeacc"));
	$('#updatePayMentBank').val(button.getAttribute("data-paymenttypebank"));
	var paymentTypeAcc = button.getAttribute("data-paymenttypeacc");
	var updatePayMentBank = button.getAttribute("data-paymenttypebank");
	if (!updatePayMentBank || updatePayMentBank.trim() === '') {
		// If updatePayMentBank is null or empty, hide the updatePayMentBank input
		$('#updatePayMentBank').prop('required', false);
		$('#updatePayMentBank').hide();
		$('#updatePayMentacc').show();
		$('#updatePayMentacc').prop('required', true);
	} else {
		// Set 'required' attribute for '#updatePayMentBank'
		$('#updatePayMentBank').prop('required', true);
		// Show '#updatePayMentBank'
		$('#updatePayMentBank').show();
		// Hide '#updatePayMentacc'
		$('#updatePayMentacc').hide();
		// Remove 'required' attribute for '#updatePayMentacc'
		$('#updatePayMentacc').prop('required', false);
	}


	$('#updatePaymenttypeModal').modal('show');
}
function openAddPayMentModal() {
	$('#addPayMentModal').modal('show');
}
function showHideFields() {
	var selectBox = document.getElementById("select");
	var phoneNumberField = document.getElementById("phoneNumberField");
	var bankAccountField = document.getElementById("bankAccountField");

	if (selectBox.value === "2" || selectBox.value === "3") {
		phoneNumberField.setAttribute('required', 'required');
		bankAccountField.removeAttribute('required');
		phoneNumberField.style.display = "block";
		bankAccountField.style.display = "none";
		bankAccountField.value = '';
	} else if (selectBox.value === "4") {
		phoneNumberField.value = '' ;
		phoneNumberField.style.display = "none";
		bankAccountField.style.display = "block";
		bankAccountField.setAttribute('required', 'required');
		phoneNumberField.removeAttribute('required');
	} else {
		bankAccountField.value = '';
		phoneNumberField.value = '' ;
		phoneNumberField.style.display = "none";
		bankAccountField.style.display = "none";
		phoneNumberField.classList.remove('required');
		bankAccountField.classList.remove('required');
	}
}
const payMemtTypeNames = Array.from(document.querySelectorAll('tbody td:nth-child(2)')).map(td => td.textContent.trim());

// Check if all three order types are present
const kpayExists = payMemtTypeNames.includes('kpay');
const waveExists = payMemtTypeNames.includes('wave');
const bankExists = payMemtTypeNames.includes('bank');

// Disable the "Add Order Type" button if all three order types exist
const addPaymentButton = document.getElementById('addPaymentType');
if (kpayExists && waveExists && bankExists) {
	addPaymentButton.style.display = 'none';
} else {
	addPaymentButton.style.display = 'block'; // Show the button if any order type is missing
}