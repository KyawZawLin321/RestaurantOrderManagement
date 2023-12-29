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
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;

@Controller
@MultipartConfig
@RequestMapping("/owner")
public class FoodController {

	@Autowired
	private FoodService foodService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderTypeService orderTypeService;
	@Autowired
	private PaymentTypeService paymentTypeService;

	@RequestMapping(value = "/addProduct", method = RequestMethod.GET)
	public ModelAndView addFood(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		model.addAttribute("categorys", categoryService.findAll(user.getRestaurantId()));
		model.addAttribute("foodSet", new FoodSet());
		model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));

		return new ModelAndView("addProduct.html", "food", new Food());
	}

	@RequestMapping(value = "/addFood", method = RequestMethod.POST)
	public ModelAndView addFood(@ModelAttribute Food food, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {
		Part file = request.getPart("image");
		User user = (User) request.getSession().getAttribute("user");

		food.setRestaurantId(user.getRestaurantId());

		if (foodService.checkFoodIsEmpty(food)) {
			redirectAttributes.addFlashAttribute("error", "Fill Food Data !");
			return new ModelAndView("redirect:/owner/addProduct");
		} else if (foodService.checkFoodInput(food)) {
			redirectAttributes.addFlashAttribute("error", "Food Data is Too Long !");
			return new ModelAndView("redirect:/owner/addProduct");
		} else if (file != null && file.getSubmittedFileName() != "") {
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
			newFileName = "/resources/images/" + uuid + fileExtension;
			food.setImage(newFileName);
			foodService.insert(food);
			redirectAttributes.addFlashAttribute("addMessage", "Success Adding New Food !");
			return new ModelAndView("redirect:/owner/food");

		} else {
			// Handle the case when no file is uploaded
			System.out.println("No file uploaded.");
			redirectAttributes.addFlashAttribute("error", "Choose Image For New Food !");
			return new ModelAndView("redirect:/owner/addProduct");
		}

	}

	@RequestMapping(value = "/food", method = RequestMethod.GET)
	public ModelAndView food(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		User user = (User) request.getSession().getAttribute("user");
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

			model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));
			model.addAttribute("categorys", categoryService.findAll(user.getRestaurantId()));

			return new ModelAndView("food.html", "food", new Food());
		}

	}

	@RequestMapping(value = "/updateFood", method = RequestMethod.POST)
	public ModelAndView updateFood(@ModelAttribute Food food, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {

		if (foodService.checkFoodIsEmpty(food)) {
			redirectAttributes.addFlashAttribute("foodId", food.getId());
			redirectAttributes.addFlashAttribute("error", "Fill Food Data !");
			return new ModelAndView("redirect:/owner/food");
		} else if (foodService.checkFoodInput(food)) {
			redirectAttributes.addFlashAttribute("foodId", food.getId());
			redirectAttributes.addFlashAttribute("error", "Food Data is Too Long !");
			return new ModelAndView("redirect:/owner/food");
		} else if (foodService.checkFoodNameAlreadyExist(food.getName(), food.getId())) {

			redirectAttributes.addFlashAttribute("foodId", food.getId());
			redirectAttributes.addFlashAttribute("error", "This Food name is already exist");
			return new ModelAndView("redirect:/owner/food");

		} else {

			System.out.println(food.getId());

			System.out.println("selec =" + food.getSelect());
			foodService.updateFood(food);
			redirectAttributes.addFlashAttribute("message", "Success Update!");
			redirectAttributes.addFlashAttribute("foodId", food.getId());

			return new ModelAndView("redirect:/owner/food");
		}
	}

	@RequestMapping(value = "/updateFoodImage", method = RequestMethod.POST)
	public ModelAndView updateFoodImage(@ModelAttribute Food food, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("image");
		if (file != null && file.getSubmittedFileName() != "") {
			String imgFileName = file.getSubmittedFileName();
			String fileExtension = imgFileName.substring(imgFileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String newFileName = uuid + fileExtension;
			// Example path within your web application
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
			newFileName = "/resources/images/" + uuid + fileExtension;
			food.setImage(newFileName);
			// Handle the case when no file is uploaded
			System.out.println("No file uploaded.");
			System.out.println(food.getId());

			foodService.updateFoodImg(food);
			redirectAttributes.addFlashAttribute("messageImage", "Success Update!");
			redirectAttributes.addFlashAttribute("foodId", food.getId());

			return new ModelAndView("redirect:/owner/food");
		} else {
			// Handle the case when no file is uploaded
			System.out.println("No file uploaded.");
			System.out.println(food.getId());

			redirectAttributes.addFlashAttribute("foodId", food.getId());
			redirectAttributes.addFlashAttribute("errorImage", "Choose Image To Update Food Image !");
			return new ModelAndView("redirect:/owner/food");
		}

	}

	@RequestMapping(value = "/deleteFood", method = RequestMethod.POST)
	public ModelAndView deleteFood(@ModelAttribute Food food, ModelMap model, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		if (!foodService.findFoodForFoodSetByFoodId(food.getId()).isEmpty()) {
			redirectAttributes.addFlashAttribute("foodId", food.getId());
			redirectAttributes.addFlashAttribute("errorDelete", "This Food is using for FoodSet.You Can't Delete !");
			return new ModelAndView("redirect:/owner/food");
		} else {

			foodService.deleteFoodImg(food.getId());
			foodService.delete(food.getId());
			redirectAttributes.addFlashAttribute("msg", "Success Delete!");
			return new ModelAndView("redirect:/owner/food");
		}
	}

}
