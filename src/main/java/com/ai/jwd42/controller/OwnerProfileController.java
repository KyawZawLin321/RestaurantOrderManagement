package com.ai.jwd42.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.jwd42.dto.Restaurant;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;
import com.ai.jwd42.service.RestaurantService;
import com.ai.jwd42.service.UserService;

@Controller
@RequestMapping("/owner")
public class OwnerProfileController {

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private OrderTypeService orderTypeService;
	
	@Autowired
	private PaymentTypeService paymentTypeService;
	
	@Autowired
	private FoodService foodService;

	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
	public ModelAndView ownerProfile(HttpServletRequest request, ModelMap model , HttpSession session) {

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

		
			String userEmail = (String) request.getSession().getAttribute("username");

			System.out.println("Email:" + userEmail);
			model.addAttribute("users", userService.findUserByEmail(userEmail));
			model.addAttribute("restaurant", restaurantService.findRestaurantProfileByEmail(userEmail));

			return new ModelAndView("ownerProfile.html", "user",  new User());
		}
	}

	
	
	@RequestMapping(value = "/updateDescription", method = RequestMethod.POST)
	public ModelAndView updateRestaurant(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

			restaurantService.updateRestaurantDescription(restaurant);
			redirectAttributes.addFlashAttribute("message", "Update Restaurant Success!");
			return new ModelAndView("redirect:/owner/profile");
		
	}

	@RequestMapping(value = "/updateRestaurantLogo", method = RequestMethod.POST)
	public ModelAndView updateRestaurantLogo(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("logo");

		if (file == null || file.getSubmittedFileName() == "") {

			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("errorLogo", "Choose Update Logo You Want !");
			return new ModelAndView("redirect:/owner/profile");
		} else {
			String imgFileName = file.getSubmittedFileName();
			String fileExtension = imgFileName.substring(imgFileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String logoFileName = uuid + fileExtension;

			String uploadPath = request.getServletContext().getRealPath("/resources/images/") + logoFileName;
			try {
				FileOutputStream fos = new FileOutputStream(uploadPath);
				InputStream is = file.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logoFileName = "/resources/images/" + logoFileName;
			restaurant.setLogo(logoFileName);
			restaurantService.updateRestaurantLogo(restaurant);

			redirectAttributes.addFlashAttribute("messageImage", "Update Restaurant Logo Success!");
			return new ModelAndView("redirect:/owner/profile");
		}

	}

	@RequestMapping(value = "/updateRestaurantImage", method = RequestMethod.POST)
	public ModelAndView updateRestaurantImage(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("image");

		if (file == null || file.getSubmittedFileName() == "") {

			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("errorImage", "Choose Update Image You Want !");
			return new ModelAndView("redirect:/owner/profile");
		} else {
			String imgFileName = file.getSubmittedFileName();
			String fileExtension = imgFileName.substring(imgFileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String newFileName = uuid + fileExtension;

			String uploadPath = request.getServletContext().getRealPath("/resources/images/") + newFileName;
			try {
				FileOutputStream fos = new FileOutputStream(uploadPath);
				InputStream is = file.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			newFileName = "/resources/images/" + newFileName;
			restaurant.setImage(newFileName);
			restaurantService.updateRestaurantImg(restaurant);

			redirectAttributes.addFlashAttribute("messageImage", "Update Restaurant Image Success!");
			return new ModelAndView("redirect:/owner/profile");
		}

	}

}
