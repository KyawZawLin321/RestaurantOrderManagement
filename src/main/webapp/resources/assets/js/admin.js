/**
 * 
 */document.addEventListener('DOMContentLoaded', function() {


	 $(document).ready(function() {
		 $('#userOrderRecord').DataTable();

	 });


 });

function validatePhoneNumber(input) {
	 // Remove non-numeric characters
    var cleaned = input.value.replace(/\D/g, '');
    
    // Truncate to a maximum length of 20 characters
    var truncated = cleaned.slice(0, 20);
    
    // Update the input value
    input.value = truncated;
}