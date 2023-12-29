package com.ai.jwd42.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.jwd42.dto.Restaurant;

import com.ai.jwd42.service.RestaurantService;
import com.ai.jwd42.service.UserService;

@Controller
@MultipartConfig
@RequestMapping("/admin")
public class AdminRestaurantController {
	@Autowired
	private UserService userService;
	@Autowired
	private RestaurantService restaurantService;

	@RequestMapping(value = { "/displayRestaurant" }, method = RequestMethod.GET)
	public ModelAndView displayRestaurant(HttpServletRequest request, ModelMap model) {

		Restaurant restaurant = new Restaurant();

		model.addAttribute("restaurants", restaurantService.findAllRestaurant());
		/* System.out.println("restaurants : " + restaurant.getName()); */
		return new ModelAndView("displayRestaurant.html", "restaurant", restaurant);

	}

	@RequestMapping(value = { "/addRestaurant" }, method = RequestMethod.GET)
	public ModelAndView addRestaurant(HttpServletRequest request, ModelMap model) {

		return new ModelAndView("addRestaurant.html", "restaurant", new Restaurant());

	}

	@RequestMapping(value = { "/displayOwner/{restaurantId}" }, method = RequestMethod.GET)
	public ModelAndView displayowner(@PathVariable("restaurantId") int restaurantId,
			@ModelAttribute Restaurant restaurant, HttpServletRequest request, ModelMap model) {

		model.addAttribute("owner", restaurantService.findAllOwner(restaurantId));
		/* System.out.println("restaurants : " + restaurant.getName()); */
		return new ModelAndView("ownerinfo.html", "restaurant", restaurant);

	}

	@RequestMapping(value = "/displayUsers", method = RequestMethod.GET)
	public ModelAndView displayuser(@ModelAttribute Restaurant restaurant, HttpServletRequest request, ModelMap model) {

		model.addAttribute("users", userService.findAllUserRole());
		/* System.out.println("restaurants : " + restaurant.getName()); */
		return new ModelAndView("displayUsers.html", "restaurant", restaurant);

	}

