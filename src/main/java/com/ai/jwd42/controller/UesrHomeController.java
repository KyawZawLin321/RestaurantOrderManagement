package com.ai.jwd42.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
@RequestMapping("/user")
public class UesrHomeController {

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response) {

		if (request.getSession().getAttribute("username") == null) {
			return "redirect:/login";
		} else {
			int count = 1;
			if (request.getSession().getAttribute("count") != null) {
				count = (int) request.getSession().getAttribute("count") + 1;
			}

			request.getSession().setAttribute("count", count);
			return "redirect:/login";
		}
	}



}
