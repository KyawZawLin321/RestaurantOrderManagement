package com.ai.jwd42.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/owner")
public class CustomerController {

	@Autowired
	private UserService userService;
	@Autowired
	private FoodService foodService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderTypeService orderTypeService;
	@Autowired
	private PaymentTypeService paymentTypeService;

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public ModelAndView customer(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		User user = (User) request.getSession().getAttribute("user");
		if (orderTypeService.findOrderTypeByRestaurantId(user.getRestaurantId()) == null
				|| orderTypeService.findOrderTypeByRestaurantId(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/ordertype");
		} else if (paymentTypeService.findPaymentByRestaurantId(user.getRestaurantId()) == null
				|| paymentTypeService.findPaymentByRestaurantId(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (categoryService.findAll(user.getRestaurantId()) == null
				|| categoryService.findAll(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/category");
		} else if (foodService.findAll(user.getRestaurantId()) == null
				|| foodService.findAll(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/addProduct");
		} else {
			model.addAttribute("users", userService.findCustomer(user.getRestaurantId()));

			return new ModelAndView("customer.html", "user", user);
		}
	}

}
