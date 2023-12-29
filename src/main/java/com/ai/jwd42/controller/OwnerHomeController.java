package com.ai.jwd42.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.FoodSetService;
import com.ai.jwd42.service.OrderService;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/owner")
public class OwnerHomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private FoodService foodService;

	@Autowired
	private FoodSetService foodSetService;

	@Autowired
	private OrderService orderService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderTypeService orderTypeService;
	@Autowired
	private PaymentTypeService paymentTypeService;


	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public ModelAndView ownerHome( HttpSession session,	ModelMap model) {
		User user = (User) session.getAttribute("user");
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

		int userrowcount = userService.userRowCount(user.getEmail());
		int foodrowcount = foodService.foodRowCount(user.getEmail());
		int foodsetrowcount = foodSetService.foodSetRowCount(user.getEmail());
		int orderrowcount = orderService.orderRowCount(user.getEmail());

		model.addAttribute("userrowcount", userrowcount);
		model.addAttribute("foodrowcount", foodrowcount);
		model.addAttribute("foodsetrowcount", foodsetrowcount);
		model.addAttribute("orderrowcount", orderrowcount);

		System.out.println("userrowcount:" + userrowcount);
		System.out.println("foodrowcount:" + foodrowcount);
		System.out.println("foodsetrowcount:" + foodsetrowcount);

		return new ModelAndView("owner.html");
		}

	}

}
