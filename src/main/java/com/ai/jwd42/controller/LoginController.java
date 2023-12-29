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

import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.RestaurantService;
import com.ai.jwd42.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, ModelMap model) {

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

		model.addAttribute("restaurants", restaurantService.findRestaurantByLimit());

		return new ModelAndView("login.html", "user", user);
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ModelAndView userLogin(@ModelAttribute("SpringWeb") User user, ModelMap model, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {

		String candidatePassword = user.getPassword();
		if (userService.checkEmailandPasswordForUser(user.getEmail(), candidatePassword)) {

			if (user.isRememberMe()) {
				Cookie ck = new Cookie("rememberUser", user.getEmail());
				response.addCookie(ck);
			}
			redirectAttributes.addFlashAttribute("msg", "Login Success");
			redirectAttributes.addFlashAttribute("message", "Welcome");
			request.getSession().setAttribute("username", user.getEmail());
			request.getSession().setAttribute("role", "user");

			// Get the referring URL
			String referringUrl = request.getHeader("Referer");
			String contextPath = request.getContextPath();
			String relativePath = referringUrl.substring(referringUrl.indexOf(contextPath) + contextPath.length());

			return new ModelAndView("redirect:" + relativePath);

		} else {
			redirectAttributes.addFlashAttribute("message", "Welcome");

			if (request.getCookies() != null) {
				for (Cookie ck : request.getCookies()) {
					if (ck.getName().equals("rememberUser")) {
						user.setEmail(ck.getValue());
						redirectAttributes.addFlashAttribute("back", " back !");
					}
				}
			}

			redirectAttributes.addFlashAttribute("error", "Username and Password are Incorrect!");
			// Get the referring URL
			String referringUrl = request.getHeader("Referer");
			String contextPath = request.getContextPath();
			String relativePath = referringUrl.substring(referringUrl.indexOf(contextPath) + contextPath.length());

			System.out.println("url - " + relativePath);
			return new ModelAndView("redirect:/" + relativePath);

		}
	}

	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		User user = new User();
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
				}
			}
		}

		request.getSession().invalidate();
		redirectAttributes.addFlashAttribute("message", "Bye !");
		// Get the referring URL
		String referringUrl = request.getHeader("Referer");
		String contextPath = request.getContextPath();
		String relativePath = referringUrl.substring(referringUrl.indexOf(contextPath) + contextPath.length());

		System.out.println("url - " + relativePath);
		return new ModelAndView("redirect:/" + relativePath);

	}

	@RequestMapping(value = { "/access-denied" }, method = RequestMethod.GET)
	public ModelAndView accessDenied(HttpServletRequest request, ModelMap model) {

		User user = new User();

		model.addAttribute("loginerror", true);
		return new ModelAndView("accessDeniedPage.html", "user", user);
	}

	@RequestMapping(value = { "/addUser" }, method = RequestMethod.POST)
	public ModelAndView addUser(@ModelAttribute("SpringWeb") User user, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException {
		user.setLoginDateTime(new java.util.Date().toString());
		String referringUrl = request.getHeader("Referer");
		String contextPath = request.getContextPath();
		String relativePath = referringUrl.substring(referringUrl.indexOf(contextPath) + contextPath.length());

		System.out.println("url - " + relativePath);

		if (userService.checkUserIsEmpty(user) || user.getPassword() == null || user.getPassword().equals("")) {
			redirectAttributes.addFlashAttribute("registerMessage", "Please Fill Register Form !");
			return new ModelAndView("redirect:/" + relativePath);
		} else if (userService.checkUserInput(user) || user.getPassword().length() > 20) {
			redirectAttributes.addFlashAttribute("registerMessage", "Register Data Too Long!");
			return new ModelAndView("redirect:/" + relativePath);
		} else if (!user.getPassword().equals(user.getComfirmPassword())) {
			redirectAttributes.addFlashAttribute("registerMessage", "Password and Comfirm Password are not match!");
			return new ModelAndView("redirect:/" + relativePath);
		} else if (userService.checkEmailAlreadyExist(user.getEmail())) {
			redirectAttributes.addFlashAttribute("registerMessage", "Register Email is Already Exist!");
			return new ModelAndView("redirect:/" + relativePath);
		} else if (userService.checkPhoneNumberAlreadyExist(user.getPhoneNumber())) {
			redirectAttributes.addFlashAttribute("registerMessage", "Register Phone Number is Already Exist!");
			return new ModelAndView("redirect:/" + relativePath);
		} else {

			String password = user.getPassword();

			// Generate a salt (for additional security)
			String salt = BCrypt.gensalt();
			// Hash the password with the generated salt
			String hashedPassword = BCrypt.hashpw(password, salt);
			// Print the hashed password
			System.out.println("Hashed password: " + hashedPassword);

			user.setPassword(hashedPassword);
			userService.insertUser(user);
			redirectAttributes.addFlashAttribute("registerSuccess", "Registration Success!");
			return new ModelAndView("redirect:/" + relativePath);

		}
	}

}
