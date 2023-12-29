package com.ai.jwd42.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.jwd42.dto.CartItem;
import com.ai.jwd42.dto.Food;
import com.ai.jwd42.dto.FoodSet;
import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.FoodSetService;
import com.ai.jwd42.service.OrderService;
import com.ai.jwd42.service.UserService;

@RestController
@RequestMapping("/api")
public class CartController {

	@Autowired
	FoodService foodService;

	@Autowired
	FoodSetService foodSetService;

	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;



	@GetMapping(value = "/user")
	public ResponseEntity<User> getUser(HttpSession session) {

		String email = (String) session.getAttribute("username");
		String role = (String) session.getAttribute("role");

		System.out.println("user - " + email);
		System.out.println("role - " + role);

		User user = new User(email, role);

		return ResponseEntity.ok(user);
	}

	@GetMapping(value = "/countOrderMessage")
	public ResponseEntity<Integer> getOrderMessage(HttpSession session) {
		int count=0;
		if(session.getAttribute("username")!=null) {
			String email = (String) session.getAttribute("username");
			User user = userService.findByEmail(email);
			count = orderService.findUnreadOrderMesssage(user.getId());
		}
		
		

		System.out.println("count - " + count);

		return ResponseEntity.ok(count);
	}

	@GetMapping(value = "/order")
	public ResponseEntity<List<Order>> getOrder(HttpSession session) {

		User user = (User) session.getAttribute("user");

		List<Order> orderList = orderService.findPendingOrder(user.getRestaurantId());

		System.out.println("session get orderItem - " + orderList);

		return ResponseEntity.ok(orderList);
	}

	@GetMapping(value = "/cartItem")
	public ResponseEntity<List<CartItem>> getCartItem(HttpSession session) {

		List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("cartItems");

		System.out.println("session get cartItem - " + session.getAttribute("cartItems"));

		return ResponseEntity.ok(cartItemList);
	}

	@PostMapping(value = "/test", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> handleCartRequest(@RequestBody List<CartItem> cartItems, HttpSession session) {
		List<CartItem> cartItemList = new ArrayList<>();
		int restaurantId = 0;
		for (CartItem cartItem : cartItems) {
			if (cartItem.getType().equalsIgnoreCase("foodCart")) {
				Food food = foodService.findFoodById(cartItem.getId());
				if (food != null) {
					CartItem cartItemDetail = new CartItem("food", food.getId(), food.getName(), food.getPrice(),
							food.getMaxQuantity(), food.getCategory(), food.getImage(), food.getRestaurantId(),
							cartItem.getQuantity(), food.isAvaliable(), food.getDescription());
					cartItemList.add(cartItemDetail);
					System.out.println(cartItem.getDescription());
				}
			} else {
				FoodSet foodSet = foodSetService.findFoodSetById(cartItem.getId());
				if (foodSet != null) {
					CartItem cartItemDetail = new CartItem("foodSet", foodSet.getId(), foodSet.getName(),
							foodSet.getPrice(), foodSet.getMaxQuantity(), foodSet.getImage(), foodSet.getRestaurantId(),
							cartItem.getQuantity(), foodSet.isAvaliable(), foodSet.getDescription());
					cartItemList.add(cartItemDetail);
				}
			}
			restaurantId = cartItem.getRestaurantId();
		}
		session.setAttribute("restaurantId", restaurantId);
		session.setAttribute("cartItems", cartItemList);
		System.out.println("session set done = " + session.getAttribute("restaurantId"));

		return ResponseEntity.ok("Item added to the cart successfully!");
	}

}
