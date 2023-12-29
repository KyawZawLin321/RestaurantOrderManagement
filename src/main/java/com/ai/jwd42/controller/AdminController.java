package com.ai.jwd42.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/admin")

public class AdminController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, ModelMap model) {

		User user = new User();

		model.addAttribute("loginerror", true);
		return new ModelAndView("adminlogin.html", "user", user);
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ModelAndView userLogin(@ModelAttribute() User user, ModelMap model, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
		if (userService.checkEmailandPasswordForAdmin(user.getEmail(), user.getPassword())) {

			if (user.isRememberMe()) {
				Cookie ck = new Cookie("rememberUser", user.getEmail());
				response.addCookie(ck);
			}

			request.getSession().setAttribute("adminname", user.getEmail());
			request.getSession().setAttribute("role", "admin");
			redirectAttributes.addFlashAttribute("message", "Welcome");
			return new ModelAndView("redirect:/admin/home");
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

			model.addAttribute("loginerror", false);
			return new ModelAndView("adminlogin.html", "user", user);
		}
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView ownerHome(HttpSession session, ModelMap model) {

		model.addAttribute("userrowcount", userService.userRow());
		model.addAttribute("ownerrowcount", userService.ownerRow());
		model.addAttribute("restaurantrowcount", userService.restaurantRow());

		return new ModelAndView("admin.html");
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
		redirectAttributes.addFlashAttribute("loginerror", true);
		redirectAttributes.addFlashAttribute("logoutmessage", "Bye !");
		return new ModelAndView("redirect:/admin/login");
	}

	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.GET)
	public ModelAndView changepassword(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		return new ModelAndView("changepasswordadmin.html", "user", new User());
	}

	@RequestMapping(value = { "/changepassword" }, method = RequestMethod.POST)
	public ModelAndView changePassword(@ModelAttribute("SpringWeb") User user, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		String email = (String) request.getSession().getAttribute("adminname");
		if (user.getPassword() == null || user.getPassword().equals("") || user.getNewPassword() == null
				|| user.getNewPassword().equals("")) {
			redirectAttributes.addFlashAttribute("error", "Fill Change Password Form!");
			return new ModelAndView("redirect:/admin/changePassword");
		} else if (user.getPassword().length() > 20 || user.getNewPassword().length() > 20
				|| user.getComfirmPassword().length() > 20) {
			redirectAttributes.addFlashAttribute("error", "Change Password Data Is Too Long!");
			return new ModelAndView("redirect:/admin/changePassword");
		} else if (!user.getNewPassword().equals(user.getComfirmPassword())) {
			redirectAttributes.addFlashAttribute("error", "New Password and Comfirm Password are not match!");
			return new ModelAndView("redirect:/admin/changePassword");
		} else if (userService.checkEmailandPasswordForAdmin(email, user.getPassword())) {
			String password = user.getNewPassword();

			// Generate a salt (for additional security)
			String salt = BCrypt.gensalt();
			// Hash the password with the generated salt
			String hashedPassword = BCrypt.hashpw(password, salt);
			// Print the hashed password
			System.out.println("Hashed password: " + hashedPassword);

			userService.changePassword(hashedPassword, email);
			redirectAttributes.addFlashAttribute("message", "Success Change Password!");
			return new ModelAndView("redirect:/admin/changePassword");
		} else {
			redirectAttributes.addFlashAttribute("error", "Old Password is not correct!");
			return new ModelAndView("redirect:/admin/changePassword");
		}
	}

}
