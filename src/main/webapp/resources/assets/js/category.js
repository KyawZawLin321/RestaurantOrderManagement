document.addEventListener('DOMContentLoaded', function() {

	updateCategory();

	function updateCategory() {
		const message = document.getElementById('message');
		const error = document.getElementById('error');
		const errorDelete = document.getElementById('errorDelete');
		const addError = document.getElementById('addError');
		const categoryId = document.getElementById('categoryId');


		if (error.innerText != '') {
			document.getElementById('update-' + categoryId.value).click();
		}
		if (message.innerText != '') {

			document.getElementById('update-' + categoryId.value).click();
		}
		if (addError.innerText != '') {

			document.getElementById('addCategory').click();
		}
		if (errorDelete.innerText != '') {

			document.getElementById('delete-' + categoryId.value).click();
		}


	}

})




function openAddModal() {
	$('#addModal').modal('show');
}
function openDeleteModal(button) {
	$('#deleteId').val(button.getAttribute("data-categoryid"));
	$('#deleteName').text(button.getAttribute("data-categoryname"));
	$('#deleteModal').modal('show');
}
function openUpdateModal(button) {
	$('#updateId').val(button.getAttribute("data-categoryid"));
	$('#updateName').val(button.getAttribute("data-categoryname"));

	$('#updateModel').modal('show');
};