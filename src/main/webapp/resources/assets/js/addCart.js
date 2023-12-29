document.addEventListener('DOMContentLoaded', function() {

	const cartBadge = document.getElementById("cartBadge");
	const foodButtons = document.querySelectorAll("#foodBtn");
	const foodSetButtons = document.querySelectorAll("#foodSetBtn");
	const FOOD_CART_KEY = "foodCart";
	const FOODSET_CART_KEY = "foodSetCart";

	if (getFoodStoredCart() == null) localStorage.setItem(FOOD_CART_KEY, JSON.stringify([]));
	let foodItems = JSON.parse(getFoodStoredCart());

	if (getFoodSetStoredCart() == null) localStorage.setItem(FOODSET_CART_KEY, JSON.stringify([]));
	let foodSetItems = JSON.parse(getFoodSetStoredCart());

	cartBadge.innerText = foodItems.length + foodSetItems.length;

	foodButtons.forEach((button) => {
		button.addEventListener("click", (e) => {
			e.preventDefault(); // Prevent the default behavior of the link
			const target = Number(e.target.getAttribute("target"));
			addToCart(target, FOOD_CART_KEY);
		});
	});

	foodSetButtons.forEach((button) => {
		button.addEventListener("click", (e) => {
			e.preventDefault(); // Prevent the default behavior of the link
			const target = Number(e.target.getAttribute("target"));
			addToCart(target, FOODSET_CART_KEY);
		});
	});

	function addToCart(target, cartKey) {
		let currentItemCount = Number(cartBadge.innerText);
		let cartItems = JSON.parse(localStorage.getItem(cartKey)) || [];

		// Check if the cart has items from a different restaurant
		const existingRestaurantId = localStorage.getItem("restaurantId");
		const newItemRestaurantId = getRestaurantIdForItem();

		if (existingRestaurantId && existingRestaurantId !== newItemRestaurantId) {
			// Ask the user for confirmation using the modal
			$('#confirmationModal').modal('show');

			// Set up a click event listener for the modal's "Proceed" button
			document.getElementById('confirmModalBtn').addEventListener('click', function() {
				// Clear the existing cart
				localStorage.removeItem(FOOD_CART_KEY);
				localStorage.removeItem(FOODSET_CART_KEY);
				cartBadge.innerText = 0;
				currentItemCount = Number(cartBadge.innerText);
				cartItems = JSON.parse(localStorage.getItem(cartKey)) || [];

				// Save the new restaurantId
				localStorage.setItem("restaurantId", newItemRestaurantId);

				// Hide the modal
				$('#confirmationModal').modal('hide');

				// Add the new item to the cart
				addItemToCart(target, cartKey, currentItemCount, cartItems, newItemRestaurantId);
			});
		} else {
			// Save the restaurantId for the first item
			localStorage.setItem("restaurantId", newItemRestaurantId);

			// Add the new item to the cart
			addItemToCart(target, cartKey, currentItemCount, cartItems, newItemRestaurantId);
		}
	}

	function addItemToCart(target, cartKey, currentItemCount, cartItems, newItemRestaurantId) {
		if (cartItems.findIndex((item) => item.id == target) == -1) {
			cartItems.push({ id: target, type: cartKey, restaurantId: newItemRestaurantId, quantity: 1 });
			cartBadge.innerText = currentItemCount + 1;

			// Save to local storage
			localStorage.setItem(cartKey, JSON.stringify(cartItems));

			// Send cart data to the backend after updating local storage
			sendCartDataToBackend();
		} else {
			alert("You've already added this item!");
		}
	}

	function getFoodStoredCart() {
		return localStorage.getItem(FOOD_CART_KEY);
	}

	function getFoodSetStoredCart() {
		return localStorage.getItem(FOODSET_CART_KEY);
	}



	function getRestaurantIdForItem() {
		const restaurantIdElement = document.getElementById('restaurantId');
		return restaurantIdElement.value;
	}

	// Send cart data to backend
	function sendCartDataToBackend() {
		foodItems = JSON.parse(getFoodStoredCart());
		foodSetItems = JSON.parse(getFoodSetStoredCart());

		const combinedCart = [...(Array.isArray(foodItems) ? foodItems : []), ...(Array.isArray(foodSetItems) ? foodSetItems : [])];
		console.log(combinedCart);
		fetch('http://localhost:8080/Restaurant_Order_Management/api/test', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'Accept': 'application/json',
			},
			body: JSON.stringify(combinedCart),
		})
			.then(response => response.json())
			.then(data => {
				// Handle the data received from the backend
				console.log('Cart details received:', data);
			})
			.catch(error => console.error('Error sending cart data:', error));
	}

});