	@RequestMapping(value = "/addRestaurant", method = RequestMethod.POST)
	public ModelAndView addRestaurantData(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("logo");
		Part file2 = request.getPart("image");
		if (file == null || file.getSubmittedFileName() == "") {

			redirectAttributes.addFlashAttribute("error", "Choose Logo You Want !");
			return new ModelAndView("redirect:/admin/addRestaurant");
		} else if (file2 == null || file2.getSubmittedFileName() == "") {

			redirectAttributes.addFlashAttribute("error", "Choose Image You Want !");
			return new ModelAndView("redirect:/admin/addRestaurant");
		} else if (restaurant.getPassword() == "" || restaurant.getConfirmPassword() == ""
				|| restaurant.getPassword().length() > 20 || restaurant.getConfirmPassword().length() > 20
				|| !restaurant.getPassword().equals(restaurant.getConfirmPassword())) {
			redirectAttributes.addFlashAttribute("error", "Password and Confirm Password Are Not Match!");
			return new ModelAndView("redirect:/admin/addRestaurant");
		} else if (restaurantService.checkEmailAlreadyExist(restaurant.getEmail())) {

			redirectAttributes.addFlashAttribute("error", "Restaurant Email is already exist!");
			return new ModelAndView("redirect:/admin/addRestaurant");
		} else if (userService.checkEmailAlreadyExist(restaurant.getOwneremail())) {

			redirectAttributes.addFlashAttribute("error", "Owner Email is already exist!");
			return new ModelAndView("redirect:/admin/addRestaurant");
		} else if (restaurantService.checkRestaurant(restaurant)) {
			redirectAttributes.addFlashAttribute("error", "Restaurant Data Is Too Long!");
			return new ModelAndView("redirect:/admin/addRestaurant");
		} else if (userService.checkOwner(restaurant)) {
			redirectAttributes.addFlashAttribute("error", "Owner Data Is Too Long!");
			return new ModelAndView("redirect:/admin/addRestaurant");
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
			// set logo
			restaurant.setLogo(logoFileName);

			String imgFileName2 = file2.getSubmittedFileName();
			String fileExtension2 = imgFileName2.substring(imgFileName2.lastIndexOf("."));
			String uuid2 = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String newFileName = uuid2 + fileExtension2;

			String uploadPath2 = request.getServletContext().getRealPath("/resources/images/") + newFileName;
			try {
				FileOutputStream fos = new FileOutputStream(uploadPath2);
				InputStream is = file2.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			newFileName = "/resources/images/" + newFileName;
			// set image
			restaurant.setImage(newFileName);

			String password = restaurant.getPassword();

			// Generate a salt (for additional security)
			String salt = BCrypt.gensalt();

			// Hash the password with the generated salt
			String hashedPassword = BCrypt.hashpw(password, salt);

			// Print the hashed password
			System.out.println("Hashed password: " + hashedPassword);

			// set password
			restaurant.setPassword(hashedPassword);

			restaurantService.addRestaurant(restaurant);
			redirectAttributes.addFlashAttribute("addMessage", "Success  Add " + restaurant.getName() + " !");
			return new ModelAndView("redirect:/admin/displayRestaurant");

		}
	}

	@RequestMapping(value = "/updateRestaurant", method = RequestMethod.POST)
	public ModelAndView updateRestaurant(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

		if (restaurantService.checkEmailAlreadyExistAtUpdate(restaurant.getEmail(), restaurant.getId())) {
			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("error", "This Email is Already Exist !");
			return new ModelAndView("redirect:/admin/displayRestaurant");
		} else if (restaurantService.checkRestaurant(restaurant)) {
			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("error", "Restaurant Data Is Too Long!");
			return new ModelAndView("redirect:/admin/displayRestaurant");
		} else {

			restaurantService.updateRestaurant(restaurant);
			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("message", "Update Restaurant Success!");
			return new ModelAndView("redirect:/admin/displayRestaurant");
		}
	}

	@RequestMapping(value = "/updateRestaurantLogo", method = RequestMethod.POST)
	public ModelAndView updateRestaurantLogo(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("logo");

		if (file == null || file.getSubmittedFileName() == "") {

			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("errorLogo", "Choose Update Logo You Want !");
			return new ModelAndView("redirect:/admin/displayRestaurant");
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
			return new ModelAndView("redirect:/admin/displayRestaurant");
		}

	}

	@RequestMapping(value = "/updateRestaurantImage", method = RequestMethod.POST)
	public ModelAndView updateRestaurantImage(@ModelAttribute Restaurant restaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("image");

		if (file == null || file.getSubmittedFileName() == "") {

			redirectAttributes.addFlashAttribute("restaurantId", restaurant.getId());
			redirectAttributes.addFlashAttribute("errorImage", "Choose Update Image You Want !");
			return new ModelAndView("redirect:/admin/displayRestaurant");
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
			return new ModelAndView("redirect:/admin/displayRestaurant");
		}

	}

	@RequestMapping(value = "/updateOwner/{ownerId}", method = RequestMethod.POST)
	public ModelAndView updateOwner(@PathVariable("ownerId") int restaurantId, @ModelAttribute Restaurant restaurant,
			ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws IOException, ServletException {
		if (userService.checkEmailAlreadyExistAtUpdate(restaurant.getOwneremail(), restaurant.getId())) {
			redirectAttributes.addFlashAttribute("ownerId", restaurant.getId());
			redirectAttributes.addFlashAttribute("error", "This Owner Email  is already exist");
			return new ModelAndView("redirect:/admin/displayOwner/" + restaurantId);
		} else if (userService.checkOwner(restaurant)) {
			redirectAttributes.addFlashAttribute("ownerId", restaurant.getId());
			redirectAttributes.addFlashAttribute("error", "Owner Data Is Too Long!");
			return new ModelAndView("redirect:/admin/displayOwner/" + restaurantId);
		} else {
			restaurantService.updateOwner(restaurant);
			redirectAttributes.addFlashAttribute("ownerId", restaurant.getId());
			redirectAttributes.addFlashAttribute("message", "Success Update Owner Info");
			return new ModelAndView("redirect:/admin/displayOwner/" + restaurantId);
		}

	}

	@RequestMapping(value = "/deleteRestaurant", method = RequestMethod.POST)
	public ModelAndView deleteFood(@ModelAttribute Restaurant restaurant, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {

		restaurantService.deleteOwner(restaurant.getId());
		restaurantService.deleteRestaurantImg(restaurant.getId());
		restaurantService.deleteRestaurant(restaurant.getId());
		request.setAttribute("message", "Delete Restaurant Success!");
		return new ModelAndView("redirect:/admin/displayRestaurant", "restaurant", restaurant);
	}

}
