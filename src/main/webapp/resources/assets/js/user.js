/**
 * 
 *//**
 * 
 */document.addEventListener("DOMContentLoaded", function() {
	 const orderMessageBadge = document.getElementById('orderMessageBadge');


	 $(document).ready(function() {
		 updateOrderMessageBadge();
		 getLoginError();
		 getError();
	 })

	 $(document).ready(function() {
		 $('#userOrderRecord').DataTable();
	 });



	 async function getLoginError() {
		 loginMsg = document.getElementById('loginMsg');
		 registerMsg = document.getElementById('registerMsg');
		 successMsg = document.getElementById('successMsg');
		 registerSuccess = document.getElementById('registerSuccess');
		 loginButton = document.getElementById('loginButton');
		 registerButton = document.getElementById('registerButton');
		 console.log('msg', loginMsg.innerText)
		 console.log('ss msg', successMsg)
		 if (loginMsg.innerText != '') {
			 loginButton.click();
		 }
		 if (registerMsg.innerText != '') {
			 document.querySelector('.wrapper').classList.add('form-active');
			 await loginButton.click();
		 }
		 if (registerSuccess.innerText != '') {
			 document.querySelector('.wrapper').classList.add('form-active');
			 loginButton.click();
		 }
		 if (successMsg.innerText != '') {
			 alert(successMsg.innerText);
		 }

	 }


	 async function updateOrderMessageBadge() {


		 const counts = await getOrderMessageFromBackend();

		 if (counts != 0) {
			 alert(counts + " Order message has arrived!Check Your Order Message Box!")
		 }
		 orderMessageBadge.innerText = counts;
	 }

	 function getError() {

		 const error = document.getElementById('errorUpdate');
		 const message = document.getElementById('messageUpdate');

		 if (error.innerText != '') {
			 document.getElementById('userUpdate').click();
		 }
		 if (message.innerText != '') {
			 document.getElementById('userUpdate').click();
		 }

	 }


	 async function getOrderMessageFromBackend() {
		 try {
			 const response = await fetch('http://localhost:8080/Restaurant_Order_Management/api/countOrderMessage');
			 const json = await response.json();
			 return json;
		 } catch (error) {
			 console.error('Error getting cart data:', error);
			 return [];
		 }
	 }



	 window.updateUser = function(button) {
		 const id = button.getAttribute('data-id');
		 const name = button.getAttribute('data-name');
		 const email = button.getAttribute('data-email');
		 const phone = button.getAttribute('data-phoneNumber');
		 const address = button.getAttribute('data-address');

		 const idInput = document.getElementById('userIdInput');
		 const nameInput = document.getElementById('userNameInput');
		 const emailInput = document.getElementById('userEmailInput');
		 const phoneInput = document.getElementById('userPhoneInput');
		 const addressInput = document.getElementById('userAddressInput');


		 console.log(id, name, email, address)

		 idInput.value = id;
		 nameInput.value = name;
		 emailInput.value = email;
		 phoneInput.value = phone;
		 addressInput.value = address;
		 $('#userUpdateModal').modal('show');

	 }





 });

function printReceipt() {
	var printContent = document.getElementById("print").outerHTML;
	var originalContent = document.body.innerHTML;

	// Replace the body content with the content of the 'print' div
	document.body.innerHTML = printContent;

	// Print the content
	window.print();

	// Restore the original content
	document.body.innerHTML = originalContent;
}

function validatePhoneNumber(input) {
	 // Remove non-numeric characters
    var cleaned = input.value.replace(/\D/g, '');
    
    // Truncate to a maximum length of 20 characters
    var truncated = cleaned.slice(0, 20);
    
    // Update the input value
    input.value = truncated;
}
