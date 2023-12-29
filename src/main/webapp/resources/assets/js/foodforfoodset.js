document.addEventListener('DOMContentLoaded', function() {

	updatefood();

	function updatefood() {

		const error = document.getElementById('errorFoodSet');

		if (error.innerText != '') {

			document.getElementById('addFoodSet').click();
		}

	}

})





function openFoodForFoodSetModal(){
			$('#addFoodForFoodSetModal').modal('show');
		}

		
		function openDeleteFoodForFoodSetModal(button) {
			$('#deleteFoodId').val(button.getAttribute("data-FoodForFoodSetId"));
			$('#deleteFoodName').text(button.getAttribute("data-FoodForFoodSetName"));
			$('#deleteFoodForFoodSetModal').modal('show');
		}
		function openUpdateFoodForFoodSetModal(button) {
			$('#updateFoodForFoodSetId').val(button.getAttribute("data-FoodForFoodSetId"));
			$('#updateFoodName').text(button.getAttribute("data-FoodForFoodSetName"));
			$('#updateFoodName').val(button.getAttribute("data-foodId"));
			$('#updateFoodForFoodSetModal').modal('show');
		}
		
		