/**
 * 
 */document.addEventListener('DOMContentLoaded', function() {


	 $(document).ready(function() {
		 $('#userOrderRecord').DataTable({
			 dom: 'lfrtip',
		 });
	 });


	 const orderBadge = document.getElementById("orderBadge");
	 const badge = document.getElementById("badge");
	 updateOrderBadge();

	 async function updateOrderBadge() {
		 let count = 0;
		 const orderItems = await getOrderItemsFromBackend();

		 // Iterate over the order items to calculate the total order
		 orderItems.forEach(() => {
			 count += 1;
		 });

		 orderBadge.innerText = count;

		 if (orderBadge.innerText > 0) {
			 badge.classList.add('p-2');
			 badge.classList.remove('p-0'); // Remove 'p-0' class
		 } else {
			 badge.classList.remove('p-2'); // Remove 'p-2' class if count is 0
			 badge.classList.add('p-0');    // Add 'p-0' class
		 }
	 }

	 async function getOrderItemsFromBackend() {
		 try {
			 const response = await fetch('http://localhost:8080/Restaurant_Order_Management/api/order');
			 const json = await response.json();
			 return json;
		 } catch (error) {
			 console.error('Error getting cart data:', error);
			 return [];
		 }
	 }

	 // JavaScript function to be called when the modal is opened
	 window.prepareConfirmationModal = async function(element) {
		 // Retrieve the order type from the data attribute
		 var orderType = $(element).data('ordertypename');
		 var orderNumber = $(element).data('ordernumber');
		 var confirmPickupInput = document.getElementById('confirmPickupInput');
		 var confirmDeliInput = document.getElementById('confirmDeliInput');
		 $('#orderNumberConfirmInput').val(orderNumber);
		 console.log(orderType);
		 // Show/hide input fields based on the order type
		 if (orderType === 'delivery') {
			 $('#deliOrderNumber').text(orderNumber);
			 $('#orderCompletionTimeDiv').hide();
			 $('#deliveryArrivalTimeDiv').show();
			 confirmPickupInput.value = '';
			 confirmPickupInput.removeAttribute('required');
			 confirmDeliInput.setAttribute('required', 'required');
		 } else {
			 $('#orderNumber').text(orderNumber);
			 $('#orderCompletionTimeDiv').show();
			 $('#deliveryArrivalTimeDiv').hide();
			 confirmDeliInput.value = '';
			 confirmDeliInput.removeAttribute('required');
			 confirmPickupInput.setAttribute('required', 'required');
		 }
	 }

	 window.prepareRejectionModal = async function(element) {
		 var orderNumber = $(element).data('ordernumber');
		 $('#orderNumberRejectInput').val(orderNumber);
		 $('#rejectOrderNumber').text(orderNumber);
	 }



 });

function validatePhoneNumber(input) {
	 // Remove non-numeric characters
    var cleaned = input.value.replace(/\D/g, '');
    
    // Truncate to a maximum length of 20 characters
    var truncated = cleaned.slice(0, 20);
    
    // Update the input value
    input.value = truncated;
}

function formatBankAccountNumber(inputField) {
	// Remove any non-digit characters from the input
	const numericString = inputField.value.replace(/\D/g, '');

	// Split the numeric string into groups of four digits
	const groups = numericString.match(/.{1,4}/g);

	// Join the groups with a separator (e.g., space)
	const formattedNumber = groups ? groups.join(' ') : '';

	// Update the input field value
	inputField.value = formattedNumber;
}

function openImageModal(button) {

	const imagePath = button.getAttribute('data-screenshot');
	// Set the source attribute of the modal image
	document.getElementById('modalImage').src = '/Restaurant_Order_Management' + imagePath;
	console.log(imagePath);
	// Open the modal
	$('#imageModal').modal('show');
}


