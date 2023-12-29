package com.ai.jwd42.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.jwd42.dto.Food;
import com.ai.jwd42.dto.FoodSet;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.FoodSetService;
import com.ai.jwd42.service.OrderService;
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/owner")
public class OwnerLoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private FoodService foodService;

	@Autowired
	private FoodSetService foodSetService;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, ModelMap model) {

		User user = new User();

		model.addAttribute("loginerror", true);
		return new ModelAndView("ownerlogin.html", "user", user);
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ModelAndView userLogin(@ModelAttribute("SpringWebUser") User user,
			@ModelAttribute("SpringWebFood") Food food, @ModelAttribute("SpringWebFoodSet") FoodSet foodset,
			ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(user.getEmail());

		if (userService.checkEmailandPasswordForOwner(user.getEmail(), user.getPassword())) {

			if (user.isRememberMe()) {
				Cookie ck = new Cookie("rememberUser", user.getEmail());
				response.addCookie(ck);
			}

			int userrowcount = userService.userRowCount(user.getEmail());
			int foodrowcount = foodService.foodRowCount(user.getEmail());
			int foodsetrowcount = foodSetService.foodSetRowCount(user.getEmail());
			int orderrowcount = orderService.orderRowCount(user.getEmail());

			user = userService.findByEmail(user.getEmail());
			request.getSession().setAttribute("username", user.getEmail());
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("role", "owner");

			model.addAttribute("userrowcount", userrowcount);
			model.addAttribute("foodrowcount", foodrowcount);
			model.addAttribute("foodsetrowcount", foodsetrowcount);
			model.addAttribute("orderrowcount", orderrowcount);

			System.out.println("userrowcount:" + userrowcount);
			System.out.println("foodrowcount:" + foodrowcount);
			System.out.println("foodsetrowcount:" + foodsetrowcount);
			return new ModelAndView("owner.html");
		} else {
			model.addAttribute("message", "Welcome");
			if (request.getCookies() != null) {
				for (Cookie ck : request.getCookies()) {
					if (ck.getName().equals("rememberUser")) {
						user.setEmail(ck.getValue());
						model.addAttribute("back", " back !");
					}
				}
			}
			user.setLoginDateTime(new java.util.Date().toString());
			model.addAttribute("loginerror", false);
			return new ModelAndView("ownerlogin.html", "user", user);
		}
	}

	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, ModelMap model) {
		User user = new User();
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
				}
			}
		}

		request.getSession().invalidate();
		model.addAttribute("loginerror", true);
		model.addAttribute("logoutmessage", "Bye !");
		return new ModelAndView("ownerlogin.html", "user", user);
	}

	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.GET)
	public ModelAndView changepassword(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("changepassword.html", "user", new User());
	}

	@RequestMapping(value = { "/changepassword" }, method = RequestMethod.POST)
	public ModelAndView changePassword(@ModelAttribute("SpringWeb") User user, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		String email = (String) request.getSession().getAttribute("username");
		if (user.getPassword() == null || user.getPassword().equals("") || user.getNewPassword() == null
				|| user.getNewPassword().equals("")) {
			redirectAttributes.addFlashAttribute("error", "Fill Change Password Form!");
			return new ModelAndView("redirect:/owner/changePassword");
		} else if (user.getPassword().length() > 20 || user.getNewPassword().length() > 20
				|| user.getComfirmPassword().length() > 20) {
			redirectAttributes.addFlashAttribute("error", "Change Password Data Is Too Long!");
			return new ModelAndView("redirect:/owner/changePassword");
		} else if (!user.getNewPassword().equals(user.getComfirmPassword())) {
			redirectAttributes.addFlashAttribute("error", "New Password and Comfirm Password are not match!");
			return new ModelAndView("redirect:/owner/changePassword");
		} else if (userService.checkEmailandPasswordForOwner(email, user.getPassword())) {
			String password = user.getNewPassword();

			// Generate a salt (for additional security)
			String salt = BCrypt.gensalt();
			// Hash the password with the generated salt
			String hashedPassword = BCrypt.hashpw(password, salt);
			// Print the hashed password
			System.out.println("Hashed password: " + hashedPassword);

			userService.changePassword(hashedPassword, email);
			redirectAttributes.addFlashAttribute("message", "Success Change Password!");
			return new ModelAndView("redirect:/owner/changePassword");
		} else {
			redirectAttributes.addFlashAttribute("error", "Old Password is not correct!");
			return new ModelAndView("redirect:/owner/changePassword");
		}
	}

}
