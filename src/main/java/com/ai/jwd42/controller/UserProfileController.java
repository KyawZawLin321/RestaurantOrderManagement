package com.ai.jwd42.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
@RequestMapping("/user")
public class UserProfileController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
	public ModelAndView ownerProfile(HttpServletRequest request, ModelMap model) {
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
		if (model.get("msg") != null) {
			model.addAttribute("msg", model.get("msg"));
		}
		if (model.get("back") != null) {
			model.addAttribute("back", model.get("back"));
		}
		if (model.get("error") != null) {
			model.addAttribute("error", model.get("error"));
		}
		if (model.get("registerMessage") != null) {
			model.addAttribute("registerMessage", model.get("registerMessage"));
		}
		if (model.get("registerSuccess") != null) {
			model.addAttribute("registerSuccess", model.get("registerSuccess"));
		}

		User users = new User();
		String userEmail = (String) request.getSession().getAttribute("username");
		users = userService.findUserByEmail(userEmail);
		model.addAttribute("users", users);
		return new ModelAndView("userprofile.html", "user", user);

	}

	
}
