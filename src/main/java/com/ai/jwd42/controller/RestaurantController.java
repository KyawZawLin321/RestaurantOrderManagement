package com.ai.jwd42.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ai.jwd42.dto.CartItem;
import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.Restaurant;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.FoodSetService;
import com.ai.jwd42.service.OrderService;
import com.ai.jwd42.service.PaymentService;
import com.ai.jwd42.service.RestaurantService;

@Controller
@RequestMapping("/user")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private FoodService foodService;

	@Autowired
	private FoodSetService foodSetService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = { "/restaurant" }, method = RequestMethod.GET)
	public ModelAndView showRestaurant(ModelMap model, HttpServletRequest request) {

		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		
		model.addAttribute("restaurants", restaurantService.findAllRestaurant());
		model.addAttribute("restaurant", new Restaurant());
		return new ModelAndView("restaurant.html", "user", user);
	}

	@RequestMapping(value = { "/cart" }, method = RequestMethod.GET)
	public ModelAndView cart(ModelMap model, HttpSession session, HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}

		if (session.getAttribute("cartItems") != null) {
			List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("orderTypes",
					orderService.findOrderTypeByRestaurantId((int) session.getAttribute("restaurantId")));
			model.addAttribute("paymentTypes",
					paymentService.findPaymentTypeByRestaurantId((int) session.getAttribute("restaurantId")));
		}
		model.addAttribute("order", new Order());
		return new ModelAndView("cart.html", "user", user);
	}

	@RequestMapping(value = { "/menu" }, method = RequestMethod.GET)
	public ModelAndView showMenu(@RequestParam("id") int id, HttpServletRequest request, ModelMap model) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		

		model.addAttribute("foods", foodService.findAllFoodByRestaurantId(id));
		model.addAttribute("foodSets", foodSetService.findAllFoodSetByRestaurantId(id));
		model.addAttribute("restaurant", restaurantService.findRestaurantById(id));

		return new ModelAndView("menu.html", "user", new User());
	}
	
	
	@RequestMapping(value = "/foodDetail/{foodId}", method = RequestMethod.GET)
	public ModelAndView showFoodDetail(@PathVariable("foodId") int foodId, ModelMap model,HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		
		model.addAttribute("food", foodService.findFoodById(foodId));

		return new ModelAndView("foodDetail.html", "user", new User());
	}
	
	
	@RequestMapping(value = "/foodSetDetail/{foodSetId}", method = RequestMethod.GET)
	public ModelAndView showFoodSetDetail(@PathVariable("foodSetId") int foodSetId, ModelMap model,HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		model.addAttribute("foodForFoodSet", foodSetService.findFoodForFoodSetByFoodSetId(foodSetId));
		model.addAttribute("foodSet", foodSetService.findFoodSetById(foodSetId));

		return new ModelAndView("foodSetDetail.html", "user", new User());
	}

}
