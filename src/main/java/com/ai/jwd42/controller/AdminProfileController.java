package com.ai.jwd42.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminProfileController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
	public ModelAndView ownerProfile(HttpServletRequest request, ModelMap model) {

		User user = new User();
		String adminEmail = (String) request.getSession().getAttribute("adminname");

		System.out.println("Email:" + adminEmail);
		model.addAttribute("admin", userService.findUserByEmail(adminEmail));

		return new ModelAndView("adminProfile.html", "user", user);
	}

}
