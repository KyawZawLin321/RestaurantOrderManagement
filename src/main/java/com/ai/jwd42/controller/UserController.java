package com.ai.jwd42.controller;

import java.io.IOException;

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
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.GET)
	public ModelAndView changepassword(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		return new ModelAndView("changepassworduser.html", "user", new User());
	}

	@RequestMapping(value = { "/changepassword" }, method = RequestMethod.POST)
	public ModelAndView changePassword(@ModelAttribute("SpringWeb") User user, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		String email = (String) request.getSession().getAttribute("username");
		if (user.getPassword() == null || user.getPassword().equals("") || user.getNewPassword() == null
				|| user.getNewPassword().equals("")) {
			redirectAttributes.addFlashAttribute("errorChange", "Fill Change Password Form!");
			return new ModelAndView("redirect:/user/changePassword");
		} else if (user.getPassword().length() > 20 || user.getNewPassword().length() > 20
				|| user.getComfirmPassword().length() > 20) {
			redirectAttributes.addFlashAttribute("errorChange", "Change Password Data Is Too Long!");
			return new ModelAndView("redirect:/user/changePassword");
		} else if (!user.getNewPassword().equals(user.getComfirmPassword())) {
			redirectAttributes.addFlashAttribute("errorChange", "New Password and Comfirm Password are not match!");
			return new ModelAndView("redirect:/user/changePassword");
		} else if (userService.checkEmailandPasswordForUser(email, user.getPassword())) {
			String password = user.getNewPassword();

			// Generate a salt (for additional security)
			String salt = BCrypt.gensalt();
			// Hash the password with the generated salt
			String hashedPassword = BCrypt.hashpw(password, salt);
			// Print the hashed password
			System.out.println("Hashed password: " + hashedPassword);

			userService.changePassword(hashedPassword, email);
			redirectAttributes.addFlashAttribute("messageChange", "Success Change Password!");
			return new ModelAndView("redirect:/user/changePassword");
		} else {
			redirectAttributes.addFlashAttribute("errorChange", "Old Password is not correct!");
			return new ModelAndView("redirect:/user/changePassword");
		}

	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public ModelAndView updateUser(@ModelAttribute("SpringWeb") User user, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) throws IOException {

		model.addAttribute("users", userService.findAllUser());
		if (userService.checkUserIsEmpty(user)) {
			redirectAttributes.addFlashAttribute("errorUpdate", "Please Fill User Edit Form !");
			return new ModelAndView("redirect:/user/profile");
		} else if (userService.checkUserInput(user)) {
			redirectAttributes.addFlashAttribute("errorUpdate", "User Edit Form Data Too Long!");
			return new ModelAndView("redirect:/user/profile");
		} else if (userService.checkEmailAlreadyExistAtUpdate(user.getEmail(), user.getId())) {
			redirectAttributes.addFlashAttribute("errorUpdate", "Email is Already exists!");
			return new ModelAndView("redirect:/user/profile");
//			return "redirect:/user";
		} else if (userService.checkPhoneNumberAlreadyExistAtUpdate(user.getPhoneNumber(), user.getId())) {
			redirectAttributes.addFlashAttribute("errorUpdate", "Phone Number is Already Exist!");
			return new ModelAndView("redirect:/user/profile");

		} else {
			userService.updateUser(user);
			redirectAttributes.addFlashAttribute("messageUpdate", "Success User Update !");
			return new ModelAndView("redirect:/user/profile");

		}
	}

}
